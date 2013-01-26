#include <opencv2/opencv.hpp>
#include <iostream>
#include "Vision.h"
using namespace cv;
using namespace std;

int main(int, char** argv)
{
    VideoCapture cap(argv[1]);
    if(!cap.isOpened())
        return -1;
    
    Vision vision(false);
    
    

    
    
    for(;;)
    {
	VideoCapture cap(argv[1]);
	Mat frame;
	cap >> frame;
        
        Vector<Rect> rects = vision.getRects(frame);
	
	for(int i=0;i<rects.size();i++) {
	  cout << "["  << rects[i].x     << ", " << rects[i].y
	       << ", " << rects[i].width << ", " << rects[i].height
	       << "]"  << endl;
	}
       
        
        if(waitKey(30) >= 0) break;
    }
    
    return 0;
}