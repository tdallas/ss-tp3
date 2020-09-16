package engine;

import system.FileGenerator;

import java.awt.geom.Line2D;
import java.util.List;
import java.util.PriorityQueue;

public class EventDrivenSimulation {
    private int collisionsCount;
    private final List<Particle> particles;
    private final List<Wall> walls;
    private final FileGenerator fileGenerator;
    private final double deltaTime;
    private double timePassed;
    private double nextSave;
    private final PriorityQueue<Collision> collisions;
    private final CutCondition cutCondition;

    public EventDrivenSimulation(List<Particle> particles, List<Wall> walls, double deltaTime, String filename, CutCondition cutCondition) {
        this.particles = particles;
        this.walls = walls;
        this.deltaTime = deltaTime;
        this.fileGenerator = new FileGenerator(filename);
        this.collisionsCount = 0;
        this.nextSave = deltaTime;
        this.collisions = new PriorityQueue<>();
        this.cutCondition = cutCondition;
    }

    public void simulate() {
        Collision collision;
        while (!cutCondition.isFinished()) {
            fillQueue();

            collision = collisions.poll();
            if (collision == null) {
                continue;
            }
            collisionsCount++;

            for (Particle particle : particles) {
                particle.evolveOverTime(collision.getTimeToCollision());
            }

            timePassed += collision.getTimeToCollision();
            if (timePassed >= nextSave) {
                nextSave += deltaTime;
                //TODO
                fileGenerator.addToFile();
            }

            collision.collide();
        }
        System.out.println("Finished");
    }

    private void fillQueue() {
        collisions.clear();
        Double aux;
        Particle p, q;
        for (int i = 0; i < particles.size(); i++) {
            p = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++) {
                q = particles.get(j);
                aux = timeToParticleCollision(p, q);
                if (aux != null) {
                    collisions.add(new ParticlesCollision(aux, p, q));
                }
            }
            for (Wall wall : walls) {
                aux = timeToWallCollision(p, wall);
                if (aux != null) {
                    collisions.add(new WallCollision(aux, p, wall));
                }
            }
        }
    }

    private Double timeToParticleCollision(Particle p, Particle q) {
        double[] deltaR = new double[]{q.getXPosition() - p.getXPosition(), q.getYPosition() - p.getYPosition()};
        double[] deltaV = new double[]{q.getXVelocity() - p.getXVelocity(), q.getYVelocity() - p.getYVelocity()};
        double deltaVR = deltaV[0] * deltaR[0] + deltaV[1] * deltaR[1];

        //caso 1
        if (deltaVR >= 0) {
            return null;
        }

        //caso 2
        double deltaRadius = p.getRadius() + q.getRadius();
        double deltaVSquared = deltaV[0] * deltaV[0] + deltaV[1] * deltaV[1];
        double deltaRSquared = deltaR[0] * deltaR[0] + deltaR[1] * deltaR[1];
        double d = (deltaVR * deltaVR) - (deltaVSquared * (deltaRSquared - deltaRadius * deltaRadius));
        if (d < 0) {
            return null;
        }

        //caso 3
        return -((deltaVR + Math.sqrt(d)) / deltaVSquared);
    }

    private Double timeToWallCollision(Particle p, Wall wall) {
        if (wall.getWallType() == WallType.HORIZONTAL) {
            if (p.getYVelocity() > 0) {
                if (p.getYPosition() < wall.getYPosition()) {
                    return (wall.getYPosition() - p.getRadius() - p.getYPosition()) / p.getYVelocity();
                }
            } else {
                if (p.getYPosition() > wall.getYPosition()) {
                    return (p.getRadius() - p.getYPosition()) / p.getYVelocity();
                }
            }
        } else {
            if (p.getXVelocity() > 0) {
                if (p.getXPosition() < wall.getXPosition()) {
                    if (Line2D.linesIntersect(p.getXPosition(), p.getYPosition(), p.getXPosition() + p.getXVelocity()*20, p.getYPosition() + p.getYVelocity()*20, wall.getXPosition(), wall.getYPosition(), wall.getXPosition(), wall.getYPosition() + wall.getLength())) {
                        //return (wall.getXPosition() - p.getRadius() - p.getXPosition()) / p.getXVelocity();
                    }
                }
            } else {
                if (p.getXPosition() > wall.getXPosition()) {
                    if (Line2D.linesIntersect(p.getXPosition(), p.getYPosition(), p.getXPosition() + p.getXVelocity()*20, p.getYPosition() + p.getYVelocity()*20, wall.getXPosition(), wall.getYPosition(), wall.getXPosition(), wall.getYPosition() + wall.getLength())) {
                        //return (p.getRadius() - p.getXPosition()) / p.getXVelocity();
                    }
                }
            }
        }
        return null;
    }
}
