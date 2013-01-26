#include <opencv2/opencv.hpp>
#include "Vision.h"
using namespace cv;

int main(int, char** argv)
{
    VideoCapture cap(argv[1]);
    if(!cap.isOpened())
        return -1;
    
    Vision vision(false);
    
    

    Mat edges;
    namedWindow("edges",1);
    
    for(;;)
    {
	VideoCapture cap(argv[1]);
	Mat frame;
	cap >> frame;
        
        Mat target = vision.doImgProc(frame);
       
        imshow("edges", target);
        if(waitKey(30) >= 0) break;
    }
    
    return 0;
}