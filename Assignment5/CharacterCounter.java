
/**
 * CharacterCounter
 * 
 * 1. This program reads in characters from a file (args[0]) and keeps
 *    track (using an array of size 128) of number of times each character appears in a file.
 * 2. Then, the program writes to the output file (args[1]) from index 32 to 126 inclusive:
 *    [character], [ascii value], [number of times the character appears in the file], [frequency]
 * 
 * @author Mary Buist
 * @author Anna Lu
 */

public class CharacterCounter {

    public static void main(String[] args) {
        
        // set up input and output files
        StdIn.setFile(args[0]);
		
        StdOut.setFile(args[1]);
        
        // array to store counts for each ASCII character (128 possible values)
        int[] charCounts = new int[128];
        int totalChars = 0;

        // read characters from the input file and count occurrences
        while (StdIn.hasNextChar()) {
            char c = StdIn.readChar();
            if (c < 128) {  // only count if within ASCII range
                charCounts[c]++;
                totalChars++;
            }
        }
        
        // output results for ASCII values from 32 to 126, including those with zero occurrences
        for (int i = 32; i <= 126; i++) {
            int occurrences = charCounts[i];
            char character = (char) i;
            double frequency = (occurrences / (double) totalChars) * 100;
            
            // display in format [character],[ascii value],[occurrences],[frequency]
            StdOut.println(character + "," + i + "," + occurrences + "," + String.format("%.2f", frequency));
        }
    }
}