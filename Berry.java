import java.util.List;
import java.util.Random;

public class Berry extends Organism {
    private static final Random rand = new Random();
    private static final int MAX_AGE = 5; // Define the age limit for berries.
    private int age;

    public Berry(Location location) {
        super(location);
        this.age = 0;
    }

    @Override
    public void act(Field currentField, Field nextFieldState) {
        incrementAge();
        if (isAlive()) {
            // Check if this berry has been eaten by Hares or Bears.
            Location foodLocation = findFood(currentField);
            if (foodLocation != null) {
                // If food (Hare or Bear) found, the berry is eaten and dies.
                setDead();
            } else {
                // If berry isn't eaten, it tries to reproduce.
                List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
                if (!freeLocations.isEmpty()) {
                    reproduce(nextFieldState, freeLocations); // Reproduce berry to a free location.
                }

                // Try to move to a free adjacent location or stay if no space is available.
                Location nextLocation = findFreeSpace(nextFieldState);
                if (nextLocation != null) {
                    setLocation(nextLocation);
                    nextFieldState.placeOrganism(this, nextLocation);
                } else {
                    // Overcrowding or no movement space, the berry dies.
                    setDead();
                }
            }
        }
    }

    private Location findFood(Field field) {
        for (Organism organism : field.getOrganisms()) {
            if ((organism instanceof Hare || organism instanceof Bear) && organism.isAlive()) {
                return organism.getLocation(); // Berry is eaten by Hares or Bears.
            }
        }
        return null;
    }

    private Location findFreeSpace(Field nextFieldState) {
        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
        if (!freeLocations.isEmpty()) {
            return freeLocations.get(rand.nextInt(freeLocations.size())); // Randomly select a free location.
        }
        return null;
    }

    private void reproduce(Field nextFieldState, List<Location> freeLocations) {
        Location birthLocation = freeLocations.get(rand.nextInt(freeLocations.size()));
        Berry newBerry = new Berry(birthLocation); // Create a new berry.
        nextFieldState.placeOrganism(newBerry, birthLocation); // Place the new berry in the field.
    }

    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead(); // Berry dies if it exceeds the maximum age.
        }
    }
}
