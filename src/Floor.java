import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Represents a floor in a building with up and down queues for passengers.
 */
class Floor {
    private Queue<Passenger> up; // Queue for passengers going up
    private Queue<Passenger> down; // Queue for passengers going down
    private int floorNumber; // The floor number
    private List<Integer> possibleDestinations; // List of possible destination floors for passengers
    private Random random = new Random(); // Randomize passengers’ destinations

    /**
     * Constructor for the Floor class.
     * @param floorNumber The floor number.
     * @param maxFloor The maximum floor in the building.
     * @param isLinked Indicates whether to use linked structures (true) or arrays (false).
     */
    public Floor(int floorNumber, int maxFloor, boolean isLinked) {
        this.floorNumber = floorNumber;
        // Initialize queues based on the type of structure chosen
        if (isLinked) {
        	this.up = new LinkedList<>();
            this.down = new LinkedList<>();
            this.possibleDestinations = new LinkedList<>();
        } else {
        	this.up = new ArrayDeque<>();
            this.down = new ArrayDeque<>();
            this.possibleDestinations = new ArrayList<>();
        }  
        // Populate the list of possible destination floors for passenger appears in this floor
        possibleDestinationFloors(maxFloor);
    }

    /**
     * Gets the queue for passengers going up.
     * @return The up queue.
     */
    public Queue<Passenger> getUpQueue() {
        return up;
    }

    /**
     * Gets the queue for passengers going down.
     * @return The down queue.
     */
    public Queue<Passenger> getDownQueue() {
        return down;
    }
    
    /**
     * Gets the floor number.
     * @return The floor number.
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Generates a passenger with a random destination floor.
     * @return A randomly generated passenger.
     */
    public Passenger generatePassenger() {
        int randomIndex = random.nextInt(this.possibleDestinations.size());
        int destination = this.possibleDestinations.get(randomIndex);
        return new Passenger(floorNumber, destination);
    }

    /**
     * Loads a passenger onto the appropriate queue (up or down) based on their destination floor.
     * @param passenger The passenger to be loaded.
     */
    public void load(Passenger passenger) {
        if (passenger.getStartFloor() < passenger.getDestinationFloor()) {
            up.offer(passenger); // Going up
        } else {
            down.offer(passenger); // Going down
        }
    }
    
    // Populates the list of possible destination floors
    private void possibleDestinationFloors(int maxFloor) {

        // Iterate from the 1st floor to the floor - 1;
        for (int floor = 1; floor < this.floorNumber; floor++) {
            this.possibleDestinations.add(floor);
        }

        // Iterate from current floor + 1 up to the maximum floor
        for (int floor = this.floorNumber + 1; floor <=  maxFloor; floor++) {
            this.possibleDestinations.add(floor);
        }
    }
    
    /**
     * Provide a string representation of the floor.
     * @return A string representation of the floor.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Floor ").append(floorNumber).append("\n");

        result.append("Up Queue:            ");
        appendQueueInfo(up, result);
        
        result.append("Down Queue:          ");
        appendQueueInfo(down, result);

        result.append("Possible Destinations: ").append(possibleDestinations);
        return result.toString();
    }

    // Appends queue information to the StringBuilder
    private void appendQueueInfo(Queue<Passenger> passengerQueue, StringBuilder result) {
        result.append("[ ");
        for (Passenger passenger : passengerQueue) {
            result.append(passenger.toString()).append(", ");
        }
        if (!passengerQueue.isEmpty()) {
            result.setLength(result.length() - 2);  // Remove the trailing comma and space
        }
        result.append(" ]\n");
    }
}