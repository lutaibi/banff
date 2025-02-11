import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that an will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.03;
    private static final double HARE_CREATION_PROBABILITY = 0.08;
    private static final double DEER_CREATION_PROBABILITY = 0.02;
    private static final double OWL_CREATION_PROBABILITY = 0.06;
    private static final double BEAR_CREATION_PROBABILITY = 0.01;
    private static final double BERRY_CREATION_PROBABILITY = 0.08;

    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        field = new Field(depth, width);
        view = new SimulatorView(depth, width);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long 
     * period (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }
    
    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        reportStats();
        for(int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(50);         // adjust this to change execution speed
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        // Use a separate Field to store the starting state of
        // the next step.
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Organism> organisms = field.getOrganisms();
        for (Organism anOrganism : organisms) {
            anOrganism.act(field, nextFieldState);
        }
        
        // Replace the old state with the new one.
        field = nextFieldState;

        reportStats();
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        populate();
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Wolf wolf = new Wolf(true, location);
                    field.placeAnimal(wolf, location);
                }
                else if(rand.nextDouble() <= HARE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hare hare = new Hare(true, location);
                    field.placeAnimal(hare, location);
                }
                else if(rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Deer deer = new Deer(true, location);
                    field.placeAnimal(deer, location);
                }
                else if(rand.nextDouble() <= BEAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Bear bear = new Bear(true, location);
                    field.placeAnimal(bear, location);
                }
                else if(rand.nextDouble() <= OWL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Owl owl = new Owl(true, location);
                    field.placeAnimal(owl, location);
                }
                // else if(rand.nextDouble() <= BERRY_CREATION_PROBABILITY) {
                    // Location location = new Location(row, col);
                    // Berry berry = new Berry(true, location);
                    // field.placeAnimal(berry, location);
                // }
                // else leave the location empty.
            }
        }
    }

    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats()
    {
        //System.out.print("Step: " + step + " ");
        field.fieldStats();
    }
    
    /**
     * Pause for a given time.
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }
}
