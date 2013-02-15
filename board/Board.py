#!/usr/bin/python2
"""Specifically for testing code on the Beagle Board"""
import sys, os
sys.path.append('lib')
import cv2, freenect
from cv2 import cv
import numpy as np

from imgproc import *
from Kinect import Kinect

imgproc = Imgproc(-1)

kinect = Kinect()

params = list()
params.append(cv.CV_IMWRITE_PNG_COMPRESSION)
params.append(8)

x_pos = 0
y_pos = 0
            
while True:
    depth = kinect.get_depth()
    
    bgr = kinect.get_video()
    rects, rects_img = imgproc.doImgProc(bgr)
    
    cv2.imwrite("target.png", np.array(bgr[::2,::2,::-1]), params)
    
    max_rect = imgproc.getMaxRect(rects)
    if max_rect:
        print max_rect.x, max_rect.y
        x_pos = max_rect.x
        y_pos = max_rect.y
    else:
        print "No Target Found!"
    
    distance = kinect.get_depth_at(x_pos, y_pos)
    print distance * 100 / 2.54
    
    if cv2.waitKey(5) == 27:
        break
