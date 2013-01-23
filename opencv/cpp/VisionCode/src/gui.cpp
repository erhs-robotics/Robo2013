#include <cv.h>
#include <highgui.h>
#include <cstdio>
using namespace cv;
// A Simple Camera Capture Framework
int main() {
    CvCapture* capture = cvCaptureFromCAM ( CV_CAP_ANY );
    if ( !capture ) {
        fprintf ( stderr, "ERROR: capture is NULL \n" );
        getchar();
        return -1;
    }
    
    cvNamedWindow ( "mywindow", CV_WINDOW_AUTOSIZE );
    
    while (true) {
        
        IplImage* frame = cvQueryFrame ( capture );
        blur(frame, frame)
        IplImage* imgHSV = cvCreateImage(cvGetSize(frame), 8, 3);
        IplImage* imgThreshed = cvCreateImage(cvGetSize(frame), 8, 1);
        cvCvtColor(frame, imgHSV, CV_BGR2HSV);
        
        
		
        cvInRangeS(imgHSV, cvScalar(60, 100, 100), cvScalar(100, 255, 255), imgThreshed);
        if ( !frame ) {
            fprintf ( stderr, "ERROR: frame is null...\n" );
            getchar();
            break;
        }
        cvShowImage ( "mywindow", imgThreshed );
        
        //if Esc pressed
        if ( ( cvWaitKey ( 10 ) & 255 ) == 27 ) break;
    }
    
    cvReleaseCapture ( &capture );
    cvDestroyWindow ( "mywindow" );
    return 0;
}
