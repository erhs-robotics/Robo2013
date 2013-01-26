import cv2
from cv2 import cv
import numpy as np
import sys
from imgproc import *
from Rectangle import Rectangle
from AxisCam import AxisCamera
import math

#focal_length_pixels = distance_mm * disparity_pixels / baseline_mm;
#sensor_pixels_per_mm = focal_length_pixels / focal_length_mm;
class StereoVision:
    def __init__(self):
        self.CAM_PIXEL_WIDTH = 640
        self.CAM_PIXEL_HEIGHT = 480
        self.CAM_DISTANCE = .16# meters
        self.FOCAL_LEN = 297.647058824# * pixels_per_mm
        #self.PPMM = # Pixels per millimeter 
    
    def getDistance(self, left_rects, right_rects):
        center_x = self.CAM_PIXEL_WIDTH / 2
        left_x = left_rects[0].center_mass_x
        right_x = right_rects[0].center_mass_x
        
        disparity = abs(left_x - right_x)
        
        dist = (self.FOCAL_LEN * self.CAM_DISTANCE) / disparity
        print disparity
        print dist
        
        return dist
