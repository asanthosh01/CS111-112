public class RutgersGame {
    public static void main(String[] args) {
        
        int nbrFriends = args.length;
        int sum = 0;
        int totalRatings = nbrFriends;

        for(int i = 0; i < nbrFriends; i++) {
        int rating = Integer.parseInt(args[i]);
        if (rating < 1 || rating > 5 ) {
            totalRatings -= 1;
        } else {
            sum = sum + rating;
        }
        i = i + 1;
    }
        double avg = (sum * 1.0) / nbrFriends;
        System.out.println("The game's ratings average is " + avg);
    }
}