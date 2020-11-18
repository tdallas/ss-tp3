import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


dt = 0.1
door_size = 0.01
repetitions = 100
particles_values = ['100', '150', '200', '250', '300']

for n in particles_values:
    cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -p %s -o repetitions-particles-%s -r %s" % (n, str(dt), str(door_size), n, str(repetitions))
    print(cmd)
    os.system(cmd)

for n in particles_values:
    print("Plotting with %s particles and %s repetitions" % (n, str(repetitions)))
    df = pd.read_csv("out/repetitions-particles-%s.csv" % n)
    times = df['time'].values.tolist()
    time_average = np.average(times)
    time_std = np.std(times)
    plt.errorbar(int(n), time_average, yerr=time_std, capsize=3, elinewidth=1, markeredgewidth=1)
    plt.scatter(int(n), time_average, label="%s partículas" % n)

plt.ylabel('Tiempo (s)', fontsize=16)
plt.xlabel('Cantidad de partículas', fontsize=16)
plt.legend(title='Cantidad de partículas')
plt.ticklabel_format(useMathText=True)
plt.tight_layout()
plt.show()
