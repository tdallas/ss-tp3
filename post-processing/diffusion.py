import os
import numpy as np
import matplotlib.pyplot as plt
from parser_xyz import XYZParser


dt = '0.1'
number_of_particles = 100
witness_particles = 100

cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -v 0.01 -o output-diffusion -nw" % (str(number_of_particles), dt)
print(cmd)
os.system(cmd)

parser = XYZParser("out/output-diffusion.xyz")

particle_iterations = []
times_iterations = []

for id in range(0, witness_particles):
    iterations, times = parser.get_iterations_of_particle_with_id_until_wall_collision(id)
    particle_iterations.append(iterations)
    times_iterations.append(times)
    print(len(iterations))
    print(len(times))

