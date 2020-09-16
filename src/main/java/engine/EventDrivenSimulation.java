package engine;

import model.Particle;
import model.Wall;
import util.FileGenerator;

import java.util.List;

public class EventDrivenSimulation {
    private int collisionsCount;
    private final List<Particle> particles;
    private final List<Wall> walls;
    private final FileGenerator fileGenerator;
    private final double deltaTime;

    public EventDrivenSimulation(List<Particle> particles, List<Wall> walls, double deltaTime, String filename) {
        this.particles = particles;
        this.walls = walls;
        this.deltaTime = deltaTime;
        this.fileGenerator = new FileGenerator(filename);
        this.collisionsCount = 0;
    }

    public void simulate(){

    }
}
