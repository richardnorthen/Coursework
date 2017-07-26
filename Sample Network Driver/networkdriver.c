/*
 * Richard Northen
 * This is own my work, other than the functions in the provided .h files.
 */

#include "networkdriver.h"
#include "networkdevice.h"
#include "freepacketdescriptorstore.h"
#include "freepacketdescriptorstore__full.h"
#include "packetdescriptor.h"
#include "packetdescriptorcreator.h"
#include "BoundedBuffer.h"
#include "diagnostics.h"
#include "destination.h"
#include "pid.h"

#include <pthread.h>

#define RETRIES 3

NetworkDevice *device;
FreePacketDescriptorStore *store;
BoundedBuffer *sending_buffer, *receiving_buffer[MAX_PID+1], *waiting_buffer;
pthread_t thread_send, thread_receive, thread_wait;

void *sending_thread();
void *receiving_thread();
void *waiting_thread();

void init_network_driver(NetworkDevice *nd, void *mem_start, unsigned long mem_length, FreePacketDescriptorStore **fpds_ptr) {
  /* determine the number of PacketDescriptors created */
  int num_pds, i = 0;
  /* NetworkDevice reference saved */
  device = nd;
  /* FreePacketDescriptorStore initialized and reference saved */
  store = create_fpds();
  num_pds = create_free_packet_descriptors(store, mem_start, mem_length);
  *fpds_ptr = store;
  /* BoundedBuffers for sending, receiving, and waiting on PacketDescriptors */
  sending_buffer = createBB(num_pds);
  DIAGNOSTICS("Driver: %d PacketDescriptors in sending_buffer\n", num_pds);
  while (i < (MAX_PID+1)) {
    receiving_buffer[i++] = createBB(num_pds);
  }
  DIAGNOSTICS("Driver: %d applications with %d PacketDescriptors each in receiving_buffer\n", MAX_PID+1, num_pds);
  waiting_buffer = createBB(num_pds);
  DIAGNOSTICS("Driver: %d PacketDescriptors in waiting_buffer\n", num_pds);
  /* dedicated threads for sending(send), receiving(wait), and processing(recieve) */
  if (pthread_create(&thread_send, NULL, sending_thread, NULL)) {
    DIAGNOSTICS("Driver: Error creating thread_send\n");
  }
  if (pthread_create(&thread_receive, NULL, receiving_thread, NULL)) {
    DIAGNOSTICS("Driver: Error creating thread_receive\n"); 
  }
  if (pthread_create(&thread_wait, NULL, waiting_thread, NULL)) {
    DIAGNOSTICS("Driver: Error creating thread_wait\n");
  }
}

/* place pd into sending_buffer */
void blocking_send_packet(PacketDescriptor *pd) {
  /* block until pd is added to buffer */
  blockingWriteBB(sending_buffer, pd);
}
int nonblocking_send_packet(PacketDescriptor *pd) {
  /* returns whether pd was added to buffer (1=OK, 0=NO) */
  return nonblockingWriteBB(sending_buffer, pd);
}

/* grab a PacketDescriptor from receiving_buffer[id] and place into pd */
void blocking_get_packet(PacketDescriptor **pd, PID id) {
  /* block until pd contains the PacketDescriptor */
  *pd = (PacketDescriptor *) blockingReadBB(receiving_buffer[id]);
}
int nonblocking_get_packet(PacketDescriptor **pd, PID id) {
  /* returns whether a PacketDescriptor was placed into pd (1=OK, 0=NO) */
  return nonblockingReadBB(receiving_buffer[id], (void **) pd);
}

/* send PacketDescriptors from the sending_buffer to the NetworkDevice */
void *sending_thread() {
  PacketDescriptor *pd;
  int i;
  while (1) {
    /* grab the packet and attempt to send it */
    pd = (PacketDescriptor *) blockingReadBB(sending_buffer);
    for (i = 0; i < RETRIES; i++) {
      if (send_packet(device, pd)) {
        DIAGNOSTICS("Driver: Packet sent after %d tries\n", i+1);
        break;
      }
      DIAGNOSTICS("Driver: Failed to send a packet, %s\n", i<(RETRIES-1) ? " retrying..." : " aborting...");
    }
    /* release the PacketDescriptor from the store */
    blocking_put_pd(store, pd);
  }
  return (void *) 0;
}

/* processes the PacketDescriptors in waiting_buffer into the application's receiving_buffer */
void *receiving_thread() {
  PacketDescriptor *pd;
  PID id;
  while (1) {
    /* process the PacketDescriptor */
    pd = (PacketDescriptor *) blockingReadBB(waiting_buffer);
    id = packet_descriptor_get_pid(pd);
    /* if the receiving_buffer is full, discard the packet */
    if (!nonblockingWriteBB(receiving_buffer[id], pd)) {
      DIAGNOSTICS("Driver: Packet for PID %d was discarded\n", id);
    }
  }
  return (void *) 0;
}

/* adds the PacketDescriptor sent to the NetworkDevice to the waiting_buffer */
void *waiting_thread() {
  PacketDescriptor *pd;
  /* reserve a PacketDescriptor for the waiting_thread */
  blocking_get_pd(store, &pd);
  while (1) {
    /* reset, register, and wait for a PacketDescriptor */
    init_packet_descriptor(pd);
    register_receiving_packetdescriptor(device, pd);
    await_incoming_packet(device);
    /* if waiting_buffer is full, discard the packet */
    if (!nonblockingWriteBB(waiting_buffer, pd)) {
      DIAGNOSTICS("Driver: Incoming packet could not be received\n");
    }
  }
  /* release the PacketDescriptor when we (ever) finish */
  blocking_put_pd(store, pd);
  return (void *) 0;
}
