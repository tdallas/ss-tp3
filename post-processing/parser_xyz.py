from particle import Particle


class XYZParser:
    def __init__(self, output_path):
        self.output = self.parse_output(output_path)

    def get_output(self):
        return self.output

    def parse_output(self, output_path):
        output = []
        with open(output_path) as f:
            lines = f.readlines()
            iteration = []
            for line in lines:
                if self.is_header(line):
                    pass
                elif self.iteration_finished(line):
                    if len(iteration) > 0:
                        output.append(iteration)
                    iteration = []
                else:
                    particle = self.create_particle(line.replace('\n', '').split(' '))
                    iteration.append(particle)
            output.append(iteration)
        return output
    
    @staticmethod
    def is_header(line):
        return line == 'id xPosition yPosition xVelocity yVelocity radius redColor blueColor mass collisionType equilibrium timePassed\n'

    def iteration_finished(self, line):
        return len(line.split(' ')) == 1

    @staticmethod
    def create_particle(line):
        return Particle(line[0], line[1], line[2], line[3], line[4], line[5], line[8], line[9], line[10], line[11])
