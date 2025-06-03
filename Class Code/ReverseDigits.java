public class ReverseDigits {
    public static void main(String[] args) {

        int number = Integer.parseInt(args[0]);
        System.out.println("Written with while");
        while (number > 0) {
            int digit = number % 10;
            System.out.println(digit);
            number /= 10; //same as number = number /10;
            
        }
        System.out.println("Written with for");
        for ( ; number > 0; number /= 10) {
            int digits = number % 10;
            System.out.println(digits);
        }
        
    }
}
