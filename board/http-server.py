import sys
from SimpleHTTPServer import SimpleHTTPRequestHandler as HandlerClass
from BaseHTTPServer import HTTPServer as ServerClass

import commands

if sys.argv[1:]:
    port = int(sys.argv[1])
else:
    port = 8000
    
# Works on Ubuntu-based systems for sure
current_ip = commands.getoutput("/sbin/ifconfig").split("wlan0")[1].split("inet addr:")[1].split(" ")[0]
server_address = (current_ip, port)

protocol     = "HTTP/1.0"
HandlerClass.protocol_version = protocol
httpd = ServerClass(server_address, HandlerClass)

sa = httpd.socket.getsockname()
print "Serving HTTP on", sa[0], "port", sa[1], "..."
httpd.serve_forever()
