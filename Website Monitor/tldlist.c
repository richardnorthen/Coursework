#include "date.h"
#include "tldlist.h"
#include <stdlib.h> /* malloc, free, NULL */
#include <string.h> /* strtok, strcmp, strcpy */

#define MAX_TLD_LEN 4

/*
 * Richard Northen 951263471
 * CIS 415 Project 0
 * This is (mostly) my own work as discussed in report.txt
 */

struct tldlist {
  Date *begin;
  Date *end;
  long count;
  long size;
  TLDNode *root;
};

struct tldnode {
  char name[MAX_TLD_LEN];
  long count;
  long height;
  TLDNode *left;
  TLDNode *right;
};

struct tlditerator {
  long size;
  long next;
  TLDNode **nodes;
};

/* member functions */
/* returns the maximum of two values */
static long max(long a, long b) { return (a > b) ? a : b; }
static TLDNode *tldlist_insert(TLDList *tld, TLDNode *node, char *tldname);
static TLDNode *tldlist_rotate_left(TLDNode *node);
static TLDNode *tldlist_rotate_right(TLDNode *node);
static void tldlist_iter_inorder(TLDIterator *iter, TLDNode *node);
static TLDNode *tldnode_create(char *tldname);
/* returns the height of a node, or 0 if the node is NULL */
static long tldnode_height(TLDNode *node) { return (node == NULL) ? 0 : node->height; }
/* calculates the balance of a node and returns it, or 0 if the node is NULL */
static long tldnode_balance(TLDNode *node) { return (node == NULL) ? 0 : tldnode_height(node->left) - tldnode_height(node->right); }

/*
 * tldlist_create generates a list structure for storing counts against
 * top level domains (TLDs)
 *
 * creates a TLDList that is constrained to the `begin' and `end' Date's
 * returns a pointer to the list if successful, NULL if not
 */
TLDList *tldlist_create(Date *begin, Date *end) {
  TLDList *tld = (TLDList *) malloc(sizeof(TLDList));
  if (tld != NULL) {
    if ((tld->begin = date_duplicate(begin)) == NULL) {
      free(tld);
      return NULL;
    }
    if ((tld->end = date_duplicate(end)) == NULL) {
      free(tld->begin);
      free(tld);
      return NULL;
    }
    tld->count = 0;
    tld->size = 0;
    tld->root = NULL;
  }
  return tld;
}

/*
 * tldlist_destroy destroys the list structure in `tld'
 *
 * all heap allocated storage associated with the list is returned to the heap
 */
void tldlist_destroy(TLDList *tld) {
  TLDIterator *it = tldlist_iter_create(tld);
  if (it == NULL) {
    return;
  }
  TLDNode *node;
  while ((node = tldlist_iter_next(it)) != NULL) {
    free(node);
  }
  tldlist_iter_destroy(it);
  date_destroy(tld->begin);
  date_destroy(tld->end);
  free(tld);
}

/*
 * tldlist_add adds the TLD contained in `hostname' to the tldlist if
 * `d' falls in the begin and end dates associated with the list;
 * returns 1 if the entry was counted, 0 if not
 */
int tldlist_add(TLDList *tld, char *hostname, Date *d) {
  char *tldname, *token = strtok(hostname, ".");
  while (token != NULL) {
    tldname = token;
    token = strtok(NULL, ".");
  }
  if (tldname == NULL || date_compare(d, tld->begin) < 0 || date_compare(d, tld->end) > 0) {
    return 0;
  }
  int i = 0;
  while (tldname[i] != '\0') {
    if (tldname[i] >= 'A' && tldname[i] <= 'Z') {
      tldname[i] += 32;
    } else if (tldname[i] >= 'a' && tldname[i] <= 'z') {
      ;
    } else {
      return 0;
    }
    i++;
  }
  tld->root = tldlist_insert(tld, tld->root, tldname);
  return 1;
}

/* tldlist_insert recursively searches for a node with a name that matches tldname,
 * incrementing it's count if found or creating a new node otherwise
 * as it finishes the recursion, it balances the tree and performs the correct rotations
 */
static TLDNode *tldlist_insert(TLDList *tld, TLDNode *node, char *tldname) {
  if (node == NULL) {
    tld->count++;
    tld->size++;
    return tldnode_create(tldname);
  }
  if (strcmp(tldname, node->name) < 0) {
    node->left = tldlist_insert(tld, node->left, tldname);
  } else if (strcmp(tldname, node->name) > 0) {
    node->right = tldlist_insert(tld, node->right, tldname);
  } else if (strcmp(tldname, node->name) == 0) {
    tld->count++;
    node->count++;
    return node;
  }
  node->height = max(tldnode_height(node->left), tldnode_height(node->right)) + 1;
  long balance = tldnode_balance(node);
  if (balance > 1 && strcmp(tldname, node->left->name) < 0) {
    return tldlist_rotate_right(node);
  }
  if (balance < -1 && strcmp(tldname, node->right->name) > 0) {
    return tldlist_rotate_left(node);
  }
  if (balance > 1 && strcmp(tldname, node->left->name) > 0) {
    node->left = tldlist_rotate_left(node->left);
    return tldlist_rotate_right(node);
  }
  if (balance < -1 && strcmp(tldname, node->right->name) < 0) {
    node->right = tldlist_rotate_right(node->right);
    return tldlist_rotate_left(node);
  }
  return node;
}

/* peforms a left rotation on the given node, returning the new root of the subtree */
static TLDNode *tldlist_rotate_left(TLDNode *node) {
  TLDNode *a = node->right;
  TLDNode *b = a->left;
  a->left = node;
  node->right = b;
  node->height = max(tldnode_height(node->left), tldnode_height(node->right)) + 1; 
  a->height = max(tldnode_height(a->left), tldnode_height(a->right)) + 1;
  return a;
}

/* performs a right rotation on the given node, returning the new root of the subtree */
static TLDNode *tldlist_rotate_right(TLDNode *node) {
  TLDNode *a = node->left;
  TLDNode *b = a->right;
  a->right = node;
  node->left = b;
  node->height = max(tldnode_height(node->left), tldnode_height(node->right)) + 1;
  a->height = max(tldnode_height(a->left), tldnode_height(a->right)) + 1;
  return a;
}

/*
 * tldlist_count returns the number of successful tldlist_add() calls since
 * the creation of the TLDList
 */
long tldlist_count(TLDList *tld) {
  return tld->count;
}

/*
 * tldlist_iter_create creates an iterator over the TLDList; returns a pointer
 * to the iterator if successful, NULL if not
 */
TLDIterator *tldlist_iter_create(TLDList *tld) {
  TLDIterator *it = (TLDIterator *) malloc(sizeof(TLDIterator));
  if (it != NULL) {
    if (tld->root == NULL) {
      return NULL;
    }
    it->size = tld->size;
    it->next = 0;
    it->nodes = (TLDNode **) malloc(sizeof(TLDNode *) * it->size);
    tldlist_iter_inorder(it, tld->root);
    it->next = 0;
  }
  return it;
}

/* traverses the tree and adds each node to the iterator (in-order) */
static void tldlist_iter_inorder(TLDIterator *iter, TLDNode *node) {
  if (node->left != NULL) {
    tldlist_iter_inorder(iter, node->left);
  }
  iter->nodes[iter->next++] = node;
  if (node->right != NULL) {
    tldlist_iter_inorder(iter, node->right);
  }
}

/*
 * tldlist_iter_next returns the next element in the list; returns a pointer
 * to the TLDNode if successful, NULL if no more elements to return
 */
TLDNode *tldlist_iter_next(TLDIterator *iter) {
  if (iter->next < iter->size) {
    return iter->nodes[iter->next++];
  }
  return NULL;
}

/*
 * tldlist_iter_destroy destroys the iterator specified by `iter'
 */
void tldlist_iter_destroy(TLDIterator *iter) {
  free(iter->nodes);
  free(iter);
}

/* creates a new node, allocating memory and instantiating the struct */
static TLDNode *tldnode_create(char *tldname) {
  TLDNode *node = (TLDNode *) malloc(sizeof(TLDNode));
  if (node != NULL) {
    strcpy(node->name, tldname);
    node->count = 1;
    node->height = 1;
    node->left = NULL;
    node->right = NULL;
  }
  return node;
}

/*
 * tldnode_tldname returns the tld associated with the TLDNode
 */
char *tldnode_tldname(TLDNode *node) {
  return node->name;
}

/*
 * tldnode_count returns the number of times that a log entry for the
 * corresponding tld was added to the list
 */
long tldnode_count(TLDNode *node) {
  return node->count;
}
