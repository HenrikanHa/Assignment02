import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;

/**
 * Represents a simulation of elevators and passengers in a building.
 */
public class ElevatorSimulation {
    private List<Elevator> elevators;
    private List<Floor> floors;
    private int tick; // Simulation time in ticks
    private int duration; // Duration of the simulation in ticks
    private double passengerProbability; // Probability of a new passenger arriving at a floor in each tick
    private SimulationResult result = new SimulationResult(); // Result object to store simulation results
	private Random random = new Random();

    /**
     * Constructor for the ElevatorSimulation class.
     * @param properties The properties specifying the simulation parameters.
     */
    public ElevatorSimulation(Properties properties) {
    	// Determine whether to use LinkedList or ArrayList based on the "structures" property
    	boolean isLinkedList = properties.getProperty("structures").equals("linked");
    	if (isLinkedList) {
    		this.elevators = new LinkedList<>();
            this.floors = new LinkedList<>();
    	} else {
    		this.elevators = new ArrayList<>();
            this.floors = new ArrayList<>();
    	}

    	// Initialize floors
        int numFloors = Integer.parseInt(properties.getProperty("floors"));
        
        for (int i = 1; i <= numFloors; i++) {
            this.floors.add(new Floor(i, numFloors, isLinkedList));
        }
        // Initialize elevators
        int numElevators = Integer.parseInt(properties.getProperty("elevators"));
        int elevatorCapacity = Integer.parseInt(properties.getProperty("elevatorCapacity"));
        for (int i = 0; i < numElevators; i++) {
            this.elevators.add(new Elevator(numFloors, elevatorCapacity));
        }
        // Initialize simulation parameters
        this.tick = 0;
        this.duration = Integer.parseInt(properties.getProperty("duration"));
        this.passengerProbability = Double.parseDouble(properties.getProperty("passengers"));
    }

    /**
     * Runs the elevator simulation for the specified duration.
     */
    public void runSimulation() {
        while (tick < this.duration) {
        	// Process simulation for each floor
        	for (Floor floor: this.floors) {
        		int floorNumber = floor.getFloorNumber();
        		
        		// Unload each elevator that stops at this floor
        		for (Elevator elevator: this.elevators) {
        			elevator.unload(floorNumber, tick, result);
        		}
       
        		// Check if there is passenger based on passenger probability given in property file
        		// if passenger arrives, then request elevator and wait in that floor
        		if (random.nextDouble() <= this.passengerProbability) {
        			// Create a new passenger, 
        			Passenger newPassenger = floor.generatePassenger();
            		// Add passenger to correct queue
        			floor.load(newPassenger);		
        			newPassenger.setStartTime(tick);
        			
        		}
        		
        		// Load passenger in up/down queue into elevator    	
        		// Check if there is any elevator that stops at this floor
    			// Load customer into available elevator with same direction
        		
    			for (Elevator elevator: this.elevators) {
    				if (elevator.getCurrentFloor() == floorNumber) {
    			        boolean elevatorDirection = elevator.isRunning() ? elevator.getElevatorDirection() : !floor.getUpQueue().isEmpty();

    			        Queue<Passenger> queueToLoad = elevatorDirection ? floor.getUpQueue() : floor.getDownQueue();

    			        // Load passengers into the elevator until it's full
    			        while (!queueToLoad.isEmpty() && elevator.load(queueToLoad.peek())) {
    			            // Assuming that load() returns true if the passenger is successfully loaded
    			            queueToLoad.poll();
    			        }
    			    }

        		}
    				
        	}
        	
        	// Process queues for any remaining passengers
        	for (Floor floor: this.floors) {
        		//if there is any passenger left to pickup, request elevator
        		processQueue(floor.getUpQueue());
        		processQueue(floor.getDownQueue());
        	}
        	
        	// When it has passenger or there is passenger is waiting, elevator need to travel
			for (Elevator elevator: this.elevators) {
    			elevator.travel();
    		} 
        	
            tick++;
        }
        
        // Calculate simulation statistics
        result.calculateStatistics();
    }
    
    /**
     * Process the given passenger queue by requesting elevators based on passenger requests.
     * @param queue The passenger queue to process.
     */
    private void processQueue(Queue<Passenger> passengerQueue) {
		for (Passenger passenger : passengerQueue) {
			for (Elevator elevator : this.elevators) {
				if (elevator.passengerRequests(passenger)) {
					// Stop requesting elevators after a successful request is accepted
					break;
				}
			}
		}
    }
}