import pandas as pd
from particle import Particle

class Parser():
    def __init__(self, output_path):
        self.output = self.parse_output(output_path)

    def get_output(self):
        return self.output

    def parse_output(self, output_path):
        output = []
        equilibriumCount = 0
        totalIterations = 0
        with open(output_path) as f:
            lines = f.readlines() # list containing lines of file
            iteration = []
            e = False
            for line in lines:
                if self.is_header(line):
                    pass
                elif self.iteration_finished(line):
                    totalIterations+=1
                    if len(iteration) > 1: 
                        output.append(iteration)
                    if e:
                        equilibriumCount+=1
                    e = False
                    iteration = []
                else:
                    particle = self.create_particle(line.replace('\n', '').split(' '))
                    if not e and particle.is_in_equilibrium():
                        e = True
                    iteration.append(particle)
        print('iteraciones en equilibrio', equilibriumCount)
        print('total iterations', totalIterations)
        return output
    
    def is_header(self, line):
        return line == 'id xPosition yPosition xVelocity yVelocity radius redColor blueColor mass wallCollision equilibrium timePassed\n'

    def iteration_finished(self, line):
        return len(line.split(' ')) == 1

    def create_particle(self, line):
        return Particle(
            line[1],
            line[2],
            line[3],
            line[4],
            line[-3],
            line[-2],
            line[-1]
        )