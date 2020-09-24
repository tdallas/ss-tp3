import matplotlib.pyplot as plt
from parser_xyz import Parser
from particle import Particle

parser = Parser('out/output.xyz')
output = parser.get_output()

pressure=[]
t=[]

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
    if temperature != 0.0 and impulse != 0.0:
        t.append(temperature/4)
        pressure.append(impulse/4.84)
    
plt.title('t = 4 (s)')
plt.plot(t, pressure, label='100 partículas')
plt.ylabel('Presión', fontsize=16)
plt.xlabel('Temperatura', fontsize=16)
plt.legend(title='Presión en función de la temperatura')
plt.show()