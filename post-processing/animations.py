import os

dt = 0.1

numbers_of_particles = [100, 150, 200, 250, 300]
doorSize = 0.01

number_of_particles = 100
doorSizeValues = ['01', '03', '05']

print("Generating animations with number of particles variation")
for n in numbers_of_particles:
    cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -p %s -o animation-%s-particles" % (str(n), str(dt), str(doorSize), str(n))
    print(cmd)
    os.system(cmd)

print("Generating animations with door size variation")
for n in doorSizeValues:
    cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -p 0.%s -o animation-%s-door" % (str(number_of_particles), str(dt), n, n)
    print(cmd)
    os.system(cmd)

print("Generating animation with no middle wall")
cmd = "java -jar target/ss-tp3-1.0.jar -n %s -dt %s -o animation-%s-no-door" % (str(number_of_particles), str(dt), str(number_of_particles))
print(cmd)
os.system(cmd)