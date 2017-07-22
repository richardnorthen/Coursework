SOURCES = maze.cpp testMaze.cpp
HEADERS = maze.hpp
OBJECTS = $(SOURCES:.cpp=.o)

CC = g++
CFLAGS = -std=c++11 -Wall -g

%.o: %.cpp
	$(CC) $(CFLAGS) -c $<

testMaze: $(OBJECTS)
	$(CC) $(CFLAGS) -o $@ $(OBJECTS)

main: utils.o main.o maze.o
	$(CC) $(CFLAGS) -o $@ main.o utils.o maze.o

clean:
	$(RM) testMaze $(OBJECTS) main.o utils.o
