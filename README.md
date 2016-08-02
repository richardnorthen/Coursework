# Coursework
This repository contains notable work from my classes at the University of Oregon.

## CIS 212 - Spring 2014
__Computer Science III__ was taught in Java and covered the basic principles of object-oriented programming. Weekly assignments required us to apply what we had learned that week into a program, following any requirements from the instructors.

### [SimplePainter](SimplePainter/)
One of my assignments required me to create an application that captured mouse movement. I developed __SimplePainter__, a simple drawing program using a Swing GUI. Important notes:
* 4 different colors, 2 tools, and a MouseMotionAdapter were implemented as per the requirements
* 3 brush sizes and a MouseAdapter were also used
* brush color and brush size aren't displayed as plain-text (_yet!_)
* Main:107 a non-zero weight was used to maintain a consistent layout

![SimplePainter](/SimplePainter/preview.png)

### [DataManipulator](DataManipulator/)
This assignment focused on the use of sorting algorithms and their implementations in Java. __DataManipulator__ can generate sets of UUIDs (up to 50,000) and perform a selection and/or merge sort on them. The application is threaded, so that the GUI remains responsive while working in the background. Important notes:
* 