# ss-tp3
## Generate executable
`mvn clean install`

## Execute command
Required options: n, o

`usage: java -jar target/ss-tp3-1.0.jar`

`-dt,--time-delta <arg>              time delta for animations`

`-n,--n-particles <arg>              number of particles`

`-o,--output <arg>                   output file name`

`-p,--partition-size <arg>           size of the door in the middle partition (optional)`

`-r,--number-of-repetitions <arg>    number of repetitions of same configuration (optional)`

`-s,--seed <arg>                     seed for randomizer (optional)`

`-t,--time-after-equilibrium <arg>   time after equilibrium to test ideal gases law (optional)`

`-v,--velocity <arg>                 particles velocity (optional)`

## Output
The program will generate two files with the given output file name:

`out/FILENAME.xyz`

`out/walls-FILENAME.xyz`

## Usage example
`java -jar target/ss-tp3-1.0.jar -n 100 -dt 0.01 -p 0.01 -o test`

And this will generate:

`out/test.xyz`

`out/walls-test.xyz`

## Visual
To run the following python visual charts must install the following libraries:

`pip3 install -r post-processing/requirements.txt`

For each python script, [output.xyz path | variables refered to simulation run] should be place in variable script.

For a visual chart of pressure vs temperature with custom door size and **t** seconds before equilibrium:

`python3 post-processing/pressure_vs_t.py`

For a visual chart of time average with door size 0.01 0.03 and 0.05:

`python3 post-processing/time_average.py`

For a visual chart of time average with custom door size:

`python3 post-processing/particles_in_right.py`


## Authors

Tomás Dallas

Tomás Dorado