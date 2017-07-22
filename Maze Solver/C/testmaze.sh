#!/bin/bash
./maze maze_input_large.txt > maze_output_large.txt
diff maze_output_large.txt maze_output_large_solution.txt
