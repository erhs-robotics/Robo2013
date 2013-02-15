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

kinect = Kinect()

print "Beginning"

params = list()
params.append(cv.CV_IMWRITE_PNG_COMPRESSION)
params.append(8)
SCALE_FACTOR = 6
            
while True:

    print "Loop Begin"

    rgb,_ = freenect.sync_get_video()
    depth = kinect.get_depth()
    
    bgr = kinect.get_video()
    rects, rects_img = imgproc.doImgProc(bgr)
    
    cv2.imwrite("target.png", cv2.resize(bgr, (bgr.shape[1]/SCALE_FACTOR, bgr.shape[0]/SCALE_FACTOR)) , params)
    
    max_rect = imgproc.getMaxRect(rects)
    if max_rect:
        print max_rect.x, max_rect.y
        x_pos = max_rect.x
        y_pos = max_rect.y
    else:
        print "No Taget Found!"
    
    cv2.circle(rgb, (x_pos, y_pos), 2, (255,255,0), 5)   
    
    distance = kinect.get_depth_at(x_pos, y_pos)
    print distance * 100 / 2.54# meters->feet->in
    
     Build a two panel color image
    d3 = np.dstack((depth,depth,depth)).astype(np.uint8)
    da = np.hstack((d3,rgb))
    
    cv2.imshow('Depth and RGB',np.array(da[::2,::2,::-1]))
    cv2.imshow('RGB Mods', rects_img)
    if cv2.waitKey(5) == 27:
        break
