import matplotlib.pyplot as plt
from parser_xyz import Parser
from particle import Particle

parser = Parser('out/output.xyz')
output = parser.get_output()

pressure_vs_t=[]

temperature =0
impulse = 0

dt = 0
count = 0
for iteration in output:
    for index, particle in enumerate(filter(lambda p: p.is_in_equilibrium(), iteration)):
        if index == 0:
            count +=1
        temperature += (particle.get_velocity()**2)/2
        if particle.get_wall_collission() == 'h':
            impulse+= abs(particle.get_y_velocity()) * 2
        elif particle.get_wall_collission() == 'v':
            impulse+= abs(particle.get_x_velocity()) * 2
        if index == 99:
            dt += 0.01
    pressure_vs_t.append({'T': temperature/4, 'I': impulse/(0.74+4)})
print(dt)
print(pressure_vs_t)
print(count)