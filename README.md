# Portfolio

This repository contains a collection of the notable programs and projects I've worked on. The README provides a brief explanation for each one, as well as links to programs that are in an external repository. Unless otherwise specified, all work is my own.

***

## Facial Recognition as Biometric Verification

**[Globally Distributed Software Development Project](https://classes.cs.uoregon.edu/16S/cis423/) - [Android Client](https://bitbucket.org/richardnorthen/dsd2016-androidclient)**   
Collaborated on a distributed software development project with universities from the USA, Colombia, China, and Thailand to create a concept of operations for remote authentication using facial recognition. I worked in a mixed USA-Colombia team to create an Android App that registered users, took pictures, and interfaced with another team's database server.

**[Circumventing Facial Recognition Authentication Systems](https://github.com/richardnorthen/cis433-project)**  
Created a proposal, program, and report with a partner to test the feasability and security of facial recognition as a biometric authentication measure. I developed the Facial Recognition Authentication System (FRAS) software that interfaced with Microsoft's FaceAPI system to use in our project.

***

## [Parallel Image Processing](Parallel%20Image%20Processing/)

Utilizes OpenCV to perform different image processing techniques on a set of images and OpenMP to parallelize both the processing techniques and pipeline. I developed the application and assisted in creating the report and presentation that demonstrated the increased performance gained by parallel processing.

***

## Android Apps

**[BriskNote](BriskNote/)**  
A lightweight note-keeping app I developed, inspired by Google Keep.

**[Tide v3](Tide%20v3/)**  
Collects predicted and historical tidal information from monitoring stations within the USA. Downloads recently viewed tables onto the device for offline access. *The API has been changed, so this app is currently inoperative*.

***

## Software Development Team Projects

**[Address Book](https://github.com/CIS422Group6/Project1)**  
Created as part of a software development team, this Java application functions as a simple address book for storing contact information. I was the front-end programmer in our team and assisted in creating the documentation and user guides.

**[Study Companion](https://github.com/CIS422Group6/Project2) and [Wiki](https://app.assembla.com/spaces/xis22w16-team6/wiki)**  
Allows a user to create a set of flash-cards for later review or testing. Our team produced a full set of deliverables including requirements, specification, code, documentation, and a wiki page. I was the primary software engineer and tester, and reviewed all of the deliverables my teammates produced.

***

## [Maze Solver](Maze%20Solver/) in C and C++

A set of programs I developed for traversing mazes that was first implemented in C and then ported into C++. Uses the wall-follower method to navigate from start to finish, recording the moves needed to successfully complete the maze. The C++ implementation also prints a diagram for each step taken.

***

## Java Applications

Both of these applications were developed in 2014 and utilize the Swing toolkit to provide a GUI.

**[Simple Painter](Simple%20Painter/)**  
A simple computer graphics program that supports 4 diifferent colors, 3 brush sizes, and an undo function. Captures mouse movement and displays various statistics about the current canvas. A fully defined Javadoc is also included within the source code.

**[Data Manipulator](Data%20Manipulator/)**  
Generates a random set of UUIDs which can be sorted using selection or merge sort. The GUI application is threaded to remain responsive while working in the background.

***

## Miscellaneous

**[UK Tidal Info](https://github.com/richardnorthen/UK-Tidal-Info)**  
A Java CLI program that uses the Environment Agency's [Tide Gauge API](https://environment.data.gov.uk/flood-monitoring/doc/tidegauge#measures) to list tidal information for various stations around the UK.

**[Website Monitor](Website%20Monitor/)**  
Takes a list of websites with dates and stores them in an AVL tree for quick searching and sorting. The data structure is implemented by me and my work is in `date.c` and  `tldlist.c`.

**[Sample Network Driver](Sample%20Network%20Driver/)**  
Simulates a network driver that stores and forwards incoming packets. The driver utilizes a bounded buffer and is non-blocking. My own work is in `networkdriver.c`.

**[Palindrome Program](palindrome.hs)**  
A basic Haskell program that tests whether a given string is a palindrome. Demonstrates various functional programming techniques and their advantages in this context.

**[Fibonacci Generator](recfib.ys)**  
Recursively generates the Fibonacci sequence up to a given number. Written in y86 as an introduction the the x86 assembly language.