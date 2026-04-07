package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Exoplanet;
import logic.QueryEngine;

public class MethodSearchWindow {

    private JFrame window;
    private List<Exoplanet> planets;

    public MethodSearchWindow(List<Exoplanet> planets){
        this.planets = planets;
        buildWindow();
    }

    private void buildWindow(){
        window = new JFrame("Search by Discovery Method");
        window.setSize(500,400);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());

        QueryEngine qe = new QueryEngine(planets);

        JTextField field = new JTextField();
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        field.addActionListener(e -> {
            String query = field.getText().trim();
            List<Exoplanet> results = qe.filterByDiscoveryMethod(query);

            resultsArea.setText("");
            for(Exoplanet p : results){
                resultsArea.append(p.toString() + "\n\n");
            }
        });

        window.add(new JLabel("Enter Discovery Method:"), BorderLayout.NORTH);
        window.add(field, BorderLayout.CENTER);
        window.add(new JScrollPane(resultsArea), BorderLayout.SOUTH);
    }

    public void show(){
        window.setVisible(true);
    }
}