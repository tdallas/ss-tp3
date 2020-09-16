package engine;

import model.Particle;

import java.util.Objects;

public class ParticlesCollision extends Collision{
    private final Particle p;
    private final Particle q;

    public ParticlesCollision(double timeToCollision, Particle p, Particle q) {
        super(timeToCollision);
        this.p = p;
        this.q = q;
    }

    @Override
    public void collide() {
        double[] deltaR, deltaV, newVelocity;
        double deltaVR, deltaRadius, j;

        deltaR = new double[] {q.getXPosition() - p.getXPosition(), q.getYPosition() - p.getYPosition()};
        deltaV = new double[] {q.getXVelocity() - p.getXVelocity(), q.getYVelocity() - p.getYVelocity()};

        deltaVR = deltaV[0] * deltaR[0] + deltaV[1] * deltaR[1];
        deltaRadius = p.getRadius() + q.getRadius();

        j = (2 * p.getMass() * q.getMass() * deltaVR) / (deltaRadius * (p.getMass() + q.getMass()));

        newVelocity = new double[] {j * deltaR[0] / deltaRadius, j * deltaR[1] / deltaRadius};
        p.setXVelocity(p.getXVelocity() + newVelocity[0] / p.getMass());
        p.setYVelocity(p.getYVelocity() + newVelocity[1] / p.getMass());
        q.setXVelocity(q.getXVelocity() - newVelocity[0] / q.getMass());
        q.setYVelocity(q.getYVelocity() - newVelocity[1] / q.getMass());
    }

    @Override
    public int compareTo(Collision c) {
        if(getTimeToCollision() > c.getTimeToCollision()){
            return 1;
        }
        else if(getTimeToCollision() < c.getTimeToCollision()) {
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
