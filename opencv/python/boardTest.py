#!/usr/bin/python2
import sys
sys.path.append('lib')
import cv2
from cv2 import cv
import numpy as np
import sys
import cPickle
from imgproc import *
from Rectangle import *

GREEN_MIN = np.array([50, 100, 100], np.uint8)
GREEN_MAX = np.array([100, 255, 255], np.uint8)
YELLOW_MIN = np.array([0, 100, 100], np.uint8)
YELLOW_MAX = np.array([30, 255, 255], np.uint8)

imgproc = imgproc(-1)

while 1:
    
    # Load the image from the camera (or a static file for testing)
    cam_img = cv2.imread(sys.argv[1], cv2.CV_LOAD_IMAGE_COLOR)
    #cam_img = imgproc.getCameraImage()

    #cam_img = cv2.blur(cam_img,(3,3))
    	
    rects = imgproc.getRect(cam_img)
    for i in range(len(rects)):
        print rects[i].x + rects[i].width/2, rects[i].y + rects[i].height/2
    
    #cv2.namedWindow('Display Window')
    #cv2.imshow('Display Window', rects_img)
    #if cv2.waitKey(5) == 27:
    #    break
            
#cv2.destroyAllWindows()
