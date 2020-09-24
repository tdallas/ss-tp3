package engine;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WallCollision extends Collision {
    private final Particle particle;
    private final WallType wallType;

    public WallCollision(double timeToCollision, Particle particle, WallType wallType) {
        super(timeToCollision);
        this.particle = particle;
        this.wallType = wallType;
    }

    @Override
    public void collide() {
        if (wallType == WallType.HORIZONTAL) {
            particle.setYVelocity(-particle.getYVelocity());
        } else {
            particle.setXVelocity(-particle.getXVelocity());
        }
        particle.addCollision();
        particle.addWallCollision();
    }

    @Override
    public List<Particle> getCollisionParticles() {
        return Collections.singletonList(particle);
    }

    @Override
    public boolean containsParticles(List<Particle> particles) {
        for (Particle p : particles) {
            if (p.equals(particle)) {
                return true;
            }
        }
        return false;
    }

    public Particle getParticle() {
        return particle;
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
        WallCollision that = (WallCollision) o;
        return Objects.equals(particle, that.particle) &&
                wallType == that.wallType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(particle, wallType);
    }

    public WallType getWallType() {
        return wallType;
    }
}
