DIR = soccerBalls/
CC = g++
CFLAGS = -I/usr/include/opencv
LDIR = -L/lib64/lib
LIBS = -lopencv_highgui -lopencv_core -lopencv_objdetect -lopencv_imgproc

all: project

project: project.cpp
	$(CC) $(CFLAGS) $(LDIR) -o project project.cpp $(LIBS) -fopenmp -pg -O3 

valgrind: project
	valgrind --tool=memcheck --time-stamp=yes ./project -f $(DIR)

tau: project
	module load tau;
	tau_exec -v ./project -f $(DIR);
	paraprof profile.0.0.0

check: project
	./project -f $(DIR) --verbose

clean:
	rm ./$(DIR)*inverted* ./$(DIR)*blurred* ./$(DIR)*sobel* ./$(DIR)*std* project gmon.out
