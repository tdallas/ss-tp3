package engine;

import model.Particle;
import model.Wall;
import model.WallType;

import java.awt.geom.Line2D;
import java.util.*;

public class CellIndexMethod {
    private final Cell[][] matrix;
    private final Map<Particle, Set<Particle>> neighbours;
    private final List<Wall> walls;
    private final double interactionRadius;
    private int size;
    private final double cellLength;

    public CellIndexMethod(List<Particle> particles, List<Wall> walls, double interactionRadius, double length) {
        this.interactionRadius = interactionRadius;
        this.walls = walls;
        this.neighbours = new HashMap<>();
        double maxRadius = 0;
        for (Particle p : particles) {
            if (p.getRadius() > maxRadius) {
                maxRadius = p.getRadius();
            }
            neighbours.put(p, new HashSet<>());
        }
        this.size = (int)(length / (2 * maxRadius + interactionRadius));
        if ((length / size) < (2 * maxRadius + interactionRadius)) {
            this.size--;
        }
        this.cellLength = length / size;
        this.matrix = new Cell[size][size];
    }

    public Map<Particle, Set<Particle>> getNeighbours(List<Particle> particles) {
        for (Particle p : particles) {
            neighbours.get(p).clear();
        }
        fillMatrix(particles);
        fillNeighbours();
        return neighbours;
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

        double distance = Math.hypot(pX - qX, pY - qY) - p.getRadius() - q.getRadius();

        if (!isWallInBetween(p, q) && distance < interactionRadius) {
            neighbours.get(p).add(q);
            neighbours.get(q).add(p);
        }
    }

    private boolean isWallInBetween(Particle p, Particle q) {
        double pX = p.getXPosition();
        double pY = p.getYPosition();
        double qX = q.getXPosition();
        double qY = q.getYPosition();
        double wX, wY, wLength;
        for(Wall wall : walls){
            wX = wall.getXPosition();
            wY = wall.getYPosition();
            wLength = wall.getLength();
            if(wall.getWallType() == WallType.HORIZONTAL){
                if((pX < wX && qX > wX) || (pX > wX && qX < wX)){
                    if(Line2D.linesIntersect(pX, pY, qX, qY, wX, wY, wX, wY + wLength)) {
                        return true;
                    }
                }
            }
            else{
                if((pY < wY && qY > wY) || (pY > wY && qY < wY)){
                    if(Line2D.linesIntersect(pX, pY, qX, qY, wX, wY, wX + wLength, wY)) {
                        return true;
                    }
                }
            }
        }
        return false;
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