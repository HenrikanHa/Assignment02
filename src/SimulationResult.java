/**
 * The SimulationResult class represents the result of a simulation for an elevator system.
 * It tracks statistics such as the total number of passengers, total conveyance time,
 * longest and shortest conveyance times.
 */
public class SimulationResult {
	private int totalPassengers; // Total number of passengers in the simulation
    private int totalConveyanceTime; // Total conveyance time for all passengers
    private int longestTime; // Longest conveyance time among all passengers
    private int shortestTime; // Shortest conveyance time among all passengers

    /**
     * Constructs a new SimulationResult object with initial values.
     * Initializes totalPassengers, totalConveyanceTime, longestTime, and shortestTime.
     */
    public SimulationResult() {
        this.totalPassengers = 0;
        this.totalConveyanceTime = 0;
        this.longestTime = Integer.MIN_VALUE;
        this.shortestTime = Integer.MAX_VALUE;
    }

    /**
     * Adds a passenger with the given conveyance time to the simulation.
     * @param conveyanceTime The time it takes for the passenger to be conveyed by the elevator.
     *                       Should be greater than 0; otherwise, an error message is printed, and
     *                       the passenger is not added.
     */
    public void addPassenger(int conveyanceTime) {
    	if (conveyanceTime <= 0) {
    		System.out.println("Invalid conveyance time");
    		return;
    	}
        // Update total passengers and total conveyance time
        totalPassengers++;
        totalConveyanceTime += conveyanceTime;

        // Update longest and shortest times
        if (conveyanceTime > longestTime) {
            longestTime = conveyanceTime;
        }

        if (conveyanceTime < shortestTime) {
            shortestTime = conveyanceTime;
        }
    }

    /**
     * Calculates and prints statistics based on the added passengers.
     * If no passengers are added, it prints a message indicating there are no passengers in the simulation.
     */
    public void calculateStatistics() {
        if (totalPassengers == 0) {
            System.out.println("No passengers in the simulation.");
            return;
        }
        
    	// Calculate average conveyance time
        double averageTime = (double) totalConveyanceTime / totalPassengers;

        // Print statistics
        System.out.println("Average Time: " + averageTime);
        System.out.println("Longest Time: " + longestTime);
        System.out.println("Shortest Time: " + shortestTime);
    }
}