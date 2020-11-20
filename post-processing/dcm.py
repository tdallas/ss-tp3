import os
import numpy as np
import matplotlib.pyplot as plt
from parser_xyz import XYZParser


dt = 0.1
number_of_particles = 100
witness_particles = 100
iterations_for_dcm = 150
iterations_for_dcm_jump = 5
repetitions = 100

for r in range(1, repetitions + 1):
    cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -v 0.01 -o output-diffusion-r-%s -nw" % (str(number_of_particles), str(dt), str(r))
    print(cmd)
    os.system(cmd)

dcms_iterations = []
times_iterations = []

for r in range(1, repetitions + 1):
    print("Parsing repetition %s" % str(r))
    parser = XYZParser("out/output-diffusion-r-%s.xyz" % str(r))

    for id in range(0, witness_particles):
        iterations, times = parser.get_iterations_of_particle_with_id_until_wall_collision(id)
        dcms = []
        for i in range(1, len(iterations)):
            dcm = (iterations[i].get_x_position() - iterations[0].get_x_position())**2 + (iterations[i].get_y_position() - iterations[0].get_y_position())**2
            dcms.append(dcm)
        if(len(times) > 1):
            times.pop(0)
            times_iterations.append(times)
            dcms_iterations.append(dcms)


dcm_averages = []
dcm_averages_errors = []
dcm_averages_times = []

i = 0
while i <= iterations_for_dcm:
    dcms_for_average = []
    for dcms in dcms_iterations:
        if i < len(dcms):
            dcms_for_average.append(dcms[i])
    if i == 0:
        dcm_averages_times.append(dt)
    else:
        dcm_averages_times.append(dt * i)
    dcm_averages.append(np.average(dcms_for_average))
    dcm_averages_errors.append((np.std(dcms_for_average)))
    i += iterations_for_dcm_jump

print("Plotting DCM vs time")
plt.errorbar(dcm_averages_times, dcm_averages, yerr=dcm_averages_errors, capsize=3, elinewidth=1, markeredgewidth=1)
plt.plot(dcm_averages_times, dcm_averages)
plt.scatter(dcm_averages_times, dcm_averages, label="DCM")
plt.ylabel('DCM (m²)', fontsize=16)
plt.xlabel('Tiempo (s)', fontsize=16)
plt.ticklabel_format(style='sci', useMathText=True)
plt.tight_layout()
plt.show()

print("Plotting error with different diffusion coefficients")
errors = []
slopes = []
best_slope = -1
best_slope_error = 1.7976931348623157e+308
for i in range(0, len(dcm_averages)):
    slope = dcm_averages[i]/dcm_averages_times[i]
    slopes.append(slope)
    error = 0
    for j in range(0, len(dcm_averages)):
        error += (dcm_averages[j] - slope * dcm_averages_times[j])**2
    errors.append(error)
    if error < best_slope_error:
        best_slope = slope
        best_slope_error = error

plt.plot(slopes, errors)
plt.scatter(slopes, errors)
plt.ylabel('Error Cuadrático Medio', fontsize=16)
plt.xlabel('Pendiente (m²/s)', fontsize=16)
plt.ticklabel_format(useMathText=True)
plt.tight_layout()
plt.show()

print("Best slope is: " + str(best_slope))
print("Plotting DCM comparison with best slope")
plt.errorbar(dcm_averages_times, dcm_averages, yerr=dcm_averages_errors, capsize=3, elinewidth=1, markeredgewidth=1)
plt.plot(dcm_averages_times, dcm_averages)
plt.scatter(dcm_averages_times, dcm_averages, label="DCM")
plt.plot(dcm_averages_times, best_slope*np.array(dcm_averages_times), label="Mejor pendiente")
plt.ylabel('DCM (m²)', fontsize=16)
plt.xlabel('Tiempo (s)', fontsize=16)
plt.legend(title='')
plt.ticklabel_format(useMathText=True)
plt.tight_layout()
plt.show()