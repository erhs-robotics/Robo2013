#!/usr/bin/python2

import commands
import BaseHTTPServer
from os import curdir, sep
import sys
sys.path.append('lib')
import struct
import Lock


def pack_data(string):
		data = []
		utf8 = string.encode('utf-8')
		length = len(utf8)
		data.append(struct.pack('!H', length))
		format = '!' + str(length) + 's'
		data.append(struct.pack(format, utf8))
		return data

if len(sys.argv) > 1 and sys.argv[1] == "debug":
    COMP_IP = "localhost"
else:
    # Works on Ubuntu-based systems for sure
    COMP_IP = commands.getoutput("/sbin/ifconfig").split("wlan0")[1].split("inet addr:")[1].split(" ")[0]

pic_addr = "http://" + COMP_IP + "/target.png"

HOST_NAME = COMP_IP
PORT_NUMBER = 80

class MyHandler(BaseHTTPServer.BaseHTTPRequestHandler):
    def do_GET(s):
        if s.path == "/target.png":
            s.send_response(200)
            s.send_header("Content-Type", "image/png")
            s.end_headers()
            
            Lock.waitforlock("target.png")
            Lock.lockfile("target.png")
            f = open("target.png", "rb")
            s.wfile.write(f.read())
            f.close()
            Lock.unlockfile("target.png")
        elif s.path == "/crio":
            s.send_response(200)
            s.send_header("Content-Type", "application/json")
            s.send_header("Access-Control-Allow-Origin", "*")
            s.end_headers() 
            Lock.waitforlock("info.json")
            Lock.lockfile("info.json")
            f = open("info.json", "r")
            Lock.unlockfile("info.json")
            data = pack_data(f.read())         
            s.wfile.write(data[0])
            s.wfile.write(data[1])            
        else:
            s.send_response(200)
            s.send_header("Content-Type", "application/json")
            s.send_header("Access-Control-Allow-Origin", "*")
            s.end_headers() 
            f = open("info.json", "r")          
            s.wfile.write(f.read())

if __name__ == '__main__':
    server_class = BaseHTTPServer.HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print "Server Starting - %s:%s" % (HOST_NAME, PORT_NUMBER)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print "Server Stopping - %s:%s" % (HOST_NAME, PORT_NUMBER)
