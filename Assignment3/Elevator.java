
/*************************************************************************
 *  Compilation:  javac Elevator.java
 *  Execution:    java Elevator 'number of floors' 'floor requests' 'number of restricted floors' 'optional passcode'
 *
 *  @author Pooja Kedia
 *  @author Vidushi Jindal
 *
 *************************************************************************/
public class Elevator {

    public static void main(String[] args) {

        int totalFloors = Integer.parseInt(args[0]);
        int requestedFloors = Integer.parseInt(args[1]);
        int restrictedFloors = Integer.parseInt(args[2]);
        int passcode = 0;

        if (args.length == 4) {
            passcode = Integer.parseInt(args[3]);
        }

        int elevator1 = 1;
        int elevator2 = 1;

        while (requestedFloors > 0) {
            int requestedFloor = requestedFloors % 10;
            requestedFloors /= 10;
            
            int distance1 = Math.abs(elevator1 - requestedFloor);
            int distance2 = Math.abs(elevator2 - requestedFloor);

            if (distance1 <= distance2) {
                elevator1 = requestedFloor;
                System.out.println("1 " + requestedFloor);
            } else {
                elevator2 = requestedFloor;
                System.out.println("2 " + requestedFloor);
            }

            if (requestedFloor > (totalFloors - restrictedFloors)) {
                if (passcode != 0 && (passcode % totalFloors == requestedFloor)) {
                    System.out.println("Granted");
                    } else if (passcode % totalFloors == 0 && requestedFloor == totalFloors) {
                        System.out.println("Granted");
                    } else {
                        System.out.println("Denied");
                }
            }
        }
    }
}