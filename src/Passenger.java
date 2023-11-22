import java.util.UUID;

/**
 * Represents a passenger in the elevator system.
 * Each passenger has a unique identifier (UUID), starting floor, destination floor,
 * start time, end time, and direction (up or down).
 */
class Passenger implements Comparable<Passenger> {
    private int startFloor;
    private int destinationFloor;
    private int startTime = 0;
    private int endTime = 0;
    private boolean up;
    private UUID uuid;
    
    /**
     * Constructs a new Passenger object with the given starting and destination floors.
     * Generates a unique identifier (UUID) for the passenger.
     * @param startFloor       The floor from which the passenger starts the journey.
     * @param destinationFloor The floor to which the passenger wants to go.
     */
    public Passenger(int startFloor, int destinationFloor) {
    	this.uuid = UUID.randomUUID();
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
        this.up = startFloor < destinationFloor;
    }

    /**
     * Gets the starting floor of the passenger.
     * @return The starting floor.
     */
    public int getStartFloor() {
        return startFloor;
    }

    /**
     * Gets the destination floor of the passenger.
     * @return The destination floor.
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /**
     * Gets the start time of the passenger's journey.
     * @return The start time.
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the passenger's journey.
     * @return The end time.
     */
    public int getEndTime() {
        return endTime;
    }
    
    /**
     * Sets the end time of the passenger's journey.
     * @param endTime The end time to set.
     */
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
    
    /**
     * Sets the start time of the passenger's journey.
     * @param startTime The start time to set.
     */
    public void setStartTime(int startTime) {
    	this.startTime = startTime;
    }
    
    /**
     * Checks if the passenger is going up.
     * @return true if going up, false if going down.
     */
    public boolean goingUp() {
    	return this.up;
    }
    
    /**
     * Gets the total time spent by the passenger during the journey.
     * @return The conveyance time.
     */
    public int getConveyanceTime() {
        return this.endTime - this.startTime;
    }
    
    /**
     * Compares two passengers based on their destination floors.
     * Used for sorting in a priority queue to unload passengers more efficiently.
     * @param other The other passenger to compare.
     * @return A negative integer, zero, or a positive integer as this passenger is
     *         less than, equal to, or greater than the other.
     */
	@Override
    public int compareTo(Passenger other) {
        // compare the destination to use in priority queue to unload passenger easier
        return Integer.compare(this.destinationFloor, other.destinationFloor);
	}
	
	/**
     * Returns a string representation of the Passenger object.
     * @return A string containing the details of the passenger.
     */
	@Override
	public String toString() {
        return "Passenger {" +
				"UUID: " + uuid +
				", Start Floor: " + startFloor +
				", End Floor: " + destinationFloor +
				", Start Time: " + startTime +
				", End Time: " + endTime +
				", Direction: " + (up ? "Up" : "Down") +
				'}';
	 }
}