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

        JTextField field = new JTextField(10);

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

        JLabel label = new JLabel("ENTER YEAR:");
        label.setFont(main.pixelFontXs);
        label.setForeground(MainWindow.COL_MUTED);

        search.add(prompt);
        search.add(label);
        search.add(field);
        search.add(searchButton);

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

        stats.add(main.statCard(
                "PAGE",
                "67",
                MainWindow.COL_TEXT));

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

        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int year = Integer.parseInt(field.getText().trim());

                    List<Exoplanet> results = qe.filterByYear(year);
                    results.sort(Comparator.comparing(Exoplanet::getYear));

                    resultsContainer.removeAll();
                    resultsContainer.add(main.buildResults(results));

                    main.updateResultCount(results.size());
                    yearResultsCountLabel.setText(String.valueOf(results.size()));

                    resultsContainer.revalidate();
                    resultsContainer.repaint();

                } catch(Exception ex) {
                    resultsContainer.removeAll();
                    resultsContainer.add(main.noResultsFound());

                    main.updateResultCount(0);
                    yearResultsCountLabel.setText("0");

                    resultsContainer.revalidate();
                    resultsContainer.repaint();
                }
            }
        };

        searchButton.addActionListener(searchAction);
        field.addActionListener(searchAction);
    }
}