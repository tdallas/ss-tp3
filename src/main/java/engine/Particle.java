package engine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Particle {
    private final int id;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private final double radius;
    private final double mass;
    private int collisions;

    public Particle(int id, double xPosition, double yPosition, double xVelocity, double yVelocity, double radius, double mass) {
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.radius = radius;
        this.mass = mass;
        this.collisions = 0;
    }

    public void addCollision(){
        this.collisions++;
    }

    public void evolveOverTime(double time) {
        xPosition = xPosition + xVelocity * time;
        yPosition = yPosition + yVelocity * time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id &&
                Double.compare(particle.xPosition, xPosition) == 0 &&
                Double.compare(particle.yPosition, yPosition) == 0 &&
                Double.compare(particle.xVelocity, xVelocity) == 0 &&
                Double.compare(particle.yVelocity, yVelocity) == 0 &&
                Double.compare(particle.radius, radius) == 0 &&
                Double.compare(particle.mass, mass) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, xPosition, yPosition, xVelocity, yVelocity, radius, mass);
    }
}
