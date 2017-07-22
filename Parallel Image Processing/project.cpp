#include <dirent.h>
#include <getopt.h>

#include <emmintrin.h>

#include <cv.h>
#include <opencv2/highgui/highgui.hpp>

#include <omp.h>

using namespace cv;
using namespace std;

Mat standardize(Mat img, char *pathnamein)
  /*
   * standardize all images before transforming or applying filters to them
   */
{
  char pathname[80];
  memcpy(pathname, pathnamein, strlen(pathnamein)+1);

  Mat stdImage(200,200, CV_8UC3);
  resize(img, stdImage, stdImage.size(), 0, 0, INTER_NEAREST);

  strcat(pathname, "_stdzd.jpg"); 
  imwrite(pathname, stdImage);

  return stdImage;
}

Mat sobelImageFilter(Mat img, char *pathnamein) 
  /*
   * create a new image with the sobel filter provided by OpenCV
   */
{
  char pathname[80];
  memcpy(pathname, pathnamein, strlen(pathnamein)+1);

  Mat grey;
  cvtColor(img, grey, CV_BGR2GRAY);

  Mat sobelx;
  Sobel(grey, sobelx, CV_32F, 1, 0);

  double minVal, maxVal;
  minMaxLoc(sobelx, &minVal, &maxVal); //find minimum and maximum intensities
  Mat draw;
  sobelx.convertTo(draw, CV_8U, 255.0/(maxVal - minVal), -minVal * 255.0/(maxVal - minVal));

  strcat(pathname, "_sobel.jpg"); 
  imwrite(pathname, sobelx);

  return sobelx;
}

static int custom_blur = 0;
Mat blurImageFilter(Mat image, char *pathnamein)
  /*
   * create new image using the blur filter (optionally use our own parallel blur)
   */
{
  char pathname[80];
  memcpy(pathname, pathnamein, strlen(pathnamein)+1);

  Mat blur;
  int scale = 17;

  if (custom_blur) {
    blur.create(image.rows, image.cols, image.type());

    // must be an odd number greater than 1
    // compute the grid values to use
    int start, end, total;
    if (scale > 2 && scale % 2) {
      end = (scale-1) / 2;
      start = end * -1;
      total = scale * scale;
    }

    // iterate over every pixel
#pragma omp parallel for
    for (int row = 0; row < image.rows; row++) {
      for (int col = 0; col < image.cols; col++) {
        // apply stencil to each pixel
        Vec3i pixel(0, 0, 0);
        for (int x = start; x <= end; x++) {
          for (int y = start; y <= end; y++) {
            // handle boundary case
            int row_off = row+x, col_off = col+y;
            if (0 > row_off || row_off >= image.rows) {
              row_off = row;
            }
            if (0 > col_off || col_off >= image.cols) {
              col_off = col;
            }
            // total up the neighboring pixel values
            pixel += image.at<Vec3b>(row_off, col_off);
          }
        }
        // average the values
        pixel = pixel / total;

        // build the blurred image
        blur.at<Vec3b>(row, col) = pixel;
      }
    }
  } else {
    medianBlur(image, blur, scale);
  }

  strcat(pathname, "_blurred.jpg"); 
  imwrite(pathname, blur);

  return blur;
}

Mat invertImageFilter(Mat image, char *pathnamein)
  /*
   * create new inverted image
   */
{
  char pathname[80];
  memcpy(pathname, pathnamein, strlen(pathnamein)+1);

  Mat invert;
  invert.create(image.rows, image.cols, image.type());

#pragma omp parallel for
  for (int row = 0; row < image.rows; row++) {
    for (int col = 0; col < image.cols; col++) {
      invert.at<Vec3b>(row,col)[0] = 255 - image.at<Vec3b>(row,col)[0];
      invert.at<Vec3b>(row,col)[1] = 255 - image.at<Vec3b>(row,col)[1];
      invert.at<Vec3b>(row,col)[2] = 255 - image.at<Vec3b>(row,col)[2];
    }
  }

  strcat(pathname, "_inverted.jpg");
  imwrite(pathname, invert);

  return invert;
}

int main(int argc, char *argv[])
  /*
   * start the program, reading in various arguments
   * -f, --folder   the target folder with the images
   * --custom_blur  use our custom parallel blur function instead of the default OpenCV one
   * --display      preview each image and the processed versions sequentially (the program is forced to run in serial to enable this feature)
   * --verbose      show output log in console
   */
{
  // local flags and variables
  int c, threads, display_flag = 0, verbose_flag = 0;
  char *folder = NULL;
  DIR *dir = NULL;

  // read in the arguments
  while (true) {
    static struct option long_options[] =
    {
      {"folder", required_argument, 0, 'f'},
      {"custom_blur", no_argument, &custom_blur, 1},
      {"display", no_argument, &display_flag, 1},
      {"verbose", no_argument, &verbose_flag, 1},
      {0, 0, 0, 0}
    };

    int option_index = 0;
    c = getopt_long(argc, argv, "f:", long_options, &option_index);
    if (c == -1) {
      break;
    }
    switch (c) {
      case 0:
        if (long_options[option_index].flag != 0) {
          break;
        }
        cout << "option " << long_options[option_index].name;
        if (optarg) {
          cout << "with arg " << optarg;
        }
        cout << endl;
        break;
      case 'f':
        folder = optarg;
        break;
      default:
        exit(1);
    }
  }

  // try to open the directory
  if ((dir = opendir(folder)) == NULL) {
    cout << "the directory could not be opened" << endl;
    return -1;
  } else {
    double start_timer = omp_get_wtime();
    struct dirent *ent;
    int num_dir = 0;

    // count the files
    while ((ent = readdir(dir)) != NULL) {
      num_dir++;
    }
    if (verbose_flag) cout << "total files: " << num_dir << endl;

    // force serial execution if the user wants to display the images sequentially
    if (display_flag) omp_set_num_threads(1);

    // reset directory pointer and process the files
    rewinddir(dir);
#pragma omp parallel for
    for (int i = 0; i < num_dir; i++) {
      // get file
      ent = readdir(dir);
      if (ent->d_name[0] != '.') {
        Mat image;
        char path[80];
        path[0] = '\0';
        strcat(path, folder);
        strcat(path, ent->d_name);

        // check that we can open the file
        image = imread(path, 1);
        if (!image.data) {
          cout << "image \"" << path << "\" could not be opened, skipping..." << endl;
          continue;
        }

        // process the file
        Mat std, invert, blur, sobel;
        if (verbose_flag) cout << "standardizing image: " << path << endl;
        std = standardize(image, path);
        if (verbose_flag) cout << "inverting image: " << path << endl;
        invert = invertImageFilter(std, path);
        if (verbose_flag) cout << "blurring image: " << path << endl;
        blur = blurImageFilter(std, path);
        if (verbose_flag) cout << "sobeling image: " << path << endl;
        sobel = sobelImageFilter(std, path);

        // display the images if desired
        if (display_flag) {
          namedWindow("Original image", WINDOW_AUTOSIZE);
          namedWindow("Standardized image", WINDOW_AUTOSIZE);
          namedWindow("Inverted image", WINDOW_AUTOSIZE);
          namedWindow("Blurred image", WINDOW_AUTOSIZE);
          namedWindow("Sobelled image", WINDOW_AUTOSIZE);
          imshow("Original image", image);
          imshow("Standardized image", std);
          imshow("Inverted image", invert);
          imshow("Blurred image", blur);
          imshow("Sobelled image", sobel);
          waitKey(0);
        }

      }
    }
    // done
    closedir(dir);
    double elapsed = omp_get_wtime() - start_timer;
    if (verbose_flag) cout << "total time elapsed processing " << num_dir << " images: " << elapsed << endl;
  }

  return 0;
}
