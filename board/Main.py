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
#kinect = Kinect()

params = [cv.CV_IMWRITE_PNG_COMPRESSION, 8]
SCALE_FACTOR = 3
msg = ""
            
def getTargets():
    #rgb,_ = freenect.sync_get_video()
    #depth = kinect.get_depth()    
    bgr = imgproc.getCameraImage()
    
    rects, rects_img = imgproc.doImgProc(bgr)
    
    targets = imgproc.filterRects(rects)
    #imgproc.labelRects(bgr, rects)
    return targets, bgr, rects_img
    
def encodeTargets(targets):
    json_template = '{"status": "%s", "message" : "%s", "target" : "%s"}'
    target_str = ""
    for target in targets:
        dist = 0
        string = "%s,%s,%s" % (target.center_mass_x, dist, target.target_height)
        target_str += string + "|"
    status = "Found " + str(len(targets)) + " Targets"
    
    json = json_template % (status, msg, target_str)
    return json

def writeInfo(image, json):
    Lock.waitforlock("info.json")
    Lock.lockfile("info.json")
    info = open("info.json", "w")
    info.seek(0)
    info.write(json)
    info.truncate()
    info.close()
    Lock.unlockfile("info.json") 
    
    Lock.waitforlock("target.png")
    Lock.lockfile("target.png")
    cv2.imwrite("target.png", cv2.resize(bgr, (bgr.shape[1]/SCALE_FACTOR, bgr.shape[0]/SCALE_FACTOR)), params)
    Lock.unlockfile("target.png")
    
if __name__ == '__main__':      
    while True:
        rects, bgr, rects_img = getTargets()
        json = encodeTargets(rects)
        writeInfo(bgr, json)
        cv2.imshow('RGB Mods', bgr)
        if cv2.waitKey(5) == 27:
            break
