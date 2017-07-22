## Makefile
The makefile compiles the code and can optionally measure the performance. The DIR variable is used to specify the working directory.

## project.cpp
Possible arguments:
* -f, --folder	must specify a target folder with all the images
* --custom_blur	will use our group's custom parallel blur function that we wrote instead of OpenCV's
* --display		preview each image sequentially as they are processed (the code will be set to run in serial for this to work)
* --verbose		show the output log in the console

The project.cpp code will read in images from a given directory and apply certain filters to each image. It will standardize each image by resizing it into a 200x200 image and then perform various filters. This standardization will ensure consistent performance between various images so that the performance can be compared.

The filters we use are invert, blur, and sobel.