import freenect
from math import tan
import cv2

class Kinect:
    def __init__(self):
        pass
        #try:
        #    _,_ = get_depth()
        #except:
        #    raise Exception("Kinect not found!")
    
    def get_depth(self):
        return freenect.sync_get_depth()[0]
        
    def get_video(self):
        rgb = freenect.sync_get_video()[0]
        return cv2.cvtColor(rgb, cv2.COLOR_RGB2BGR)

    # needs testing!!!
    def get_IR_image(self):
        ir = freenect.sync_get_video(0, freenect.VIDEO_IR_10BIT)
        return ir



        
    def get_raw_depth_at(self, x, y):
        return self.get_depth()[y][x]
        
    def get_depth_at(self, x, y):
        raw = self.get_raw_depth_at(x, y)
        return self.raw_to_meters(raw)
        
    def raw_to_meters(self, raw):
        return 0.1236 * tan(raw / 2842.5 + 1.1863)
