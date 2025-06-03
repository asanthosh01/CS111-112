package rumaps;

import java.util.*;

/**
 * This class represents the information that can be attained from the Rutgers University Map.
 * 
 * The RUMaps class is responsible for initializing the network, streets, blocks, and intersections in the map.
 * 
 * You will complete methods to initialize blocks and intersections, calculate block lengths, find reachable intersections,
 * minimize intersections between two points, find the fastest path between two points, and calculate a path's information.
 * 
 * Provided is a Network object that contains all the streets and intersections in the map
 * 
 * @author Vian Miranda
 * @author Anna Lu
 */
public class RUMaps {
    
    private Network rutgers;

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Constructor for the RUMaps class. Initializes the streets and intersections in the map.
     * For each block in every street, sets the block's length, traffic factor, and traffic value.
     * 
     * @param mapPanel The map panel to display the map
     * @param filename The name of the file containing the street information
     */
    public RUMaps(MapPanel mapPanel, String filename) {
        StdIn.setFile(filename);
        int numIntersections = StdIn.readInt();
        int numStreets = StdIn.readInt();
        StdIn.readLine();
        rutgers = new Network(numIntersections, mapPanel);
        ArrayList<Block> blocks = initializeBlocks(numStreets);
        initializeIntersections(blocks);

        for (Block block: rutgers.getAdjacencyList()) {
            Block ptr = block;
            while (ptr != null) {
                ptr.setLength(blockLength(ptr));
                ptr.setTrafficFactor(blockTrafficFactor(ptr));
                ptr.setTraffic(blockTraffic(ptr));
                ptr = ptr.getNext();
            }
        }
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Overloaded constructor for testing.
     * 
     * @param filename The name of the file containing the street information
     */
    public RUMaps(String filename) {
        this(null, filename);
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Overloaded constructor for testing.
     */
    public RUMaps() { 
        
    }

    /**
     * Initializes all blocks, given a number of streets.
     * the file was opened by the constructor - use StdIn to continue reading the file
     * @param numStreets the number of streets
     * @return an ArrayList of blocks
     */
    public ArrayList<Block> initializeBlocks(int numStreets) {
        ArrayList<Block> blocks = new ArrayList<>();
        
        for (int s = 0; s < numStreets; s++) {
            // read the street name
            String streetName = StdIn.readLine();
            // read how many blocks this street has
            int numBlocks = StdIn.readInt();
            StdIn.readLine(); // read end of line after the int
            
            // for each block, read its data and all of its points
            for (int b = 0; b < numBlocks; b++) {
                int blockNumber = StdIn.readInt();
                int numPoints   = StdIn.readInt();
                double roadSize = StdIn.readDouble();
                StdIn.readLine(); // read trailing newline
                
                // create block with size, name, and number
                Block block = new Block(roadSize, streetName, blockNumber);
                
                // read first point then call startPoint()
                int x = StdIn.readInt();
                int y = StdIn.readInt();
                block.startPoint(new Coordinate(x, y));
                StdIn.readLine(); // newline after the pair
                
                // read the remaining points, call nextPoint() on each
                for (int p = 1; p < numPoints; p++) {
                    x = StdIn.readInt();
                    y = StdIn.readInt();
                    block.nextPoint(new Coordinate(x, y));
                    StdIn.readLine();
                }
                
                // add block to our list
                blocks.add(block);
            }
        }
        
        return blocks;
    }

    /**
     * This method traverses through each block and finds
     * the block's start and end points to create intersections. 
     * 
     * It then adds intersections as vertices to the "rutgers" graph if
     * they are not already present, and adds UNDIRECTED edges to the adjacency
     * list.
     * 
     * Note that .addEdge(__) ONLY adds edges in one direction (a -> b). 
     */
    public void initializeIntersections(ArrayList<Block> blocks) {
        for (Block block : blocks) {
            // grab start and end coordinates
            List<Coordinate> pts = block.getCoordinatePoints();
            Coordinate startCoord = pts.get(0);
            Coordinate endCoord   = pts.get(pts.size() - 1);
    
            // find or create the start intersection
            int startIdx = rutgers.findIntersection(startCoord);
            Intersection startI;
            if (startIdx < 0) {
                startI = new Intersection(startCoord);
                rutgers.addIntersection(startI);
            } else {
                startI = rutgers.getIntersections()[startIdx];
            }
            block.setFirstEndpoint(startI);
    
            // find or create the end intersection
            int endIdx = rutgers.findIntersection(endCoord);
            Intersection endI;
            if (endIdx < 0) {
                endI = new Intersection(endCoord);
                rutgers.addIntersection(endI);
            } else {
                endI = rutgers.getIntersections()[endIdx];
            }
            block.setLastEndpoint(endI);
    
            // add the undirected edges where one copies going start to end and one going end to start
            // forward edge
            Block forward = block.copy();
            forward.setFirstEndpoint(startI);
            forward.setLastEndpoint(endI);
            rutgers.addEdge(rutgers.findIntersection(startCoord), forward);
    
            // reverse edge
            Block reverse = block.copy();
            reverse.setFirstEndpoint(endI);
            reverse.setLastEndpoint(startI);
            rutgers.addEdge(rutgers.findIntersection(endCoord), reverse);
        }
    }

    /**
     * Calculates the length of a block by summing the distances between consecutive points for all points in the block.
     * 
     * @param block The block whose length is being calculated
     * @return The total length of the block
     */
    public double blockLength(Block block) {
        double total = 0.0;
        List<Coordinate> pts = block.getCoordinatePoints();
        // sum the distance between each consecutive pair
        for (int i = 1; i < pts.size(); i++) {
            total += coordinateDistance(pts.get(i - 1), pts.get(i));
        }
        return total;
    }

    /**
     * Use a DFS to traverse through blocks, and find the order of intersections
     * traversed starting from a given intersection (as source).
     * 
     * Implement this method recursively, using a helper method.
     */
    public ArrayList<Intersection> reachableIntersections(Intersection source) {
        ArrayList<Intersection> result = new ArrayList<>();
        // track which intersections we've visited
        boolean[] visited = new boolean[rutgers.getIntersections().length];
        // find the starting intersection index
        int startIdx = rutgers.findIntersection(source.getCoordinate());
        // begin the DFS
        dfsReach(startIdx, visited, result);
        return result;
    }
    
    private void dfsReach(int idx, boolean[] visited, ArrayList<Intersection> result) {
        // stop if index is invalid or already visited
        if (idx < 0 || visited[idx]) return;
        visited[idx] = true;
        // add this intersection to the result list
        Intersection here = rutgers.getIntersections()[idx];
        result.add(here);
        // for each connected block, explore the neighbor
        for (Block edge = rutgers.adj(idx); edge != null; edge = edge.getNext()) {
            Intersection nbr = edge.getLastEndpoint();
            int nbrIdx = rutgers.findIntersection(nbr.getCoordinate());
            dfsReach(nbrIdx, visited, result);
        }
    }
     

    /**
     * Finds and returns the path with the least number of intersections (nodes) from the start to the end intersection.
     * 
     * - If no path exists, return an empty ArrayList.
     * - This graph is large. Find a way to eliminate searching through intersections that have already been visited.
     * 
     * @param start The starting intersection
     * @param end The destination intersection
     * @return The path with the least number of turns, or an empty ArrayList if no path exists
     */
    public ArrayList<Intersection> minimizeIntersections(Intersection start, Intersection end) {
        // prep the result list
        ArrayList<Intersection> path = new ArrayList<>();
        if (start == null || end == null) 
        return path;  // return if start or end is missing
    
        // how many intersections we have
        int n = rutgers.getNextIndex();
        boolean[] visited = new boolean[n]; // track the visited nodes
        int[] edgeTo = new int[n]; // store where we came from
        Arrays.fill(edgeTo, -1);
    
        // find the indices for start and end
        int startIdx = rutgers.findIntersection(start.getCoordinate());
        int endIdx   = rutgers.findIntersection(end.getCoordinate());
        if (startIdx < 0 || endIdx < 0) return path;  // invalid start or end
    
        // set up a FIFO queue for BFS
        Queue<Integer> q = new Queue<>();
        visited[startIdx] = true;
        q.enqueue(startIdx);
    
        // run BFS until we either exhaust or reach the end
        while (!q.isEmpty()) {
            int v = q.dequeue();
            if (v == endIdx) break; // stop early if we found the target
    
            // look at each edge leaving v
            for (Block blk = rutgers.adj(v); blk != null; blk = blk.getNext()) {
                // find the neighboring intersections
                Intersection nbrI = blk.other(rutgers.getIntersections()[v]);
                int w = rutgers.findIntersection(nbrI.getCoordinate());
                if (w < 0 || visited[w]) continue;  // skip invalid or already visited
    
                // mark and remember how we got here
                visited[w] = true;
                edgeTo[w]  = v;
                q.enqueue(w);  // add to queue for further exploration
            }
        }
    
        // if we never reached the end, return empty
        if (!visited[endIdx]) return path;
    
        // build the path by walking back from end to start
        for (int x = endIdx; x != -1; x = edgeTo[x]) {
            path.add(rutgers.getIntersections()[x]);
        }
        Collections.reverse(path); // reverse so it goes start to end
    
        return path;
    }

    /**
     * Finds the path with the least traffic from the start to the end intersection using a variant of Dijkstra's algorithm.
     * The traffic is calculated as the sum of traffic of the blocks along the path.
     * 
     * What is this variant of Dijkstra?
     * - We are using traffic as a cost - we extract the lowest cost intersection from the fringe.
     * - Once we add the target to the done set, we're done. 
     * 
     * @param start The starting intersection
     * @param end The destination intersection
     * @return The path with the least traffic, or an empty ArrayList if no path exists
     */
    public ArrayList<Intersection> fastestPath(Intersection start, Intersection end) {
        ArrayList<Intersection> path = new ArrayList<>();
        if (start == null || end == null) return path;
    
        int n = rutgers.getNextIndex();
        double[] dist = new double[n]; // best known traffic cost to each node
        int[] pred = new int[n]; // the predecessor of each node on best path
        boolean[] done = new boolean[n]; // which nodes are finalized
    
        // initialize distances and predecessors
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(pred, -1);
    
        // find start and end indices
        int startIdx = rutgers.findIntersection(start.getCoordinate());
        int endIdx   = rutgers.findIntersection(end.getCoordinate());
        if (startIdx < 0 || endIdx < 0) return path;
        dist[startIdx] = 0.0; // the cost to reach start from itself is zero
    
        // use an arraylist as the fringe
        ArrayList<Integer> fringe = new ArrayList<>();
        fringe.add(startIdx);
    
        // dijkstra's loop
        while (!fringe.isEmpty()) {
            // extract the fringe index u with smallest dist[u]
            double best = Double.POSITIVE_INFINITY;
            int u = -1;
            for (int idx : fringe) {
                if (dist[idx] < best) {
                    best = dist[idx];
                    u = idx;
                }
            }
    
            // remove u from fringe and mark it done
            fringe.remove(Integer.valueOf(u));
            if (u == endIdx) break; // reached the target
            done[u] = true;
    
            // relax all edges out of u
            for (Block blk = rutgers.adj(u); blk != null; blk = blk.getNext()) {
                Intersection curr = rutgers.getIntersections()[u];
                Intersection nbrI = blk.other(curr);
                int v = rutgers.findIntersection(nbrI.getCoordinate());
                if (v < 0 || done[v]) continue; // skip invalid or finalized
    
                double alt = dist[u] + blk.getTraffic();
                if (alt < dist[v]) {
                    dist[v] = alt; // found cheaper path to v
                    pred[v] = u; // recall we came through u
                    if (!fringe.contains(v)) {
                        fringe.add(v); // schedule v for searching
                    }
                }
            }
        }
    
        // if end was not reached, return empty
        if (startIdx != endIdx && pred[endIdx] == -1) return path;
    
        // rebuild path by walking pred[] back from endIdx
        for (int x = endIdx; x != -1; x = pred[x]) {
            path.add(rutgers.getIntersections()[x]);
        }
        Collections.reverse(path); // reverse to get start to end order
        return path;
    }

    /**
     * Calculates the total length, average experienced traffic factor, and total traffic for a given path of blocks.
     * 
     * You're given a list of intersections (vertices); you'll need to find the edge in between each pair.
     * 
     * Compute the average experienced traffic factor by dividing total traffic by total length.
     *  
     * @param path The list of intersections representing the path
     * @return A double array containing the total length, average experienced traffic factor, and total traffic of the path (in that order)
     */
    public double[] pathInformation(ArrayList<Intersection> path) {
        double totalLength  = 0.0;
        double totalTraffic = 0.0;
    
        // go through each consecutive pair of intersections
        for (int i = 0; i < path.size() - 1; i++) {
            Intersection u = path.get(i);
            Intersection v = path.get(i + 1);
            int uIdx = rutgers.findIntersection(u.getCoordinate());
    
            // look through all blocks from u to find the one that reaches v
            for (Block blk = rutgers.adj(uIdx); blk != null; blk = blk.getNext()) {
                if ((blk.getFirstEndpoint().equals(u) && blk.getLastEndpoint().equals(v)) ||
                    (blk.getFirstEndpoint().equals(v) && blk.getLastEndpoint().equals(u))) {
                    totalLength  += blk.getLength(); // add this block’s length and traffic to our totals
                    totalTraffic += blk.getTraffic();
                    break;  // move on to the next pair
                }
            }
        }
        
        // compute the average traffic factor, so traffic per unit length
        double avgTrafficFactor = (totalLength > 0)
            ? totalTraffic / totalLength
            : 0.0;
        
            // return the total length, avg traffic factor, and total traffic
        return new double[] { totalLength, avgTrafficFactor, totalTraffic };
    }

    /**
     * Calculates the Euclidean distance between two coordinates.
     * PROVIDED - do not modify
     * 
     * @param a The first coordinate
     * @param b The second coordinate
     * @return The Euclidean distance between the two coordinates
     */
    private double coordinateDistance(Coordinate a, Coordinate b) {
        // PROVIDED METHOD

        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Calculates and returns a randomized traffic factor for the block based on a Gaussian distribution.
     * 
     * This method generates a random traffic factor to simulate varying traffic conditions for each block:
     * - < 1 for good (faster) conditions
     * - = 1 for normal conditions
     * - > 1 for bad (slower) conditions
     * 
     * The traffic factor is generated with a Gaussian distribution centered at 1, with a standard deviation of 0.2.
     * 
     * Constraints:
     * - The traffic factor is capped between a minimum of 0.5 and a maximum of 1.5 to avoid extreme values.
     * 
     * @param block The block for which the traffic factor is calculated
     * @return A randomized traffic factor for the block
     */
    public double blockTrafficFactor(Block block) {
        double rand = StdRandom.gaussian(1, 0.2);
        rand = Math.max(rand, 0.5);
        rand = Math.min(rand, 1.5);
        return rand;
    }

    /**
     * Calculates the traffic on a block by the product of its length and its traffic factor.
     * 
     * @param block The block for which traffic is being calculated
     * @return The calculated traffic value on the block
     */
    public double blockTraffic(Block block) {
        // PROVIDED METHOD
        
        return block.getTrafficFactor() * block.getLength();
    }

    public Network getRutgers() {
        return rutgers;
    }

}
