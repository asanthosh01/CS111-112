public class BetweenTwo {
    public static void main(String[] args) {
        int num1 = Integer.parseInt(args[0]);
        int num2 = Integer.parseInt(args[1]);
        int min = Math.min(num1, num2);
        int max = Math.max(num1, num2);

        for(int i = 0; i < args.length; i++) {
            int num = Integer.parseInt(args[i]);
            if ((num > min) && (num < max)) {
                System.out.print(num + " ");
            }
        }
        System.out.println();
    }
}
