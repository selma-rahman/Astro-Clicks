package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import model.Exoplanet;
import logic.QueryEngine;

public class OrbitSearchWindow {

    private JFrame window;
    private List<Exoplanet> planets;

    public OrbitSearchWindow(List<Exoplanet> planets){
        this.planets = planets;
        buildWindow();
    }

    private void buildWindow(){
        window = new JFrame("Search by Orbit Period");
        window.setSize(500,400);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());

        QueryEngine qe = new QueryEngine(planets);

        JTextField field = new JTextField();
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        field.addActionListener(e -> {
            try {
                String[] parts = field.getText().split(",");
                double min = Double.parseDouble(parts[0].trim());
                double max = Double.parseDouble(parts[1].trim());

                List<Exoplanet> results = qe.filterByOrbitalPeriod(min,max);

                resultsArea.setText("");
                for (Exoplanet p : results){
                    resultsArea.append(p.toString() + "\n\n");
                }
            } catch(Exception ex){
                resultsArea.setText("to nter min, max values separated by a comma!");
            }
        });

        window.add(new JLabel("Enter min,max for Orbit Period:"), BorderLayout.NORTH);
        window.add(field, BorderLayout.CENTER);
        window.add(new JScrollPane(resultsArea), BorderLayout.SOUTH);
    }

    public void show(){
        window.setVisible(true);
    }
}