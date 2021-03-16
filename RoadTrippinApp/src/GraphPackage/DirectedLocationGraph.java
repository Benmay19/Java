package GraphPackage;
import java.util.Iterator;

/**
 * Models a map with various "cities" represented as layers in the graph.
 * @author Benjamin May
 *
 * @param <Location> Location object representing a physical location.
 */
public class DirectedLocationGraph<Location> implements GraphInterface<Location> {
    private DictionaryInterface<Location, VertexInterface<Location>> vertices;
    private int edgeCount;

    /**
     * Default constructor to initialize an empty directed
     * graph representing a map.
     */
    public DirectedLocationGraph() {
        vertices = new LinkedDictionary<>();
        edgeCount = 0;
    } // end default constructor

    /**
     * Adds a given vertex to this graph.
     *
     * @param vertexLabel An object that labels the new vertex and is
     *                    distinct from the labels of current vertices.
     * @return True if the vertex is added, or false if not.
     */
    @Override
    public boolean addVertex(Location vertexLabel) {
        VertexInterface<Location> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
        return addOutcome == null;
    }

    /**
     * Adds a weighted edge between two given distinct vertices that
     * are currently in this graph. The desired edge must not already
     * be in the graph. In a directed graph, the edge points toward
     * the second vertex given.
     *
     * @param begin      An object that labels the origin vertex of the edge.
     * @param end        An object, distinct from begin, that labels the end vertex of the edge.
     * @param edgeWeight The real value of the edge's weight.
     * @return True if the edge is added, or false if not.
     */
    @Override
    public boolean addEdge(Location begin, Location end, double edgeWeight) {
        boolean result = false;
        VertexInterface<Location> beginVertex = vertices.getValue(begin);
        VertexInterface<Location> endVertex = vertices.getValue(end);
        if ( (beginVertex != null) && (endVertex != null) )
            result = beginVertex.connect(endVertex, edgeWeight);
        if (result)
            edgeCount++;
        return result;
    } // end addEdge

    /**
     * Adds an unweighted edge between two given distinct vertices
     * that are currently in this graph. The desired edge must not
     * already be in the graph. In a directed graph, the edge points
     * toward the second vertex given.
     *
     * @param begin An object that labels the origin vertex of the edge.
     * @param end   An object, distinct from begin, that labels the end vertex of the edge.
     * @return True if the edge is added, or false if not.
     */
    @Override
    public boolean addEdge(Location begin, Location end) {
        return addEdge(begin, end, 0);
    }

    /**
     * Sees whether an edge exists between two given vertices.
     *
     * @param begin An object that labels the origin vertex of the edge.
     * @param end   An object that labels the end vertex of the edge.
     * @return True if an edge exists.
     */
    @Override
    public boolean hasEdge(Location begin, Location end) {
        boolean found = false;
        VertexInterface<Location> beginVertex = vertices.getValue(begin);
        VertexInterface<Location> endVertex = vertices.getValue(end);
        if ( (beginVertex != null) && (endVertex != null) )
        {
            Iterator<VertexInterface<Location>> neighbors =
                    beginVertex.getNeighborIterator();
            while (!found && neighbors.hasNext())
            {
                VertexInterface<Location> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    found = true;
            } // end while
        } // end if
        return found;
    }

    /**
     * Sees whether this graph is empty.
     *
     * @return True if the graph is empty.
     */
    @Override
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    /**
     * Gets the number of vertices in this graph.
     *
     * @return The number of vertices in the graph.
     */
    @Override
    public int getNumberOfVertices() {
        return vertices.getSize();
    }

    /**
     * Gets the number of edges in this graph.
     *
     * @return The number of edges in the graph.
     */
    @Override
    public int getNumberOfEdges() {
        return edgeCount;
    }

    /**
     * Removes all vertices and edges from this graph resulting in an empty graph.
     */
    @Override
    public void clear() {
        vertices.clear();
        edgeCount = 0;
    }

    protected void resetVertices()
    {
        Iterator<VertexInterface<Location>> vertexIterator = vertices.getValueIterator();
        while (vertexIterator.hasNext())
        {
            VertexInterface<Location> nextVertex = vertexIterator.next();
            nextVertex.unvisit();
            nextVertex.setCost(0);
            nextVertex.setPredecessor(null);
        } // end while
    } // end resetVertices

    /**
     * Performs a breadth-first traversal of this graph.
     *
     * @param origin An object that labels the origin vertex of the traversal.
     * @return A queue of labels of the vertices in the traversal, with
     * the label of the origin vertex at the queue's front.
     * O(n + m)
     */
    public QueueInterface<Location> getRoadTripPath(Location origin, User thisUser) {
        resetVertices();
        QueueInterface<Location> roadTripPath = new LinkedQueue<>();
        QueueInterface<VertexInterface<Location>> vertexQueue = new LinkedQueue<>();
        VertexInterface<Location> originVertex = vertices.getValue(origin);
        originVertex.visit();
        roadTripPath.enqueue(origin);    // Enqueue vertex label
        vertexQueue.enqueue(originVertex); // Enqueue vertex
        while (!vertexQueue.isEmpty())
        {
            VertexInterface<Location> frontVertex = vertexQueue.dequeue();
            Iterator<VertexInterface<Location>> neighbors = frontVertex.getNeighborIterator();
            GraphPackage.Location[] locationsToProcess = new GraphPackage.Location[5];
            int index = 0;
            while (neighbors.hasNext()) {
                VertexInterface<Location> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    locationsToProcess[index] = (GraphPackage.Location) nextNeighbor.getLabel();
                    index++;
                    // counter++
                }
            }
            GraphPackage.Location bestLocation = null;
            for (GraphPackage.Location location : locationsToProcess) {
                if (location != null) {
                    if (bestLocation == null)
                        bestLocation = location;
                    else if (location.getCategory().equals(thisUser.getInterest()) &&
                            bestLocation.getCategory().equals(thisUser.getInterest())) {
                        if (location.getStarRating() > bestLocation.getStarRating())
                            bestLocation = location;
                    } else if (location.getCategory().equals(thisUser.getInterest()) &&
                            !bestLocation.getCategory().equals(thisUser.getInterest()))
                        bestLocation = location;
                    else if (!location.getCategory().equals(thisUser.getInterest()) &&
                            !bestLocation.getCategory().equals(thisUser.getInterest())) {
                        if (location.getStarRating() > bestLocation.getStarRating())
                            bestLocation = location;
                    }
                }
            }
            if (bestLocation != null) {
                roadTripPath.enqueue((Location) bestLocation);
                vertexQueue.enqueue(vertices.getValue((Location) bestLocation));
            } // end if
        } // end while
        return roadTripPath;
    }

    /**
     * Performs a breadth-first traversal of this graph.
     *
     * @param origin An object that labels the origin vertex of the traversal.
     * @return A queue of labels of the vertices in the traversal, with
     * the label of the origin vertex at the queue's front.
     */
    @Override
    public QueueInterface<Location> getBreadthFirstTraversal(Location origin) {
        resetVertices();
        QueueInterface<Location> traversalOrder = new LinkedQueue<>();
        QueueInterface<VertexInterface<Location>> vertexQueue = new LinkedQueue<>();
        VertexInterface<Location> originVertex = vertices.getValue(origin);
        originVertex.visit();
        traversalOrder.enqueue(origin);    // Enqueue vertex label
        vertexQueue.enqueue(originVertex); // Enqueue vertex
        while (!vertexQueue.isEmpty())
        {
            VertexInterface<Location> frontVertex = vertexQueue.dequeue();
            Iterator<VertexInterface<Location>> neighbors = frontVertex.getNeighborIterator();
            while (neighbors.hasNext())
            {
                VertexInterface<Location> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited())
                {
                    nextNeighbor.visit();
                    traversalOrder.enqueue(nextNeighbor.getLabel());
                    vertexQueue.enqueue(nextNeighbor);
                } // end if
            } // end while
        } // end while
        return traversalOrder;
    }

    /**
     * Performs a depth-first traversal of this graph.
     *
     * @param origin An object that labels the origin vertex of the traversal.
     * @return A queue of labels of the vertices in the traversal, with
     * the label of the origin vertex at the queue's front.
     */
    @Override
    public QueueInterface<Location> getDepthFirstTraversal(Location origin) {
        return null;
    }

    /**
     * Performs a topological sort of the vertices in a graph without cycles.
     *
     * @return A stack of vertex labels in topological order, beginning
     * with the stack's top.
     */
    @Override
    public StackInterface<Location> getTopologicalOrder() {
        return null;
    }

    /**
     * Finds the shortest-length path between two given vertices in this graph.
     *
     * @param begin An object that labels the path's origin vertex.
     * @param end   An object that labels the path's destination vertex.
     * @param path  A stack of labels that is empty initially;
     *              at the completion of the method, this stack contains
     *              the labels of the vertices along the shortest path;
     *              the label of the origin vertex is at the top, and
     *              the label of the destination vertex is at the bottom.
     * @return The length of the shortest path.
     */
    @Override
    public int getShortestPath(Location begin, Location end, StackInterface<Location> path) {
        resetVertices();
        boolean done = false;
        QueueInterface<VertexInterface<Location>> vertexQueue = new LinkedQueue<>();
        VertexInterface<Location> originVertex = vertices.getValue(begin);
        VertexInterface<Location> endVertex = vertices.getValue(end);
        originVertex.visit();
// Assertion: resetVertices() has executed setCost(0)
// and setPredecessor(null) for originVertex
        vertexQueue.enqueue(originVertex);
        while (!done && !vertexQueue.isEmpty())
        {
            VertexInterface<Location> frontVertex = vertexQueue.dequeue();
            Iterator<VertexInterface<Location>> neighbors = frontVertex.getNeighborIterator();
            while (!done && neighbors.hasNext())
            {
                VertexInterface<Location> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited())
                {
                    nextNeighbor.visit();
                    nextNeighbor.setCost(1 + frontVertex.getCost());
                    nextNeighbor.setPredecessor(frontVertex);
                    vertexQueue.enqueue(nextNeighbor);
                } // end if
                if (nextNeighbor.equals(endVertex))
                    done = true;
            } // end while
        } // end while
// Traversal ends; construct shortest path
        int pathLength = (int)endVertex.getCost();
        path.push(endVertex.getLabel());
        VertexInterface<Location> vertex = endVertex;
        while (vertex.hasPredecessor())
        {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        } // end while
        return pathLength;
    }

    /**
     * Finds the least-cost path between two given vertices in this graph.
     *
     * @param begin An object that labels the path's origin vertex.
     * @param end   An object that labels the path's destination vertex.
     * @param path  A stack of labels that is empty initially;
     *              at the completion of the method, this stack contains
     *              the labels of the vertices along the cheapest path;
     *              the label of the origin vertex is at the top, and
     *              the label of the destination vertex is at the bottom.
     * @return The cost of the cheapest path.
     */
    @Override
    public double getCheapestPath(Location begin, Location end, StackInterface<Location> path) {
        return 0;
    }
} // end DirectedGraph
