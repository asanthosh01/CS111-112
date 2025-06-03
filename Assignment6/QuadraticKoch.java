/*************************************************************************
 * Compilation: javac QuadraticKoch.java
 * Execution: java QuadraticKoch n
 *
 * @author Jeremy Hui
 *
 *************************************************************************/
public class QuadraticKoch {

    /**
     * Gets the set of coordinates to draw one segment of the Quadratic Koch Curve.
     * Returns the coordinates in a 2D array of doubles in the following format:
     * {array of x-coordinates,
     * array of y-coordinates}
     * 
     * @param x0 the x-coordinate of one endpoint
     * @param y0 the y-coordinate of one endpoint
     * @param x5 the x-coordinate of the other endpoint
     * @param y5 the y-coordinate of the other endpoint
     * @return the set of coordinates to draw one segment of the Quadratic Koch Curve
     */
    public static double[][] getCoords(double x0, double y0, double x5, double y5) {
    
        //calculate the change in x and y to divide the segment into 3 equal parts
        double dx = (x5 - x0) / 3;
        double dy = (y5 - y0) / 3;

        //calculate the coordinates of the 5 points along the x-axis
        double x1 = x0 + dx;
        double x2 = x1 - dy;
        double x3 = x2 + dx;
        double x4 = x3 + dy;

        //calculate the coordinates of the 5 points along the y-axis
        double y1 = y0 + dy;
        double y2 = y1 + dx;
        double y3 = y2 + dy;    
        double y4 = y3 - dx;

        //return the coordinates in a 2D array of doubles (one for x's and one for y's)
        double[][] coords = {{x0, x1, x2, x3, x4, x5}, {y0, y1, y2, y3, y4, y5}};

        //return the coordinates
        return coords;
    }
    /**
     * Gets the set of coordinates from getCoords() to draw the snowflake,
     * and calls Koch on two adjacent array indices with n being one less.
     * The method draws a line between the two endpoints if n == 0.
     * 
     * @param x0 the x-coordinate of one endpoint
     * @param y0 the y-coordinate of one endpoint
     * @param x5 the x-coordinate of the other endpoint
     * @param y5 the y-coordinate of the other endpoint
     * @param n  The current order
     */
    public static void koch(double x0, double y0, double x5, double y5, int n) {
        //if n is 0, draw a line between the two endpoints (base case)
        if (n == 0) {
            //draw the line
            StdDraw.line(x0, y0, x5, y5);
            return;
        }

        //get the coordinates of the 5 points for current segment using getCoords()
        double[][] coords = getCoords(x0, y0, x5, y5);

        //recursively call koch for each segment to draw the koch curve
        koch(coords[0][0], coords[1][0], coords[0][1], coords[1][1], n - 1); //segement 1
        koch(coords[0][1], coords[1][1], coords[0][2], coords[1][2], n - 1); //segment 2
        koch(coords[0][2], coords[1][2], coords[0][3], coords[1][3], n - 1); //peak segment
        koch(coords[0][3], coords[1][3], coords[0][4], coords[1][4], n - 1); //segment 3
        koch(coords[0][4], coords[1][4], coords[0][5], coords[1][5], n - 1); //segment 4
    }

    /**
     * Takes an integer command-line argument n,
     * and draws a Quadratic Koch Curve of order n in a 1 x 1 canvas
     * with an initial square side length of 0.5.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        //parse the recursion order (n) from the command-line argument
        int n = Integer.parseInt(args[0]);

        //define the coordinates of the 4 points of the initial square
        double[][] coordPoints = {
            {0.25, 0.25, 0.25, 0.75}, //bottom left
            {0.25, 0.75, 0.75, 0.75}, //bottom right
            {0.75, 0.75, 0.75, 0.25}, //top right
            {0.75, 0.25, 0.25, 0.25} //top left
        };
        //loop through each side of the square and draw the koch curve
        for (int i = 0; i < coordPoints.length; i++) {
            koch(coordPoints[i][0], coordPoints[i][1], coordPoints[i][2], coordPoints[i][3], n);
        }
    }
}