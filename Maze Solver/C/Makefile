SOURCES = maze.c
OBJECTS = $(SOURCES:.c=.o)

CC = gcc
CFLAGS = -std=c99 -g -Wall

maze: $(OBJECTS)
	$(CC) -o $@ $(OBJECTS)

clean:
	$(RM) maze $(OBJECTS)
