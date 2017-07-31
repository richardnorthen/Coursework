/* author: richardnorthen */

#include <fstream>
#include <iostream>
#include "maze.hpp"

// maze constructor
Maze::Maze(int size) {
    // create a size * size array
    maze = new char*[size];
    for (int i = 0; i < size; i++) {
        maze[i] = new char[size];

        // initialize the elements
        for (int j = 0; j < size; j++) {
            maze[i][j] = '0';
        }
    }

    // store the size in a class variable
    n = size;
}

// copy a maze from a file into the array
void Maze::readFromFile(std::ifstream &f) {
    // string for holding each line of input
    std::string line(n, '0');

    // check that the file is accessible
    if (f.is_open()) {
        // loop over each line we need to read
        for (int i = 0; i < n; i++) {
            // check that the line is the correct length n
            do {
                getline(f, line);
            } while (line.length() != (unsigned int) n);

            // copy each character from the line into the maze array
            for (int j = 0; j < n; j++) {
                char c = line.at(j);
                maze[i][j] = c;

                // check if the character is the start point and record the location
                if (c == 'x') {
                    row = i;
                    col = j;
                }
            }
        }

        // set the initial facing direction
        if (row == 0) {
            facing = DOWN;
        } else if (row == n-1) {
            facing = UP;
        } else if (col == 0) {
            facing = RIGHT;
        } else if (col == n-1) {
            facing = LEFT;
        } else {
            // will never reach here unless the maze is not laid out correctly
        }
    }
}

// take a single step to solving the maze
void Maze::step() {
    // keep track of the direction we print (as opposed to the direction relative to the current position)
    Direction absoluteDirection = UP;

    // a bit of logic that translates the relative facing direction into the absolute direction
    for (int i = 5; i > 1; i--) {
        // the int is needed to convert into an enum
        int direction = (facing + i) % 4;
        absoluteDirection = (Maze::Direction) direction;

        // process the direction that we want to travel
        switch (absoluteDirection) {
            case RIGHT:
                // if we can move in this direction
                if (maze[row][col+1] == '.') {
                    // update the position and the facing direction
                    col = col + 1;
                    facing = RIGHT;
                    // print the (absolute) move we need to take
                    std::cout << "RIGHT" << std::endl;
                    // and end the loop
                    return;
                }
            break;

            case UP:
                if (maze[row-1][col] == '.') {
                    row = row - 1;
                    facing = UP;
                    std::cout << "UP" << std::endl;
                    return;
                }
            break;

            case LEFT:
                if (maze[row][col-1] == '.') {
                    col = col - 1;
                    facing = LEFT;
                    std::cout << "LEFT" << std::endl;
                    return;
                }
            break;

            case DOWN:
                if (maze[row+1][col] == '.') {
                    row = row + 1;
                    facing = DOWN;
                    std::cout << "DOWN" << std::endl;
                    return;
                }
            break;

            default:
                // unreachable default condition
            break;
        }
    }
}

// check if we have found the exit of the maze
bool Maze::atExit() {
    // if we are on the border we must have found the exit
    if (row == 0 || row == n-1 || col == 0 || col == n-1) {
        return true;
    } else {
        return false;
    }
}

// return the current position of the current path
void Maze::getCurrentPosition(int &row, int &col) {
    row = this->row;
    col = this->col;
}