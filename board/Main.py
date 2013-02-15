#!/usr/bin/python2
import sys
import os
sys.path.append('lib')
import cv2 
from cv2 import cv
import numpy as np
import freenect
from imgproc import *
from Kinect import Kinect
import Lock
imgproc = Imgproc(0)

x_pos = 100
y_pos = 100
def update_x(value):
    global x_pos
    x_pos = value
def update_y(value):
    global y_pos
    y_pos = value
cv2.namedWindow('Depth and RGB')
cv.CreateTrackbar("X", 'Depth and RGB', x_pos, 632, update_x)
cv.CreateTrackbar("Y", 'Depth and RGB', y_pos, 479, update_y)



print "Beginning"

params = list()
params.append(cv.CV_IMWRITE_PNG_COMPRESSION)
params.append(8)
            
while True:

    print "Loop Begin"

    #rgb,_ = freenect.sync_get_video()
    #depth = kinect.get_depth()
    
    bgr = imgproc.getCameraImage()
    
    rects, rects_img = imgproc.doImgProc(bgr)
    Lock.waitforlock("target.png")
    Lock.lockfile("target.png")
    cv2.imwrite("target.png", np.array(bgr[::2,::2,::-1]), params)
    Lock.unlockfile("target.png") 
    
    
   
    cv2.imshow('RGB Mods', rects_img)
    if cv2.waitKey(5) == 27:
        break
