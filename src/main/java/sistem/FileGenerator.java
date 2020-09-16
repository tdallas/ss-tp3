package sistem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {
    private FileWriter fw;
    private final BufferedWriter bw;

    public FileGenerator(String filename) {
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
        writeWall();
    }

    public void addToFile() {
        try {
            bw.write(" \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFiles() {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeWall() {
        double dY = (Math.sqrt(3) / 2) / 2;
        double dX = 0.5;
        try {
            FileWriter pw = new FileWriter("out/walls.xyz");
            pw.close();
            pw = new FileWriter("out/walls.xyz", true);
            BufferedWriter bw = new BufferedWriter(pw);
            bw.write("1923\n");
            bw.write("wall particles: x y\n");
            for (int i = 0; i < 406; i++) {
                bw.write((dX * i) + " " + (dY * 0) + "\n");
                bw.write((dX + dX * i) + " " + (dY * 404) + "\n");
            }
            for (int i = 0; i < 404; i++) {
                bw.write((dX * 0) + " " + (dY * i) + "\n");
                bw.write((dX + dX * 406) + " " + (dY * i) + "\n");
                if (i <= 150 || i >= 252) {
                    bw.write((dX * 202) + " " + (dY * i) + "\n");
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
