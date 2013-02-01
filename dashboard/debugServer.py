#!/usr/bin/python2
import socket 

host = 'localhost' 
port = 85
backlog = 5 
size = 1024 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.bind((host,port)) 
s.listen(backlog) 
while 1: 
    try:
        client, address = s.accept() 
        data = client.recv(size) 
        if data:
            client.send('HTTP/1.0 200 OK\r\n')
            client.send("Content-Type: application/json\r\n")
            client.send("Access-Control-Allow-Origin: *\r\n\r\n")
            client.send('{"status": "Ready", "image" : "http://a57.foxnews.com/global.fncstatic.com/static/managed/img/Scitech/0/371/tardar-sauce-the-cat.jpg", "message" : "yo"}') 
        client.close()
    except:
        s.close()
        exit()
