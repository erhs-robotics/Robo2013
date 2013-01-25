import cv2
from cv2 import cv
import numpy as np
import cPickle
import sys
from Rectangle import Rectangle


class imgproc:

	def __init__(self, cam):
		if cam >= 0:
			self.camera = cv2.VideoCapture(cam)
		#self.GREEN_MIN = np.array([50, 100, 100], np.uint8)
		#self.GREEN_MAX = np.array([100, 255, 255], np.uint8)
		self.GREEN_MIN = np.array([70, 138, 156], np.uint8)
		self.GREEN_MAX = np.array([100, 255, 255], np.uint8)
        
		self.YELLOW_MIN = np.array([0, 100, 100], np.uint8)
		self.YELLOW_MAX = np.array([30, 255, 255], np.uint8)
		
	def getCameraImage(self):
		_, self.cam_img = self.camera.read()
		self.cam_img = cv2.blur(self.cam_img,(3,3))
		return self.cam_img
		
	def getHSVImage(self, cam_img):
		self.hsv_img = cv2.cvtColor(cam_img, cv2.COLOR_BGR2HSV)
		return self.hsv_img

	def getThreshImage(self, hsv_img, min_array, max_array):
		self.thresh_img = cv2.inRange(hsv_img, min_array, max_array)
		self.thresh_img = cv2.medianBlur(self.thresh_img, 5)
		return self.thresh_img
		
	def getContours(self, image):
		self.contours, _ = cv2.findContours(image,
											cv2.RETR_LIST,
											cv2.CHAIN_APPROX_SIMPLE)
        #this is for a bug in opencv, it should be fixed in the newest
        #version or a later version
		tmp = cPickle.dumps(self.contours)
		self.contours = cPickle.loads(tmp)
		
		return self.contours
		
	def fillContours(self, image, contours):
		for i in range(len(contours)):
			x, y, w, h = cv2.boundingRect(contours[i])
			cv2.rectangle(image,(x,y),(x+w,y+h),(0,255,255),-1)
			
	def getBoundingRectangles(self, contours):
		rects = []
		for i in range(len(contours)):
			center_x, center_y, width, height = cv2.boundingRect(contours[i])
			rect = Rectangle(center_x, center_y, width, height)
			rects.append(rect)
		return rects
	
	def doImgProc(self, cam_img):
		cam_img = cv2.blur(cam_img,(3,3))
		
		hsv_img = self.getHSVImage(cam_img)
		thresh_img = self.getThreshImage(hsv_img, self.GREEN_MIN, self.GREEN_MAX) #cv2.inRange(hsv_img, GREEN_MIN, GREEN_MAX)
		
		thresh_contours = self.getContours(thresh_img.copy())
		cv2.drawContours(cam_img, thresh_contours, -1, (0,0,255), 3)
		
		rects = self.getBoundingRectangles(thresh_contours)
		
		return (rects, cam_img)		
		
	def getRect(self, img):
		img = cv2.blur(img,(3,3))
		
		hsv_img = self.getHSVImage(img)
		thresh_img = self.getThreshImage(hsv_img, self.GREEN_MIN, self.GREEN_MAX)
		thresh_contours = self.getContours(thresh_img)
		rects = self.getBoundingRectangles(thresh_contours)
		return rects
		
