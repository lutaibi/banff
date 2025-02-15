import java.util.List;
import java.util.Random;

/**
 * A simple model of a hare.
 * Hares age, move, breed, and die.
 * Prey - Eats {Berries}, hunted by {Wolf, Owl, Bear}
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Hare extends Organism
{
    // Characteristics shared by all hares (class variables).
    // The age at which a hare can start to breed.
    private static final int BREEDING_AGE = 1;
    // The age to which a hare can live.
    private static final int MAX_AGE = 5;
    // The likelihood of a hare breeding.
    private static final double BREEDING_PROBABILITY = 0.9;
    // The max number of births.
    private static final int MAX_LITTER_SIZE = 8;
    // The food value of a single berry. In effect, this is the
    // number of steps a hare can go before it has to eat again.
    private static final int BERRY_FOOD_VALUE = 12;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The hare's age.
    private int age;
    private int foodLevel;

    /**
     * Create a new hare. A hare may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param location The location within the field.
     */
    public Hare(boolean randomAge, Location location)
    {
        super(location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        else {
            age = 0;
        }
        foodLevel = rand.nextInt(BERRY_FOOD_VALUE);
    }
    
    /**
     * This is what the hare does most of the time - it runs around.
     * Sometimes it will breed or die of old age.
     * @param currentField The field occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState)
    {
        incrementAge();
        if(isAlive()) {
            List<Location> freeLocations = 
                nextFieldState.getFreeAdjacentLocations(getLocation());
            if(!freeLocations.isEmpty()) {
                giveBirth(nextFieldState, freeLocations);
            }
            // Try to move into a free location.
            if(! freeLocations.isEmpty()) {
                Location nextLocation = freeLocations.get(0);
                setLocation(nextLocation);
                nextFieldState.placeOrganism(this, nextLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    @Override
    public String toString() {
        return "Hare{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this hare is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New hare are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Hare young = new Hare(false, loc);
                nextFieldState.placeOrganism(young, loc);
            }
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * A hare can breed if it has reached the breeding age.
     * @return true if the hare can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
