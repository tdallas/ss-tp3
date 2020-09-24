from parser import Parser
from math import sqrt

parser = Parser('out/output.xyz')
output = parser.get_output()

impulse_left_dt = []
impulse_right_dt = []


impulse_left = 0
impulse_right = 0
for iteration in output:
    for particle in filter(lambda p:  p.get_wall_collission() == 'v' or p.get_wall_collission() == 'h', iteration):
        if particle.belongs_to_left_container():
            if particle.get_wall_collission() == 'h':
                impulse_left += (abs(particle.get_y_velocity()) * 2)
            else:
                impulse_right += (abs(particle.get_x_velocity()) * 2)
        else:
            if particle.get_wall_collission() == 'h':
                impulse_left += (abs(particle.get_y_velocity()) * 2)
            else:
                impulse_right += (abs(particle.get_x_velocity()) * 2)
    impulse_left_dt.append(impulse_left)
    impulse_right_dt.append(impulse_right)

print('impulse_left =', impulse_left)
print('impulse_right =', impulse_right)
# impulse / (longitud ancho * 2) * (longitud pared izquierda + longitud pared compuerta) / segundos
print('total pressure in left container =', impulse_left / ((0.24 + (0.08 + 0.09))) / 6.1)
print('total pressure in right container =', impulse_right / ((0.24 + (0.08 + 0.09))) / 6.1)

temperature_left = 0
temperature_right = 0
for particle in output[-1]:
    velocity = sqrt((particle.get_x_velocity() ** 2) + (particle.get_y_velocity()**2))
    if particle.belongs_to_left_container():
        temperature_left += (velocity**2)/2
    else:
        temperature_right += (velocity**2)/2

print('temperature left =', temperature_left)
print('temperature right =', temperature_right)

