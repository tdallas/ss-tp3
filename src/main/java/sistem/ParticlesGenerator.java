package sistem;

import model.Particle;
import model.Wall;
import model.WallType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticlesGenerator {
    private final Random rand;
    private final List<Particle> particles;
    private final List<Wall> walls;
    private final double xLength;
    private final double yLength;
    private final double velocity;
    private double quantity;
    private final double radius;
    private int idCounter;
    private final double mass;
    private final double doorSize;

    private static final int ALLOWED_ATTEMPTS = 30;

    public ParticlesGenerator(Random random, double doorSize, double xLength, double yLength, int quantity, double mass, double radius, double velocity) {
        this.rand = random;
        this.doorSize = doorSize;
        this.radius = radius;
        this.xLength = xLength;
        this.yLength = yLength;
        this.quantity = quantity;
        this.velocity = velocity;
        this.idCounter = 1;
        this.mass = mass;
        this.particles = new ArrayList<>();
        this.walls = new ArrayList<>();
        generateRandomParticles();
        generateWalls();
    }

    private void generateWalls() {
        walls.add(new Wall(WallType.HORIZONTAL, 0, 0, yLength));
        walls.add(new Wall(WallType.HORIZONTAL, xLength, 0, yLength));
        walls.add(new Wall(WallType.VERTICAL, 0, 0, xLength));
        walls.add(new Wall(WallType.VERTICAL, 0, yLength, xLength));
        double length = (xLength - doorSize) / 2;
        walls.add(new Wall(WallType.VERTICAL, 0, yLength / 2, length));
        walls.add(new Wall(WallType.VERTICAL, length + doorSize, yLength / 2, length));
    }

    private void generateRandomParticles() {
        while (quantity > 0) {
            particles.add(createParticle());
            quantity--;
        }
    }

    private Particle createParticle() {
        double randomX = 0, randomY = 0, randomAngle, xVelocity, yVelocity;
        int checkedParticles;
        int attempts = 0;
        boolean particleOverlaps = true;

        while (particleOverlaps && attempts < ALLOWED_ATTEMPTS) {
            randomX = generateRandomDouble(0, xLength);
            randomY = generateRandomDouble(0, yLength / 2);
            checkedParticles = checkCorrectParticleDistribution(randomX, randomY, radius);
            if (checkedParticles == particles.size()) {
                particleOverlaps = false;
            }

            attempts++;
        }

        if (particleOverlaps) {
            throw new IllegalArgumentException("Could not generate particle in less attempts than allowed.");
        }

        randomAngle = generateRandomDouble(0, 360);
        double angleInRadians = randomAngle * Math.PI / 180.0;
        xVelocity = Math.cos(angleInRadians) * velocity;
        yVelocity = Math.sin(angleInRadians) * velocity;

        return new Particle(idCounter++, randomX, randomY, xVelocity, yVelocity, radius, mass);
    }

    private double generateRandomDouble(final double min, final double max) {
        double r = rand.nextDouble();
        return min + (max - min) * r;
    }

    private double circlesDistance(final double x1, final double y1, final double r1, final double x2, final double y2, final double r2) {
        return Math.hypot(x1 - x2, y1 - y2) - r1 - r2;
    }

    private int checkCorrectParticleDistribution(final double x, final double y, final double radius) {
        if (x - radius < 0 || y - radius < 0 || x + radius > xLength || y + radius > yLength) {
            return 0;
        }

        Particle curr;
        int checkedParticles = 0;

        while (checkedParticles < particles.size()) {
            curr = particles.get(checkedParticles);

            if (circlesDistance(curr.getXPosition(), curr.getYPosition(), curr.getRadius(), x, y, radius) < 0) {
                break;
            }

            checkedParticles++;
        }

        return checkedParticles;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public List<Wall> getWalls() {
        return walls;
    }
}
