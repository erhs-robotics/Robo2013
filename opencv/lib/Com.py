#!/usr/bin/python2

class Com:
	def __init__(self, host, port):
		self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
		self.s.bind((host, port))
		self.s.listen(5)
	
	def recieve_stream(self, size = 1024):
		self.client, _ = self.s.accept()
		data = self.client.recv(size)
		return data
	
	def write_steam(self, string):
		self.client.send(string)
		self.client.close()
		
	
		
        
		
