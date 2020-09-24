import engine.CutCondition;
import engine.EventDrivenSimulation;
import org.apache.commons.cli.*;
import system.EquilibriumCutCondition;
import system.GasesLawEquilibriumCutCondition;
import system.SystemGenerator;

import java.util.Random;

public class Main {
    private static int numberOfParticles;
    private static String filename;
    private static Long seed = null;
    private static double deltaTime;              //seconds
    private static Double doorSize;               //meters
    private static Double timeAfterEquilibrium = null; //seconds
    private static final double xLength = 0.24;   //meters
    private static final double yLength = 0.09;   //meters
    private static final double mass = 1;         //kg
    private static final double radius = 0.0015;  //meters
    private static final double velocity = 0.01;  //meters per second
    private static final double equilibriumPercentage = 0.01;


    public static void main(String[] args) {
        long time;
        parseArguments(args);

        Random random;
        if (seed == null) {
            random = new Random();
            seed = random.nextLong();
            random.setSeed(seed);
        } else {
            random = new Random(seed);
        }
        System.out.println("Seed: " + seed);

        SystemGenerator systemGenerator;
        CutCondition equilibriumCutCondition;
        EventDrivenSimulation eventDrivenSimulation;

        if (doorSize != null) {
            //With partition and doorSize (con tabique)
            systemGenerator = new SystemGenerator(random, doorSize, xLength, yLength, numberOfParticles, mass, radius, velocity);
            if(timeAfterEquilibrium == null) {
                equilibriumCutCondition = new EquilibriumCutCondition(systemGenerator.getParticles(), xLength, equilibriumPercentage);
            }
            else{
                equilibriumCutCondition = new GasesLawEquilibriumCutCondition(systemGenerator.getParticles(), xLength, equilibriumPercentage, timeAfterEquilibrium);
            }
            eventDrivenSimulation = new EventDrivenSimulation(systemGenerator.getParticles(), systemGenerator.getWalls(), deltaTime, filename, equilibriumCutCondition, xLength, yLength, doorSize);
        } else {
            //Without partition (sin tabique)
            systemGenerator = new SystemGenerator(random, xLength, yLength, numberOfParticles, mass, radius, velocity);
            if(timeAfterEquilibrium == null) {
                equilibriumCutCondition = new EquilibriumCutCondition(systemGenerator.getParticles(), xLength, equilibriumPercentage);
            }
            else{
                equilibriumCutCondition = new GasesLawEquilibriumCutCondition(systemGenerator.getParticles(), xLength, equilibriumPercentage, timeAfterEquilibrium);
            }
            eventDrivenSimulation = new EventDrivenSimulation(systemGenerator.getParticles(), systemGenerator.getWalls(), deltaTime, filename, equilibriumCutCondition, xLength, yLength);
        }

        time = System.currentTimeMillis();
        eventDrivenSimulation.simulate();
        time = System.currentTimeMillis() - time;

        System.out.println("Simulation finished in " + time + " ms.");
    }

    private static void parseArguments(String[] args) {
        Options options = new Options();

        Option numberParticlesOption = new Option("n", "n-particles", true, "number of particles");
        numberParticlesOption.setRequired(true);
        options.addOption(numberParticlesOption);

        Option outputOption = new Option("o", "output", true, "output file name");
        outputOption.setRequired(true);
        options.addOption(outputOption);

        Option deltaTimeOption = new Option("dt", "time-delta", true, "time delta for animations");
        deltaTimeOption.setRequired(true);
        options.addOption(deltaTimeOption);

        Option partitionOption = new Option("p", "partition-size", true, "size of the door in the middle partition (optional)");
        partitionOption.setRequired(false);
        options.addOption(partitionOption);

        Option seedOption = new Option("s", "seed", true, "seed for randomizer (optional)");
        seedOption.setRequired(false);
        options.addOption(seedOption);

        Option timeAfterEquilibriumOption = new Option("t", "time-after-equilibrium", true, "time after equilibrium to test ideal gases law (optional)");
        timeAfterEquilibriumOption.setRequired(false);
        options.addOption(timeAfterEquilibriumOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("java -jar target/ss-tp3-1.0.jar", options);

            System.exit(1);
        }

        try {
            numberOfParticles = Integer.parseInt(cmd.getOptionValue("n-particles"));
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument number of particles, must be integer");
            System.exit(1);
        }
        if (numberOfParticles < 0 || numberOfParticles > 10000) {
            System.out.println("Invalid number of particles, must be positive lower than 10000");
            System.exit(1);
        }

        try {
            deltaTime = Double.parseDouble(cmd.getOptionValue("time-delta"));
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument number of particles, must be double");
            System.exit(1);
        }
        if (deltaTime < 0) {
            System.out.println("Invalid time delta, must be positive");
            System.exit(1);
        }

        filename = cmd.getOptionValue("output");
        if (filename.equals("walls")) {
            System.out.println("Invalid filename, cannot be named: walls");
            System.exit(1);
        }

        String aux = cmd.getOptionValue("time-after-equilibrium");
        if (aux != null) {
            try {
                timeAfterEquilibrium = Double.parseDouble(aux);
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument time after equilibrium, must be double");
                System.exit(1);
            }
            if (timeAfterEquilibrium <= 0) {
                System.out.println("Invalid argument time after equilibrium, must be positive");
                System.exit(1);
            }
        } else {
            timeAfterEquilibrium = null;
        }

        aux = cmd.getOptionValue("partition-size");
        if (aux != null) {
            try {
                doorSize = Double.parseDouble(aux);
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument partition door size, must be double");
                System.exit(1);
            }
            if (doorSize <= 0 || doorSize >= yLength) {
                System.out.println("Invalid argument partition door size, must be positive lower than " + yLength);
                System.exit(1);
            }
        } else {
            doorSize = null;
        }

        aux = cmd.getOptionValue("seed");
        if (aux != null) {
            try {
                seed = Long.parseLong(aux);
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument seed, must be long");
                System.exit(1);
            }
        }

    }
}
