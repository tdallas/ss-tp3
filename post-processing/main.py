from parser import Parser

parser = Parser('out/output.xyz')
output = parser.get_output()

impulse_left_dt = []
impulse_right_dt = []


impulse_left = 0
impulse_right = 0
for iteration in output:
    for particle in filter(lambda p:  p.get_wall_collission() == 'v' or p.get_wall_collission() == 'h', iteration):
        if particle.belongs_to_left_container():
            if particle.get_wall_collission() == 'v':
                impulse_left += (abs(particle.get_y_velocity()) * 2)
            else:
                impulse_right += (abs(particle.get_x_velocity()) * 2)
        else:
            if particle.get_wall_collission() == 'v':
                impulse_left += (abs(particle.get_y_velocity()) * 2)
            else:
                impulse_right += (abs(particle.get_x_velocity()) * 2)
    impulse_left_dt.append(impulse_left)
    impulse_right_dt.append(impulse_right)

print('impulse_left_dt', impulse_left_dt)
print('impulse_right_dt', impulse_right_dt)