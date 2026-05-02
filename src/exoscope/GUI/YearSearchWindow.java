package GUI;

// for year search
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import model.Exoplanet;
import logic.QueryEngine;

public class YearSearchWindow extends JPanel {

    private MainWindow main;
    private JPanel resultsContainer;
    private JLabel yearResultsCountLabel;
    private List<Exoplanet> planets;

    public YearSearchWindow(MainWindow main, List<Exoplanet> planets) {
        this.main = main;
        this.planets = planets;

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        buildYearSearchPanel();
    }

    private void buildYearSearchPanel() {

        this.setLayout(new BorderLayout());
        this.setBackground(MainWindow.BG_BASE);
        this.setOpaque(false);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        // --- UPDATED: Swapping JTextField for JComboBox ---
        Integer[] years = {null, 2026, 2025, 2024, 2023, 2022, 2021, 2020, 2019, 2018, 2017, 2016, 2015, 2014, 2013, 2012, 2011, 2010, 2009, 2008, 2007, 2006, 2005, 2004, 2003, 2002, 2001, 2000, 1999};
        JComboBox<Integer> yearDropdown = new JComboBox<>(years);
        
        // renderer is solution to little check box next to selected line that pixelfont wasn't picking up on
        yearDropdown.setRenderer(new DefaultListCellRenderer() {
        	@Override
        	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        		
        		if (index >= 0 && index < years.length) {
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
        
        yearDropdown.setFont(MainWindow.pixelFontXs);
        yearDropdown.setForeground(MainWindow.COL_MUTED);
        // Ensure the dropdown doesn't stretch too thin
        yearDropdown.setPreferredSize(new Dimension(200, 30)); 

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT,15,6));
        search.setBackground(MainWindow.BG_PANEL);
        search.setBorder(BorderFactory.createLineBorder(MainWindow.COL_BORDER,3));
        search.setMaximumSize(new Dimension(Integer.MAX_VALUE,45));

        JLabel prompt = new JLabel("?");
        prompt.setFont(main.pixelFontSm);
        prompt.setForeground(MainWindow.COL_BORDER);

        JLabel label = new JLabel("SELECT YEAR:");
        label.setFont(main.pixelFontXs);
        label.setForeground(MainWindow.COL_MUTED);

        search.add(prompt);
        search.add(label);
        search.add(yearDropdown); // Added dropdown instead of field

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

        yearResultsCountLabel = new JLabel("0");
        yearResultsCountLabel.setFont(main.pixelFontSm);
        yearResultsCountLabel.setForeground(new Color(0x00,255,136));
        yearResultsCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        yearResultsCountLabel.setBorder(BorderFactory.createEmptyBorder(6,0,0,0));

        resultsCard.add(resultsLabel);
        resultsCard.add(yearResultsCountLabel);

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

     // --- UPDATED: Action Listener now reads from JComboBox ---
        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int query = (int) yearDropdown.getSelectedItem();

                List<Exoplanet> results = qe.filterByYear(query);
                results.sort(Comparator.comparing(Exoplanet::getYear));

                resultsContainer.removeAll();
                resultsContainer.add(main.buildResults(results));

                main.updateResultCount(results.size());
                yearResultsCountLabel.setText(String.valueOf(results.size()));

                resultsContainer.revalidate();
                resultsContainer.repaint();
            }
        };

        yearDropdown.addActionListener(searchAction); 

    }
}