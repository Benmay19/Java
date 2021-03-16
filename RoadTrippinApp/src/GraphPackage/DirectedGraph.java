package GraphPackage;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class DirectedGraph<T> implements GraphInterface<T> {
    private DictionaryInterface<T, VertexInterface<T>> vertices;
    // private Map<T, VertexInterface<T>> vertices;
    private int edgeCount;

    public DirectedGraph() {
        vertices = new LinkedDictionary<>();
        // vertices = new LinkedHashMap<>();
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
    public boolean addVertex(T vertexLabel) {
        VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
        // VertexInterface<T> addOutcome = vertices.put(vertexLabel, new Vertex<>(vertexLabel));
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
    public boolean addEdge(T begin, T end, double edgeWeight) {
        boolean result = false;
        VertexInterface<T> beginVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        // VertexInterface<T> beginVertex = vertices.get(begin);
        // VertexInterface<T> endVertex = vertices.get(end);
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
    public boolean addEdge(T begin, T end) {
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
    public boolean hasEdge(T begin, T end) {
        boolean found = false;
        VertexInterface<T> beginVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        // VertexInterface<T> beginVertex = vertices.get(begin);
        // VertexInterface<T> endVertex = vertices.get(end);
        if ( (beginVertex != null) && (endVertex != null) )
        {
            Iterator<VertexInterface<T>> neighbors =
                    beginVertex.getNeighborIterator();
            while (!found && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
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
        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        while (vertexIterator.hasNext())
        {
            VertexInterface<T> nextVertex = vertexIterator.next();
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
     */
    @Override
    public QueueInterface<T> getBreadthFirstTraversal(T origin) {
        resetVertices();
        QueueInterface<T> traversalOrder = new LinkedQueue<>();
        QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();
        VertexInterface<T> originVertex = vertices.getValue(origin);
        originVertex.visit();
        traversalOrder.enqueue(origin);    // Enqueue vertex label
        vertexQueue.enqueue(originVertex); // Enqueue vertex
        while (!vertexQueue.isEmpty())
        {
            VertexInterface<T> frontVertex = vertexQueue.dequeue();
            Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
            while (neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
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
////////////////////////////////////////////CHECK DFS ????????????????????????
    /**
     * Performs a depth-first traversal of this graph.
     *
     * @param origin An object that labels the origin vertex of the traversal.
     * @return A queue of labels of the vertices in the traversal, with
     * the label of the origin vertex at the queue's front.
     */
    @Override
    public QueueInterface<T> getDepthFirstTraversal(T origin) {
        resetVertices();
        QueueInterface<T> traversalOrder = new LinkedQueue<>();
        StackInterface<VertexInterface<T>> vertexStack = new LinkedStack<>();
        VertexInterface<T> originVertex = vertices.getValue(origin);
        originVertex.visit();
        traversalOrder.enqueue(origin);    // Enqueue vertex label
        vertexStack.push(originVertex); // Enqueue vertex
        while (!vertexStack.isEmpty())
        {
            VertexInterface<T> frontVertex = vertexStack.pop();
            Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
            while (neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited())
                {
                    nextNeighbor.visit();
                    traversalOrder.enqueue(nextNeighbor.getLabel());
                    vertexStack.push(nextNeighbor);
                } // end if
            } // end while
        } // end while
        return traversalOrder;
    }

    /**
     * Performs a topological sort of the vertices in a graph without cycles.
     *
     * @return A stack of vertex labels in topological order, beginning
     * with the stack's top.
     */
    @Override
    public StackInterface<T> getTopologicalOrder() {
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
    public int getShortestPath(T begin, T end, StackInterface<T> path) {
        resetVertices();
        boolean done = false;
        QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<>();
        VertexInterface<T> originVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);
        originVertex.visit();
// Assertion: resetVertices() has executed setCost(0)
// and setPredecessor(null) for originVertex
        vertexQueue.enqueue(originVertex);
        while (!done && !vertexQueue.isEmpty())
        {
            VertexInterface<T> frontVertex = vertexQueue.dequeue();
            Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
            while (!done && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
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
        VertexInterface<T> vertex = endVertex;
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
    public double getCheapestPath(T begin, T end, StackInterface<T> path) {
        return 0;
    }
} // end DirectedGraph
