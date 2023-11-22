import java.util.PriorityQueue;

/**
 * The Elevator class represents an elevator system with the ability to load, unload, and travel between floors.
 */

class Elevator {
    private PriorityQueue<Passenger> upPassengers; // Holding going up passengers
    private PriorityQueue<Passenger> downPassengers; //Holding doing down passengers
    private int currentFloor; // current floor
    private int maxFloor; // the highest floor
    private int capacity; // elevator capacity 
    private boolean up; // elevator direction
    private static final int MAX_FLOORS_PER_TICK = 5; 
    private PriorityQueue<Integer> upDestination;
    private PriorityQueue<Integer> downDestination;
   
    /**
     * Constructs an Elevator object.
     * @param maxFloor      The highest floor the elevator can reach.
     * @param capacity      The capacity of the elevator.
     */
    public Elevator(int maxFloor, int capacity) {
        this.upPassengers = new PriorityQueue<>(); // passenger in the elevator
        this.downPassengers = new PriorityQueue<>((a, b) -> b.compareTo(a)); // max priority queue
        this.currentFloor = 1; //floor that elevator starts
        this.up = true; //direction of elevator
        this.capacity = capacity;
        this.upDestination = new PriorityQueue<>();
        this.downDestination = new PriorityQueue<>((a, b) -> Integer.compare(b, a)); // max priority queue
        this.maxFloor = maxFloor;
    }

    /**
     * Unloads passengers at the current floor and updates simulation results.
     * @param currentFloor The current floor of the elevator.
     * @param currentTick  The current tick of the simulation.
     * @param result       The simulation result to be updated.
     */
    public void unload(int currentFloor, int currentTick, SimulationResult result) {
    	if (this.up) {
    		// unload passengers that have a destination on the current floor
            unloadPassengers(upPassengers, currentFloor, currentTick, result);
            // remove the current floor from upDestination and downDestination
            removeCurrentFloorFromDestination(upDestination, currentFloor);
    	} else {
    		unloadPassengers(downPassengers, currentFloor, currentTick, result);
            removeCurrentFloorFromDestination(downDestination, currentFloor);
    	}
    }
    
    /**
     * Unloads passengers with the given destination floor at the current floor.
     * @param passengers   The PriorityQueue of passengers to unload.
     * @param currentFloor The current floor of the simulation.
     * @param currentTick  The current tick of the simulation.
     * @param result       The simulation result to be updated.
     */
    private void unloadPassengers(PriorityQueue<Passenger> passengers, int currentFloor, int currentTick, SimulationResult result) {
    	// If the elevator is at the current floor, remove all passengers whose destination floor is the same as the current floor.
        while (!passengers.isEmpty() && passengers.peek().getDestinationFloor() == this.currentFloor && this.currentFloor == currentFloor) {
            passengers.peek().setEndTime(currentTick);
            result.addPassenger(passengers.peek().getConveyanceTime());
            passengers.poll();
        }
    }
    
    /**
     * Removes the current floor from the specified destination PriorityQueue.
     * @param destinations The PriorityQueue of destinations to be modified.
     * @param currentFloor The current floor to be removed from the destinations.
     */
    private void removeCurrentFloorFromDestination(PriorityQueue<Integer> destinations, int currentFloor) {
        while (!destinations.isEmpty() && destinations.peek() == this.currentFloor && this.currentFloor==currentFloor) {
            destinations.poll();
        }
    }
    
    /**
     * Checks if the elevator is available to load a passenger based on direction and capacity.
     * @param floor      The floor where the passenger is waiting.
     * @param upRequest  True if the passenger is going up, false if going down.
     * @return True if the elevator is available to load the passenger, false otherwise.
     */
    private boolean isAvailableToLoad(int floor, boolean upRequest) {
    	if (upPassengers.isEmpty() && downPassengers.isEmpty()) { // if elevator has no passengers
    		return true;
    	}
        if (upRequest) {
            return (upPassengers.size() < capacity && up); // not full capacity
        } else {
            return (downPassengers.size() < capacity && !up); 
        }
    }
    
    /**
     * Loads a passenger into the elevator if it is available.
     * @param passenger The passenger to be loaded.
     * @return True if the passenger is successfully loaded, false otherwise.
     */
    public boolean load(Passenger passenger) {
        if (isAvailableToLoad(passenger.getStartFloor(), passenger.goingUp())) {
        	// Set elevator direction only when successfully loading a passenger
        	this.up = passenger.goingUp(); // update elevator direction to direction of passenger
            if (up) {
                upPassengers.add(passenger);
                upDestination.add(passenger.getDestinationFloor());
            } else {
                downPassengers.add(passenger);
                downDestination.add(passenger.getDestinationFloor());
            }
            return true;
        }
        return false;
    }

    public int getCurrentFloor() { // Gets the current floor of the elevator.
    	return this.currentFloor;
    }
    
    public boolean getElevatorDirection() { // Gets the direction of the elevator.
    	return this.up;
    }
    
    /**
     * Checks if the elevator is available to pick up a passenger based on direction, capacity, and current requests.
     * @param floor      The floor where the passenger is waiting.
     * @param upRequest  True if the passenger is going up, false if going down.
     * @return True if the elevator is available to pick up the passenger, false otherwise.
     */
    private boolean isAvailableToRequest(int floor, boolean upRequest) {
    	// check if the elevator is idle
    	if (!isRunning())
    		return true;
        // check if there is capacity and same direction to pick up passenger
        if ((upRequest && upPassengers.size() < capacity && up == upRequest) 
        	|| (!upRequest && downPassengers.size() < capacity && up == upRequest)) {
            // don't use equal to avoid requesting the elevator that already stops on the current floor
            // and can't pick up more passengers
            if (upRequest) {
                return currentFloor < floor;
            } else {
                return currentFloor > floor;
            }
        }

        return false;
    }
   
    /**
     * Handles passenger requests by updating the elevator state based on the passenger's request.
     * @param passenger The passenger requesting the elevator.
     * @return True if the elevator can fulfill the request, false otherwise.
     */
    public boolean passengerRequests(Passenger passenger) {
    	if (isAvailableToRequest(passenger.getStartFloor(), passenger.goingUp())) {
    		// update elevator direction to pick up passenger
    		if (this.currentFloor < passenger.getStartFloor()) {
    			this.up = true;
    		} else {
    			this.up = false;
    		}    		
    		if (up) {
    			this.upDestination.add(passenger.getStartFloor());
    		} else {
    			this.downDestination.add(passenger.getStartFloor());
    		}
    		
    		return true;
    	}
    	return false;
    }
    
    /**
    * Moves the elevator to the next floor based on its current state and passenger requests.
    */
    public void travel() {
    	// elevator need to travel to next floor after load and unload passenger
    	// during 1 tick, An elevator may travel between no more than 5 floors 
    	if (this.isRunning()) { // only travel when there is passenger or there is request for elevator
    		int floorNeedToTravel = MAX_FLOORS_PER_TICK;

            // Find the next floor to travel to
            int nextFloor = findNextDestination();

            // Calculate the floors needed to travel based on the next floor
            floorNeedToTravel = Math.min(Math.abs(nextFloor - this.currentFloor), floorNeedToTravel);
            // The elevator moves to the next floor
            if (up) {
                this.currentFloor = Math.min(this.currentFloor + floorNeedToTravel, this.maxFloor);
            } else {
                this.currentFloor = Math.max(this.currentFloor - floorNeedToTravel, 1);
            }
    	}
    	
    }
    
    public boolean isRunning() { // Checks if the elevator is in a running state by examining its queues.
    	// if any of this queue is not empty, elevator is in running state
        return !(upPassengers.isEmpty() && downPassengers.isEmpty() && upDestination.isEmpty() && downDestination.isEmpty());
    }

    private int findNextDestination() { // Finds the next destination floor based on remaining passenger requests and current state.
        // Determine the next floor based on the remaining passenger requests
    	if (up && !upDestination.isEmpty()) {
            return upDestination.peek();
        } else if (!up && !downDestination.isEmpty()) {
            return downDestination.peek();
        }
        // If no passengers or requests, stay on the current floor
        return currentFloor;
    }
    
    @Override
    public String toString() { // Generates a string representation of the elevator for debugging and logging purposes.
        return "Elevator{" +
                " up passengers =" + upPassengers +
                " down passengers =" + downPassengers +
                ", currentFloor =" + currentFloor +
                ", maxFloor =" + maxFloor +
                ", capacity =" + capacity +
                ", up =" + up +
                ", upDestination =" + upDestination +
                ", downDestination =" + downDestination +
                "}";
    }
}
