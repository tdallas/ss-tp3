class Particle():
    def __init__(self, x_position, y_position, x_velocity, y_velocity, wall_collission):
        self.x_position = x_position
        self.y_position = y_position
        self.x_velocity = x_velocity
        self.y_velocity = y_velocity
        # h is horizontal wall, v is vertical wall
        self.wall_collission = self.get_wall_collision(wall_collission)

    def __str__(self):
        return 'Position: [' + str(self.x_position) + ', ' + str(self.y_position) + ']\nVelocity: [' + str(self.x_velocity) + ', ' + str(self.y_velocity) + ']\nWall collission: ' + str(self.wall_collission) +'\n' 

    def get_wall_collision(self, wall_collission):
        if wall_collission == 'n':
            return 'N'
        elif wall_collission == 'v':
            return 'VERTICAL'
        else:
            return 'HORIZONTAL'