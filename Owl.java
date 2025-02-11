import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Owl.
 * Owls age, move, eat, and die.
 * Nocturnal predator - eats {Hare}
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Owl extends Organism
{
    // Characteristics shared by all owls (class variables).
    // The age at which a owl can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a owl can live.
    private static final int MAX_AGE = 15;
    // The likelihood of a owl breeding.
    private static final double BREEDING_PROBABILITY = 0.5;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single hare. In effect, this is the
    // number of steps a owl can go before it has to eat again.
    private static final int HARE_FOOD_VALUE = 12;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).

    // The owl's age.
    private int age;
    // The owl's food level, which is increased by eating hares.
    private int foodLevel;

    /**
     * Create a owl. A owl can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the owl will have random age and hunger level.
     * @param location The location within the field.
     */
    public Owl(boolean randomAge, Location location)
    {
        super(location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        else {
            age = 0;
        }
        foodLevel = rand.nextInt(HARE_FOOD_VALUE);
    }
    
    /**
     * This is what the owl does most of the time: it hunts for
     * hare. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            List<Location> freeLocations =
                    nextFieldState.getFreeAdjacentLocations(getLocation());
            if(! freeLocations.isEmpty()) {
                giveBirth(nextFieldState, freeLocations);
            }
            // Move towards a source of food if found.
            Location nextLocation = findFood(currentField);
            if(nextLocation == null && ! freeLocations.isEmpty()) {
                // No food found - try to move to a free location.
                nextLocation = freeLocations.remove(0);
            }
            // See if it was possible to move.
            if(nextLocation != null) {
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
        return "Owl{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }

    /**
     * Increase the age. This could result in the owl's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this owl more hungry. This could result in the owl's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for hares adjacent to the current location.
     * Only the first live hare is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Organism organism = field.getOrganismAt(loc);
            if(organism instanceof Hare hare) {
                if(hare.isAlive()) {
                    hare.setDead();
                    foodLevel = HARE_FOOD_VALUE;
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }
    
    /**
     * Check whether this owl is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New owls are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && ! freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Owl young = new Owl(false, loc);
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
     * A owl can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
