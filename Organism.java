/**
 * Common elements of all Animals and Plants in the forest.
 * Prey - {Hare, Deer}
 * Predator - {Wolf, Owl, Bear}
 * Plant - {Berries and Acorn}
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Organism
{
    // Whether the organism is alive or not.
    private boolean alive;
    // The organissm's position.
    private Location location;

    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Organism(Location location)
    {
        this.alive = true;
        this.location = location;
    }
    
    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void act(Field currentField, Field nextFieldState);
    
    /**
     * Check whether the animal/plant is alive or not.
     * @return true if the animal/plant is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the organism is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Set the animal's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }
}
