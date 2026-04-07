package GUI;

// for year search
import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Exoplanet;
import logic.QueryEngine;

public class YearSearchWindow {

    private JFrame window;
    private List<Exoplanet> planets;

    public YearSearchWindow(List<Exoplanet> planets){
        this.planets = planets;
        buildWindow();
    }

    private void buildWindow(){
        window = new JFrame("Search by Year");
        window.setSize(500,400);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());

        QueryEngine qe = new QueryEngine(planets);

        JTextField field = new JTextField();
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        field.addActionListener(e -> {
            try {
                int year = Integer.parseInt(field.getText().trim());
                List<Exoplanet> results = qe.filterByYear(year);

                resultsArea.setText("");
                for(Exoplanet p : results){
                    resultsArea.append(p.toString() + "\n\n");
                }
            } catch(Exception ex){
                resultsArea.setText("Enter a valid year!");
            }
        });

        window.add(new JLabel("Enter Year:"), BorderLayout.NORTH);
        window.add(field, BorderLayout.CENTER);
        window.add(new JScrollPane(resultsArea), BorderLayout.SOUTH);
    }

    public void show(){
        window.setVisible(true);
    }
}