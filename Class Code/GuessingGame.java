public class GuessingGame {
    public static void main(String[] args) {
             
            int random = (int)(Math.random()*100) +1;

            int guess = StdIn.readInt();

            while (guess != random) {
                if (guess > random) {
                    StdOut.println("Too Low");
                } else (guess < random)
                    StdOut.println("Too High");
                }
                StdOut.println("Guess Again");
                guess = StdIn.readInt();

            }
            StdOut.println("You got it");
             
     }
}