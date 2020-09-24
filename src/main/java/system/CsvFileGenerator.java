package system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvFileGenerator {
    private BufferedWriter bw;
    private FileWriter fw;

    public CsvFileGenerator(String filename) {
        try {
            File directory = new File("out/");
            if (!directory.exists()) {
                directory.mkdir();
            }
            FileWriter pw = new FileWriter("out/" + filename + ".csv");
            pw.close();
            this.fw = new FileWriter("out/" + filename + ".csv", true);
            this.bw = new BufferedWriter(fw);
            bw.write("doorSize,time\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToFile(double doorSize, double time) {
        try {
            bw.write(doorSize + "," + time + "\n");
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
}
