import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal/object.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The dimensions of the field.
    private final int depth, width;
    // Animals mapped by location.
    private final Map<Location, Organism> organism = new HashMap<>();
    // The animals.
    private final List<Organism> organisms = new ArrayList<>();

    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will be lost.
     * @param anAnimal The animal to be placed.
     * @param location Where to place the animal.
     */
    public void placeOrganism(Organism anOrganism, Location location)
    {
        assert location != null;
        Object other = organism.get(location);
        if(other != null) {
            organisms.remove(other);
        }
        organism.put(location, anOrganism);
        organisms.add(anOrganism);
    }
    
    /**
     * Return the animal at the given location, if any.
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Organism getOrganismAt(Location location)
    {
        return organism.get(location);
    }
    
    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getAdjacentLocations(location);
        for(Location next : adjacent) {
            Organism anOrganism = organism.get(next);
            if(anOrganism == null) {
                free.add(next);
            }
            else if(!anOrganism.isAlive()) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location)
    {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Print out the number of foxes and rabbits in the field.
     */
    public void fieldStats()
    {
        int numWolves = 0, numHares = 0, numDeers = 0, numOwls = 0, numBears = 0;
        int numBerries = 0, numAcorns = 0;
    
        for (Object obj : organism.values()) {
            if (obj instanceof Organism organism && organism.isAlive()) {
                switch (organism) {
                    case Wolf w -> numWolves++;
                    case Hare h -> numHares++;
                    case Deer d -> numDeers++;
                    case Owl o -> numOwls++;
                    case Bear b -> numBears++;
                    case Berry b -> numBerries++;
                    case Acorn a -> numAcorns++;
                    default -> System.out.println("Unhandled animal: " + organism.getClass().getSimpleName());
                }
            }
        }
        StringBuilder text = new StringBuilder();
        text.append("Hares: " + numHares);
        text.append("  Wolves: " + numWolves);
        text.append("  Deers: " + numDeers);
        text.append("  Owls: " + numOwls);
        text.append("  Bears: " + numBears);
        text.append("  Berries: " + numBerries);
        text.append("  Acorns: " + numAcorns);
        System.out.println(text);
    }
    
    /**
     * Empty the field.
     */
    public void clear()
    {
        organism.clear();
    }

    /**
     * Return whether there is at least one rabbit and one fox in the field.
     * @return true if there is at least one rabbit and one fox in the field.
     */
    public boolean isViable()
    {
        boolean hareFound = false;
        boolean wolfFound = false;
        Iterator<Organism> it = organisms.iterator();
        while(it.hasNext() && ! (hareFound && wolfFound)) {
            Organism anOrganism = it.next();
            if(anOrganism instanceof Hare hare) {
                if(hare.isAlive()) {
                    hareFound = true;
                }
            }
            else if(anOrganism instanceof Wolf wolf) {
                if(wolf.isAlive()) {
                    wolfFound = true;
                }
            }
        }
        return hareFound && wolfFound;
    }
    
    /**
     * Get the list of animals.
     */
    public List<Organism> getOrganisms()
    {
        return organisms;
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
}
