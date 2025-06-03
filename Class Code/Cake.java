public class Cake {
    public static void main(String[] args) {
        
        int cakeSize = Integer.parseInt(args[0]);

        if ( cakeSize <= 0 ) {
            System.out.println("Error");
        } else {
            if ( cakeSize < 6 ) {
                System.out.println(cakeSize = 10);
            } else {
                if ( cakeSize >= 6 && cakeSize <= 12) {
                    System.out.println(cakeSize = 15);
                } else {
                    System.out.println(cakeSize = 20);
            }
        }
        
    }   
}
}
