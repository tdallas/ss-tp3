package engine;

import model.Particle;
import model.Wall;
import model.WallType;

import java.util.Objects;

public class WallCollision extends Collision{
    private final Particle particle;
    private final Wall wall;

    public WallCollision(double timeToCollision, Particle particle, Wall wall) {
        super(timeToCollision);
        this.particle = particle;
        this.wall = wall;
    }

    @Override
    public void collide() {
        if(wall.getWallType() == WallType.HORIZONTAL){
            particle.setYVelocity(-particle.getYVelocity());
        }
        else{
            particle.setXVelocity(-particle.getXVelocity());
        }
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
        WallCollision that = (WallCollision) o;
        return Objects.equals(particle, that.particle) &&
                Objects.equals(wall, that.wall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(particle, wall);
    }
}
