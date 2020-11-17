from math import sqrt


class Particle:
    def __init__(self, id, x_position, y_position, x_velocity, y_velocity, radius, mass, collision_type, in_equilibrium, time_passed):
        self.id = int(id)
        self.x_position = float(x_position)
        self.y_position = float(y_position)
        self.x_velocity = float(x_velocity)
        self.y_velocity = float(y_velocity)
        # h is horizontal wall, v is vertical wall, p is particle collision, n is no collision
        self.radius = radius
        self.mass = mass
        self.collision_type = collision_type
        self.in_equilibrium = in_equilibrium
        self.time_passed = float(time_passed)

    def __str__(self):
        return 'Position: [' + str(self.x_position) + ', ' + str(self.y_position) + ']\nVelocity: [' + str(self.x_velocity) + ', ' + str(self.y_velocity) + ']\nCollission Type: ' + str(self.collision_type) + '\n'

    def get_id(self):
        return self.id

    def get_x_position(self):
        return self.x_position

    def get_y_position(self):
        return self.y_position

    def get_x_velocity(self):
        return self.x_velocity

    def get_y_velocity(self):
        return self.y_velocity

    def get_velocity(self):
        return sqrt((self.get_x_velocity() ** 2) + (self.get_y_velocity()**2))

    def get_collision_type(self):
        return self.collision_type

    def is_in_equilibrium(self):
        return self.in_equilibrium == 'y'

    def get_time_passed(self):
        return self.time_passed
