import urllib2, cStringIO
from PIL import Image
import numpy as np
import cv2
from cv2 import cv

class AxisCamera:
    def __init__(self, ip):
        self.ip = ip
        username = "FRC"
        password = "FRC"
        url = "http://"+self.ip + "/jpg/image.jpg"

        passman = urllib2.HTTPPasswordMgrWithDefaultRealm()        
        passman.add_password(None, url, username, password)
        authhandler = urllib2.HTTPBasicAuthHandler(passman)
        self.opener = urllib2.build_opener(authhandler)        
    
    def get(self):
        urllib2.install_opener(self.opener) 
             
        url = "http://"+self.ip + "/jpg/image.jpg"                

        img_url = urllib2.urlopen(url).read() # get Image from url
        file = cStringIO.StringIO(img_url)
        source = Image.open(file).convert("RGB") # Convert to RGB Image
        bitmap = cv.CreateImageHeader(source.size, cv.IPL_DEPTH_8U, 3) # create new Image as cv Matrix
        cv.SetData(bitmap, source.tostring()) # Convert Image to cv Matrix
        cv.CvtColor(bitmap, bitmap, cv.CV_RGB2BGR) # Convert RGB to BGR
        
        
        return np.asarray(bitmap[:,:]) # Convert cv Matrix to numpy array
