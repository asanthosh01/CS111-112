package investigation;

import java.util.ArrayList; 

/*  
 * This class represents a cyber crime investigation.  It contains a directory of hackers, which is a resizing
 * hash table. The hash table is an array of HNode objects, which are linked lists of Hacker objects.  
 * 
 * The class contains methods to add a hacker to the directory, remove a hacker from the directory.
 * You will implement these methods, to create and use the HashTable, as well as analyze the data in the directory.
 * 
 * @author Colin Sullivan
 */
public class CyberCrimeInvestigation {
       
    private HNode[] hackerDirectory;
    private int numHackers = 0; 

    public CyberCrimeInvestigation() {
        hackerDirectory = new HNode[10];
    }

    /**
     * Initializes the hacker directory from a file input.
     * @param inputFile
     */
    public void initializeTable(String inputFile) { 
        // DO NOT EDIT
        StdIn.setFile(inputFile);  
        while(!StdIn.isEmpty()){
            addHacker(readSingleHacker());
        }
    }

    /**
     * Reads a single hackers data from the already set file,
     * Then returns a Hacker object with the data, including 
     * the incident data.
     * 
     * StdIn.setFile() has already been called for you.
     * 
     * @param inputFile The name of the file to read hacker data from.
     */
     public Hacker readSingleHacker(){ 
        // read the seven lines in the order:
        // 1. hacker name 
        // 2. IP address hash 
        // 3. location
        // 4. OS
        // 5. web server
        // 6. date
        // 7. url hash
        String name = StdIn.readLine();
        String ipHash = StdIn.readLine();
        String location = StdIn.readLine();
        String os = StdIn.readLine();
        String webServer = StdIn.readLine();
        String date = StdIn.readLine();
        String urlHash = StdIn.readLine();
        
        // create the incident
        Incident incident = new Incident(os, webServer, date, location, ipHash, urlHash);
        
        // create the hacker object with the given name and add the incident
        Hacker hacker = new Hacker(name);
        hacker.addIncident(incident);
        
        // return the created hacker
        return hacker;
    }

    /**
     * Adds a hacker to the directory.  If the hacker already exists in the directory,
     * instead adds the given Hacker's incidents to the existing Hacker's incidents.
     * 
     * After a new insertion (NOT if a hacker already exists), checks if the number of 
     * hackers in the table is >= table length divided by 2. If so, calls resize()
     * 
     * @param toAdd
     */
    public void addHacker(Hacker toAdd) {
        // calculate the index in the hash table
        int index = toAdd.hashCode() % hackerDirectory.length;
        
        // if the bucket is empty, then insert the hacker as the head of the list
        if (hackerDirectory[index] == null) {
            hackerDirectory[index] = new HNode(toAdd);
            numHackers++;
            
            // check the load factor and if number of hackers reaches table length/2, then resize
            if (numHackers >= hackerDirectory.length / 2) {
                resize();
            }
            return;
        }
        
        // otherwise we traverse the linked list at this bucket to look for a duplicate
        HNode current = hackerDirectory[index];
        while (current != null) {
            // if a hacker with the same name exists, then merge the incident lists
            if (current.getHacker().getName().equals(toAdd.getName())) {
                current.getHacker().getIncidents().addAll(toAdd.getIncidents());
                return;  // dont increment numHackers because it's not a new insertion
            }
            // keep the last node to add a new node if no duplicate is found
            if (current.getNext() == null) {
                break;
            }
            current = current.getNext();
        }
        
        // if no duplicate found, then append the new hacker at the end of the list
        current.setNext(new HNode(toAdd));
        numHackers++;
        
        // check load factor after inserting a new hacker
        if (numHackers >= hackerDirectory.length / 2) {
            resize();
        }
    }

    /**
     * Resizes the hacker directory to double its current size.  Rehashes all hackers
     * into the new doubled directory.
     */
    private void resize() {
        // save a reference to the old directory
        HNode[] oldDirectory = hackerDirectory;
        
        // create a new directory with double the size of the old one
        hackerDirectory = new HNode[oldDirectory.length * 2];
        
        // reset the number of hackers so they will be added back to addHacker
        numHackers = 0;
        
        // traverse the old directory
        for (int i = 0; i < oldDirectory.length; i++) {
            HNode current = oldDirectory[i];
            while (current != null) {
                // reinsert the hacker into the new directory
                addHacker(current.getHacker());
                
                // move to the next node in the current linked list
                current = current.getNext();
            }
        }
    }

    /**
     * Searches the hacker directory for a hacker with the given name.
     * Returns null if the Hacker is not found
     * 
     * @param toSearch
     * @return The hacker object if found, null otherwise.
     */
    public Hacker search(String toSearch) {
        // compute the bucket index
        int index = Math.abs(toSearch.hashCode()) % hackerDirectory.length;
        
        // walk the chain at that bucket
        HNode current = hackerDirectory[index];
        while (current != null) {
            Hacker h = current.getHacker();
            if (h.getName().equals(toSearch)) {
                return h; // return when u find it
            }
            current = current.getNext();
        }
        
        // if not found
        return null;
    }

    /**
     * Removes a hacker from the directory.  Returns the removed hacker object.
     * If the hacker is not found, returns null.
     * 
     * @param toRemove
     * @return The removed hacker object, or null if not found.
     */
    public Hacker remove(String toRemove) {
        // compute the bucket index
        int index = Math.abs(toRemove.hashCode()) % hackerDirectory.length;
        
        HNode current = hackerDirectory[index];
        HNode prev = null;
        
        // traverse the linked list in this bucket
        while (current != null) {
            Hacker h = current.getHacker();
            if (h.getName().equals(toRemove)) {
                // found the node to remove
                if (prev == null) {
                    // remove head of the list
                    hackerDirectory[index] = current.getNext();
                } else {
                    // bypass current node
                    prev.setNext(current.getNext());
                }
                numHackers--;
                return h;
            }
            prev = current;
            current = current.getNext();
        }
        
        // if hacker not found
        return null;
    }

    /**
     * Merges two hackers into one based on number of incidents.
     * 
     * @param hacker1 One hacker
     * @param hacker2 Another hacker to attempt merging with
     * @return True if the merge was successful, false otherwise.
     */
    public boolean mergeHackers(String hacker1, String hacker2) {
        // find both hacker objects
        Hacker h1 = search(hacker1);
        Hacker h2 = search(hacker2);
        if (h1 == null || h2 == null) {
            return false;  // one or both not found
        }
    
        // decide which one is target (more incidents) and which is source (fewer)
        Hacker target, source;
        int n1 = h1.numIncidents();
        int n2 = h2.numIncidents();
        if (n1 > n2) {
            // h1 has more incidents
            target = h1;
            source = h2;
        } else if (n2 > n1) {
            // h2 has more incidents
            target = h2;
            source = h1;
        } else {
            // equal counts so merge hacker2 into hacker1
            target = h1;
            source = h2;
        }
    
        // add all incidents from source into target
        target.getIncidents().addAll(source.getIncidents());
        // add the source's name as an alias on target
        target.addAlias(source.getName());
        // remove the source hacker from the table
        remove(source.getName());
        return true;
    }

    /**
     * Gets the top n most wanted Hackers from the directory, and
     * returns them in an arraylist. 
     * 
     * You should use the provided MaxPQ class to do this. You can
     * add all hackers, then delMax() n times, to get the top n hackers.
     * 
     * @param n
     * @return Arraylist containing top n hackers
     */
    public ArrayList<Hacker> getNMostWanted(int n) {
        // create a max heap priority queue for hacker objects
        MaxPQ<Hacker> pq = new MaxPQ<>();
        
        // insert every hacker in the directory into the max heap
        for (HNode bucketHead : hackerDirectory) {
            HNode current = bucketHead;
            while (current != null) {
                pq.insert(current.getHacker());
                current = current.getNext();
            }
        }
        
        // get the top n hackers
        ArrayList<Hacker> topHackers = new ArrayList<>();
        for (int i = 0; i < n && !pq.isEmpty(); i++) {
            topHackers.add(pq.delMax());
        }
        
        return topHackers;
    }

    /**
     * Gets all hackers that have been involved in incidents at the given location.
     * 
     * You should check all hackers, and ALL of each hackers incidents.
     * You should not add a single hacker more than once.
     * 
     * @param location
     * @return Arraylist containing all hackers who have been involved in incidents at the given location.
     */
    public ArrayList<Hacker> getHackersByLocation(String location) {
        ArrayList<Hacker> result = new ArrayList<>();
        
        // iterate over every bucket in the hash table
        for (HNode bucketHead : hackerDirectory) {
            HNode current = bucketHead;
            while (current != null) {
                Hacker h = current.getHacker();
                // check each incident for a matching location
                for (Incident inc : h.getIncidents()) {
                    if (inc.getLocation().equals(location)) {
                        result.add(h);
                        break;  // stop checking this hacker’s incidents and move to next hacker
                    }
                }
                current = current.getNext();
            }
        }
        
        return result;
    }

    /**
     * PROVIDED--DO NOT MODIFY!
     * Outputs the entire hacker directory to the terminal. 
     */
     public void printHackerDirectory() { 
        System.out.println(toString());
    } 

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.hackerDirectory.length; i++) {
            HNode headHackerNode = hackerDirectory[i];
            while (headHackerNode != null) {
                if (headHackerNode.getHacker() != null) {
                    sb.append(headHackerNode.getHacker().toString()).append("\n");
                    ArrayList<Incident> incidents = headHackerNode.getHacker().getIncidents();
                    for (Incident incident : incidents) {
                        sb.append("\t" +incident.toString()).append("\n");
                    }
                }
                headHackerNode = headHackerNode.getNext();
            } 
        }
        return sb.toString();
    }

    public HNode[] getHackerDirectory() {
        return hackerDirectory;
    }
}
