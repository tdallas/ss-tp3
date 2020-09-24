import matplotlib.pyplot as plt
from parser_xyz import Parser
from particle import Particle

parser = Parser('out/output.xyz')
output = parser.get_output()

t=[]
particles_r=[]

dt = 0
for iteration in output:
    right_count=0
    for particle in iteration:
        if particle.belongs_to_right_container():
            right_count+=1
    t.append(dt)
    particles_r.append(right_count/100)
    dt += 0.01

plt.title('t = 1 (s)')
plt.plot(t, particles_r, label='200 partículas')
plt.ylabel('Fracción de partículas en recinto derecho', fontsize=13)
plt.xlabel('Tiempo [s]', fontsize=13)
plt.legend(title='')
plt.show()