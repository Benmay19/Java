The concept behind this project is an application that generates a road trip
itinerary of attractions, based on a specified user interest, that users can
stop at along the route from a designated starting location and final destination.
Users can create a profile and designate an area of interest that coincide with
the categories for the various locations/attractions (think categories in Google
Maps). When the program runs, the output is the road trip stops along the route 
selected specifically for the individual user based on a combination of interest
area and the star ratings of the different locations.

As is the case with many services, the first step if for the user to great a profile
To do this, Scanner is used to take user input in the creation of their user profile
which is stored as a User object. A User object contains the user's full name,
username, and their main area of interest with regard to attraction categories.

Once the user profile is created, Scanner is used again so the user can input the
starting location and destination of road trip.

This project uses a graph data structure that implements a linked dictionary to
create a sample map containing the locations of several generic attractions (museums,
parks, etc.). Location data is read into the program from a text file using the
File and Scanner classes. This data is used to create various Location objects which
are then used as the data portion of the vertices in the graph. Starting and ending
at the respective locations previously input by the user, weighted/directed edges
connect the vertices into "cities" (a "city" is represented by one layer in the graph),
with each "city" containing a total of five locations/attractions.

The main working part of the code uses a modified Breadth First Search (BFS) that 
is implemented with a queue. From the starting vertex, the BFS traverses one layer,
or "city," at a time and in each "city" one location/attraction is selected for the 
final road trip itinerary. This selection is made by exploring all possible 
locations in the "city" and makes a selection based on the following criteria:
 	1. If the "city" contains only one location/attraction with a category 
	   that matches the user's interest, that location is selected.
	2. If the "city" contains more than one location/attraction with a category 
	   that matches the user's interest, the star ratings of those locations 
	   are compared and the location with the highest star rating is selected.
	3. If the "city" contains no locations/attractions with a category 
	   that matches the user's interest, the location with the highest star 
	   rating is selected.

Once all vertices in the graph have been explored, the modified BFS returns a 
queue containing the stops selected for that user's custom road trip. These 
locations/attractions are then output, in order, for the user to serve as their 
personalized road trip itinerary.

No special installation requirements/dependencies are necessary. For best results, 
use the most up to date version of Java.

**Note: This project is just a working concept for an application that does the same
thing but would utilize a Google Maps (or some other map) API to identify actual
locations (with associated categories and star ratings) along the designated route.

Honor Code:
“I affirm that I have carried out my academic endeavors with full academic honesty.”
[Signed, Benjamin May]
