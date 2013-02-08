#!/usr/bin/python2
import sys
sys.path.append('../board/lib')
from Com import Com
if len(sys.argv) < 2:
        print "Supply the ip adress in the argument!"
        exit()
com = Com(sys.argv[1], 80)

while True: 
    try:
        com.open_stream()        
        com.pack_string('{"status": "Ready", "image" : "http://a57.foxnews.com/global.fncstatic.com/static/managed/img/Scitech/0/371/tardar-sauce-the-cat.jpg", "message" : "yo"}') 
        com.close_stream()
        
    except NameError as e:
        print "ERROR!"
        print e        
        exit()
