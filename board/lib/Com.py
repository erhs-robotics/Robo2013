#!/usr/bin/python2
import socket
import struct

class Com:
	def __init__(self, host, port):
		self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
		self.s.bind((host, port))
		self.s.listen(5)
		
	def pack_data(self, string):
		data = []
		utf8 = string.encode('utf-8')
		length = len(utf8)
		data.append(struct.pack('!H', length))
		format = '!' + str(length) + 's'
		data.append(struct.pack(format, utf8))
		return data
		
	def read_stream(self, size = 1024):		
		data = self.client.recv(size)
		return data
		
	def open_stream(self):
		self.client, _ = self.s.accept()
		
	def pack_string(self, string):
		string += '\n'
		data = self.pack_data(string)
		self.write_stream(data[0])
		self.write_stream(data[1])				
	
	def write_stream(self, string):		
		self.client.send(string)		
		
	def close_stream(self):
		self.client.close()	
        
		
