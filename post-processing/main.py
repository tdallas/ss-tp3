from parser import Parser

parser = Parser('out/output.xyz')
output = parser.get_output()

impulse_left_dt = []
impulse_right_dt = []


impulse_left = 0
impulse_right = 0
impulse = 0

count =0
temperature=0
for iteration in output:
    for particle in filter(lambda p:  p.is_in_equilibrium() and (p.get_wall_collission() == 'v' or p.get_wall_collission() == 'h'), iteration):
        if particle.get_wall_collission() == 'h':
            impulse+= abs(particle.get_y_velocity()) * 2
        else:
            impulse+= abs(particle.get_x_velocity()) * 2

for iteration in output:
    for particle in filter(lambda p: p.is_in_equilibrium(), iteration):
        temperature += (particle.get_velocity()**2)/2

print('total pressure in system =', (impulse) / (0.48 + 0.18 + 0.08 + 4))
print('temperature =', temperature/4)

