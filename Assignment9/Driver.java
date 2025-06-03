import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        // Scanner to read input from user
        Scanner scanner = new Scanner(System.in);

        // Prompt for the track file
        System.out.println("Enter the name of the track file:");
        String trackFile = scanner.nextLine();

        // Read the coordinates from the file
        int[] trackPoints = RURacing.readTrackFile(trackFile);

        // Create the raceway
        char[][] raceway = RURacing.createRaceway(trackPoints);

        // Print the created raceway
        System.out.println("Raceway Map:");
        RURacing.printMap(raceway);

        // Test Racer 1
        System.out.println("\nTesting Racer 1:");
        RURacing.racer1(raceway);

        // Reset the raceway for Racer 2
        raceway = RURacing.createRaceway(trackPoints);

        // Test Racer 2
        System.out.println("\nTesting Racer 2:");
        RURacing.racer2(raceway);

        // Reset the raceway for Racer 3
        raceway = RURacing.createRaceway(trackPoints);

        // Test Racer 3
        System.out.println("\nTesting Racer 3:");
        RURacing.racer3(raceway);

        // Reset the raceway for Racer 4
        raceway = RURacing.createRaceway(trackPoints);

        // Test Racer 4
        System.out.println("\nTesting Racer 4:");
        RURacing.racer4(raceway);

        scanner.close();
    }
}


