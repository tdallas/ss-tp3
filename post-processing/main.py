from parser import Parser

parser = Parser('out/output.xyz')
output = parser.get_output()
print('choques en n', len(list(filter(lambda p:  p.get_wall_collission() == 'n' , output[5]))))
print('choques en h', len(list(filter(lambda p:  p.get_wall_collission() == 'h' , output[5]))))
print('choques en v', len(list(filter(lambda p:  p.get_wall_collission() == 'v' , output[5]))))

# for particle in filter(lambda p:  p.get_wall_collission() == 'n' , output[0]):
    # print(particle)