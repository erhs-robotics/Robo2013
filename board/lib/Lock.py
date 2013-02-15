import os
import time
def lockfile(name):
    lockfile = open(name + ".lock", 'w')
    lockfile.close()
    
def unlockfile(name):
    if islocked(name):
        os.remove(name + ".lock")
    
def islocked(name):
    try:
        with open(name + ".lock") as f: return True
    except IOError as e:
        return False
        
def waitforlock(name, timeout=5):
    start = time.time()
    while islocked(name):
        if time.time() - start > timeout:
            break

    
