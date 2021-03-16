package GraphPackage;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * driver for RoadTrippin' Application (CSC 151 Final Project)
 *
 * @author Benjamin May
 * “I affirm that I have carried out my academic endeavors with full academic honesty.”
 * [Signed, Benjamin May]
 */
public class Client {

    public static final int LOCATIONS_PER_CITY = 5;

    public static void main(String[] args) {
        User currentUser;
        DirectedLocationGraph<Location> locationGraph;
        Location[] locations;
        Location start;
        Location end;

        // create user profiles
        currentUser = createNewUser();

        // Get start and end locations for trip from user
        Scanner getStart = new Scanner(System.in);
        System.out.println("Please enter your starting location: ");
        String startingLocation = getStart.nextLine();
        start = new Location(startingLocation, "Start", 0);
        Scanner getEnd = new Scanner(System.in);
        System.out.println("Please enter your starting location: ");
        String endingLocation = getEnd.nextLine();
        end = new Location(endingLocation, "Destination", 0);


        // Create new Scanner to read graph locations from file
        // System.out.println(System.getProperty("user.dir"));  // Get File read location
        String fileName = "locations.txt";
        Scanner fileData = null;
        try {
            fileData = new Scanner(new File(fileName));
        }
        catch (FileNotFoundException e) {
            System.out.println("Scanner error opening the file: " + fileName);
            System.out.println(e.getMessage());
        }

        // parse location data from text file and store Location objects in array
        assert fileData != null;
        locations = readLocationFile(fileData, start, end);

        // create graph (add vertices and edges)
        locationGraph = createLocationGraph(locations);

        System.out.println("User Information:\n" + currentUser);
        System.out.println("\n" + currentUser.getFullName() + ",\nThank you for choosing " +
                "the RoadTrippin' App to create your Custom Road Trip Experience.\n" +
                "Your Road Trip starts in " + locations[0].getLocationName() + " and ends at " +
                locations[locations.length - 1].getLocationName() + ".\n\nBelow are the stops selected " +
                "specifically for you based on your interest in " + currentUser.getInterest() + "!\n");

        // Returns a queue of all the locations selected for the specific road trip.
        // Iterates through the queue and prints out the road trip location information
        // for the user.
        LinkedQueue<Location> roadTripResults = (LinkedQueue<Location>)
                locationGraph.getRoadTripPath(locations[0], currentUser);
        int totalStops = roadTripResults.getSize();
        int stopNumber = 0;
        while (!roadTripResults.isEmpty()) {
            if (stopNumber == 0)
                System.out.println("Starting Location:");
            else if (stopNumber == totalStops - 1)
                System.out.println("Road Trip Destination:");
            else
                System.out.println("Stop #" + stopNumber);
            System.out.println(roadTripResults.dequeue());
            stopNumber++;
        }
    }

    // Start private helper methods

    /**
     * Creates a new User object from user input
     *
     * @return a new User object
     * O(1)
     */
    private static User createNewUser() {
        Scanner newName = new Scanner(System.in);
        Scanner newUserName = new Scanner(System.in);
        System.out.println("Please enter your full name: ");
        String name = newName.nextLine();
        System.out.println("Please enter your user name: ");
        String userName = newUserName.nextLine();
        String interest = getInterestCategory();
        return new User(name, userName, interest);
    }

    /**
     * Allows users to select an interest category from a list of options.
     *
     * @return the user's interest as a String
     * O(1)
     */
    private static String getInterestCategory() {
        Scanner newInterestCategory = new Scanner(System.in);
        System.out.println("Please enter your travel interest by selecting a number " +
                "from the following list:\n 1. Art\n 2. Parks/Outdoor\n 3. History\n " +
                "4. Sports\n 5. Science/Technology\n Press any other key for 'None'");
        int intInterestCategory = newInterestCategory.nextInt();
        return switch (intInterestCategory) {
            case 1 -> "Art";
            case 2 -> "Parks/Outdoor";
            case 3 -> "History";
            case 4 -> "Sports";
            case 5 -> "Science/Technology";
            default -> "None";
        };
    }

    /**
     * Reads attraction/location data from a text file and creates
     * a new Location object from the data on each line of the text
     * file and adds each new Location object to a Location array.
     *
     * @param data Scanner data read from read from a text file
     * @return an array of Location objects
     * O(n) where n is the number of delimiter separated words in the file
     */
    private static Location[] readLocationFile(Scanner data, Location start, Location end) {
        Location[] locations = new Location[27];
        locations[0] = start;
        int index = 1;
        data.useDelimiter(",|\\r\\n");
        while (data.hasNext()) {
            String locationName = data.next();
            String category = data.next();
            int starRating = Integer.parseInt(data.next());
            locations[index] = new Location(locationName, category, starRating);
            index++;
        } // end while
        data.close();
        locations[index] = end;
        return locations;
    } // end readFile

    /**
     * Creates a new graph of Location objects.
     * Adds all vertices to graph then adds edges to connect the vertices.
     *
     * Note: Since this is a specific sample implementation, I chose an
     * implementation that allowed exactly five locations per "city."
     *
     * @param locations an array of location objects
     * @return a weighted, directed graph of Location objects
     * O(n^2)
     */
    private static DirectedLocationGraph<Location> createLocationGraph(Location[] locations) {
        DirectedLocationGraph<Location> locationGraph = new DirectedLocationGraph<>();
        int iCount = 1;
        int connectNextCity = 2 * LOCATIONS_PER_CITY;

        for (Location location : locations)
            locationGraph.addVertex(location);

        for (int i = iCount; i < LOCATIONS_PER_CITY + iCount; i++) {
            locationGraph.addEdge(locations[0], locations[i], locations[i].getStarRating());
            for (int j = LOCATIONS_PER_CITY + iCount; j < connectNextCity + iCount; j++)
                locationGraph.addEdge(locations[i], locations[j], locations[j].getStarRating());
        } iCount += LOCATIONS_PER_CITY;

        for (int i = iCount; i < LOCATIONS_PER_CITY + iCount; i++) {
            for (int j = LOCATIONS_PER_CITY + iCount; j < connectNextCity + iCount; j++)
                locationGraph.addEdge(locations[i], locations[j], locations[j].getStarRating());
        } iCount += LOCATIONS_PER_CITY;

        for (int i = iCount; i < LOCATIONS_PER_CITY + iCount; i++) {
            for (int j = LOCATIONS_PER_CITY + iCount; j < connectNextCity + iCount; j++)
                locationGraph.addEdge(locations[i], locations[j], locations[j].getStarRating());
        } iCount += LOCATIONS_PER_CITY;

        for (int i = iCount; i < LOCATIONS_PER_CITY + iCount; i++) {
            for (int j = LOCATIONS_PER_CITY + iCount; j < locations.length - 1; j++)
                locationGraph.addEdge(locations[i], locations[j], locations[j].getStarRating());
        } iCount += LOCATIONS_PER_CITY;

        for (int i = iCount; i < locations.length - 1; i++)
            locationGraph.addEdge(locations[i], locations[locations.length - 1]);

        return locationGraph;
    }
}
