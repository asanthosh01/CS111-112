package graph;

/**
 * City node, simple linked list node containing a string
 * @author Colin Sullivan
 */
public class City {
    
    private City next; // Next linked list node
    private String city; // Name of this City

    /**
     * Constructor which initializes city
     * @param city
     */
    public City(String city) {
        if (city == null) {
            throw new IllegalArgumentException("City name cannot be null");
        }
        this.city = city;
    }
    
    public void setNext(City next) {
        this.next = next;
    }
    
    public City getNext() {
        return next;
    }

    public String getCity() {
        return city;
    }
}
