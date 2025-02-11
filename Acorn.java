import java.util.Random;
import java.util.List;

public class Acorn extends Organism {
    private static final Random rand = new Random();
    private static final int MAX_AGE = 10; // Define the age limit for acorns.
    private int age;

    public Acorn(Location location) {
        super(location);
        this.age = 0;
    }

    @Override
    public void act(Field currentField, Field nextFieldState) {
        incrementAge();
        if (isAlive()) {
            // Check if this acorn has been eaten by a Deer
            Location foodLocation = findFood(currentField);
            if (foodLocation != null) {
                // If food found, the acorn is eaten and dies.
                setDead();
            } else {
                // If acorn isn't eaten, it tries to reproduce.
                List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
                if (!freeLocations.isEmpty()) {
                    reproduce(nextFieldState, freeLocations); // Reproduce acorn to a free location.
                }
                // Try to move to a free adjacent location or stay if no space is available
                Location nextLocation = findFreeSpace(nextFieldState);
                if (nextLocation != null) {
                    setLocation(nextLocation);
                    nextFieldState.placeOrganism(this, nextLocation);
                } else {
                    // Overcrowding or no movement space, the acorn dies
                    setDead();
                }
            }
        }
    }

    private Location findFood(Field field) {
        for (Organism organism : field.getOrganisms()) {
            if (organism instanceof Deer && organism.isAlive()) {
                return organism.getLocation(); // Acorn is eaten by Deer.
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
        Acorn newAcorn = new Acorn(birthLocation); // Create a new acorn.
        nextFieldState.placeOrganism(newAcorn, birthLocation); // Place the new acorn in the field.
    }

    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead(); // Acorn dies if it exceeds the maximum age.
        }
    }
}
