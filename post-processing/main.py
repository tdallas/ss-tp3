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
    for particle in filter(lambda p:  p.get_wall_collission() == 'v' or p.get_wall_collission() == 'h', iteration):
        temperature += (particle.get_velocity()**2)/2
        if particle.get_wall_collission() == 'h':
            if (abs(particle.get_y_velocity()) * 2 > 1.0):
                print('ES MAYOR A 1')
            impulse+= abs(particle.get_y_velocity()) * 2
            count+=1
print('hay ', count, 'choques')
print('impulse', impulse)

# for iteration in output:
#     for particle in filter(lambda p:  p.get_wall_collission() == 'v' or p.get_wall_collission() == 'h', iteration):
#         if particle.get_wall_collission() == 'h':
#             # print(abs(particle.get_y_velocity() * 2))
#             # print('\n')
#             impulse += abs(particle.get_y_velocity() * 2)

#         # if particle.belongs_to_left_container():
#         #     if particle.get_wall_collission() == 'h':
#         #         impulse_left += (abs(particle.get_y_velocity()) * 2)
#         #     # else:
#         #     #     impulse_right += (abs(particle.get_velocity()) * 2)
#         # else:
#         #     if particle.get_wall_collission() == 'h':
#         #         impulse_left += (abs(particle.get_y_velocity()) * 2)
#         #     # else:
#         #     #     impulse_right += (abs(particle.get_velocity()) * 2)
#     impulse_left_dt.append(impulse_left)
#     impulse_right_dt.append(impulse_right)

# print('impulse_left =', impulse_left)
# print('impulse_right =', impulse_right)
# impulse / (longitud ancho * 2) * (longitud pared izquierda + longitud pared compuerta) / segundos
print('total pressure in system =', (impulse) / (0.48+6.51))

# temperature = 0
# impulse_last = 0
# for particle in output[-1]:
#     if particle.get_y_velocity() > 1.0:
#         print('MAYOR A UNO')

print('temperature =', temperature)

