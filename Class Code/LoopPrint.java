public class LoopPrint {
    public static void main(String[] args) {
        
        int n = Integer.parseInt(args[0]); // 1 operation

        int sum = 0; // 1 operation

        while ( n > 0 ) { // 1 operation * n conditional is true + 1 operation when condition is false
            sum = sum + n; // 1 operation
            n = n - 1; // 1 operation
        }
        System.out.println("Sum of 1 up to and including " + n + " is " + sum); // 1 operation
      

    }
    
}
