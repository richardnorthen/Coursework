#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

/* identifies direction (compiles as 0, 1, 2, 3)*/
typedef enum directionEnum direction;
enum directionEnum {
    UP,
    RIGHT,
    DOWN,
    LEFT
};

/* point type for tracking location in maze */
typedef struct pointStruct point;
struct pointStruct {
    int i;
    int j;
};

/* checks if we are still solving the maze */
bool isSolving(int size, char maze[size][size], point position) {
    if (position.i == 0 || position.j == 0 || position.i == size-1 || position.j == size-1) {
        /* if we are on any edge of the maze we are at the exit */
        return false;
    } else {
        /* if not we are still solving the maze */
        return true;
    }
}

/* moves the current position of the path and returns whether or not it was successful */
bool move(int size, char maze[size][size], point *position, direction *facing, direction movement) {
    /* calculate the absolute direction we want to travel (this is the direction relative to the maze and not the position) */
    direction absoluteDirection = (*facing + movement) % 4; 

    /* move according to the direction we are facing and the direction of movement we want */
    switch (absoluteDirection) {
        case RIGHT:
            /* check that the movement is valid */
            if (maze[position->i][position->j+1] == '.') {
                /* update the position and the facing direction */
                position->j = position->j + 1;
                *facing = RIGHT;
                /* print the correct step we took */
                printf("RIGHT\n");
                return true;
            } else {
                return false;
            }
            break;

        case UP:
            if (maze[position->i-1][position->j] == '.') {
                position->i = position->i - 1;
                *facing = UP;
                printf("UP\n");
                return true;
            } else {
                return false;
            }
            break;

        case LEFT:
            if (maze[position->i][position->j-1] == '.') {
                position->j = position->j - 1;
                *facing = LEFT;
                printf("LEFT\n");
                return true;
            } else {
                return false;
            }
            break;

        case DOWN:
            if (maze[position->i+1][position->j] == '.') {
                position->i = position->i + 1;
                *facing = DOWN;
                printf("DOWN\n");
                return true;
            } else {
                return false;
            }
            break;

        default:
            /* if we reach this then something went very wrong */
            printf ("unexpected case in switch(absoluteDirection), exiting");
            exit(1);
    }
}

/* finds and prints the path through a maze as it calculates it */
void mazeSolve(int size, char maze[size][size], point position) {
    /* use the edge of the starting position to determine what direction we begin facing */
    direction facing = UP;
    if (position.i == 0) {
        facing = DOWN;
    } else if (position.i == size-1) {
        facing = UP;
    } else if (position.j == 0) {
        facing = RIGHT;
    } else if (position.j == size-1) {
        facing = LEFT;
    }

    /* strategy
     * - to solve the maze, we keep the maze wall to our right until we have found the exit
     * - we keep track of our current position in the maze and the position we are facing
     * - we then move in such a pattern that we always keep a wall to our right
     */

    /* flag to determine if we have completed the maze */
    bool solving = true;
    while (solving) {
        /* try to move right, then up, then left, then down */
        if (move(size, maze, &position, &facing, RIGHT)) {
             /* move() will update position and facing and print the direction */
        } else if (move(size, maze, &position, &facing, UP)) {
             /* move() will update position and facing and print the direction */
        } else if (move(size, maze, &position, &facing, LEFT)) {
             /* move() will update position and facing and print the direction */
        } else if (move(size, maze, &position, &facing, DOWN)) {
             /* move() will update position and facing and print the direction */
        } else {
            printf("unexpected case in move(direction), exiting");
            exit(1);
        }

        /* update the flag */
        solving = isSolving(size, maze, position);
    }
}

int main(int argc, const char *argv[]) {
    /* check that argument for file is given */
    if (argc != 2) {
        printf("no input file name\n");
        return 1;
    }

    /* open given file */
    FILE *fp;
    fp = fopen(argv[1], "r");
    if (fp == NULL) {
        printf("can't open file %s", argv[1]);
        return 1;
    }

    /* check the number of mazes and loop for each one */
    int mazes = 0;
    fscanf(fp, "%d\n", &mazes);

    for (int maze = 0; maze < mazes; maze++) {
        /* get the size of the maze*/
        int size = 0;
        fscanf(fp, "%d\n", &size);

        /* copy the maze into an array of chars */
        char maze[size][size];
        point start = {0, 0};
        /* grab each line from the file */
        for (int i = 0; i < size; i++) {
            char line[size];
            fscanf(fp, "%s", line);
            /* copy the line into the array */
            for (int j = 0; j < size; j++) {
                maze[i][j] = line[j];
                /* make a note of the starting point if we find it */
                if (maze[i][j] == 'x') {
                    start.i = i;
                    start.j = j;
                }
            }
        }

        /* start pathing through the maze */
        printf("ENTER\n");
        mazeSolve(size, maze, start);
        printf("EXIT\n***\n");
        /* move on to next maze */
    }

    fclose(fp);
    return 0;
}
