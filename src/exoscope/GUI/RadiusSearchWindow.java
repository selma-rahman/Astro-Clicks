package GUI;

// for radius search
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import model.Exoplanet;
import logic.QueryEngine;

public class RadiusSearchWindow extends JPanel{

	private MainWindow main;
	private JPanel resultsContainer;
    private List<Exoplanet> planets;

    public RadiusSearchWindow(MainWindow main, List<Exoplanet> planets){
        this.planets = planets;
        this.main = main;
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
       
        buildRadiusSearchPanel();
    }

    private void buildRadiusSearchPanel(){
        this.setLayout(new BorderLayout());
        this.setBackground(main.BG_BASE);
        this.setOpaque(false);
        
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);
        
        
        JTextField minField = new JTextField(5);
        JTextField maxField = new JTextField(5);
        JButton searchButton = new JButton("SEARCH");
        
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT,15,6));
        search.setBackground(MainWindow.BG_PANEL);
        search.setBorder(BorderFactory.createLineBorder(MainWindow.COL_BORDER,3));
        search.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        
        JLabel prompt = new JLabel("?");
        prompt.setFont(main.pixelFontSm);
        prompt.setForeground(MainWindow.COL_BORDER);
        
        JLabel label = new JLabel("MIN RADIUS:");
        label.setFont(main.pixelFontXs);
        label.setForeground(MainWindow.COL_MUTED);
        
        search.add(prompt);
        search.add(label);
        search.add(minField);
        JLabel maxLabel = new JLabel(" MAX RADIUS: ");
        maxLabel.setFont(main.pixelFontXs);
        maxLabel.setForeground(MainWindow.COL_MUTED);
        search.add(maxLabel);
        search.add(maxField);
        search.add(searchButton);
        
        JPanel stats = main.buildStatCards(planets);
        
        top.add(search);
        top.add(Box.createVerticalStrut(12));
        top.add(stats);
        top.add(Box.createVerticalStrut(12));
        top.add(main.buildResultsHeader());
        top.add(Box.createVerticalStrut(6));
        
        resultsContainer = new JPanel();
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
        resultsContainer.setOpaque(false);
        

        JScrollPane scrollPane = new JScrollPane(resultsContainer);
        main.styleScrollPane(scrollPane);
        
        this.add(top, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        /*
		 window = new JFrame("Search by Radius"); 
		 window.setSize(500,400);
		 window.setLocationRelativeTo(null); 
		 window.setLayout(new BorderLayout());
		 */

        QueryEngine qe = new QueryEngine(planets);
		/*
		 * JTextField minField = new JTextField(5); JTextField maxField = new
		 * JTextField(5); JButton searchButton = new JButton("SEARCH");
		 */
        
		/*
		 * JPanel search = new JPanel(); search.add(new
		 * JLabel("Min radius (in earth radi):")); search.add(minField); search.add(new
		 * JLabel("Max radius (in earth radi):")); search.add(maxField);
		 * search.add(searchButton);
		 */
		/*
		 * JTextArea resultsArea = new JTextArea(); resultsArea.setEditable(false);
		 */

        searchButton.addActionListener(e -> {
            try {
                double min = Double.parseDouble(minField.getText());
                double max = Double.parseDouble(maxField.getText());

                List<Exoplanet> results = qe.filterByRadius(min, max);

                resultsContainer.removeAll();
                resultsContainer.add(main.buildResults(results));
                main.updateResultCount(results.size());

                resultsContainer.revalidate();
                resultsContainer.repaint();
            } catch(Exception ex){
            }
        });
    }

}