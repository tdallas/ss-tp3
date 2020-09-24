import os
import pandas as pd
import matplotlib.pyplot as plt

n_values = [2000, 3000, 5000]

for n in n_values:
    cmd = 'java -jar ../target/ss-tp2-1.0.jar -n {:g} -o output{:g}'.format(n, n)
    print(cmd)
    os.system(cmd)

for n in n_values:
    df = pd.read_csv('out/output{:g}.csv'.format(n))
    plt.plot(df['nR'], df['t'], label=n)

plt.ylabel('Tiempo [mS]', fontsize=16)
plt.xlabel('Fracción de partículas recinto derecho', fontsize=16)
plt.legend(title='Cantidad de partículas')
plt.show()