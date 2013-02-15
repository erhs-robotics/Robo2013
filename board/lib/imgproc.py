import cv2
from cv2 import cv
import numpy as np
import cPickle
import sys
from Rectangle import Rectangle


class Imgproc:

    # MIN 31, 69, 144
    # MAX 92, 198, 255

	def __init__(self, cam):
		if cam >= 0:
			self.camera = cv2.VideoCapture(cam)
		#self.GREEN_MIN = np.array([50, 100, 100], np.uint8)
		#self.GREEN_MAX = np.array([100, 255, 255], np.uint8)
		self.GREEN_MIN = np.array([31,69,144], np.uint8) #70, 138, 156
		self.GREEN_MAX = np.array([92,198,255], np.uint8) # 100, 255, 255
        
		self.YELLOW_MIN = np.array([0, 100, 100], np.uint8)
		self.YELLOW_MAX = np.array([30, 255, 255], np.uint8)
		self.LOW = 1.208333333
		self.MED = 2.571428571
		self.HIGH = 4.5
		self.THRESHHOLD = 0.1
		self.LOW_HEIGHT = 0.4826 + 0.3048# meters from floor to center
		self.MED_HEIGHT = 2.25108  + 0.2667# meters from floor to center
		self.HIGH_HEIGHT = 2.64478 + 0.1524# meters from floor to center
		
		
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

	def getMaxRect(self, rects):
		if len(rects) > 0:
			big = rects[0]
			for rect in rects:
				if rect.area > big.area:
					big = rect
			return big
	
	def doImgProc(self, cam_img):
		cam_img = cv2.blur(cam_img,(4,4))
		hsv_img = self.getHSVImage(cam_img)
		
		thresh_img = self.getThreshImage(hsv_img, self.GREEN_MIN, self.GREEN_MAX)
		thresh_contours = self.getContours(thresh_img.copy())
		
		#cv2.drawContours(cam_img, thresh_contours, -1, (0,0,255), 3)
		self.fillContours(cam_img, thresh_contours)
		rects_img = cv2.inRange(cam_img, self.YELLOW_MIN, self.YELLOW_MAX)
		rects_contours = self.getContours(rects_img.copy())
		
		rects = self.getBoundingRectangles(rects_contours)
		
		return (rects, rects_img)		
		
	def getRect(self, img):
		img = cv2.blur(img,(3,3))
		hsv_img = self.getHSVImage(img)
		
		thresh_img = self.getThreshImage(hsv_img, self.GREEN_MIN, self.GREEN_MAX)
		thresh_contours = self.getContours(thresh_img.copy())
		
		self.fillContours(img, thresh_contours)
		rects_img = cv2.inRange(img, self.YELLOW_MIN, self.YELLOW_MAX)
		rects_contours = self.getContours(rects_img.copy())
		
		rects = self.getBoundingRectangles(rects_contours)
		return rects
		
	def filterRects(self, rects):
		filtered = []
		for rect in rects:
			height = self.getTargetHeight(rect)
			if height != 0:
				filtered.append((rect, height))
		return filtered
		
	def getTargetHeight(self, rect):
		ratio = rect.width / rect.height
		#First filter out anything to small
		if rect.width <= 5 or rect.height <= 5:
			return 0
		if abs(ratio - self.LOW) <= self.THRESHHOLD:
			return self.LOW_HEIGHT
		elif abs(ratio - self.MED) <= self.THRESHHOLD:
			return self.MED_HEIGHT
		elif abs(ratio - self.HIGH) <= self.THRESHHOLD:
			return self.HIGH_HEIGHT
		else:
			return 0
			
		
		
