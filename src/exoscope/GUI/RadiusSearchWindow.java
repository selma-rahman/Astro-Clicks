package GUI;

// for radius search
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import model.Exoplanet;
import logic.QueryEngine;

public class RadiusSearchWindow extends JPanel{

	private MainWindow main;
	private JPanel resultsContainer;
	private JLabel radiusResultsCountLabel;
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
        searchButton.setFont(MainWindow.pixelFontXs);
        searchButton.setBackground(MainWindow.COL_ACCENT); 
        searchButton.setForeground(MainWindow.BG_BASE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        
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
        JLabel maxLabel = new JLabel("MAX RADIUS: ");
        maxLabel.setFont(main.pixelFontXs);
        maxLabel.setForeground(MainWindow.COL_MUTED);
        search.add(maxLabel);
        search.add(maxField);
        search.add(searchButton);
        
        top.add(search);
        top.add(Box.createVerticalStrut(8));
        top.add(main.buildRangeInfoBox(planets, "RADIUS"));

     // local stats row
     JPanel stats = new JPanel(new GridLayout(1, 3, 8, 0));
     stats.setBackground(MainWindow.BG_BASE);
     stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
     stats.setOpaque(false);

     // planets loaded
     stats.add(main.statCard("PLANETS LOADED",
             Integer.toString(planets.size()),
             MainWindow.COL_TEXT));

     // results found
     JPanel resultsCard = new JPanel();
     resultsCard.setLayout(new BoxLayout(resultsCard, BoxLayout.Y_AXIS));
     resultsCard.setBackground(MainWindow.BG_PANEL);
     resultsCard.setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(MainWindow.COL_BORDER, 2),
             BorderFactory.createEmptyBorder(10, 12, 10, 12)
     ));

     JLabel resultsLabel = new JLabel("RESULTS FOUND");
     resultsLabel.setFont(MainWindow.pixelFontXs);
     resultsLabel.setForeground(MainWindow.COL_BORDER);

     radiusResultsCountLabel = new JLabel("0");
     radiusResultsCountLabel.setFont(main.pixelFontSm);
     radiusResultsCountLabel.setForeground(new Color(0x00,255,136));

     resultsCard.add(resultsLabel);
     resultsCard.add(radiusResultsCountLabel);

     stats.add(resultsCard);

     // page
     stats.add(main.statCard("DATA UPDATED", "02/2026", MainWindow.COL_TEXT));

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


        QueryEngine qe = new QueryEngine(planets);
		
        ActionListener searchAction = new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		JPanel x;
        		
        		if (!minField.getText().trim().matches("-?\\d+(\\.\\d+)?")) {
            		x = main.nonnumericError();
            		main.updateResultCount(0);
			        radiusResultsCountLabel.setText(String.valueOf(0));
			        resultsContainer.removeAll();
	                
		            resultsContainer.add(x);
		            resultsContainer.revalidate();
		            resultsContainer.repaint();
				    return;
            	
            	}
        		
        		double min = Double.parseDouble(minField.getText().trim());
        		double max = Double.parseDouble(maxField.getText().trim());

                if (max < min) {
			        x = main.maxMinError();
			        main.updateResultCount(0);
			        radiusResultsCountLabel.setText(String.valueOf(0));
			    } else {
			    	List<Exoplanet> results = qe.filterByRadius(min, max);
			    	x = main.buildResults(results);
			    	main.updateResultCount(results.size());
	                radiusResultsCountLabel.setText(String.valueOf(results.size()));
			    }
	            
                resultsContainer.removeAll();
	                
	            resultsContainer.add(x);
	            resultsContainer.revalidate();
	            resultsContainer.repaint();
			}
    
               
        };
        
        searchButton.addActionListener(searchAction);
        maxField.addActionListener(searchAction);
        minField.addActionListener(searchAction);
        
		
    }

}