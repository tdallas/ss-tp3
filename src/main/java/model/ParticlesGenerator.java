package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticlesGenerator {
    private final Random rand;
    private final List<Particle> particles;
    private final double length;
    private double quantity;
    private final double minRadius;
    private double maxRadius;
    private final boolean fixedRadius;
    private int idCounter;
    private double mass;

    private static final int ALLOWED_ATTEMPTS = 30;

    public ParticlesGenerator(double fixedRadius, double length, int quantity, double mass) {
        this.rand = new Random();
        this.minRadius = fixedRadius;
        this.fixedRadius = true;
        this.length = length;
        this.quantity = quantity;
        this.idCounter = 1;
        this.mass = mass;
        this.particles = new ArrayList<>();
        generateRandomParticles();
    }

    public ParticlesGenerator(double minRadius, double maxRadius, double length, int quantity, double mass) {
        this.rand = new Random();
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.fixedRadius = false;
        this.length = length;
        this.quantity = quantity;
        this.particles = new ArrayList<>();
        generateRandomParticles();
    }

    public void generateRandomParticles() {
        while (quantity > 0) {
            particles.add(createParticle());
            quantity--;
        }
    }

    public Particle createParticle() {
        double randomX = 0, randomY = 0, randomRadius = 0;
        int checkedParticles = 0;
        int attempts = 0;
        boolean particleOverlaps = true;

        while (particleOverlaps && attempts < ALLOWED_ATTEMPTS) {
            randomX = generateRandomDouble(0, length);
            randomY = generateRandomDouble(0, length);
            if (fixedRadius) {
                randomRadius = minRadius;
            } else {
                randomRadius = generateRandomDouble(minRadius, maxRadius);
            }
            checkedParticles = checkCorrectParticleDistribution(randomX, randomY, randomRadius);
            if (checkedParticles == particles.size()) {
                particleOverlaps = false;
            }

            attempts++;
        }

        if (particleOverlaps && attempts < ALLOWED_ATTEMPTS) {
            throw new IllegalArgumentException("Could not generate particle in less attempts than allowed.");
        }

        return new Particle(idCounter++, randomX, randomY, randomRadius, 0, 0, mass);
    }

    private double generateRandomDouble(final double min, final double max) {
        double r = rand.nextDouble();
        return min + (max - min) * r;
    }

    private double circlesDistance(final double x1, final double y1, final double r1, final double x2, final double y2, final double r2) {
        return Math.hypot(x1 - x2, y1 - y2) - r1 - r2;
    }

    private int checkCorrectParticleDistribution(final double x, final double y, final double radius) {
        if (x - radius < 0 || y - radius < 0 || x + radius > length || y + radius > length) {
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
}
