import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

n_values = ['01', '03', '05']

for n in n_values:
    cmd = "java -jar target/ss-tp3-1.0.jar -n 100 -dt 0.1 -p 0.%s -o repetitions%s -r 100" % (n, n)
    print(cmd)
    os.system(cmd)

for n in n_values:
    df = pd.read_csv("out/repetitions%s.csv" % n)
    times = df['time'].values.tolist()
    nv = df['doorSize'].values.tolist()[0]
    time_average = np.average(times)
    time_std = np.std(times)
    plt.errorbar(time_average, nv, xerr=time_std)
    plt.scatter(time_average, nv, label=nv)

plt.xlabel('Tiempo [S]', fontsize=16)
plt.ylabel('Tamaño de tabique', fontsize=16)
plt.legend(title='Tamaño de tabique')
plt.show()
