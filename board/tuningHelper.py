#!/usr/bin/python2
import sys, getopt
sys.path.append('lib')
import cv2 
from cv2 import cv
import numpy as np
from freenect import sync_get_depth as get_depth, sync_get_video as get_video
from math import tan
from imgproc import *
from Kinect import Kinect

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
    
# 1 - Webcam
# 2 - File
action = 2

imgproc = None
if action == 1:
    imgproc = Imgproc(0)
else:
    imgproc = Imgproc(-1)

kinect = None
#if len(sys.argv) < 1:
kinect = Kinect()

cv2.namedWindow('Original, HSV, Thresh, Rects')
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
        if action == 2:
            cam_img = cv2.imread(sys.argv[1], cv2.CV_LOAD_IMAGE_COLOR)
            #cam_img = cv2.cvtColor(cam_img, cv2.COLOR_RGB2BGR)
        elif action == 1:
            cam_img = imgproc.getCameraImage()
    else:
        cam_img = kinect.get_IR_image()
        #rgb_img = kinect.get_video()

    rects, rects_img = imgproc.doImgProc(cam_img)
    for i in range(len(rects)):
        print rects[i].x + rects[i].width/2, rects[i].y + rects[i].height/2
        
    imgproc.labelRects(cam_img, rects)
    
    color = np.hstack((cam_img, imgproc.hsv_img)) #, imgproc.thresh_img, rects_img))
    binary = np.hstack((imgproc.thresh_img, rects_img))
    binary_3d = np.dstack((binary, binary, binary)).astype(np.uint8)
    all_images = np.hstack((color, binary_3d))
    cv2.imshow('Original, HSV, Thresh, Rects', np.array(all_images[::2,::2,::1]))
    
    if cv2.waitKey(5) == 27:
        break
            
cv2.destroyAllWindows()
