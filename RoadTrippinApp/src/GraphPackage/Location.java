package GraphPackage;

/**
 * models a physical location
 * @author Benjamin May
 */
public class Location {

    private final String locationName;
    private final String category;
    private final int starRating;

    /**
     * constructor using strings to initialize a user profile
     *
     * @param locationName location name as a String
     * @param category location category as a String
     */
    public Location(String locationName, String category) {
        this(locationName, category, 0);
    }

    /**
     * constructor using strings to initialize a user profile
     *
     * @param locationName location name as a String
     * @param category location category as a String
     * @param starRating location star rating a an integer
     */
    public Location(String locationName, String category, int starRating) {
        this.locationName = locationName;
        this.category = category;
        this.starRating = starRating;
    }

    /**
     * returns the name of this Location
     *
     * @return Location name as a String
     * O(1)
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * returns the category of this Location
     *
     * @return Location name as a String
     * O(1)
     */
    public String getCategory() {
        return category;
    }

    /**
     * returns the star rating of this Location
     *
     * @return Location star rating as an integer
     * O(1)
     */
    public int getStarRating() {return starRating;}

    /**
     * return a string version of the Location
     *
     * @return Location as a String
     * O(1)
     */
    public String toString() {
        return "Location Name: " + getLocationName() + "\nCategory: " + getCategory() +
                "\nStar Rating: " + getStarRating() + "\n";
    }
}
