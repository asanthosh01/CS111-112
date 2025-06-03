public class Score {
    public static void main(String[] args) {
        
        int score = Integer.parseInt(args[0]);

        if (score < 0 || score > 100) {
            System.out.println("Error");
        } else
            if (score >= 90) {
                System.out.println("Excellent");
            } else
                if (score <= 89 && score >= 70) {
                    System.out.println("Pass");
                } else 
                    System.out.println("Retake the course");    
    }
}
