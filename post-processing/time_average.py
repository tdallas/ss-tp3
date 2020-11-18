import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


dt = '0.1'
number_of_particles = 100
repetitions = 100
n_values = ['01', '02', '03', '04', '05']

for n in n_values:
    cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -p 0.%s -o repetitions-particles-%s-door-%s -r %s" % (str(number_of_particles), dt, n, str(number_of_particles), n, str(repetitions))
    print(cmd)
    os.system(cmd)

for n in n_values:
    print("Plotting with 0.%s door size, %s particles and %s repetitions" % (n, str(number_of_particles), str(repetitions)))
    df = pd.read_csv("out/repetitions-particles-%s-door-%s.csv" % (str(number_of_particles), n))
    times = df['time'].values.tolist()
    nv = df['doorSize'].values.tolist()[0]
    time_average = np.average(times)
    time_std = np.std(times)
    plt.errorbar(nv, time_average, yerr=time_std, capsize=3, elinewidth=1, markeredgewidth=1)
    plt.scatter(nv, time_average, label="0.%s metros" % n)

plt.ylabel('Tiempo (s)', fontsize=16)
plt.xlabel('Tamaño de tabique (m)', fontsize=16)
plt.legend(title='Tamaño de tabique')
plt.tight_layout()
plt.show()
