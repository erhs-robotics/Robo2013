#!/usr/bin/python2
import sys
sys.path.append('lib')
import cv2 
from cv2 import cv
import numpy as np
from freenect import sync_get_depth as get_depth, sync_get_video as get_video
from math import tan
from imgproc import *

def update_lowerH(value):
    imgproc.GREEN_MIN[0] = value

def update_lowerS(value):
    imgproc.GREEN_MIN[1] = value
    
def update_lowerV(value):
    imgproc.GREEN_MIN[2] = value
    
def update_upperH(value):
    imgproc.GREEN_MAX[0] = value
    
def update_upperS(value):
    imgproc.GREEN_MAX[1] = value
    
def update_upperV(value):
    imgproc.GREEN_MAX[2] = value
    

imgproc = imgproc(-1)

cv2.namedWindow('Display Window')
cv2.namedWindow('Thresh View')
cv2.namedWindow('Tuning Window')
cv.CreateTrackbar("Lower H", 'Tuning Window', imgproc.GREEN_MIN[0], 255, update_lowerH)
cv.CreateTrackbar("Lower S", 'Tuning Window', imgproc.GREEN_MIN[1], 255, update_lowerS)
cv.CreateTrackbar("Lower V", 'Tuning Window', imgproc.GREEN_MIN[2], 255, update_lowerV)
cv.CreateTrackbar("Upper H", 'Tuning Window', imgproc.GREEN_MAX[0], 255, update_upperH)
cv.CreateTrackbar("Upper S", 'Tuning Window', imgproc.GREEN_MAX[1], 255, update_upperS)
cv.CreateTrackbar("Upper V", 'Tuning Window', imgproc.GREEN_MAX[2], 255, update_upperV)

while 1:
    
    # Load the image from the camera (or a static file for testing)
    if len(sys.argv) > 1:
        cam_img = cv2.imread(sys.argv[1], cv2.CV_LOAD_IMAGE_COLOR)
    else:
        cam_img, _ = get_video()

    #cam_img = cv2.blur(cam_img,(3,3))
    	
    rects, rects_img = imgproc.doImgProc(cam_img)
    for i in range(len(rects)):
        print rects[i].x + rects[i].width/2, rects[i].y + rects[i].height/2
    
    
    cv2.imshow('Display Window', rects_img)
    cv2.imshow('Thresh View', imgproc.thresh_img)
    if cv2.waitKey(5) == 27:
        break
            
cv2.destroyAllWindows()
