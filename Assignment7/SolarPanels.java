/**
 * Class represents solar panels, street map, and
 * an array of parking lot projects.
 * 
 * @author Jessica De Brito
 * @author Kal Pandit
 */
public class SolarPanels {
    
    private Panel[][] panels;
    private String[][] streetMap;
    private ParkingLot[] lots;

    /**
     * Default constructor: initializes empty panels and objects.
     */
    public SolarPanels() {
        panels = null;
        streetMap = null;
        lots = null;
        StdRandom.setSeed(2023);
    }

    /**
     * Updates the instance variable streetMap to be an l x w
     * array of Strings. Reads each label from input file in parameters.
     * 
     * @param streetMapFile the input file to read from
     */
    public void setupStreetMap(String streetMapFile) {
        StdIn.setFile(streetMapFile); // open the input file

        int length = StdIn.readInt(); // read the length of the map
        int width = StdIn.readInt();  // read the width of the map
        streetMap = new String[length][width]; // initialize streetMap with given dimensions

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                streetMap[i][j] = StdIn.readString(); // fill each cell with data from the file
            }
        }
    }

    /**
     * Adds parking lot information to an array of parking lots.
     * Updates the instance variable lots to store these parking lots.
     * 
     * @param parkingLotFile the lot input file to read
     */
    public void setupParkingLots(String parkingLotFile) {
        StdIn.setFile(parkingLotFile); // open the input file
        int numberOfLots = StdIn.readInt(); // read the number of parking lots

        lots = new ParkingLot[numberOfLots]; // initialize lots array

        for (int i = 0; i < numberOfLots; i++) { // fill array with data for each parking lot

            String name = StdIn.readString(); // lot name
            int maxPanels = StdIn.readInt(); // maximum panels the lot can hold
            double budget = StdIn.readDouble(); // budget for the lot
            int energyCapacity = StdIn.readInt(); // energy capacity per panel
            double panelEfficiency = StdIn.readDouble(); // panel efficiency

            lots[i] = new ParkingLot(name, maxPanels, budget, energyCapacity, panelEfficiency); // create and add a lot
        }
    }   

    /**
     * Insert panels on each lot as much as space and budget allows.
     * Updates the instance variable panels to be a 2D array parallel to
     * streetMap, storing panels placed.
     * 
     * Panels have a 95% chance of working. Use StdRandom.uniform(); if
     * the resulting value is < 0.95 the panel works.
     * 
     * @param costPerPanel the fixed cost per panel, as a double
     */
    public void insertPanels(double costPerPanel) {
        panels = new Panel[streetMap.length][streetMap[0].length]; // initialize panels array

        for (ParkingLot lot : lots) { // iterate through each parking lot
            double currentBudget = lot.getBudget();
            int currentPanels = 0;

            for (int i = 0; i < streetMap.length; i++) { // check every cell in the street map
                for (int j = 0; j < streetMap[i].length; j++) {

                    if (streetMap[i][j].equals(lot.getLotName()) // if the cell matches the lot name, budget, and capacity constraints then place a panel
                            && currentBudget >= costPerPanel
                            && currentPanels < lot.getMaxPanels()) {

                        boolean works = StdRandom.uniform() < 0.95; // 95% chance the panel works
                        panels[i][j] = new Panel(lot.getPanelEfficiency(), lot.getEnergyCapacity(), works);
                        currentBudget -= costPerPanel; // deduct panel cost
                        currentPanels++; // increment installed count
                    }
                }
            }
        }
    }

    /**
     * Given a temperature and coefficient, update panels' actual efficiency
     * values. Panels are most optimal at 77 degrees F.
     * 
     * Panels perform worse in hotter environments and better in colder ones.
     * worse = efficiency loss, better = efficiency gain.
     * 
     * Coefficients are usually negative to represent energy loss.
     * 
     * @param temperature the current temperature, in degrees F
     * @param coefficient the coefficient to use
     */
    public void updateActualEfficiency(int temperature, double coefficient) {
        for (int i = 0; i < panels.length; i++) { //iterate through the panels array
            for (int j = 0; j < panels[i].length; j++) {
                
                if (panels[i][j] != null) { // check if there is a panel in the current cell
                    double ratedEfficiency = panels[i][j].getRatedEfficiency(); // get the rated efficiency of the current panel

                    // update the actual efficiency of the panel based on the temperature and coefficient
                    panels[i][j].setActualEfficiency(ratedEfficiency - (coefficient * (temperature - 77)));
                }
            }
        }
    }
    
    /**
     * For each WORKING panel, update the electricity generated for 4 hours 
     * of sunlight as follows:
     * 
     * (actual efficiency / 100) * 1500 * 4
     * 
     * RUN updateActualEfficiency BEFORE running this method.
     */
    public void updateElectricityGenerated() {
        for (int i = 0; i < panels.length; i++) { //iterate through the rows and columns of the panels array
            for (int j = 0; j < panels[i].length; j++) {
                if (panels[i][j] != null && panels[i][j].isWorking()) { // check if panel exists and is working   
                    
                    double totalElectricity = (panels[i][j].getActualEfficiency() / 100) * 1500 * 4; // calculate totaal electricity generated
    
                    panels[i][j].setElectricityGenerated((int)totalElectricity); // set the total electricity generatred for the current panel
                }
            }
        }
    }

    /**
     * Count the number of working panels in a parking lot.
     * 
     * @param parkingLot the parking lot name
     * @return the number of working panels
     */
    public int countWorkingPanels(String parkingLot) {
        int workingPanelsCount = 0; // initialize the count of working panels

        for (int i = 0; i < streetMap.length; i++) { // loop through the street map
            for (int j = 0; j < streetMap[i].length; j++) {

                // check if the current cell matches the parking lot, has a panel, and the panel is working
                if (streetMap[i][j].equals(parkingLot)
                        && panels[i][j] != null
                        && panels[i][j].isWorking()) {
                            workingPanelsCount++; // inrement the count for a working panel
                }
            }
        }
        return workingPanelsCount; // return the total number of working panels
    }

    /**
     * Find the broken panels in the map and repair them.
     * @return the count of working panels in total, after repair
     */
    public int updateWorkingPanels() {
        int workingPanelsCount = 0; // initialize the count of working panels

        for (int i = 0; i < panels.length; i++) { // loop through the street map
            for (int j = 0; j < panels[i].length; j++) {

                // check if the panel exists and is not working
                if (panels[i][j] != null && !panels[i][j].isWorking()) {
                    panels[i][j].setIsWorking(true); // repair the panel that is broken
                }
                    // check if the panel exists and is working
                if (panels[i][j] != null && panels[i][j].isWorking()) {
                    workingPanelsCount++; // increment the count of working panels
                }
            }
        }
        return workingPanelsCount; // return the total count of working panels after repairs
    }

    /**
     * Calculate Rutgers' savings on energy by using
     * these solar panels.
     * 
     * ASSUME:
     * - Multiply total electricity generated by 0.001 to convert to KwH.
     * - There are 365 days in a year.
     * 
     * RUN electricityGenerated before running this method.
     */
    public double calculateSavings() {
        double totalElectricity = 0; // initialize the total electricity generated (watt-hours)

        for (int i = 0; i < panels.length; i++) { // loop through the panels array
            for (int j = 0; j < panels[i].length; j++) {

                // add the electricity generated by each panel to the total only if the panel exists
                if (panels[i][j] != null) {
                    totalElectricity += panels[i][j].getElectricityGenerated();
                }
            }
        }
        
        double yearlyKWh = totalElectricity * 0.001 * 365; // convert to kWh and calculate yearly total
        double percentCovered = yearlyKWh / 4270000; // calculate percentage of rutgers' needs

        return percentCovered * 60000000; // calculate savings
    }

    /*
     * Getter and Setter methods
     */
    public Panel[][] getPanels() {
        // DO NOT TOUCH THIS METHOD
        return this.panels;
    }

    public void setPanels(Panel[][] panels) {
        // DO NOT TOUCH THIS METHOD
        this.panels = panels;
    }

    public String[][] getStreetMap() {
        // DO NOT TOUCH THIS METHOD
        return this.streetMap;
    }

    public void setStreetMap(String[][] streetMap) {
        // DO NOT TOUCH THIS METHOD
        this.streetMap = streetMap;
    }

    public ParkingLot[] getLots() {
        // DO NOT TOUCH THIS METHOD
        return this.lots;
    }

    public void setLots(ParkingLot[] lots) {
        // DO NOT TOUCH THIS METHOD
        this.lots = lots;
    }
}
