public class Formulas {


public static double triangleArea(double base, double height) {
    return (base * height) / 2;
}

public static double celsiusToFahrenheit(double Celsius) {
    return(Celsius * (9.0/5.0) + 32);
}

public static int speed(int distance, int time) {
    return(distance/time);
}
public static double diameter(double radius) {
    return(2 * radius);
}
public static void main(String[]args) {
    System.out.println(celsiusToFahrenheit(50));

}
}