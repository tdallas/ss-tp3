import os
import numpy as np
import matplotlib.pyplot as plt
from parser_xyz import XYZParser


x_length = 0.24
y_length = 0.09
boltzmann_constant = 1.38064852-23

dt = '0.1'
particle_mass = 1
number_of_particles = 100
door_size = '0.01'
v_values = ['01', '02', '03', '04', '05', '06']
repetitions = 20
time_after_equilibrium = 10

for v in v_values:
    for r in range(1, repetitions + 1):
        cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -p %s -v 0.%s -o output-%s-vel-%s-rep-%s -t %s -nw" % (str(number_of_particles), dt, str(door_size), v, str(number_of_particles), v, str(r), str(time_after_equilibrium))
        print(cmd)
        os.system(cmd)

pressures = []
pressures_error = []
energies = []

for v in v_values:
    print("Parsing for velocity : 0.%s" % v)
    pressures_at_v = []
    for r in range(1, repetitions + 1):
        print("Repetition %s..." % str(r))
        parser = XYZParser("out/output-%s-vel-%s-rep-%s.xyz" % (str(number_of_particles), v, str(r)))
        iterations_after_equilibrium = parser.get_iterations_after_equilibrium()
        total_impulse = 0
        for iteration in iterations_after_equilibrium:
            for particle in iteration:
                if particle.get_collision_type() == 'h':
                    total_impulse += abs(particle.get_y_velocity()) * particle_mass
                elif particle.get_collision_type() == 'v':
                    total_impulse += abs(particle.get_x_velocity()) * particle_mass
        area = (2 * x_length + 2 * y_length + 2 * (y_length / 2 + float(door_size) / 2) + 2 * (y_length / 2 - float(door_size) / 2))
        pressure_at_iteration = total_impulse / (area * time_after_equilibrium)
        pressures_at_v.append(pressure_at_iteration)
    pressures.append(np.average(pressures_at_v))
    pressures_error.append(np.std(pressures_at_v))
    velocity = float(("0.%s" % v))
    energy = (1/2)*particle_mass*velocity*velocity
    energies.append(energy)

plt.errorbar(energies, pressures, yerr=pressures_error, capsize=3, elinewidth=1, markeredgewidth=1)
plt.scatter(energies, pressures)
plt.plot(energies, pressures)
plt.ylabel('Presión (N/m)', fontsize=16)
plt.xlabel('Energía (J)', fontsize=16)
plt.tight_layout()
plt.show()