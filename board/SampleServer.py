#!/usr/bin/python2
import sys
sys.path.append('../board/lib')
import commands
from Com import Com

if len(sys.argv) < 2:
        print "Supply the ip adress in the argument!"
        exit()
com = Com(sys.argv[1], 80)

# Gets the current ip of the machine for sending the target picture
# Works on Ubuntu-based systems for sure
current_ip = commands.getoutput("/sbin/ifconfig").split("wlan0")[1].split("inet addr:")[1].split(" ")[0]
http_serv_port = ":8000"
pic_addr = "http://" + current_ip + http_serv_port + "/target.png"

while True: 
    try:
        com.open_stream()       
        com.read_stream() 
        com.write_stream('HTTP/1.0 200 OK\r\n')
        com.write_stream("Content-Type: application/json\r\n")
        com.write_stream("Access-Control-Allow-Origin: *\r\n\r\n")
        com.write_stream('{"status": "Ready", "image" : "%s", "message" : "yo"}' % pic_addr) 
        com.close_stream()
    
    except KeyboardInterrupt:
        print "Keyboard Interupt"
        print "Closing Communications and Exiting"
        com.close_stream()
        exit()
        
    except NameError as e:
        print "ERROR!"
        print e
        com.close_stream()
        exit()
