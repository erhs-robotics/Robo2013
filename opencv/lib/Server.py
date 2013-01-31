import socket 

host = 'localhost' 
port = 5001 
backlog = 5 
size = 1024 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.bind((host,port)) 
s.listen(backlog) 
while 1: 
    client, address = s.accept() 
    data = client.recv(size) 
    if data: 
        client.send('{"status": "Ready", "image" : "http://a57.foxnews.com/global.fncstatic.com/static/managed/img/Scitech/0/371/tardar-sauce-the-cat.jpg", "message" : "yo"}') 
    client.close()
