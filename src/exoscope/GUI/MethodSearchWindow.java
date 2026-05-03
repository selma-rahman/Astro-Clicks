package GUI;

// for method search
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import model.Exoplanet;
import logic.QueryEngine;

public class MethodSearchWindow extends JPanel {

    private MainWindow main;
    private JPanel resultsContainer;
    private JLabel methodResultsCountLabel;
    private List<Exoplanet> planets;

    public MethodSearchWindow(MainWindow main, List<Exoplanet> planets) {
        this.main = main;
        this.planets = planets;

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        buildMethodSearchPanel();
    }

    private void buildMethodSearchPanel() {

        this.setLayout(new BorderLayout());
        this.setBackground(MainWindow.BG_BASE);
        this.setOpaque(false);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        // Swapping JTextField for JComboBox 
        String[] methods = {"","Transit", "Radial Velocity", "Imaging", "Transit Timing Variations", "Orbital Brightness Modulation", "Pulsation Timing Variations", "Microlensing" };
        JComboBox<String> methodDropdown = new JComboBox<>(methods);
        
        // renderer is solution to little check box next to selected line that pixelfont wasn't picking up on
        methodDropdown.setRenderer(new DefaultListCellRenderer() {
        	@Override
        	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        		
        		if (index >= 0 && index < methods.length) {
        			c.setBackground(MainWindow.BG_PANEL);
        		}
        		if (isSelected) {
        			c.setBackground(MainWindow.BG_CARD);
        			c.setForeground(MainWindow.COL_MUTED);
        		} else {
        			c.setForeground(MainWindow.COL_MUTED);
        		}
        		return c;
        	}
        });
        
        methodDropdown.setFont(MainWindow.pixelFontXs);
        methodDropdown.setForeground(MainWindow.COL_MUTED);
        // Ensure the dropdown doesn't stretch too thin
        methodDropdown.setPreferredSize(new Dimension(350, 30)); 

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT,15,6));
        search.setBackground(MainWindow.BG_PANEL);
        search.setBorder(BorderFactory.createLineBorder(MainWindow.COL_BORDER,3));
        search.setMaximumSize(new Dimension(Integer.MAX_VALUE,45));

        JLabel prompt = new JLabel("?");
        prompt.setFont(main.pixelFontSm);
        prompt.setForeground(MainWindow.COL_BORDER);

        JLabel label = new JLabel("SELECT METHOD:");
        label.setFont(main.pixelFontXs);
        label.setForeground(MainWindow.COL_MUTED);

        search.add(prompt);
        search.add(label);
        search.add(methodDropdown); // Added dropdown instead of field
        top.add(search);

        // stat cards
        JPanel stats = new JPanel(new GridLayout(1,3,8,0));
        stats.setBackground(MainWindow.BG_BASE);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
        stats.setOpaque(false);

        stats.add(main.statCard(
                "PLANETS LOADED",
                Integer.toString(planets.size()),
                MainWindow.COL_TEXT));

        JPanel resultsCard = new JPanel();
        resultsCard.setLayout(new BoxLayout(resultsCard, BoxLayout.Y_AXIS));
        resultsCard.setBackground(MainWindow.BG_PANEL);
        resultsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainWindow.COL_BORDER,2),
                BorderFactory.createEmptyBorder(10,12,10,12)
        ));

        JLabel resultsLabel = new JLabel("RESULTS FOUND");
        resultsLabel.setFont(MainWindow.pixelFontXs);
        resultsLabel.setForeground(MainWindow.COL_BORDER);
        resultsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        methodResultsCountLabel = new JLabel("0");
        methodResultsCountLabel.setFont(main.pixelFontSm);
        methodResultsCountLabel.setForeground(new Color(0x00,255,136));
        methodResultsCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        methodResultsCountLabel.setBorder(BorderFactory.createEmptyBorder(6,0,0,0));

        resultsCard.add(resultsLabel);
        resultsCard.add(methodResultsCountLabel);

        stats.add(resultsCard);

        stats.add(main.statCard("DATA UPDATED", "02/2026", MainWindow.COL_TEXT));

        top.add(Box.createVerticalStrut(12));
        top.add(stats);
        top.add(Box.createVerticalStrut(12));
        top.add(main.buildResultsHeader());
        top.add(Box.createVerticalStrut(6));

        // results area
        resultsContainer = new JPanel();
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
        resultsContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(resultsContainer);
        main.styleScrollPane(scrollPane);

        this.add(top, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        QueryEngine qe = new QueryEngine(planets);

        //Action Listener now reads from JComboBox
        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String query = (String) methodDropdown.getSelectedItem();

                List<Exoplanet> results = qe.filterByDiscoveryMethod(query);
                results.sort(Comparator.comparing(Exoplanet::getDiscoveryMethod));

                resultsContainer.removeAll();
                resultsContainer.add(main.buildResults(results));

                main.updateResultCount(results.size());
                methodResultsCountLabel.setText(String.valueOf(results.size()));

                resultsContainer.revalidate();
                resultsContainer.repaint();
            }
        };

        methodDropdown.addActionListener(searchAction); 
    }
}