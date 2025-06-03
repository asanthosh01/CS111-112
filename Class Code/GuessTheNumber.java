public class GuessTheNumber {
    public static void main(String[] args) {
        // Hardcoded number to guess and user guesses for simplicity
        int numberToGuess = 42;
        int[] guesses = {10, 50, 35, 42}; // Replace with guesses of your choice

        int numberOfTries = 0;
        boolean win = false;

        // Simulate guesses
        for (int guess : guesses) {
            numberOfTries++;

            if (guess == numberToGuess) {
                win = true;
                System.out.println("Congratulations! You guessed the right number in " + numberOfTries + " tries.");
                break;
            } else if (guess < numberToGuess) {
                System.out.println("Too low!");
            } else {
                System.out.println("Too high!");
            }
        }

        if (!win) {
            System.out.println("Sorry, you didn't guess the number. It was: " + numberToGuess);
        }
    }
}