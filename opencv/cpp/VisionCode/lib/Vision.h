#ifndef VISION_H
#define VISION_H
#include <opencv2/opencv.hpp>

using namespace cv;

class Vision {
public:
    Vision(bool webcam);
    Mat getCameraImage();
    Mat doImgProc(Mat);
    Vector<Rect> getRects(Mat);
private:
    Scalar HSV_lower;
    Scalar HSV_upper;
    
};
#endif
