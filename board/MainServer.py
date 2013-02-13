#!/usr/bin/python2

import commands
import BaseHTTPServer
from os import curdir, sep
    
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
            f = open("target.png", "rb")
            s.wfile.write(f.read())
            f.close()
        else:
            s.send_response(200)
            s.send_header("Content-Type", "application/json")
            s.send_header("Access-Control-Allow-Origin", "*")
            s.end_headers()
            s.wfile.write('{"status": "Ready", "image" : "%s", "message" : "yo"}' % pic_addr)

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
