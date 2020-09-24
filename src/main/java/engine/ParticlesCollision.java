package engine;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ParticlesCollision extends Collision {
    private final Particle p;
    private final Particle q;

    public ParticlesCollision(double timeToCollision, Particle p, Particle q) {
        super(timeToCollision);
        this.p = p;
        this.q = q;
    }

    @Override
    public void collide() {
        double xDeltaR, yDeltaR, xDeltaV, yDeltaV, deltaVR, deltaRadius, j, xNewVelocity, yNewVelocity;

        xDeltaR = q.getXPosition() - p.getXPosition();
        yDeltaR = q.getYPosition() - p.getYPosition();
        xDeltaV = q.getXVelocity() - p.getXVelocity();
        yDeltaV = q.getYVelocity() - p.getYVelocity();

        deltaVR = xDeltaV * xDeltaR + yDeltaV * yDeltaR;
        deltaRadius = p.getRadius() + q.getRadius();

        j = (2 * p.getMass() * q.getMass() * deltaVR) / (deltaRadius * (p.getMass() + q.getMass()));

        xNewVelocity = j * xDeltaR / deltaRadius;
        yNewVelocity = j * yDeltaR / deltaRadius;

        p.setXVelocity(p.getXVelocity() + xNewVelocity / p.getMass());
        p.setYVelocity(p.getYVelocity() + yNewVelocity / p.getMass());
        q.setXVelocity(q.getXVelocity() - xNewVelocity / q.getMass());
        q.setYVelocity(q.getYVelocity() - yNewVelocity / q.getMass());

        p.addCollision();
        q.addCollision();
        p.addParticleCollision();
        q.addParticleCollision();
    }

    @Override
    public List<Particle> getCollisionParticles() {
        return Arrays.asList(p, q);
    }

    @Override
    public boolean containsParticles(List<Particle> particles) {
        for (Particle particle : particles) {
            if (particle.equals(p) || particle.equals(q)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Collision c) {
        if (getTimeToCollision() > c.getTimeToCollision()) {
            return 1;
        } else if (getTimeToCollision() < c.getTimeToCollision()) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticlesCollision that = (ParticlesCollision) o;
        return Objects.equals(p, that.p) &&
                Objects.equals(q, that.q);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p, q);
    }
}
