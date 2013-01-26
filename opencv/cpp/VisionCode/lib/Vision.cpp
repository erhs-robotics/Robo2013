#include "Vision.h"

Vision::Vision ( bool webcam ) {
  HSV_lower = cvScalar(10, 100, 100);
  HSV_upper = cvScalar(255, 255, 255);

}

IplImage* Vision::getHSVimage (IplImage* image) {
  IplImage* hsv_image = cvCreateImage(cvGetSize(image), 8, 3);
  cvCvtColor(image, hsv_image, CV_BGR2HSV);
  return hsv_image;

}

IplImage* Vision::getThreshImage (IplImage* image) {
  IplImage* threshed_image = cvCreateImage(cvGetSize(image), 8, 1);
  cvInRangeS(image, HSV_lower, HSV_upper, threshed_image);
  return threshed_image;

}

CvSeq* Vision::getContours(IplImage* image) {
  CvSeq* contours;
  CvMemStorage *storage=cvCreateMemStorage(0);        
  cvFindContours(image, storage, &contours, sizeof(CvContour), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
  return contours;
}

void Vision::getBoundingRectangles() {

}



IplImage* Vision::doImgProc (IplImage* image) {
  //Mat imgMat(image);
  //blur(imgMat, imgMat, Size(3,3));
  cvSmooth( image, image, CV_GAUSSIAN, 11, 11 );
  IplImage* hsv_image = getHSVimage(image);
  IplImage* threshed_image = getThreshImage(hsv_image);
  CvSeq* contours = getContours(threshed_image);
  
 
  return threshed_image;//new IplImage(imgMat);

}

