#!/usr/bin/python2
import sys
sys.path.append('../board/lib')
from Com import Com

com = Com("10.0.53.42", 80)

while True: 
    try:
        com.recieve_stream()        
        com.write_stream('HTTP/1.0 200 OK\r\n')
        com.write_stream("Content-Type: application/json\r\n")
        com.write_stream("Access-Control-Allow-Origin: *\r\n\r\n")
        com.write_stream('{"status": "Ready", "image" : "http://a57.foxnews.com/global.fncstatic.com/static/managed/img/Scitech/0/371/tardar-sauce-the-cat.jpg", "message" : "yo"}') 
        com.close_stream()
    except NameError as e:
        print e
        s.close()
        exit()
