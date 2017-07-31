/* author: norris */

#ifndef MAZE_HPP_
#define MAZE_HPP_

#include <fstream>

// Maze class
class Maze {
public:
    // constructor and destructor
    Maze(int size);
    ~Maze() {}

    // enum for keeping track of facing direction
    enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    };
    
    // copy a maze from a file into the array
    void readFromFile(std::ifstream &f);

    // take a single step to solving the maze
    void step();

    // check if we have found the exit of the maze
    bool atExit();

    // return the current position of the current path
    void getCurrentPosition(int &row, int &col);

private:
    // maze array and size
    char **maze;
    int n = 0;
    
    // current position and direction
    int row = 0, col = 0;
    Direction facing = UP;
};

#endif