package engine;

import system.FileGenerator;
import system.SystemGenerator;

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
    }

    public void simulate() {
        Collision collision;
        double tC;

        fillQueue();

        while (!cutCondition.isFinished() && !collisions.isEmpty()) {

            collision = collisions.poll();
            collisionsCount++;

            for (Particle particle : particles) {
                particle.evolveOverTime(collision.getTimeToCollision());
            }
            for(Collision c : collisions){
                c.setTimeToCollision(c.getTimeToCollision() - collision.getTimeToCollision());
            }

            timePassed += collision.getTimeToCollision();
            if (timePassed >= nextSave) {
                nextSave += deltaTime;
                fileGenerator.addToFile(particles);
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
        double[] deltaR = new double[]{q.getXPosition() - p.getXPosition(), q.getYPosition() - p.getYPosition()};
        double[] deltaV = new double[]{q.getXVelocity() - p.getXVelocity(), q.getYVelocity() - p.getYVelocity()};
        double deltaVR = deltaV[0] * deltaR[0] + deltaV[1] * deltaR[1];

        if (deltaVR >= 0) {
            return null;
        }

        double deltaRadius = p.getRadius() + q.getRadius();
        double deltaVSquared = deltaV[0] * deltaV[0] + deltaV[1] * deltaV[1];
        double deltaRSquared = deltaR[0] * deltaR[0] + deltaR[1] * deltaR[1];
        double d = (deltaVR * deltaVR) - (deltaVSquared * (deltaRSquared - deltaRadius * deltaRadius));
        if (d < 0) {
            return null;
        }

        return -((deltaVR + Math.sqrt(d)) / deltaVSquared);
    }

    private void addTimeToWallCollisions(Particle p){
        Double aux;
        aux = timeToBottomWallCollision(p);
        if(aux != null) {
            collisions.add(new WallCollision(aux, p, new Wall(WallType.HORIZONTAL, 0, 0, 0)));
        }
        aux = timeToLeftWallCollision(p);
        if(aux != null) {
            collisions.add(new WallCollision(aux, p, new Wall(WallType.VERTICAL, 0, 0, 0)));
        }
        aux = timeToRightWallCollision(p);
        if(aux != null) {
            collisions.add(new WallCollision(aux, p, new Wall(WallType.VERTICAL, 0, 0, 0)));
        }
        aux = timeToTopWallCollision(p);
        if(aux != null) {
            collisions.add(new WallCollision(aux, p, new Wall(WallType.HORIZONTAL, 0, 0, 0)));
        }
    }

    private Double timeToLeftWallCollision(Particle p) {
        if(p.getXVelocity() > 0 ){
            return null;
        }
        double timeToMiddle = (p.getRadius() - p.getXPosition() + SystemGenerator.xLength / 2) / p.getXVelocity();
        double timeToBorder = (p.getRadius() - p.getXPosition()) / p.getXVelocity();
        if(p.getXPosition() <= SystemGenerator.xLength / 2){
            return timeToBorder;
        }else{
            double ypos = p.getYPosition() + p.getYVelocity()*timeToMiddle;
            if(ypos > SystemGenerator.yLength / 2 + SystemGenerator.doorSize / 2 || ypos < SystemGenerator.yLength / 2 - SystemGenerator.doorSize / 2){
                return timeToMiddle;
            }else{
                return timeToBorder;
            }
        }
    }

    private Double timeToRightWallCollision(Particle p) {
        if(p.getXVelocity() < 0 ){
            return null;
        }
        double timeToMiddle = (SystemGenerator.xLength / 2 - p.getRadius() - p.getXPosition()) / p.getXVelocity();
        double timeToBorder = (SystemGenerator.xLength - p.getRadius() - p.getXPosition()) / p.getXVelocity();
        if(p.getXPosition() <= SystemGenerator.xLength / 2){
            double ypos = p.getYPosition() + p.getYVelocity()*timeToMiddle;
            if(ypos > SystemGenerator.yLength / 2 + SystemGenerator.doorSize / 2 || ypos < SystemGenerator.yLength /2 - SystemGenerator.doorSize / 2){
                return timeToMiddle;
            }else{
                return timeToBorder;
            }
        }else {
            return timeToBorder;
        }
    }

    private Double timeToTopWallCollision(Particle p) {
        return (p.getYVelocity() > 0)? ( SystemGenerator.yLength - p.getRadius() - p.getYPosition()) / p.getYVelocity() : null;
    }

    private Double timeToBottomWallCollision(Particle p) {
        return (p.getYVelocity() < 0)? (p.getRadius() - p.getYPosition()) / p.getYVelocity() : null;
    }
}
