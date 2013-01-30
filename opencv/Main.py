#!/usr/bin/python2
import sys
sys.path.append('lib')
import cv2 
from cv2 import cv
import numpy as np
from imgproc import *
from Kinect import Kinect

imgproc = Imgproc(-1)

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

while True:
    
    bgr = kinect.get_video()
    rects, rects_img = imgproc.doImgProc(bgr)
    
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
    
    # Build a two panel color image
    d3 = np.dstack((depth,depth,depth)).astype(np.uint8)
    da = np.hstack((d3,rgb))
    
    cv2.imshow('Depth and RGB',np.array(da[::2,::2,::-1]))
    cv2.imshow('RGB Mods', rects_img)
    if cv2.waitKey(5) == 27:
        break
