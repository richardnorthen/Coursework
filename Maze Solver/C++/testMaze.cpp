/* author: richardnorthen */

#include <fstream>
#include <iostream>
#include <string>
#include "maze.hpp"

using namespace std;

int main(int argc, const char *argv[]) {
    // check that the arguments are correct
    if (argc != 2) {
        cerr << "no input file name" << endl;
        cerr << "usage: ./" << argv[0] << " input_file.txt" << endl;
    }

    // open the file given in the argument
    std::ifstream file(argv[1]);
    int totalMazes = 0;
    file >> totalMazes;
    
    // loop over each maze in the file
    for (int currentMaze = 0; currentMaze < totalMazes; currentMaze++) {
        int size = 0;
        file >> size;

        // build the maze
        Maze maze(size);
        maze.readFromFile(file);

        // solve the maze
        cout << "ENTER" << endl;
        do {
            maze.step();
        } while (!maze.atExit());
        cout << "EXIT" << endl << "***" << endl;
    }
}