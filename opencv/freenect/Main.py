#!/usr/bin/python2
import cv2 
from cv2 import cv
import numpy as np
from freenect import sync_get_depth as get_depth, sync_get_video as get_video
from math import tan
from imgproc import *

imgproc = imgproc(-1)

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

while True:

    depth,_ = get_depth()
    rgb,_ = get_video()
    
    bgr = cv2.cvtColor(rgb, cv2.COLOR_RGB2BGR)
    rects, rects_img = imgproc.doImgProc(bgr)
    if len(rects) > 0:
        max_rect = rects[0]
        for i in range(len(rects)):
            if rects[i].area > max_rect.area:
                max_rect = rects[i]
                
        print max_rect.x, max_rect.y
        #x_pos = max_rect.x
        #y_pos = max_rect.y
    
    cv2.circle(rgb, (x_pos, y_pos), 2, (255,255,0), 5)        
    
    raw_depth_val = depth[y_pos][x_pos]
    distance = 0.1236 * tan(raw_depth_val / 2842.5 + 1.1863) #raw->meters
    print distance * 100 / 2.54# meters->feet->in
    
    # Build a two panel color image
    d3 = np.dstack((depth,depth,depth)).astype(np.uint8)
    da = np.hstack((d3,rgb))
    
    cv2.imshow('Depth and RGB',np.array(da[::2,::2,::-1]))
    cv2.imshow('RGB Mods', rects_img)
    if cv2.waitKey(5) == 27:
        break
