import urllib2, cStringIO
from PIL import Image
import numpy as np
import cv2
from cv2 import cv
import cPickle

class AxisCamera:
    def __init__(self, ip):
        self.ip = ip
        username = "FRC"
        password = "FRC"
        url = "http://"+self.ip + "/jpg/image.jpg"

        passman = urllib2.HTTPPasswordMgrWithDefaultRealm()
        
        passman.add_password(None, url, username, password)
        

        authhandler = urllib2.HTTPBasicAuthHandler(passman)
        

        opener = urllib2.build_opener(authhandler)

        urllib2.install_opener(opener)
    
    def get(self):        
        url = "http://"+self.ip + "/jpg/image.jpg"
                

        img_url = urllib2.urlopen(url).read()
        file = cStringIO.StringIO(img_url)
        source = Image.open(file).convert("RGB")
        bitmap = cv.CreateImageHeader(source.size, cv.IPL_DEPTH_8U, 3)
        cv.SetData(bitmap, source.tostring())
        cv.CvtColor(bitmap, bitmap, cv.CV_RGB2BGR)
        
        
        return np.asarray(bitmap[:,:])
        
#cam = AxisCamera("10.0.53.11")

#cv2.namedWindow('Axis 1')

#while 1:
    
    
#    cam_img = cam.get()

    
    
#    cv2.imshow('Axis 1', cam_img)
    
#    if cv2.waitKey(5) == 27:
#        break
            
#cv2.destroyAllWindows()
        
