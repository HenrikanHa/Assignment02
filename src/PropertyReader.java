import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * The PropertyReader class reads and validates properties for an elevator simulation.
 * It loads properties from a file and ensures that the values meet certain criteria.
 * If no file is provided as a command-line argument, it defaults to default values.
 */
public class PropertyReader {
	// Default values
	private static final String STRUCTURES = "linked";
    private static final int FLOOR = 32;
    private static final double PASSENGER = 0.03;
    private static final int ELEVATOR = 1;
    private static final int CAPACITY = 10;
    private static final int DURATION = 500;
	
	/**
     * The main method of the PropertyReader class.
     * It reads and validates properties for an elevator simulation, then prints the read values.
     * @param args Command-line arguments. If a property file path is provided, it is used; otherwise, the default values is loaded.
     * @throws Exception If there is an error during file reading or property parsing.
     */
    public static void main(String[] args) throws Exception {
        // Load properties from the file
        Properties properties = new Properties();
        try (FileReader input = new FileReader(getPropertyFilePath(args))) {
            properties.load(input);
        } catch (IOException e) {
        	// If there is an error reading the file, fall back to constant values
            setDefaultProperties(properties);
        }

        // Validate and adjust properties
        validateAndAdjustProperties(properties);

        // Print the read values
        printProperties(properties);      

        // Initiate and run ElevatorSimulation
        ElevatorSimulation elevatorSimulation = new ElevatorSimulation(properties);
        elevatorSimulation.runSimulation();
    }
    
    /**
     * Retrieves the property file path from command-line arguments.
     * @param args Command-line arguments
     * @return The property file path if provided, otherwise an empty string
     */
    private static String getPropertyFilePath(String[] args) {
        // Check if a property file is provided as a command-line argument
        return args.length > 0 ? args[0] : "";
    }
    
    /**
     * Sets default properties for the simulation.
     * @param properties The properties object to set default values
     */
    private static void setDefaultProperties(Properties properties) {
        properties.setProperty("structures", STRUCTURES);
        properties.setProperty("floors", String.valueOf(FLOOR));
        properties.setProperty("passengers", String.valueOf(PASSENGER));
        properties.setProperty("elevators", String.valueOf(ELEVATOR));
        properties.setProperty("elevatorCapacity", String.valueOf(CAPACITY));
        properties.setProperty("duration", String.valueOf(DURATION));
    }
    
    /**
     * Validates and adjusts properties based on predefined rules.
     * @param properties The properties object to be validated and adjusted
     */
    private static void validateAndAdjustProperties(Properties properties) {
        // Validate and adjust properties
    	validateAndAdjustStructureProperty(properties, "structures", STRUCTURES);
    	// If floors property is less than 2, set it to 32
        validateAndAdjustIntProperty(properties, "floors", FLOOR, 2);
        // If elevators property is less than 1, set it to 1
        validateAndAdjustIntProperty(properties, "elevators", ELEVATOR, 1);
        // If elevatorCapacity property is less than 1, set it to 10
        validateAndAdjustIntProperty(properties, "elevatorCapacity", CAPACITY, 1);
        // If duration property is less than 1, set it to 500
        validateAndAdjustIntProperty(properties, "duration", DURATION, 1);
        // If passengers property is less than 0 or more than 1, set it to 0.03
        validateAndAdjustDoubleProperty(properties, "passengers", PASSENGER, 0, 1);
    }

    /**
     * Validates and adjusts the "structures" property.
     * @param properties    The properties object to be validated and adjusted
     * @param key           The key of the property to be validated
     * @param defaultValue  The default value to be set if the property is invalid
     */
    private static void validateAndAdjustStructureProperty(Properties properties, String key, String defaultValue) {
    	String structures = properties.getProperty("structures");
        if (!structures.equals("array") && !structures.equals("linked")) {
        	// If structures property is invalid, set it to "linked"
        	properties.setProperty("structures", defaultValue);
        }
        
    }

    /**
     * Validates and adjusts an integer property based on predefined rules.
     * @param properties    The properties object to be validated and adjusted
     * @param key           The key of the property to be validated
     * @param defaultValue  The default value to be set if the property is invalid
     * @param minValue      The minimum allowed value for the property
     */
    private static void validateAndAdjustIntProperty(Properties properties, String key, int defaultValue, int minValue) {
        int value = Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        if (value < minValue) {
            properties.setProperty(key, String.valueOf(defaultValue));
        }
    }

    /**
     * Validates and adjusts a double property based on predefined rules.
     * @param properties    The properties object to be validated and adjusted
     * @param key           The key of the property to be validated
     * @param defaultValue  The default value to be set if the property is invalid
     * @param minValue      The minimum allowed value for the property
     * @param maxValue      The maximum allowed value for the property
     */
    private static void validateAndAdjustDoubleProperty(Properties properties, String key, double defaultValue, double minValue, double maxValue) {
        double value = Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
        if (value <= minValue || value >= maxValue) {
            properties.setProperty(key, String.valueOf(defaultValue));
        }
    }

    /**
     * Prints the simulation properties to the console.
     * @param properties The properties object to be printed
     */
    private static void printProperties(Properties properties) {
        System.out.println("structures: " + properties.getProperty("structures"));
        System.out.println("floors: " + properties.getProperty("floors"));
        System.out.println("passengers: " + properties.getProperty("passengers"));
        System.out.println("elevators: " + properties.getProperty("elevators"));
        System.out.println("elevatorCapacity: " + properties.getProperty("elevatorCapacity"));
        System.out.println("duration: " + properties.getProperty("duration"));
    }
}