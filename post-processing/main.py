from parser import Parser

parser = Parser('out/output.xyz')
output = parser.get_output()
for particle in output[0]:
    print(particle)