package GUI;

// for radius and mass search
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import model.Exoplanet;
import logic.QueryEngine;

public class RadiusMassSearchWindow {

    private JFrame window;
    private List<Exoplanet> planets;

    public RadiusMassSearchWindow(List<Exoplanet> planets){
        this.planets = planets;
        buildWindow();
    }

    private void buildWindow(){
        window = new JFrame("Search by Radius / Mass");
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

                List<Exoplanet> radiusResults = qe.filterByRadius(min, max);
                List<Exoplanet> massResults = qe.filterByMass(min, max);

                Set<Exoplanet> combined = new LinkedHashSet<>();
                combined.addAll(radiusResults);
                combined.addAll(massResults);

                resultsArea.setText("");
                for (Exoplanet p : combined){
                    resultsArea.append(p.toString() + "\n\n");
                }
            } catch(Exception ex){
                resultsArea.setText("Enter min,max values separated by a comma!");
            }
        });

        window.add(new JLabel("Enter min,max for Radius or Mass:"), BorderLayout.NORTH);
        window.add(field, BorderLayout.CENTER);
        window.add(new JScrollPane(resultsArea), BorderLayout.SOUTH);
    }

    public void show(){
        window.setVisible(true);
    }
}