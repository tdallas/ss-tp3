package system;

import engine.Particle;
import engine.Wall;
import engine.WallType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileGenerator {
    private static final double WALLS_RADIUS = 0.0001;

    private final BufferedWriter bw;
    private FileWriter fw;

    public FileGenerator(String filename, List<Particle> particles, List<Wall> walls) {
        try {
            File directory = new File("out/");
            if (!directory.exists()) {
                directory.mkdir();
            }
            FileWriter pw = new FileWriter("out/" + filename + ".xyz");
            pw.close();
            this.fw = new FileWriter("out/" + filename + ".xyz", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bw = new BufferedWriter(fw);
        writeWall(walls);
        addToFile(particles);
    }

    public void addToFile(List<Particle> particles) {
        try {
            bw.write(particles.size() + "\n");
            bw.write("id xPos yPos xVel yVel radius \n");
            for (Particle particle : particles) {
                bw.write(particle.getId() + " " + particle.getXPosition() + " " + particle.getYPosition() + " " + particle.getXVelocity() + " " + particle.getYVelocity() + " " + particle.getRadius() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFile() {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeWall(List<Wall> walls) {
        int n = 0;
        double x, y, length;
        try {
            FileWriter pw = new FileWriter("out/walls.xyz");
            pw.close();
            pw = new FileWriter("out/walls.xyz", true);
            BufferedWriter bw = new BufferedWriter(pw);

            bw.write("x y (radius " + WALLS_RADIUS + ")\n");
            for (Wall wall : walls) {
                x = wall.getXPosition();
                y = wall.getYPosition();
                if (wall.getWallType() == WallType.HORIZONTAL) {
                    length = wall.getXPosition() + wall.getLength();
                    while (x < length) {
                        bw.write(x + " " + y + "\n");
                        n++;
                        x += WALLS_RADIUS;
                    }
                } else {
                    length = wall.getYPosition() + wall.getLength();
                    while (y < length) {
                        bw.write(x + " " + y + "\n");
                        n++;
                        y += WALLS_RADIUS;
                    }
                }
            }
            bw.close();

            Path path = Paths.get("out/walls.xyz");
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.add(0, Integer.toString(n));
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
