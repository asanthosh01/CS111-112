package graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Driver { 

    private City[] flightPaths;
    private FlightPathDisplay flightDisplay;

    private static String[] cities = { "New York", "Paris", "San Fran", "London", "Tokyo", "Sydney", "Dublin",
            "Berlin" };

    public Driver(FlightPathGraph graph) {
        flightPaths = graph.flightPaths;

        JFrame display = new JFrame("Flight Paths Graph Lab");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.PAGE_AXIS));

        flightDisplay = new FlightPathDisplay();

        JPanel controls = new JPanel();
        controls.setPreferredSize(new Dimension(550, 100));
        JComboBox<String> from = new JComboBox<>();
        JComboBox<String> to = new JComboBox<>();
        for (int i = 0; flightPaths != null && i < flightPaths.length; i++) {
            from.addItem(flightPaths[i].getCity());
            to.addItem(flightPaths[i].getCity());
        }
        JButton addEdge = new JButton("Add Edge");
        addEdge.addActionListener((ActionEvent e) -> {
            String f = ((String) from.getSelectedItem() != null) ? ((String) from.getSelectedItem()) : ("");
            String t = ((String) to.getSelectedItem() != null) ? ((String) to.getSelectedItem()) : ("");
            graph.addEdge(f, t);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    flightDisplay.repaint();
                }
            });
        });
        JButton removeEdge = new JButton("Remove Edge");
        removeEdge.addActionListener((ActionEvent e) -> {
            String f = ((String) from.getSelectedItem() != null) ? ((String) from.getSelectedItem()) : ("");
            String t = ((String) to.getSelectedItem() != null) ? ((String) to.getSelectedItem()) : ("");
            graph.removeEdge(f, t);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    flightDisplay.repaint();
                }
            });
        });
        controls.add(addEdge);
        controls.add(from);
        controls.add(to);
        controls.add(removeEdge);
        controls.setPreferredSize(new Dimension(320, 90));
        controls.setMaximumSize(new Dimension(450, 30));
        window.add(controls);
        window.add(flightDisplay);

        display.add(window);
        display.pack();
        display.setResizable(false);
        display.setVisible(true);
    }

    public static void main(String args[]) { 
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { 
                new Driver(new FlightPathGraph(cities));
            }
        });
    }


    private class FlightPathDisplay extends JPanel {

        private FlightPathDisplay() {
            super();
            this.setPreferredSize(new Dimension(550, 450));
        }

        final int circleRadius = 70;

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g2);
            int midpoint = (this.getWidth() / 2) - (circleRadius / 2);
            int quarterPoint = midpoint / 2;
            int midheight = (this.getHeight() / 2) - (circleRadius / 2);
            int quarterHeight = (midheight / 2);
            // Locations in the display for each vertice
            // Top, Bottom, Right, Left, BL, BR, TL, TR
            int[] xVals = { midpoint, midpoint, 20, this.getWidth() - 90, quarterPoint - 25, 3 * quarterPoint + 25,
                    quarterPoint - 25, 3 * quarterPoint + 25 };
            int[] yVals = { 5, this.getHeight() - 85, midheight, midheight, 3 * quarterHeight + 25,
                    3 * quarterHeight + 25, quarterHeight - 25, quarterHeight - 25 };
            // Offsets for the connecting lines for each vertice
            int[][] offsets = { { circleRadius / 2, circleRadius }, { circleRadius / 2, 0 },
                    { circleRadius, circleRadius / 2 }, { 0, circleRadius / 2 },
                    { circleRadius - 4, circleRadius / 4 }, { 4, circleRadius / 4 },
                    { circleRadius - 4, (3 * circleRadius) / 4 }, { 4, (3 * circleRadius) / 4 } };
            if (flightPaths == null) {
                return;
            }
            for (int i = 0; i < flightPaths.length && i < xVals.length; i++) {
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(135, 135, 133));
                g2.fillOval(xVals[i], yVals[i], circleRadius, circleRadius);
                g2.setColor(Color.BLACK);
                g2.drawOval(xVals[i], yVals[i], circleRadius, circleRadius);
                g.drawString(flightPaths[i].getCity(), xVals[i] + 5 + (4 * (8 - flightPaths[i].getCity().length())),
                        yVals[i] + 40);
                for (City ptr = flightPaths[i].getNext(); ptr != null; ptr = ptr.getNext()) {
                    int target = indexOfCity(ptr.getCity());
                    int[] x = {xVals[i] + offsets[i][0], xVals[target] + offsets[target][0]};
                    int[] y = {yVals[i] + offsets[i][1], yVals[target] + offsets[target][1]};
                    if (isDirected(flightPaths[i].getCity(), ptr.getCity())) {
                        g2.setColor(Color.RED);
                    } else {
                        g2.setColor(Color.BLACK);
                    }
                    g2.setStroke(new BasicStroke(2));
                    g2.drawPolyline(x, y, 2);
                }
            }
        }

        private int indexOfCity(String s) {
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].equals(s)) {
                    return i;
                }
            }
            return -1;
        }

        private boolean isDirected(String c1, String c2) {
            boolean uv = false;
            boolean vu = false;

            boolean foundc1 = false;
            boolean foundc2 = false;
            for (City u : flightPaths) {
                if (u.getCity().equals(c1)) {
                    City v = u.getNext(); 
                    while (v != null) {
                        if (v.getCity().equals(c2)) {
                            uv = true;
                            break;
                        }
                        v = v.getNext();
                    }
                    foundc1 = true;
                    if (foundc1 && foundc2) {
                        break;
                    }
                } else if (u.getCity().equals(c2)) {
                    City v = u.getNext(); 
                    while (v != null) {
                        if (v.getCity().equals(c1)) {
                            vu = true;
                            break;
                        }
                        v = v.getNext();
                    }
                    foundc2 = true;
                    if (foundc1 && foundc2) {
                        break;
                    }
                }
            }
            return uv ^ vu;
        }
    }

}


