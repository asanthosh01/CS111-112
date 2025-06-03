package graph; 

/*
 * CS112 Graph Lab
 * 
 * Implement the constructor, addEdge(), removeEdge() using an linked adjacency list
 * based representation of an directed graph
 * 
 * @author Colin Sullivan
 */
public class FlightPathGraph {
    // Adjacency list of cities. Each index corresponds to a City node which is the head of a linked list.
    // Each list is an edge list, meaning the first node has an edge to all the rest of the nodes in the list.
    public City[] flightPaths;

    // i.e. if flightPaths[0].getCity() is "New York", and flightPaths[0].next.getCity() is "London", it means New York
    // has a directed edge to London. If New York is also placed in Londons list, the edge is then undirected.

    /**
     * Constructor which initializes the adjacency list with the given verticies, with no edges
     *  
     * 1) Initiate the flightPaths array to the same size as the cities array
     * 2) Add a new City node to each index in flightPaths, with the corresponding
     *    city name from the same index in cities[]
     * @param cities array of city names to be added to the graph
     */
    public FlightPathGraph(String[] cities) {
        // allocate array
        flightPaths = new City[cities.length];
        // initialize each head node
        for (int i = 0; i < cities.length; i++) {
            flightPaths[i] = new City(cities[i]);
        }
    }

    /**
     * Adds an directed edge between the departure and arrival locations
     * 
     * Don't insert if an edge already exists
     * 
     * Add a new City node containing the arrival city to the END 
     * of the departure citys edgelist
     * @param departure the city to add an edge from
     * @param arrival the city to add an edge to
     */
    public void addEdge(String departure, String arrival) {
        // to avoid nulls or self loops
        if (departure == null || arrival == null || departure.equals(arrival)) {
            return;
        }
        // find index of departure
        int depIndex = -1;
        for (int i = 0; i < flightPaths.length; i++) {
            if (flightPaths[i].getCity().equals(departure)) {
                depIndex = i;
                break;
            }
        }
        if (depIndex == -1) return;  // departure not in graph

        // make sure arrival is a valid vertex
        boolean arrivalExists = false;
        for (City c : flightPaths) {
            if (c.getCity().equals(arrival)) {
                arrivalExists = true;
                break;
            }
        }
        if (!arrivalExists) return;

        // check if the edge already exists
        City head = flightPaths[depIndex];
        City curr = head.getNext();
        while (curr != null) {
            if (curr.getCity().equals(arrival)) {
                return;  // already there
            }
            curr = curr.getNext();
        }

        // append new edge to end of list
        City tail = head;
        while (tail.getNext() != null) {
            tail = tail.getNext();
        }
        tail.setNext(new City(arrival));
    }

    /**
     * Removes the directed edge between the departure and arrival cities
     * 
     * Remove the City node containing "arrival" from "departure"'s edge list
     * 
     * @param departure the city to remove an edge from
     * @param arrival the city to remove an edge to
     */
    public void removeEdge(String departure, String arrival) {
        if (departure == null || arrival == null) return;

        // find index of departure
        int depIndex = -1;
        for (int i = 0; i < flightPaths.length; i++) {
            if (flightPaths[i].getCity().equals(departure)) {
                depIndex = i;
                break;
            }
        }
        if (depIndex == -1) return;  // departure not in graph

        // traverse departure's edge list to remove arrival
        City head = flightPaths[depIndex];
        City prev = head;
        City curr = head.getNext();
        while (curr != null) {
            if (curr.getCity().equals(arrival)) {
                // unlink it
                prev.setNext(curr.getNext());
                return;
            }
            prev = curr;
            curr = curr.getNext();
        }
    }

    /*
     * Getter method for number of vertices
     * 
     * @return number of riders in line (lineLength)
     */
    public int getNumberOfVertices() {
        // DO NOT MODIFY
        return flightPaths.length;
    }

}
