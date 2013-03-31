#!/usr/bin/python2
import sys
import os
sys.path.append('lib')
import cv2 
from cv2 import cv
import numpy as np
from imgproc import *
import Lock

imgproc = Imgproc(-1)

params = [cv.CV_IMWRITE_PNG_COMPRESSION, 8]
SCALE_FACTOR = 3
msg = ""
            
def getTargets():
    image = cam_img = cv2.imread(sys.argv[1], cv2.CV_LOAD_IMAGE_COLOR)    
    
    targets = imgproc.getRect(image)
    
    targets = imgproc.filterRects(targets)
    imgproc.labelRects(image, targets)
    print targets
    return targets, image
    
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
	i = 0  
    while True:
        print "Looping"
        print "[" + i + "]"
        rects, bgr = getTargets()
        json = encodeTargets(rects)
        writeInfo(bgr, json)
        i += 1  
