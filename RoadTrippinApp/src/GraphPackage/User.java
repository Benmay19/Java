package GraphPackage;

/**
 * models a user profile
 * @author Benjamin May
 */
public class User {
    private String fullName;
    private String userName;
    private String interest;

    /**
     * constructor using strings to initialize a user profile
     *
     * @param fullName users name as a String
     * @param userName users username as a String
     * @param interest user interest as a String
     */
    public User(String fullName, String userName, String interest) {
        this.fullName = fullName;
        this.userName = userName;
        this.interest = interest;
    }

    /**
     * returns the name of this User
     *
     * @return User name as a String
     * O(1)
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * updates the User's name by replacing the current name
     *
     * @param fullName user name as a String
     * O(1)
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * returns the username of this User
     *
     * @return User username as a String
     * O(1)
     */
    public String getUserName() {
        return userName;
    }

    /**
     * updates the User's username by replacing the current username
     *
     * @param userName User username as a String
     * O(1)
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * returns the interest of this User
     *
     * @return User interest as a String
     * O(1)
     */
    public String getInterest() {
        return interest;
    }

    /**
     * updates the User's interest by replacing the current interest
     *
     * @param interest user interest as a String
     * Precondition: Must provide a valid interest category and
     *                 should be controlled for by the Client.
     * O(1)
     */
    public void setInterest(String interest) {
        this.interest = interest;
    }

    /**
     * return a string version of the User
     *
     * @return User as a String
     * O(1)
     */
    public String toString() {
        return "Full Name: " + getFullName() + ", Username: " + getUserName() + ", Interest: " + getInterest();
    }
}
