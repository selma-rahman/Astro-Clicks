package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.io.InputStream;
import java.awt.event.*;
import logic.QueryEngine;
import model.Exoplanet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.awt.Desktop;
import java.net.URI;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class MainWindow{
	// color palette, hex codes
	// BG = background
	// COL = foreground/accent colors
	
	public static final Color BG_BASE = new Color(0x05, 0x05, 0x10); // black navy ish
	public static final Color BG_PANEL = new Color(0x07, 0x07, 0x18); // lighter, for sidebar and cards
	public static final Color BG_CARD = new Color(0x0a, 0x0a, 0x2a); // active or highlight stuff
	public static final Color COL_BORDER = new Color(0x3a, 0x3a, 0xff); // bright blue/purple for borders
	public static final Color COL_TEXT = new Color(0xe0, 0xe0, 0xff); // softer white/blue for main text color
	public static final Color COL_MUTED = new Color(0x5a, 0x5a, 0x9a); // dimmer purple/grey for inactive items
	public static final Color COL_ACCENT = new Color(0x7b, 0x7b, 0xff); // blue/purple for titles and things to be highlights
	private static final Color COL_GREEN = new Color(0x00, 0xff, 0x88); // neon green, for the result count
	
	private String currentSearchType = "HOME"; // default search
	
	private Font pixelFont; // large
	public Font pixelFontSm; // medium
	public static Font pixelFontXs; // small
	
	protected JFrame window;
	protected JPanel sidebar;
	
	private JPanel main;
	private JPanel infoHomePanel;
	private JPanel homePanel; // for homepagee
	private JPanel infoDetailPanel;

	private DiscoveryMethodsPanel discoveryMethodsPanel;

	private JLabel searchPromptLabel;
	
	private JPanel currentResults;
	
	protected boolean infoMode = false;
	
	JPanel centerPanel; // removed private for graphics
	CardLayout centerLayout; // removed private for graphics
		
	//private JLabel resultsCountLabel = new JLabel("0");;
	private JLabel resultsCountLabel; //make it direct
	private JPanel resultsBody; 
	
	private List<Exoplanet> planets;
	
	public MainWindow(List<Exoplanet> planets) {
		this.planets = planets;
		loadFont();
		buildWindow();
	}
	
	private void loadFont() {
		try {
			InputStream is = getClass().getResourceAsStream("fonts/PressStart2P.ttf"); 
			if ( is != null) {
				Font base = Font.createFont(Font.TRUETYPE_FONT, is);
				GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
				pixelFont = base.deriveFont(18f);
				pixelFontSm = base.deriveFont(15f);
				pixelFontXs = base.deriveFont(10f);
			} else {
				pixelFont = new Font("monospaced", Font.PLAIN, 10);
				pixelFontSm = new Font("Monospaced", Font.PLAIN, 8);
				pixelFontXs = new Font("Monospaced", Font.PLAIN, 6);
			}
			
		} catch (Exception e) {
			pixelFont = new Font("Monospaced", Font.PLAIN, 10);
			pixelFontSm = new Font("Monospaced", Font.PLAIN, 8);
			pixelFontXs = new Font("Monospaced", Font.PLAIN, 6);
		}
	}
	
	private void buildWindow()  { //(List<Exoplanet> planets) {
		window = new JFrame("EXOSCOPE v1.0");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(1200, 700);
		window.setLocationRelativeTo(null);
		window.setMinimumSize(new Dimension(950,550));
		
		//okay panel to paint the star background
		JPanel root = new JPanel(new BorderLayout()) {
			// paintCompoenent called by swing whenever the panel needs to redraw
			@Override
			protected void paintComponent(Graphics g) {
			    super.paintComponent(g);
			    g.setColor(BG_BASE);
			    g.fillRect(0, 0, getWidth(), getHeight());

			    // regular stars 
			    java.util.Random rand = new java.util.Random(42);
			    for (int i = 0; i < 150; i++) {
			        int x = rand.nextInt(1200);
			        int y = rand.nextInt(700);
			        int size = rand.nextInt(3) + 1;
			        if (i % 4 == 0) {
			            g.setColor(new Color(180, 180, 255, 200));
			        } else {
			            g.setColor(new Color(150, 150, 220, 130));
			        }
			        g.fillOval(x, y, size, size);
			    }

			    // feature stars, small cluster of brighter white-blue dots
			    java.util.Random rand2 = new java.util.Random(99);
			    for (int i = 0; i < 10; i++) {
			        int x = rand2.nextInt(1200);
			        int y = rand2.nextInt(700);
			        g.setColor(new Color(220, 220, 255, 255)); //opaque
			        g.fillOval(x, y, 2, 2);
			    }
			}
		};
				
	    root.setBackground(BG_BASE);
	    root.setBorder(BorderFactory.createLineBorder(COL_BORDER, 3));


	    centerLayout = new CardLayout();
	    centerPanel = new JPanel(centerLayout);
	    centerPanel.setOpaque(false);

	    homePanel = buildHomePanel(); // to build a seprate home panell
	    main = buildMain(this.planets);
	    discoveryMethodsPanel = new DiscoveryMethodsPanel(this);
	    infoHomePanel = discoveryMethodsPanel.buildInfoHome();
	    infoDetailPanel = discoveryMethodsPanel.buildInfoDetail();

	    PlanetNameSearchWindow planetSearch = new PlanetNameSearchWindow(this, planets);
		RadiusSearchWindow radiusSearch = new RadiusSearchWindow(this, planets);
		MassSearchWindow massSearch = new MassSearchWindow(this, planets);
		OrbitSearchWindow orbitSearch = new OrbitSearchWindow(this, planets);
		HostStarSearchWindow hostSearch = new HostStarSearchWindow(this, planets);
		YearSearchWindow yearSearch = new YearSearchWindow(this, planets);
		MethodSearchWindow methodSearch = new MethodSearchWindow(this, planets);
		
		
		
		centerPanel.add(homePanel, "HOME"); 
		centerPanel.add(main, "MAIN");
		centerPanel.add(infoHomePanel, "INFO_HOME");
		centerPanel.add(infoDetailPanel,"INFO_DETAIL");
		centerPanel.add(planetSearch, "PLANET_PANEL");
		centerPanel.add(radiusSearch, "RADIUS_PANEL");
		centerPanel.add(massSearch, "MASS_PANEL");
		centerPanel.add(orbitSearch, "ORBIT_PANEL");

		centerPanel.add(hostSearch, "HOST_PANEL");
		centerPanel.add(methodSearch, "METHOD_PANEL");
		centerPanel.add(yearSearch, "YEAR_PANEL");
	
		PlanetDetailPanel planetDetail = new PlanetDetailPanel(this);
		centerPanel.add(planetDetail, "PLANET_DETAIL");		
		
		centerLayout.show(centerPanel, "HOME");
		
			
		root.add(buildTitleBar(), BorderLayout.NORTH);
	    root.add(buildSidebar(), BorderLayout.WEST);
		root.add(centerPanel, BorderLayout.CENTER);
		
		window.setContentPane(root);
	}
		
		// title bar
	private JPanel buildTitleBar() {
		JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
		bar.setBackground(new Color(0x0a, 0x0a, 0x2a));
		bar.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0,  COL_BORDER));
		
		JLabel title = new JLabel("\u2605 EXOSCOPE v1.0 \u2605");
		title.setFont(pixelFontSm);
		title.setForeground(COL_ACCENT);
		bar.add(title);
		
		
		return bar;
	}
		
		// sidebar
	protected JPanel buildSidebar() {
		//JPanel sidebar = new JPanel();
		sidebar = new JPanel();
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setBackground(BG_PANEL);
		sidebar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, COL_BORDER), BorderFactory.createEmptyBorder(14, 10, 14, 10)));
		sidebar.setPreferredSize(new Dimension(200, 0));
		
		sidebar.add(sidebarSection("// NAVIGATION"));
		sidebar.add(sidebarItem("HOME", currentSearchType.equals("HOME")));
		sidebar.add(Box.createVerticalStrut(8));
		
		sidebar.add(sidebarSection("// SEARCH BY"));
		
		sidebar.add(sidebarItem("PLANET NAME", currentSearchType.equals("PLANET NAME")));
		sidebar.add(sidebarItem("HOST STAR", currentSearchType.equals("HOST STAR")));
		sidebar.add(sidebarItem("METHOD", currentSearchType.equals("METHOD")));
		sidebar.add(sidebarItem("RADIUS", currentSearchType.equals("RADIUS")));
		sidebar.add(sidebarItem("MASS", currentSearchType.equals("MASS")));
		sidebar.add(sidebarItem("ORBIT PERIOD", currentSearchType.equals("ORBIT PERIOD")));
		sidebar.add(sidebarItem("YEAR", currentSearchType.equals("YEAR")));
		sidebar.add(Box.createVerticalStrut(6));
		sidebar.add(sidebarSection("// INFO"));
		sidebar.add(Info(infoMode));
		
		return sidebar;
	}
	
	private JPanel Info(boolean active) {
	    JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 5));
	    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
	    item.setAlignmentX(Component.LEFT_ALIGNMENT);

	    if (active) {
	        item.setBackground(BG_CARD);
	        item.setBorder(BorderFactory.createLineBorder(COL_BORDER, 2));
	    } else {
	        item.setBackground(BG_PANEL);
	        item.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	    }

	    JLabel arrow = new JLabel(active ? "►" : "  ");
	    arrow.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
	    arrow.setForeground(COL_BORDER);

	    JLabel label = new JLabel("DISC. METHOD");
	    label.setFont(pixelFontXs);
	    label.setForeground(active ? COL_TEXT : COL_MUTED);

	    item.add(arrow);
	    item.add(label);
	    item.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    item.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	        	infoMode = true;
	        	currentSearchType = "";   // clears PLANET NAME, METHOD, RADIUS, etc.
	            centerLayout.show(centerPanel, "INFO_HOME");

	            window.remove(sidebar);
	            sidebar = buildSidebar();
	            window.add(sidebar, BorderLayout.WEST);

	            window.revalidate();
	            window.repaint();
	        }
	    });

	    return item;
	}
	

	protected JButton retroButton(String text) {
	    JButton btn = new JButton(text);
	    btn.setFont(pixelFontXs);
	    btn.setForeground(COL_TEXT);
	    btn.setBackground(BG_CARD);
	    btn.setFocusPainted(false);
	    btn.setBorder(BorderFactory.createLineBorder(COL_BORDER, 2));
	    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    return btn;
	}
	
	private JPanel sidebarItem(String text, boolean active) {

	    JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 5));
	    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
	    item.setAlignmentX(Component.LEFT_ALIGNMENT);
	  
	    if (active) {
	        item.setBackground(BG_CARD);
	        item.setBorder(BorderFactory.createLineBorder(COL_BORDER, 2));
	    } else {
	        item.setBackground(BG_PANEL);
	        item.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	    }

	    JLabel arrow = new JLabel(active ? "►" : "  ");
	    arrow.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
	    arrow.setForeground(COL_BORDER);

	    JLabel label = new JLabel(text);
	    label.setFont(pixelFontXs);
	    label.setForeground(active ? COL_TEXT : COL_MUTED);

	    item.add(arrow);
	    item.add(label);

	    // make clickable
	    item.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    item.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		currentSearchType = text; // this tells the main search bar which type to search
	    		infoMode = false;
	    	
	    		
	    		if (searchPromptLabel != null) {
	    		searchPromptLabel.setText("ENTER " + currentSearchType + ":");
	    		}
	
	    		
	    		if (text.equals("HOME")) {
	    		    centerLayout.show(centerPanel, "HOME");

	    		} else if (text.equals("PLANET NAME")) {
	    			centerLayout.show(centerPanel, "PLANET_PANEL");

	    		} else if (text.equals("RADIUS")) {
	    		    centerLayout.show(centerPanel, "RADIUS_PANEL");

	    		} else if (text.equals("MASS")) {
	    		    centerLayout.show(centerPanel, "MASS_PANEL");

	    		} else if (text.equals("ORBIT PERIOD")) {
	    		    centerLayout.show(centerPanel, "ORBIT_PANEL");

	    		} else if (text.equals("HOST STAR")) {
	    		    centerLayout.show(centerPanel, "HOST_PANEL");
	    		} else if (text.equals("METHOD")) {
	    		    centerLayout.show(centerPanel, "METHOD_PANEL");
	    		} else if (text.equals("YEAR")) {
	    		    centerLayout.show(centerPanel, "YEAR_PANEL");
	    		} else {
	    		    centerLayout.show(centerPanel, "MAIN");
	    		}
	    	
	    	// to refresh sidebar 
	    	window.remove(sidebar);
	    	sidebar = buildSidebar();
	    	window.add(sidebar, BorderLayout.WEST);
	    	window.revalidate();
	    	window.repaint();
	    }
	 });

	 return item;
}
	
	private JLabel sidebarSection(String text) {
		JLabel l = new JLabel(text);
		l.setFont(pixelFontXs);
		l.setForeground(COL_BORDER);
		l.setBorder(BorderFactory.createEmptyBorder(10, 4, 4, 0));
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
		return l;
	}
		
		// main area
	private JPanel buildMain(List<Exoplanet> planets) {
	    JPanel main = new JPanel();
	    main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
	    main.setBackground(BG_BASE);
	    main.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
	    main.setOpaque(false);

	    JPanel resultsSection = new JPanel();
	    resultsSection.setLayout(new BoxLayout(resultsSection, BoxLayout.Y_AXIS));
	    resultsSection.setOpaque(false);

	    resultsBody = new JPanel();
	    resultsBody.setLayout(new BoxLayout(resultsBody, BoxLayout.Y_AXIS));
	    resultsBody.setOpaque(false);

	    resultsSection.add(buildResultsHeader());
	    resultsSection.add(Box.createVerticalStrut(6));
	    resultsSection.add(resultsBody);

	    main.add(buildSearchBar(planets, resultsBody));
	    main.add(Box.createVerticalStrut(12));
	    main.add(buildStatCards(planets));
	    main.add(Box.createVerticalStrut(12));
	    
	    main.add(resultsSection);

	    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    bottom.setOpaque(false);

	    JButton homeBtn = retroButton("HOME");
	    homeBtn.addActionListener(e ->
	        centerLayout.show(centerPanel, "HOME")
	    );

	    bottom.add(homeBtn);

	    main.add(Box.createVerticalStrut(8));
	    main.add(bottom);

	    
	    return main;
	}
	
	private JPanel buildSearchBar(List<Exoplanet> planets, JPanel rc) {
		QueryEngine qe = new QueryEngine(planets);
		JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
		JTextField field = new JTextField(20);
        JButton searchButton = new JButton("SEARCH");
        searchButton.setFont(pixelFontXs);
        searchButton.setBackground(COL_ACCENT); 
        searchButton.setForeground(BG_BASE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        ActionListener searchAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = field.getText();
				field.setText("");

				List<Exoplanet> results;

				switch(currentSearchType) {
					case "PLANET NAME":
						results = qe.filterByName(query);
						results.sort(Comparator.comparing(Exoplanet::getName));
						break;
					case "HOST STAR":
						results = qe.filterByHostStar(query);
						results.sort(Comparator.comparing(Exoplanet::getHostStar));
						break;
					case "METHOD":
						results = qe.filterByDiscoveryMethod(query);
						break;
					case "RADIUS":
						String[] parts = query.split(",");
						double min = Double.parseDouble(parts[0].trim());
						double max = Double.parseDouble(parts[1].trim());
						results = qe.filterByRadius(min, max); 
					    showResults(results);
						break;
					case "MASS":
						String[] parts2 = query.split(",");
						double min2 = Double.parseDouble(parts2[0].trim());
						double max2 = Double.parseDouble(parts2[1].trim());
						results = qe.filterByMass(min2, max2); 
						break;
					case "ORBIT PERIOD":
						parts = query.split(",");
						double min3 = Double.parseDouble(parts[0].trim());
						double max3 = Double.parseDouble(parts[1].trim());
						results = qe.filterByOrbitalPeriod(min3, max3); 
						break;
					case "YEAR":
						int year = Integer.parseInt(query.trim());
						results = qe.filterByYear(year);
						break;
					default:
						results = qe.filterByName(query);
						}
				showResults(results);
			}
		};
		
		searchButton.addActionListener(searchAction);
	    field.addActionListener(searchAction);

		bar.setBackground(BG_PANEL);
		bar.setBorder(BorderFactory.createLineBorder(COL_BORDER, 3));
		bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		
		// small arrow prompt
		JLabel prompt = new JLabel("?");
		prompt.setFont(pixelFontSm);
		prompt.setForeground(COL_BORDER);

		// this is now the class field so we can update dynamically
		searchPromptLabel = new JLabel("ENTER PLANET NAME:");
		searchPromptLabel.setFont(pixelFontXs);
		searchPromptLabel.setForeground(COL_MUTED);
		bar.add(prompt);
		bar.add(searchPromptLabel); // this is updated label
		bar.add(field);
		bar.add(searchButton);

		return bar;}
	
	protected JPanel buildStatCards(List<Exoplanet> planets) {
	    JPanel row = new JPanel(new GridLayout(1, 3, 8, 0));
	    row.setBackground(BG_BASE);
	    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

	    row.add(statCard("PLANETS LOADED", Integer.toString(planets.size()), COL_TEXT));

	    JPanel resultsCard = new JPanel();
	    resultsCard.setLayout(new BoxLayout(resultsCard, BoxLayout.Y_AXIS));
	    resultsCard.setBackground(BG_PANEL);
	    resultsCard.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(COL_BORDER, 2),
	        BorderFactory.createEmptyBorder(10, 12, 10, 12)
	    ));

	    JLabel resultsLabel = new JLabel("RESULTS FOUND");
	    resultsLabel.setFont(pixelFontXs);
	    resultsLabel.setForeground(COL_BORDER);
	    resultsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

	    resultsCountLabel = new JLabel("0");
	    resultsCountLabel.setFont(pixelFont);
	    resultsCountLabel.setForeground(COL_GREEN);
	    resultsCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	    resultsCountLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

	    resultsCard.add(resultsLabel);
	    resultsCard.add(resultsCountLabel);

	    row.add(resultsCard);
	    row.add(statCard("DATA UPDATED", "02/2026", COL_TEXT));

	    return row;
	}	
	
	
	// single stat box
	protected JPanel statCard(String label, String value, Color valueColor) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(BG_PANEL);;
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COL_BORDER, 2), BorderFactory.createEmptyBorder(10, 12, 10, 12)));
		
		JLabel lbl = new JLabel(label);
		lbl.setFont(pixelFontXs);
		lbl.setForeground(COL_BORDER);
		lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel val = new JLabel(value);
		val.setFont(pixelFont);
        val.setForeground(valueColor);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);
        val.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        card.add(lbl);
        card.add(val);
        return card;
	}
	
	protected JPanel buildRangeInfoBox(List<Exoplanet> planets, String fieldType) {
	    double minVal = 0, maxVal = 0;
	    String unit  = "";
	    String label = "";

	    switch (fieldType) {
	        case "RADIUS":
	            minVal = planets.stream()
	                .mapToDouble(Exoplanet::getRadius)
	                .filter(v -> v > 0)
	                .min().orElse(0);
	            maxVal = planets.stream()
	                .mapToDouble(Exoplanet::getRadius)
	                .filter(v -> v > 0)
	                .max().orElse(0);
	            unit  = "Earth radii";
	            label = "PLANET RADIUS RANGE";
	            break;
	        case "MASS":
	            minVal = planets.stream()
	                .mapToDouble(Exoplanet::getMass)
	                .filter(v -> v > 0)
	                .min().orElse(0);
	            maxVal = planets.stream()
	                .mapToDouble(Exoplanet::getMass)
	                .filter(v -> v > 0)
	                .max().orElse(0);
	            unit  = "Earth masses";
	            label = "PLANET MASS RANGE";
	            break;
	        case "ORBIT PERIOD":
	            minVal = planets.stream()
	                .mapToDouble(Exoplanet::getOrbitalPeriod)
	                .filter(v -> v > 0)
	                .min().orElse(0);
	            maxVal = planets.stream()
	                .mapToDouble(Exoplanet::getOrbitalPeriod)
	                .filter(v -> v > 0)
	                .max().orElse(0);
	            unit  = "days";
	            label = "ORBIT PERIOD RANGE";
	            break;
	        default:
	            // unsupported field — return an empty placeholder
	            return new JPanel();
	    }

	    String minStr = String.format("%.2f", minVal);
	    String maxStr = String.format("%.2f", maxVal);

	    JPanel box = new JPanel();
	    box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
	    box.setBackground(BG_PANEL);
	    box.setOpaque(true);
	    box.setAlignmentX(Component.CENTER_ALIGNMENT);
	    box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
	    box.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(COL_BORDER, 2),
	        BorderFactory.createEmptyBorder(8, 12, 8, 12)
	    ));

	    JLabel heading = new JLabel(label + "  (" + unit + ")");
	    heading.setFont(pixelFontXs);
	    heading.setForeground(COL_BORDER);
	    heading.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel range = new JLabel("MIN: " + minStr + "     \u2014     MAX: " + maxStr);
	    range.setFont(pixelFontXs);
	    range.setForeground(COL_GREEN);
	    range.setAlignmentX(Component.CENTER_ALIGNMENT);
	    range.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

	    box.add(heading);
	    box.add(range);

	    return box;
	}

	protected JPanel buildResultsHeader() {
		JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(BG_BASE);
        hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        hdr.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0x1a, 0x1a, 0x4a)));

        JLabel left = new JLabel("\u25BC RESULTS: BY NAME");
        left.setFont(pixelFontXs);
        left.setForeground(COL_BORDER);


        hdr.add(left, BorderLayout.WEST);
        return hdr;
	}
	
		protected JPanel buildResults(List<Exoplanet> p) {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBackground(BG_BASE);
			panel.setOpaque(false);
			
			if (p.size() == 0) {
				return noResultsFound();
			}

			for (Exoplanet planet : p) {
				panel.add(buildPlanetRow(planet));
				panel.add(Box.createVerticalStrut(5));
			}
			return panel;
		}

		private JPanel buildHomePanel() {
		    JPanel home = new JPanel();
		    home.setLayout(new BoxLayout(home, BoxLayout.Y_AXIS));
		    home.setOpaque(false);
		    home.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

		    JLabel welcome = new JLabel("WELCOME TO");
		    welcome.setFont(pixelFontSm);
		    welcome.setForeground(COL_TEXT);
		    welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

		    JLabel title = new JLabel(" EXOSCOPE ");
		    title.setFont(pixelFont);
		    title.setForeground(COL_ACCENT);
		    title.setAlignmentX(Component.CENTER_ALIGNMENT);
		    
		    JLabel exoStar = new JLabel("✦");
		    JLabel exoStar2 = new JLabel("✦");
		    exoStar.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
		    exoStar2.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
		    exoStar.setForeground(COL_ACCENT);
		    exoStar2.setForeground(COL_ACCENT);
		    
		    JPanel panel = new JPanel();
		    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		    panel.setOpaque(false);

		    // Add labels to panel
		    panel.add(exoStar);
		    panel.add(title);
		    panel.add(exoStar2);
		    
		    JLabel subtitle = new JLabel("EXPLORE NASA EXOPLANET DATA");
		    subtitle.setFont(pixelFontXs);
		    subtitle.setForeground(COL_GREEN);
		    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		    JTextPane info = new JTextPane();
		    info.setText("Exoscope is an application that allows users to explore and analyze data about discovered exoplanets using publicly available NASA datasets. Users will be able to query exoplanets based on characteristics such as: size, orbital period, distance from Earth, and host star properties. The system will display results in a structured format and later revisions will provide visualizations to help users understand patterns and trends." +
		        "Choose whichever search option that you are most curious about from the sidebar or start with planet name.");		    
		    SimpleAttributeSet attributes = new SimpleAttributeSet();
		    StyleConstants.setLineSpacing(attributes,0.7f);
		    info.setParagraphAttributes(attributes, false);
		    info.setMaximumSize(new Dimension(750, 90));
		    info.setFont(pixelFontXs);
		    info.setForeground(COL_TEXT);
		    info.setBackground(BG_PANEL);
		    info.setEditable(false);
		    info.setBorder(BorderFactory.createCompoundBorder(
		        BorderFactory.createLineBorder(COL_BORDER, 2),
		        BorderFactory.createEmptyBorder(15, 15, 5, 15)
		    ));		  
		    StyleConstants.setSpaceBelow(attributes, 0f);
		    
		    JPanel cards = new JPanel(new GridLayout(1, 3, 10, 0));
		    cards.setOpaque(false);
		    cards.setMaximumSize(new Dimension(750, 85));

		    cards.add(statCard("PLANETS LOADED", Integer.toString(planets.size()), COL_TEXT));
		    cards.add(statCard("SEARCH MODES", "7", COL_GREEN));
		    cards.add(statCard("DATA UPDATED", "02/2026", COL_TEXT));

		    JButton startButton = retroButton("START EXPLORING");
		    startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		    startButton.setMaximumSize(new Dimension(260, 45));
		    startButton.addActionListener(e -> {
		    	currentSearchType = "PLANET NAME";
		    	centerLayout.show(centerPanel, "PLANET_PANEL");
		    	window.remove(sidebar);
		    	sidebar = buildSidebar();
		    	window.add(sidebar, BorderLayout.WEST);
		    	window.revalidate();
		    	window.repaint();
		    });
		  
		    
		    JLabel hint = new JLabel("Use the sidebar to search by different planet features.");
		    hint.setFont(pixelFontXs);
		    hint.setForeground(COL_MUTED);
		    hint.setAlignmentX(Component.CENTER_ALIGNMENT);

		    home.add(Box.createVerticalGlue());
		    home.add(welcome);
		    home.add(Box.createVerticalStrut(10));
		    home.add(panel);
		    home.add(Box.createVerticalStrut(12));
		    home.add(subtitle);
		    home.add(Box.createVerticalStrut(25));
		    home.add(info);
		    home.add(Box.createVerticalStrut(20));
		    home.add(cards);
		    home.add(Box.createVerticalStrut(25));
		    home.add(startButton);
		    home.add(Box.createVerticalStrut(15));
		    home.add(hint);
		    home.add(Box.createVerticalStrut(20));

		    JLabel stars = new JLabel("✦　　·　　　*　　　✯　　　•　　　☆　　　*　　　·　　✦");
		    stars.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		    stars.setForeground(COL_GREEN);
		    stars.setAlignmentX(Component.CENTER_ALIGNMENT);

		    JLabel planets = new JLabel("　　✧　　　◌　　　☾　　　⊹　　　◍　　　✦　　　◌　　　☽　　　✧");
		    planets.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		    planets.setForeground(COL_GREEN);
		    planets.setAlignmentX(Component.CENTER_ALIGNMENT);

		    JLabel stars2 = new JLabel("*　　°　　✵　　　•　　　✶　　★　　·　　　☆　　　°　　*");
		    stars2.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		    stars2.setForeground(COL_GREEN);
		    stars2.setAlignmentX(Component.CENTER_ALIGNMENT);

		    home.add(stars);
		    home.add(Box.createVerticalStrut(5));
		    home.add(planets);
		    home.add(Box.createVerticalStrut(5));
		    home.add(stars2);

		    home.add(Box.createVerticalGlue());

		    return home;
		}
		
		private JPanel buildPlanetRow(Exoplanet planet) {
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
			row.setBackground(BG_PANEL);
			row.setOpaque(false);
			row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
			row.setBorder(BorderFactory.createLineBorder(new Color(0x1a, 0x1a, 0x4a), 2));
			row.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			JLabel star = new JLabel("\u2605");
			star.setFont(pixelFontSm);
			star.setForeground(COL_ACCENT);
			star.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
			
			// Okay so only the name is shown and it will be underlined to signal to user that it's click-able
			JLabel nameLabel = new JLabel("<html><u>" + planet.getName() + "</u></html>");
			nameLabel.setFont(pixelFontXs);
			nameLabel.setForeground(COL_ACCENT);
			
			row.add(star);
			row.add(nameLabel);
			row.add(Box.createHorizontalGlue());
			
			// when the user clicks on the row they're navigated to the planet detail page
			MouseAdapter click = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// find the planetdetailpanel card and update
					for (Component c : centerPanel.getComponents()) {
						if (c instanceof PlanetDetailPanel) {
							((PlanetDetailPanel) c).show(planet, currentSearchType);
							break;
						}
					}
					centerLayout.show(centerPanel, "PLANET_DETAIL");
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					row.setBackground(BG_CARD);
					row.setBorder(BorderFactory.createLineBorder(COL_BORDER, 2));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					row.setBackground(BG_PANEL);
					row.setBorder(BorderFactory.createLineBorder(new Color(0x1a, 0x1a, 0x4a), 2));
				}
			};
			row.addMouseListener(click);
			
			return row;
		}
		
		protected JPanel noResultsFound() {
			JPanel nrf = new JPanel();
			
			nrf.setBackground(BG_PANEL);
			nrf.setOpaque(false);
			nrf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
			nrf.setBorder(BorderFactory.createLineBorder(new Color(0x1a, 0x1a, 0x4a), 2));
			
			JLabel text = new JLabel("No results found.");
			text.setFont(pixelFont);
			text.setForeground(COL_TEXT);
			
			nrf.add(text);
			return(nrf);

		}
		
		protected JPanel maxMinError() {
			updateResultCount(0);
		    resultsBody.removeAll();
		   		
			JPanel mme = new JPanel(new GridBagLayout()); // GridBag centers the text
			mme.setBackground(BG_PANEL);
			mme.setOpaque(false);
			mme.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
			mme.setBorder(BorderFactory.createLineBorder(new Color(0x1a, 0x1a, 0x4a),2));
			
			JLabel text = new JLabel("Enter a maximum value larger than your minimum value.");
			text.setFont(pixelFontSm);
			text.setForeground(COL_TEXT);
			
			mme.add(text);
			
			return mme;
			
		}
		
		protected JPanel nonnumericError() {
			updateResultCount(0);
			resultsBody.removeAll();
			
			JPanel nne = new JPanel();
		    nne.setLayout(new BoxLayout(nne, BoxLayout.Y_AXIS)); 
			nne.setBackground(BG_PANEL);
			nne.setOpaque(false);
			nne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
			
			JLabel line1 = new JLabel("Please enter numeric values in the search fields.");
			JLabel line2 = new JLabel("These can be integers or decimals.");
			line1.setFont(pixelFontSm);
			line1.setForeground(COL_TEXT);
			line2.setFont(pixelFontSm);
			line2.setForeground(COL_TEXT);
			
			nne.add(line1);
		    nne.add(Box.createVerticalStrut(8));
		    nne.add(line2);
		    
		    return(nne);
		}
			
		
		protected void updateResultCount(int count) {
		    if (resultsCountLabel != null) {
		        resultsCountLabel.setText(String.valueOf(count));
		        resultsCountLabel.revalidate();
		        resultsCountLabel.repaint();
		    }
		}
		
		protected void styleScrollPane(JScrollPane scrollPane) {
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false);
			scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
				@Override
				protected void configureScrollBarColors() {
					this.thumbColor = COL_BORDER;
					this.trackColor = BG_CARD;}
				});
			scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
				@Override
				protected void configureScrollBarColors() {
					this.thumbColor = COL_BORDER;
					this.trackColor = BG_CARD;
					}
				});
		}
		
		protected void showResults(List<Exoplanet> results) {
		    updateResultCount(results.size());
		    resultsBody.removeAll();
		    JScrollPane scrollPane = new JScrollPane(buildResults(results));
		    styleScrollPane(scrollPane);

		    resultsBody.add(scrollPane);
		    centerLayout.show(centerPanel, "RESULTS");

		    resultsBody.revalidate();
		    resultsBody.repaint();
		    }
		
		public void show() {
			window.setVisible(true);
		}
	

	}