import os
import math
import numpy as np
import matplotlib.pyplot as plt
from parser_xyz import XYZParser


dt = 0.1
number_of_particles = 100
witness_particles = 100
iterations_for_coefficient = 100
iterations_for_coefficient_jump = 10
repetitions = 100

for r in range(1, repetitions + 1):
    cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -v 0.01 -o output-diffusion-r-%s -nw" % (str(number_of_particles), str(dt), str(r))
    print(cmd)
    os.system(cmd)

diffusion_coefficients_repetitions = []
diffusion_coefficients_times_repetitions = []

for r in range(1, repetitions + 1):
    print("Parsing repetition %s" % str(r))
    parser = XYZParser("out/output-diffusion-r-%s.xyz" % str(r))

    particle_iterations = []
    times_iterations = []
    dcms_iterations = []

    for id in range(0, witness_particles):
        iterations, times = parser.get_iterations_of_particle_with_id_until_wall_collision(id)
        dcms = []
        for i in range(1, len(iterations)):
            dcm = math.sqrt((iterations[i].get_x_position() - iterations[0].get_x_position())**2 + (iterations[i].get_y_position() - iterations[0].get_y_position())**2)
            dcms.append(dcm)
        particle_iterations.append(iterations)
        times_iterations.append(times)
        dcms_iterations.append(dcms)

    for i in range(0, witness_particles):
        if(len(times_iterations[i]) > 2):
            times_iterations[i].pop(0)

    diffusion_coefficients = []
    diffusion_coefficients_times = []

    i = 0
    while i <= iterations_for_coefficient:
        dcms_for_average = []
        for dcms in dcms_iterations:
            if i < len(dcms):
                dcms_for_average.append(dcms[i])
        if i == 0:
            diffusion_coefficients_times.append(dt)
        else:
            diffusion_coefficients_times.append(dt * i)
        dcm_average = np.average(dcms_for_average)
        diffusion_coefficient = (dcm_average**2) / (4 * diffusion_coefficients_times[len(diffusion_coefficients_times) - 1])
        diffusion_coefficients.append(diffusion_coefficient)
        i += iterations_for_coefficient_jump

    diffusion_coefficients_repetitions.append(diffusion_coefficients)
    diffusion_coefficients_times_repetitions.append(diffusion_coefficients_times)

diffusion = []
diffusion_errors = []
times = diffusion_coefficients_times_repetitions[0]
for i in range(0, len(times)):
    diffusions = []
    for r in range(0, repetitions):
        diffusions.append(diffusion_coefficients_repetitions[r][i])
    diffusion.append(np.average(diffusions))
    diffusion_errors.append(np.std(diffusions))

plt.errorbar(times, diffusion, yerr=diffusion_errors, capsize=3, elinewidth=1, markeredgewidth=1)
plt.plot(times, diffusion)
plt.scatter(times, diffusion)
plt.ylabel('Coeficiente de Difusión (m²/s)', fontsize=16)
plt.xlabel('Tiempo (s)', fontsize=16)
plt.ticklabel_format(useMathText=True)
plt.tight_layout()
plt.show()