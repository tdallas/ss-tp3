package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

}
