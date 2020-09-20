package engine;

import system.EquilibriumCutCondition;
import system.FileGenerator;

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
    private final double xLength;
    private final double yLength;
    private final double doorSize;
    private final boolean hasPartition;

    public EventDrivenSimulation(List<Particle> particles, List<Wall> walls, double deltaTime, String filename, CutCondition cutCondition, double xLength, double yLength, double doorSize) {
        this.particles = particles;
        this.walls = walls;
        this.deltaTime = deltaTime;
        this.fileGenerator = new FileGenerator(filename, particles, walls);
        this.collisionsCount = 0;
        this.nextSave = deltaTime;
        this.collisions = new PriorityQueue<>((o1, o2) -> {
            if (o1.getTimeToCollision() > o2.getTimeToCollision()) {
                return 1;
            } else if (o1.getTimeToCollision() < o2.getTimeToCollision()) {
                return -1;
            }
            return 0;
        });
        this.cutCondition = cutCondition;
        this.xLength = xLength;
        this.yLength = yLength;
        this.doorSize = doorSize;
        this.hasPartition = true;
    }

    public EventDrivenSimulation(List<Particle> particles, List<Wall> walls, double deltaTime, String filename, CutCondition cutCondition, double xLength, double yLength) {
        this.particles = particles;
        this.walls = walls;
        this.deltaTime = deltaTime;
        this.fileGenerator = new FileGenerator(filename, particles, walls);
        this.collisionsCount = 0;
        this.nextSave = deltaTime;
        this.collisions = new PriorityQueue<>((o1, o2) -> {
            if (o1.getTimeToCollision() > o2.getTimeToCollision()) {
                return 1;
            } else if (o1.getTimeToCollision() < o2.getTimeToCollision()) {
                return -1;
            }
            return 0;
        });
        this.cutCondition = cutCondition;
        this.xLength = xLength;
        this.yLength = yLength;
        this.doorSize = 0;
        this.hasPartition = false;
    }

    public void simulate() {
        Collision collision;
        fillQueue();
        while (!cutCondition.isFinished() && !collisions.isEmpty()) {
            collision = collisions.poll();

            if (timePassed >= nextSave) {
                nextSave += deltaTime;
                fileGenerator.addToFile(particles, (EquilibriumCutCondition) cutCondition);
            }
            timePassed += collision.getTimeToCollision();
            collisionsCount++;

            for (Particle particle : particles) {
                particle.evolveOverTime(collision.getTimeToCollision());
            }
            for (Collision c : collisions) {
                c.setTimeToCollision(c.getTimeToCollision() - collision.getTimeToCollision());
            }

            collision.collide();
            refillQueue(collision);
        }
        fileGenerator.closeFile();
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
            addTimeToWallCollisions(p);
        }
    }

    public void refillQueue(Collision collision) {
        Double aux;
        collisions.removeIf(h -> h.containsParticles(collision.getCollisionParticles()));
        for (Particle q : collision.getCollisionParticles()) {
            for (Particle p : particles) {
                if (!p.equals(q)) {
                    aux = timeToParticleCollision(p, q);
                    if (aux != null) {
                        collisions.add(new ParticlesCollision(aux, p, q));
                    }
                }
            }
            addTimeToWallCollisions(q);
        }
    }

    private Double timeToParticleCollision(Particle p, Particle q) {
        double xDeltaR = q.getXPosition() - p.getXPosition();
        double yDeltaR = q.getYPosition() - p.getYPosition();
        double xDeltaV = q.getXVelocity() - p.getXVelocity();
        double yDeltaV = q.getYVelocity() - p.getYVelocity();

        double deltaVR = xDeltaV * xDeltaR + yDeltaV * yDeltaR;

        if (deltaVR >= 0) {
            return null;
        }

        double deltaRadius = p.getRadius() + q.getRadius();
        double deltaVSqrt = xDeltaV * xDeltaV + yDeltaV * yDeltaV;
        double deltaRSqrt = xDeltaR * xDeltaR + yDeltaR * yDeltaR;
        double d = (deltaVR * deltaVR) - (deltaVSqrt * (deltaRSqrt - deltaRadius * deltaRadius));

        if (d < 0) {
            return null;
        }

        return -((deltaVR + Math.sqrt(d)) / deltaVSqrt);
    }

    private void addTimeToWallCollisions(Particle p) {
        Double aux;
        aux = timeToBottomWallCollision(p);
        if (aux != null) {
            collisions.add(new WallCollision(aux, p, WallType.HORIZONTAL));
        }
        aux = timeToTopWallCollision(p);
        if (aux != null) {
            collisions.add(new WallCollision(aux, p, WallType.HORIZONTAL));
        }
        aux = timeToLeftWallCollision(p);
        if (aux != null) {
            collisions.add(new WallCollision(aux, p, WallType.VERTICAL));
        }
        aux = timeToRightWallCollision(p);
        if (aux != null) {
            collisions.add(new WallCollision(aux, p, WallType.VERTICAL));
        }
    }

    private Double timeToLeftWallCollision(Particle p) {
        if (p.getXVelocity() < 0) {
            if (hasPartition) {
                //Si tiene tabique busca el tiempo a las dos paredes
                double timeToPartition = (p.getRadius() - p.getXPosition() + xLength / 2) / p.getXVelocity();
                double timeToBorder = (p.getRadius() - p.getXPosition()) / p.getXVelocity();

                if (p.getXPosition() <= xLength / 2) {
                    return timeToBorder;
                } else {
                    double yPosition = p.getYPosition() + p.getYVelocity() * timeToPartition;

                    if (yPosition > yLength / 2 + doorSize / 2 || yPosition < yLength / 2 - doorSize / 2) {
                        return timeToPartition;
                    } else {
                        return timeToBorder;
                    }
                }
            } else {
                return (p.getRadius() - p.getXPosition()) / p.getXVelocity();
            }
        }
        return null;
    }

    private Double timeToRightWallCollision(Particle p) {
        if (p.getXVelocity() > 0) {
            if (hasPartition) {
                //Si tiene tabique busca el tiempo a las dos paredes
                double timeToPartition = (xLength / 2 - p.getRadius() - p.getXPosition()) / p.getXVelocity();
                double timeToBorder = (xLength - p.getRadius() - p.getXPosition()) / p.getXVelocity();

                if (p.getXPosition() <= xLength / 2) {
                    double yPosition = p.getYPosition() + p.getYVelocity() * timeToPartition;

                    if (yPosition > yLength / 2 + doorSize / 2 || yPosition < yLength / 2 - doorSize / 2) {
                        return timeToPartition;
                    } else {
                        return timeToBorder;
                    }
                } else {
                    return timeToBorder;
                }
            } else {
                return (xLength - p.getRadius() - p.getXPosition()) / p.getXVelocity();
            }
        }
        return null;
    }

    private Double timeToTopWallCollision(Particle p) {
        if (p.getYVelocity() > 0) {
            return (yLength - p.getRadius() - p.getYPosition()) / p.getYVelocity();
        }
        return null;
    }

    private Double timeToBottomWallCollision(Particle p) {
        if (p.getYVelocity() < 0) {
            return (p.getRadius() - p.getYPosition()) / p.getYVelocity();
        }
        return null;
    }
}
