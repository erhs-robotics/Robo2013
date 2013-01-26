#include "Vision.h"

Vision::Vision ( bool webcam ) {
  HSV_lower = Scalar(70, 138, 156);
  HSV_upper = Scalar(100, 255, 255);

}

Mat Vision::getHSVimage (Mat image) {
  //IplImage* hsv_image = cvCreateImage(cvGetSize(image), 8, 3);
  Mat hsv_image;
  cvtColor(image, hsv_image, CV_BGR2HSV);
  //cvCvtColor(image, hsv_image, CV_BGR2HSV);
  return hsv_image;

}

Mat Vision::doImgProc (Mat image) {
  Mat binimg;
  vector<vector<Point> > contours;
  blur(image, image, Size(3,3));
  
  cvtColor(image, binimg, CV_BGR2HSV);
  inRange(binimg, HSV_lower, HSV_upper, binimg);
  findContours(binimg, contours, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
  for( int i = 0; i< contours.size(); i++ ) {    
    drawContours( image, contours, i, Scalar( 0, 0, 255 ), 3);
  }
 
  
 
  return image;

}

Vector<Rect> Vision::getRects(Mat image) {
  Mat binimg;
  vector<vector<Point> > contours;
  blur(image, image, Size(3,3));
  
  cvtColor(image, binimg, CV_BGR2HSV);
  inRange(binimg, HSV_lower, HSV_upper, binimg);
  findContours(binimg, contours, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
  Vector<Rect> rects;
  
  for( int i = 0; i< contours.size(); i++ ) {    
    rects.push_back(boundingRect(contours[i]));    
  }
  
  return rects;

}


