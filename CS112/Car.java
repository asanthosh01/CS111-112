public class Car {

    // instance variable-characterisitics of object
    private int mileage; // how many miles are on the car
    private String make; // make of the car
    private String model; // model of the car
    private boolean isElectric; // true for electric; false otherwise
    private double gas; // 0 - 1, 0 - empty, 1 - full

    // constructor(s) - create new object in memory
 
    public Car(int mileage, String make, String model, boolean isElectric) {
        this.mileage = mileage; 
        this.make = make;
        this.model = model;
        this.isElectric = isElectric;
        gas = 1;
    }

    // methods / operations

    public int getMileage() {
        return mileage;
    
    }

    public void goForDrive(int miles) { // for every 1 mile, we use 0.05 of gas tank
        mileage += miles;
        gas -= miles * 0.05;
    }

    
}