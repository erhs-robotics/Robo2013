class Target:
	def __init__(self, x, y, width, height):
		self.x = x
		self.y = y
		self.width = width
		self.height = height		
		self.center_mass_x = x + width/2
		self.center_mass_y = y + height/2
		self.area = width * height
		self.target_height = None
	def __repr__(self):
		info = (self.x, self.y, self.width, self.height)
		return "[%s, %s, %s, %s]" % info
