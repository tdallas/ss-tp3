from math import sqrt

class Particle():
    def __init__(self, x_position, y_position, x_velocity, y_velocity, wall_collission):
        self.x_position = float(x_position)
        self.y_position = float(y_position)
        self.x_velocity = float(x_velocity)
        self.y_velocity = float(y_velocity)
        # h is horizontal wall, v is vertical wall
        self.wall_collission = wall_collission

    def __str__(self):
        return 'Position: [' + str(self.x_position) + ', ' + str(self.y_position) + ']\nVelocity: [' + str(self.x_velocity) + ', ' + str(self.y_velocity) + ']\nWall collission: ' + str(self.wall_collission) +'\n' 

    def get_wall_collission(self):
        return self.wall_collission

    def get_x_velocity(self):
        return self.x_velocity

    def get_y_velocity(self):
        return self.y_velocity

    def belongs_to_left_container(self):
        return self.x_position < 0.12

    def belongs_to_right_container(self):
        return not self.belongs_to_left_container()

    def get_velocity(self):
        return sqrt((self.get_x_velocity() ** 2) + (self.get_y_velocity()**2))