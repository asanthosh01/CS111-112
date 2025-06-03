
/**
 * Compilation: javac EgyptianPyramid.java
 * Execution:   java EgyptianPyramid 'size of grid' 'number of initial blocks'
 * 
 * @author Ayla Muminovic
 * @author Kushi Sharma
 * 
 * DO NOT change the class name
 * DO NOT use System.exit()
 * DO NOT change add import statements
 * DO NOT add project statement
 */
public class EgyptianPyramid {

    public static void main(String[] args) {

        int pyramidSize = Integer.parseInt(args[0]);
        int remainingBricks = Integer.parseInt(args[1]);

        if (pyramidSize == 0) {
            System.out.println("0 Bricks Remaining");
            return;
        }
        
        String[][] pyramidGrid = new String[pyramidSize][pyramidSize];
        int currentLayer = 0;

        if (pyramidSize > 0) {
            if (pyramidSize != 1) {
                for (int row = 0; row < pyramidGrid.length; row++) { // fill out the entire grid with filler "="
                    for (int col = 0; col < pyramidGrid[0].length; col++) {
                        pyramidGrid[row][col] = "=";
                    }
                }
                for (int row = pyramidGrid.length - 1; row >= 0; row--) { // start loop to fill up bricks with "X" from the bottom up
                    int startCol = currentLayer;
                    int endCol = pyramidGrid.length - currentLayer - 1;
                    for (int col = startCol; col <= endCol && remainingBricks > 0; col++) {
                        pyramidGrid[row][col] = "X";
                        remainingBricks--;
                    }
                    currentLayer++;
                }
            } else {
                // special case to handle 1x1 pyramid grid
                if (remainingBricks > 0) {
                    pyramidGrid[0][0] = "X";
                    remainingBricks--;
                } else {
                    pyramidGrid[0][0] = "=";
                }
            }
            for (int row = 0; row < pyramidGrid.length; row++) { // print the pyramid grid
                for (int col = 0; col < pyramidGrid[row].length; col++) {
                    System.out.print(pyramidGrid[row][col]);
                }
                System.out.println();
            }
            System.out.println(remainingBricks + " Bricks Remaining"); // print how many bricks are remaining
        }
    }
}