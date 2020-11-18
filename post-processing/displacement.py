import os
import math
import numpy as np
import matplotlib.pyplot as plt
from parser_xyz import XYZParser


dt = 0.1
number_of_particles = 100
witness_particles = 20

cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -v 0.01 -o output-diffusion -nw" % (str(number_of_particles), str(dt))
print(cmd)
os.system(cmd)

diffusion_coefficients_repetitions = []
diffusion_coefficients_times_repetitions = []

parser = XYZParser("out/output-diffusion.xyz")

times_iterations = []
dcms_iterations = []

for id in range(0, witness_particles):
    iterations, times = parser.get_iterations_of_particle_with_id_until_wall_collision(id)
    dcms = []
    for i in range(1, len(iterations)):
        dcm = (iterations[i].get_x_position() - iterations[0].get_x_position())**2 + (iterations[i].get_y_position() - iterations[0].get_y_position())**2
        dcms.append(dcm)
    times_iterations.append(times)
    dcms_iterations.append(dcms)

for i in range(0, witness_particles):
    if(len(times_iterations[i]) > 1):
        times_iterations[i].pop(0)
        plt.plot(times_iterations[i], dcms_iterations[i])

plt.ylabel('Desplazamiento (m)', fontsize=16)
plt.xlabel('Tiempo (s)', fontsize=16)
plt.ticklabel_format(useMathText=True)
plt.tight_layout()
plt.show()