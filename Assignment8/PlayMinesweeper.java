/**
 * Play Minesweeper.
 * Methods to complete:
 * placeMines(), fillGrid(), openSquare(), placeFlag(), 
 * checkWinCondition(), chooseDifficulty(), playRandom()
 * 
 * @author Jessica de Brito
 * @author Jeremy Hui
 */

public class PlayMinesweeper {
    private Square[][] grid;
    private int flagCount;   // the number of mines in the grid
    private int totalMines;   // the number of mines in the grid

    /**
     * DO NOT UPDATE OR REMOVE THIS METHOD
     * Default constructor.
     * Initializes a 0x0 game grid with 0 flags.
     */
    public PlayMinesweeper() {
        this(new Square[0][0], 0);
    }

    /**
     * DO NOT UPDATE OR REMOVE THIS METHOD
     * Overloaded constructor: initalizes a Play Minesweeper object
     * based on a given 2D array of squares and the number of flags.
     * @param grid the 2D array of squares
     * @param totalMines the number of mines in the grid
     */
    public PlayMinesweeper(Square[][] grid, int totalMines) {
        StdRandom.setSeed(2024);
        this.grid = grid;
        this.flagCount = 0;
        this.totalMines = totalMines;
    }

    /**
     * DO NOT UPDATE OR REMOVE THIS METHOD
     * Returns the 2D array of squares.
     * @return the 2D array of squares
     */
    public Square[][] getGrid() {
        return grid;
    }

    /**
     * DO NOT UPDATE OR REMOVE THIS METHOD
     * REturns the flag count.
     * @return the flag count
     */
    public int getFlagCount() {
        return flagCount;
    }

    /**
     * DO NOT UPDATE OR REMOVE THIS METHOD
     * Returns the mines count.
     * @return the mines count
     */
    public int getMinesCount() {
        return totalMines;
    }

    /**
     * This method takes in an input file and sets the 2D array of squares and
     * flag count according to the specification of the file.
     * The first two integers m and n represent a 2D array of squares with
     * m rows and n columns.
     * Each following line represents a mine to be placed in the 2D array of
     * squares, with the first integer representing the row number, and the
     * second integer representing the column number.
     * 
     * A square with a mine will be represented with a -1 Square number,
     * otherwise the Square number will be left at its default value of 0.
     * All Squares will be left at its default state of being CLOSED.
     * 
     * If the square is a mine, increment totalMines.
     * 
     * @param inputFile the given input file
     * NOTE: All square objects in the 2D array of squares will be initialized.
     */
    public void placeMines(String inputFile) {
        StdIn.setFile(inputFile); // Opens the input file for reading. DO NOT REMOVE OR EDIT THIS LINE.
    
        // read the number of rows (m) and columns (n) for the grid from the input file
        int rows = StdIn.readInt();
        int cols = StdIn.readInt();
    
        // initialize the grid as a 2D array of square objects with dimensions m x n
        grid = new Square[rows][cols];
    
        // populate the grid with default square objects
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Square(); // create a new square at each position
            }
        }
    
        // read mine coordinates from the input file until all coordinates are processed
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt(); // read the row index of the mine
            int col = StdIn.readInt(); // read the column index of the mine
            grid[row][col].setSqNum(-1); // set sqNum to -1 to indicate a mine
            totalMines++; // increment the total mine count
        }
    }

    /**
     * This method sets the square number of each square in the 2D array
     * of squares array without a mine to equal the number of adjacent mines 
     * in all 8 directions (up, down, left, right, and diagonally).
     * Squares without no adjacent mines will be left at a square number
     * of 0.
     */
    public void fillGrid() {
        // arrays to represent the directions of the 8 neighboring cells (adjacent squares)
        int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};  // x offsets for neighbors
        int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};  // y offsets for neighbors
    
        // iterate through each cell in the grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].getSqNum() == -1) continue; // if the current square is a mine (sqNum == -1), skip it
    
                // initialize a variable to count adjacent mines
                int mineCount = 0;
    
                // check all 8 neighboring cells
                for (int d = 0; d < 8; d++) {
                    int ni = i + dx[d], nj = j + dy[d]; // calculate the row and column of the neighboring cell
    
                    // check if the neighboring cell is within the bounds of the grid and if it contains a mine (sqNum == -1)
                    if (ni >= 0 && ni < grid.length && nj >= 0 && nj < grid[0].length && grid[ni][nj].getSqNum() == -1) {
                        mineCount++;  // increment the mine count if a mine is found
                    }
                }
    
                // set the current square's number to the count of adjacent mines
                grid[i][j].setSqNum(mineCount);
            }
        }
    }

    /**
     * This method sets the selected square to be OPEN.
     * If the original state of the square is flagged,
     * remove the flag, don't forget to decrement flagCount.
     * 
     * Additionally, if the square number of the selected square
     * is 0, this method also opens all adjacent squares in all 8
     * directions (up, down, left, right, and diagonally) by
     * setting its square state to be OPEN.
     * 
     * This method returns true if the square selected did not have
     * a mine, and false otherwise.
     * 
     * @param row the given row
     * @param col the given column
     * @return true if the square selected did not have a mine, false otherwise
     */
    public boolean openSquare(int row, int col) {
        // check if the coordinates are out of bounds
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return true; // ignore out-of-bounds squares
        }
    
        Square square = grid[row][col];
    
        // skip already opened squares
        if (square.getSqState() == State.OPEN) {
            return true; // already opened, no action needed
        }
    
        // handle flagged squares
        if (square.getSqState() == State.FLAGGED) {
            flagCount--; // decrement the flag count
            square.setSqState(State.OPEN); // open the square (removes the flag)
        } else {
            square.setSqState(State.OPEN); // open the square
        }
    
        // check if the square contains a mine
        if (square.getSqNum() == -1) {
            return false; // if it's a mine, return false
        }
    
        // if the square is empty (number 0), recursively open adjacent squares
        if (square.getSqNum() == 0) {
            // arrays for 8-directional movement
            int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
            int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};
    
            for (int d = 0; d < 8; d++) {
                int newRow = row + dx[d];
                int newCol = col + dy[d];
    
                // recursively open adjacent squares
                openSquare(newRow, newCol);
            }
        }
    
        return true; // successfully opened a non-mine square
    }
    /**
     * This method sets the state of square at a given row and column
     * depending on its current state.
     *
     * If the square is FLAGGED, set the state of the square to be CLOSED
     * to represent removing a flag.
     * 
     * If the square is CLOSED, set the state of the square to be FLAGGED
     * to represent placing a flag.
     * 
     * The flag count must be incremented if a flag is placed
     * and decremented if a flag is removed.
     * 
     * @param row the given row
     * @param col the given column
     */
    public void placeFlag(int row, int col) {

        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) { // ensure the coordinates are within bounds
            return; // ignore invalid input
        }
    
        Square square = grid[row][col];
    
        // check if the square is currently flagged
        if (square.getSqState() == State.FLAGGED) {
            square.setSqState(State.CLOSED); // remove the flag
            flagCount--; // decrement the flag count
        } 
        // check if the square is closed
        else if (square.getSqState() == State.CLOSED) {
            square.setSqState(State.FLAGGED); // add a flag
            flagCount++; // increment the flag count
        }
    }

    /**
     * This method returns false if there still exists a square in the 
     * 2D array of squares that is not a mine that does not have an 
     * OPEN state, and true otherwise.
     * @return false if the win condition has not been met, true otherwise
     */
    public boolean checkWinCondition() {
        for (Square[] row : grid) { // traverse the grid
            for (Square square : row) {
                if (square.getSqNum() != -1 && square.getSqState() != State.OPEN) { // check if square is safe but unopened
                    return false; // if an unopened safe square is found, then the game is not won yet
                }
            }
        }
        return true; // all safe squares are opened
    }

    /**
     * This method sets the dimension of the 2D array of squares
     * and the total mines based on the given difficulty:
     *  - an 8x8 grid and a total mines of 10 on a "Beginner" level,
     *  - a 16x16 grid and a total mines of 40 on an "Intermediate" level,
     *  - and a 30x16 grid and a total mines of 99 on an "Advanced" level. 
     * 
     * @param level the given difficulty level
     * NOTE: All square objects in the 2D array of squares will be initialized.
     */
    public void chooseDifficulty(String level) {
        // set the grid size and total mines based on the difficulty level
        switch (level) {
            case "Beginner": // beginner level: 8x8 grid with 10 mines
                grid = new Square[8][8];
                totalMines = 10;
                break;
            case "Intermediate": // intermediate level: 16x16 grid with 40 mines
                grid = new Square[16][16];
                totalMines = 40;
                break;
            case "Advanced": // advanced level: 30x16 grid with 99 mines
                grid = new Square[30][16];
                totalMines = 99;
                break;
        }
    
        // initialize each square object in the grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new Square(); // create a new square for each cell
            }
        }
    }

    /**
     * This method places mines in the 2D array of squares according
     * to the specifications of the following algorithm:
     *
     * 1. Use StdRandom.uniform() twice, the first of which will
     * be a random row number and the second of which will be a 
     * random row column to place a mine.
     *
     * 2. If the randomly generated row and column numbers already
     * are the position of the first click (given by the input 
     * parameters row and col), generate a new row and column number
     * and repeat this step. Otherwise place a mine at the given
     * row and column.
     *
     * 3. Repeat this process until the grid contains n mines, 
     * where n = 10 for "Beginner" difficulty,
     * n = 40 for "Intermediate" difficulty, and n = 99 for
     * "Advanced" difficulty given by the level input parameter.
     *
     * @param level the given difficulty level
     * @param row the given row of the fist click
     * @param col the given column of the first click
     */
    public void playRandom(String level, int row, int col) {
        chooseDifficulty(level);

        int minesPlaced = 0; // keep track of how many mines have been placed

        while (minesPlaced < totalMines) { // continue placing mines until the designated # of mines is placed

            int randomRow = StdRandom.uniform(grid.length); // generate random row and colum coordinates using std random
            int randomCol = StdRandom.uniform(grid[0].length);

            // check if the generated coordinates are not the first click or if there's already a mine at that position
            if ((randomRow == row && randomCol == col) || grid[randomRow][randomCol].getSqNum() == -1) { // if either condition is true, then skip to the next iteration of the loop
                continue;
            }

            grid[randomRow][randomCol].setSqNum(-1); // place a mine at the selected random position 
            minesPlaced++;
        }

        fillGrid(); // fill the grid with values after all mines are placed
    }

    /**
     * DO NOT UPDATE OR REMOVE THIS METHOD
     * This method prints all square numbers in 
     * the 2D array of squares, regardless of its current state.
     * Called when testing individual methods in the TextDriver.
     */
    public void printGridNums() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++)
                StdOut.print(grid[i][j].getSqNum() + "\t");
            StdOut.println();
        }
    }

    /**
     * DO NOT UPDATE OR REMOVE THIS METHOD
     * This method sets all squares to the OPEN state.
     * Called when testing individual methods in the Driver.
     */
    public void openAllSquares() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].setSqState(State.OPEN);
            }
        }   
    }

    /**
     * DO NOT UPDATE OR REMOVE THIS METHOD
     * This method sets all squares to the CLOSED state.
     * Called when testing individual methods in the Driver.
     */
    public void closeAllSquares() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].setSqState(State.CLOSED);
            }
        }   
    }
}
