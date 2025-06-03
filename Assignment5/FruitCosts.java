/**
 * FruitCosts 
 * 
 * 1. This program reads in a list of fruits and their costs from a file (args[0]).
 * 2. Then finds the two lowest cost fruits and prints their names and costs
 * along with the total cost.
 * 
 * @author Srimathi Vadivel
 * @author Sarah Benedicto
 */
public class FruitCosts {
    /**
     * Main function to execute the program
     * 
     * @param args command-line arguments, where args[0] is the file name to read from
     */
    public static void main(String[] args) {

	// Do not remove this line, it opens the file for reading.
	StdIn.setFile(args[0]);
	// StdIn.readDouble, StdIn.readString() to read from the file
    
    // read the number of fruits
    int numberOfFruits = StdIn.readInt();
    
    // initialize the arrays for fruit names and their costs
    String[] fruitNames = new String[numberOfFruits];
    double[] fruitCosts = new double[numberOfFruits];
    
    // read the fruit data
    for (int i = 0; i < numberOfFruits; i++) {
        fruitNames[i] = StdIn.readString();
        fruitCosts[i] = StdIn.readDouble();
    }
        // initialize the indices for the two lowest costs
        int lowestIndex = 0;
        int secondLowestIndex = 1;
    
        // compare the initial elements
        if (fruitCosts[secondLowestIndex] < fruitCosts[lowestIndex]) {
            int temp = lowestIndex;
            lowestIndex = secondLowestIndex;
            secondLowestIndex = temp;
        }
    
        // use a for loop to find the two lowest costs 
        for (int i = 2; i < numberOfFruits; i++) {
            if (fruitCosts[i] < fruitCosts[lowestIndex]) {
                secondLowestIndex = lowestIndex;
                lowestIndex = i;
                
            } else if (fruitCosts[i] < fruitCosts[secondLowestIndex]) {
                    secondLowestIndex = i;
                }
            }
    
        // calculate total cost of two cheapest fruits
        double totalCost = fruitCosts[lowestIndex] + fruitCosts[secondLowestIndex];

        // output the result
        StdOut.printf("%s %.2f\n", fruitNames[lowestIndex], fruitCosts[lowestIndex]);
        StdOut.printf("%s %.2f\n", fruitNames[secondLowestIndex], fruitCosts[secondLowestIndex]);
        StdOut.printf("Total %.2f\n", totalCost);
    }
}