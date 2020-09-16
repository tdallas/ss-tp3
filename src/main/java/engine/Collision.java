package engine;

public abstract class Collision implements Comparable<Collision> {
    private final double timeToCollision;

    public Collision(double timeToCollision) {
        this.timeToCollision = timeToCollision;
    }

    public double getTimeToCollision() {
        return timeToCollision;
    }

    public abstract void collide();
}
