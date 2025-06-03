public class SimpleCalculator {
    public static void main(String[] args) {

        double[] numbers = {2.5, 4.0, 6.5};
        char operation = '+'; 

        double result = numbers[0];

        for (int i = 1; i < numbers.length; i++) {
            switch (operation) {
                case '+':
                    result += numbers[i];
                    break;
                case '-':
                    result -= numbers[i];
                    break;
                case '*':
                    result *= numbers[i];
                    break;
                case '/':
                    result /= numbers[i];
                    break;
                default:
                    System.out.println("Invalid operation!");
                    return;
            }
        }
        System.out.println("The result is: " + result);
    }
}