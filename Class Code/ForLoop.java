public class ForLoop {
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);

        int sum = 0;

        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        System.out.println("Sum of 1 up to and including " + n + " is " + sum);
    }
}
