package test;

import static org.junit.Assert.*;
import org.junit.*;
import graph.FlightPathGraph;

public class FlightPathGraphTest {
    
    @Test
    public void testConstructor() {
        String[] testCities = {"0","1","2","3","4","5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);
        assertEquals(graph.flightPaths.length, testCities.length);
        for (int i = 0; i < graph.flightPaths.length; i++) {
            assertEquals(graph.flightPaths[i].getCity(), testCities[i]);
            assertNull(graph.flightPaths[i].getNext());
        }
    } 

    @Test
    public void testAddEdge() {
        String[] testCities = {"0","1","2","3","4","5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);
        
        fail("You haven't written this test case yet!");
    } 

    @Test
    public void testRemoveEdge() {
        String[] testCities = {"0","1","2","3","4","5"};
        FlightPathGraph graph = new FlightPathGraph(testCities);
        
        fail("You haven't written this test case yet!");
    } 
}
