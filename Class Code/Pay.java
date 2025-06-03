public class Pay {
    public static void main(String[] args) {

        double hoursWorked = Double.parseDouble(args[0]);

        double payRate = Double.parseDouble(args[1]);

        double totalPay;

        if ( hoursWorked < 0 || payRate <= 0 ) {
            System.out.println("Error");
        } else {
            
            totalPay = hoursWorked * payRate;
            System.out.println(totalPay);
        }

    }
}
