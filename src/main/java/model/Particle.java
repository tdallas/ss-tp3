package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class Particle {
    private final int id;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private final double radius;
    private final double mass;

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
