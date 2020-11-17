import os
import matplotlib.pyplot as plt
from parser_xyz import XYZParser

x_length = 0.24

dt = '0.1'
number_of_particles = 100
n_values = ['01', '03', '05']

for n in n_values:
    cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -p 0.%s -o output-%s-door-%s -nw" % (str(number_of_particles), dt, n, str(number_of_particles), n)
    print(cmd)
    os.system(cmd)

for n in n_values:
    print("Plotting with 0.%s door size and %s particles" % (n, str(number_of_particles)))
    parser = XYZParser("out/output-%s-door-%s.xyz" % (str(number_of_particles), n))
    output = parser.get_output()

    times = []
    particles_right = []

    for iteration in output:
        right_count = 0
        time_passed = 0
        for particle in iteration:
            if particle.get_x_position() >= x_length / 2:
                time_passed = particle.get_time_passed()
                right_count += 1
        times.append(time_passed)
        particles_right.append(right_count/number_of_particles)

    plt.plot(times, particles_right)
    plt.ylabel('Fracción de partículas', fontsize=16)
    plt.xlabel('Tiempo (s)', fontsize=16)
    plt.tight_layout()
    plt.show()