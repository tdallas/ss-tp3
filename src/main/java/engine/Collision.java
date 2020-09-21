package engine;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public abstract class Collision implements Comparable<Collision> {
    private double timeToCollision;

    public Collision(double timeToCollision) {
        this.timeToCollision = timeToCollision;
    }

    public abstract void collide();

    public abstract List<Particle> getCollisionParticles();

    public abstract boolean containsParticles(List<Particle> particles);
}

