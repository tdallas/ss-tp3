import matplotlib.pyplot as plt
from parser_xyz import Parser
from particle import Particle

parser = Parser('out/output01.xyz')
output = parser.get_output()

t=[]
particles_r=[]

for iteration in output:
    right_count=0
    dt = 0
    for particle in iteration:
        if particle.belongs_to_right_container():
            dt = particle.get_dt()
            right_count+=1
    t.append(dt)
    particles_r.append(right_count/100)

print('t',len(t))
print('p',len(particles_r))

plt.plot(t, particles_r, label='100 partículas')
plt.ylabel('Fracción de partículas en recinto derecho', fontsize=13)
plt.xlabel('Tiempo [s]', fontsize=13)
plt.legend(title='')
plt.show()