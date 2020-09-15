package engine;

import model.Particle;

import java.util.*;

public class CellIndexMethod {
    private final Cell[][] matrix;
    private final Map<Particle, Set<Particle>> neighbours;
    private final boolean periodicBoundary;
    private final double interactionRadius;
    private final double length;
    private final int size;
    private final double cellLength;

    public CellIndexMethod(List<Particle> particles, boolean periodicBoundary, double interactionRadius, double length, int size) throws IllegalArgumentException {
        this.matrix = new Cell[size][size];
        this.periodicBoundary = periodicBoundary;
        this.interactionRadius = interactionRadius;
        this.length = length;
        this.size = size;
        this.cellLength = length / size;
        this.neighbours = new HashMap<>();
        double maxRadius = 0;
        for (Particle p : particles) {
            if (p.getRadius() > maxRadius) {
                maxRadius = p.getRadius();
            }
            neighbours.put(p, new HashSet<>());
        }
        //Check argument l/m > 2*rMax + rc
        if ((length / size) < (2 * maxRadius + interactionRadius)) {
            throw new IllegalArgumentException("Incorrect arguments for Cell Index Method.");
        }
    }

    public Map<Particle, Set<Particle>> getNeighbours(List<Particle> particles) {
        for (Particle p : particles) {
            neighbours.get(p).clear();
        }
        fillMatrix(particles);
        fillNeighbours();
        return neighbours;
    }

    public double getLength() {
        return length;
    }

    public double getInteractionRadius() {
        return interactionRadius;
    }

    public int getSize() {
        return size;
    }

    private void fillMatrix(List<Particle> particles) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = new Cell();
            }
        }
        for (Particle particle : particles) {
            matrix[(int) (particle.getXPosition() / cellLength)][(int) (particle.getYPosition() / cellLength)].addParticle(particle);
        }
    }

    private void fillNeighbours() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (Particle p : matrix[x][y].getParticles()) {
                    //ESTA FEO, SE PUEDE PONER MAS PROLIJO
                    //DIRECCIONES: CENTRO, ABAJO, ABAJO DERECHA, DERECHA, ARIBA DERECHA
                    int[] dx = {0, 0, 1, 1, 1};
                    int[] dy = {0, 1, 1, 0, -1};
                    for (int k = 0; k < 5; k++) {
                        int xComp = x + dx[k];
                        int yComp = y + dy[k];
                        if (xComp < size && yComp < size && xComp >= 0 && yComp >= 0) {
                            for (Particle q : matrix[xComp][yComp].getParticles()) {
                                addNeighbour(p, q);
                            }
                        } else if (periodicBoundary) {
                            xComp = (xComp + size) % size;
                            yComp = (yComp + size) % size;
                            for (Particle q : matrix[xComp][yComp].getParticles()) {
                                addNeighbour(p, q);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addNeighbour(Particle p, Particle q) {
        if (p.equals(q)) {
            return;
        }

        double pX = p.getXPosition();
        double pY = p.getYPosition();
        double qX = q.getXPosition();
        double qY = q.getYPosition();

        if (periodicBoundary) {
            if (pX < qX && pX + 2 * cellLength <= qX) {
                pX += length;
            }
            if (qX < pX && qX + 2 * cellLength <= pX) {
                qX += length;
            }
            if (pY < qY && pY + 2 * cellLength <= qY) {
                pY += length;
            }
            if (qY < pY && qY + 2 * cellLength <= pY) {
                qY += length;
            }
        }

        double distance = Math.hypot(pX - qX, pY - qY) - p.getRadius() - q.getRadius();

        if (distance < interactionRadius) {
            neighbours.get(p).add(q);
            neighbours.get(q).add(p);
        }
    }

    private static class Cell {
        private final Set<Particle> particles;

        public Cell() {
            this.particles = new HashSet<>();
        }

        public Set<Particle> getParticles() {
            return particles;
        }

        public void addParticle(Particle newParticle) {
            particles.add(newParticle);
        }
    }
}