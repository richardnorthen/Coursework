# Coursework
This repository contains notable projects and assignments from time at the University of Oregon.


## Remote Authentication Using Facial Recognition

## Globally Distributed Software Development Project
### Spring 2016
Focused on Android Client that interfaced with another group's server. 

## Circumventing Facial Recognition Authentication Systems
### Winter 2017
Proposal, project, and program developed to test the security of using facial recognition as biometric security.

## Parallel Computing and Image Processing


CIS 423
Spring 2016
	DSD Group Project - Android Client for Facial Recognition Software
CIS 433
Winter 2017
	Group Project - Desktop Facial Recognition Software for Biometric Security
CIS 410 - Parallel Computing
Spring 2015
	Group Project - Parallel Image Processing
	
CIS 415 - Operating Systems
Spring 2016
	BST and AVL for Date comparisons
	Sample network card driver with queue
CIS 399 - Android Apps
Summer 2016
	Tide and BriskNote
CIS 422
Winter 2016
	Group Project 1 - Address Book
	Group Project 2 - Study Companion Program & Wiki
CIS 425
Fall 2016
	Haskell Palindrome Progam
CIS 314 - y86 and Computer Architecture
Fall 2014
	Recursive Fibonacci Generator
CIS 330 - C/C++
Winter 2015
	Maze program in C
	Maze program in C++
CIS 212 - Java Programming with Swing GUI
Spring 2014
	DataManipulator
	SimplePainter


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