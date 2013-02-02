class Rectangle:
	def __init__(self, x, y, width, height):
		self.x = x
		self.y = y
		self.width = width
		self.height = height		
		self.center_mass_x = x + width/2
		self.center_mass_y = y + height/2
		self.area = width * height
