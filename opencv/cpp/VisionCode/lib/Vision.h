#ifndef VISION_H
#define VISION_H
#include <cv.h>
#include <highgui.h>
#include <cstdio>

using namespace cv;
class Vision {
public:
    Vision(bool webcam);
    IplImage* getCameraImage();
    IplImage* doImgProc(IplImage*);
    void getRects();
private:
    IplImage* getHSVimage(IplImage*);
    IplImage* getThreshImage(IplImage*);
    CvSeq* getContours(IplImage*);
    void fillCountours(IplImage*, CvSeq*);
    void getBoundingRectangles();
    CvScalar HSV_lower;
    CvScalar HSV_upper;
    
};

struct Rectangle {
  int x, y, width, height, center_x, center_y;
  Rectangle(int x, int y, int width, int height) {
    this->x = x;
    this->y = y;
    this->width = width;
    this->height = height;
    this->center_x = x + width / 2;
    this->center_y = y + height / 2;
  }

  
};
#endif
