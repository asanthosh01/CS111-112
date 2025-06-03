/*
 * Write your program inside the main method to find the order
 * which the bus the student needs to take will arrive
 * according to the assignemnt description. 
 *
 * To compile:
 *        javac BusStop.java
 * 
 * DO NOT change the class name
 * DO NOT use System.exit()
 * DO NOT change add import statements
 * DO NOT add project statement
 * 
 */
public class BusStop {

    public static void main(String[] args) {

        char[] busStops  = new char[args.length - 1]; // create an array to store the bus stops except the last one

        for(int i = 0; i < args.length - 1; i++) { 
            busStops[i] = args[i].charAt(0);
        }

        char targetStop = args[args.length -1].charAt(0); // last argument is the target bus stop we are looking for 

        for(int i = 0; i < busStops.length; i++) { // find the index of the target bus stop
            if (busStops[i] == targetStop) {
                System.out.println(i + 1);
                return; // exit after finding the first match
            }
        }
        System.out.println(-1); // if target bus stop is not found then print "-1"
    }
}