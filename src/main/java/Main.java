import engine.EventDrivenSimulation;
import org.apache.commons.cli.*;
import sistem.ParticlesGenerator;

import java.util.Random;

public class Main {
    private static int numberOfParticles;
    private static String filename;
    private static Long seed = null;
    private static double deltaTime;              //seconds
    private static final double doorSize = 0.01;  //meters
    private static final double xLength = 0.24;   //meters
    private static final double yLength = 0.09;   //meters
    private static final double mass = 1;         //kg
    private static final double radius = 0.0015;  //meters
    private static final double velocity = 0.01;  //meters per second

    public static void main(String[] args){
        parseArguments(args);
        //Just in case testing is needed to have the same seed on everything (argument -s is optional)
        Random random;
        if(seed == null) {
            random = new Random();
            seed = random.nextLong();
            random.setSeed(seed);
        }
        else{
            random = new Random(seed);
        }

        ParticlesGenerator particlesGenerator = new ParticlesGenerator(random, doorSize, xLength, yLength, numberOfParticles, mass, radius, velocity);
        EventDrivenSimulation eventDrivenSimulation = new EventDrivenSimulation(particlesGenerator.getParticles(), particlesGenerator.getWalls(), deltaTime, filename);
        eventDrivenSimulation.simulate();
    }

    private static void parseArguments(String[] args){
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

        Option seedOption = new Option("s", "seed", true, "seed for randomizer (optional)");
        seedOption.setRequired(false);
        options.addOption(seedOption);

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
        } catch(NumberFormatException e){
            System.out.println("Invalid argument number of particles, must be integer");
            System.exit(1);
        }
        if(numberOfParticles < 0 || numberOfParticles > 10000){
            System.out.println("Invalid number of particles, must be positive lower than 10000");
            System.exit(1);
        }

        try {
            deltaTime = Double.parseDouble(cmd.getOptionValue("time-delta"));
        } catch(NumberFormatException e){
            System.out.println("Invalid argument number of particles, must be double");
            System.exit(1);
        }
        if(deltaTime < 0){
            System.out.println("Invalid time delta, must be positive");
            System.exit(1);
        }

        filename = cmd.getOptionValue("output");
        if(filename.equals("walls")){
            System.out.println("Invalid filename, cannot be named: walls");
            System.exit(1);
        }

        String aux = cmd.getOptionValue("seed");
        if(aux != null) {
            try {
                seed = Long.parseLong(aux);
            } catch(NumberFormatException e){
                System.out.println("Invalid argument seed, must be long");
                System.exit(1);
            }
        }

    }
}
