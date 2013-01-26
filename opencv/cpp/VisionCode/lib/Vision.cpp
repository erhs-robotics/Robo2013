#include "Vision.h"

Vision::Vision ( bool webcam ) {
  HSV_lower = Scalar(70, 138, 156);
  HSV_upper = Scalar(100, 255, 255);

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


