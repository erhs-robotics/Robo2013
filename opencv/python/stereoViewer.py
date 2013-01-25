#!/usr/bin/python2
import sys
sys.path.append('lib')
import cv2
from cv2 import cv
import numpy as np
import sys
from imgproc import *
from Rectangle import *
from AxisCam import AxisCamera
from StereoVision import StereoVision

left_cam = AxisCamera("10.0.53.12")
right_cam = AxisCamera("10.0.53.11")

imgproc = imgproc(-1)
cv2.namedWindow('Left')
cv2.namedWindow('Right')

vision = StereoVision()

while 1:
    
    left_img = left_cam.get()
    right_img = right_cam.get()
    

    
    	
    left_rects, left_img = imgproc.doImgProc(left_img)
    right_rects, right_img = imgproc.doImgProc(right_img)
    
    vision.getDistance(left_rects, right_rects)
    
    #for i in range(len(rects)):
    #    print rects[i].x + rects[i].width/2, rects[i].y + rects[i].height/2
    
    
    cv2.imshow('Left', left_img)
    cv2.imshow('Right', right_img)
    if cv2.waitKey(5) == 27:
        break
    del(right_img)
            
cv2.destroyAllWindows()
