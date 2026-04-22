package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.io.InputStream;
import java.awt.event.*;
import logic.QueryEngine;
import model.Exoplanet;
import java.util.List;
import java.awt.Desktop;
import java.net.URI;

public class MainWindow{
	// color palette, hex codes
	// BG = background
	// COL = foreground/accent colors
	
	public static final Color BG_BASE = new Color(0x05, 0x05, 0x10); // black navy ish
	public static final Color BG_PANEL = new Color(0x07, 0x07, 0x18); // lighter, for sidebar and cards
	private static final Color BG_CARD = new Color(0x0a, 0x0a, 0x2a); // active or highlight stuff
	public static final Color COL_BORDER = new Color(0x3a, 0x3a, 0xff); // bright blue/purple for borders
	public static final Color COL_TEXT = new Color(0xe0, 0xe0, 0xff); // softer white/blue for main text color
	public static final Color COL_MUTED = new Color(0x5a, 0x5a, 0x9a); // dimmer purple/grey for inactive items
	public static final Color COL_ACCENT = new Color(0x7b, 0x7b, 0xff); // blue/purple for titles and things to be highlights
	private static final Color COL_GREEN = new Color(0x00, 0xff, 0x88); // neon green, for the result count
	
	private String currentSearchType = "PLANET NAME"; // default search
	
	private Font pixelFont; // large
	public Font pixelFontSm; // medium
	public static Font pixelFontXs; // small
	
	private JFrame window;
	private JPanel sidebar;
	
	private JPanel main;
	private JPanel infoHomePanel;
	private JPanel infoDetailPanel;
	
	private JLabel detailTitleLabel;
	private JTextArea detailTextArea;
	private JLabel detailImageLabel;
	private JLabel searchPromptLabel;
	
	private boolean infoMode = false;
	
	private JPanel centerPanel;
	private CardLayout centerLayout;
	
	private JPanel cards;
	private CardLayout cardLayout;
	
	
	private JLabel resultsCountLabel = new JLabel("0");;
	
	private List<Exoplanet> planets;
	
	public MainWindow(List<Exoplanet> planets) {
		this.planets = planets;
		loadFont();
		buildWindow();
	}
	
	// loads the retroyy pixel font from the font folder/ still need to put in
	// defaults to plain monospaced if the file isn't found
	
	private void loadFont() {
		try {
			InputStream is = getClass().getResourceAsStream("fonts/PressStart2P.ttf"); // REMEMBER TO DOWNLOAD THIS FONT
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
		
		//okay panel to paint the star background
		JPanel root = new JPanel(new BorderLayout()) {
			// paintCompoenent called by swing whenever the panel needs to redraw
			@Override
			protected void paintComponent(Graphics g) {
			    super.paintComponent(g);
			    g.setColor(BG_BASE);
			    g.fillRect(0, 0, getWidth(), getHeight());

			    // regular stars — more of them, brighter
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

	    main = buildMain(this.planets);
	    infoHomePanel = buildInfoHome();
	    infoDetailPanel = buildInfoDetail();

		
		RadiusSearchWindow radiusSearch = new RadiusSearchWindow(this, planets);
		MassSearchWindow massSearch = new MassSearchWindow(this, planets);
		OrbitSearchWindow orbitSearch = new OrbitSearchWindow(this, planets);
		
		
		centerPanel.add(main, "MAIN");
		centerPanel.add(infoHomePanel, "INFO_HOME");
		centerPanel.add(infoDetailPanel,"INFO_DETAIL");
		centerPanel.add(radiusSearch, "RADIUS_PANEL");
		centerPanel.add(massSearch, "MASS_PANEL");
		centerPanel.add(orbitSearch, "ORBIT_PANEL");
		    
		
			
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
	private JPanel buildSidebar() {
		//JPanel sidebar = new JPanel();
		sidebar = new JPanel();
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setBackground(BG_PANEL);
		sidebar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, COL_BORDER), BorderFactory.createEmptyBorder(14, 10, 14, 10)));
		sidebar.setPreferredSize(new Dimension(200, 0));
		
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

	    JLabel arrow = new JLabel(active ? "\u25BA" : "  ");
	    arrow.setFont(pixelFontXs);
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
	
	private JPanel buildInfo() {
	    JPanel mainInfo = new JPanel();
	    mainInfo.setLayout(new BoxLayout(mainInfo, BoxLayout.Y_AXIS));
	    mainInfo.setBackground(BG_BASE);
	    mainInfo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
	    mainInfo.setOpaque(false);

	    JLabel title = new JLabel("EXOSCOPE INFO");
	    title.setFont(pixelFontSm);
	    title.setForeground(COL_ACCENT);
	    title.setAlignmentX(Component.LEFT_ALIGNMENT);

	    JTextArea textArea = new JTextArea();
	    textArea.setText(
	        "Welcome to EXOSCOPE.\n\n" +
	        "This program allows you to search exoplanets by:\n" +
	        "- Planet Name\n" +
	        "- Host Star\n" +
	        "- Discovery Method\n" +
	        "- Radius / Mass\n" +
	        "- Orbit Period\n" +
	        "- Year\n\n" +
	        "Use the sidebar to switch search modes."
	    );
	    textArea.setFont(pixelFontXs);
	    textArea.setForeground(COL_TEXT);
	    textArea.setBackground(BG_PANEL);
	    textArea.setCaretColor(COL_TEXT);
	    textArea.setEditable(false);
	    textArea.setLineWrap(true);
	    textArea.setWrapStyleWord(true);
	    textArea.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(COL_BORDER, 2),
	        BorderFactory.createEmptyBorder(10, 10, 10, 10)
	    ));
	    textArea.setAlignmentX(Component.LEFT_ALIGNMENT);

	    mainInfo.add(title);
	    mainInfo.add(Box.createVerticalStrut(10));
	    mainInfo.add(textArea);

	    return mainInfo;
	}
	
	private JPanel buildInfoHome() {
	    JPanel page = new JPanel(new BorderLayout());
	    page.setBackground(BG_BASE);
	    page.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    page.setOpaque(false);

	    JPanel top = new JPanel(new BorderLayout());
	    top.setOpaque(false);

	    JButton backButton = retroButton("<< back to search");
	    backButton.addActionListener(e -> {
	        infoMode = false;
	        centerLayout.show(centerPanel, "MAIN");

	        window.remove(sidebar);
	        sidebar = buildSidebar();
	        window.add(sidebar, BorderLayout.WEST);

	        window.revalidate();
	        window.repaint();
	    });

	    JLabel title = new JLabel("DISCOVERY METHODS", SwingConstants.CENTER);
	    title.setFont(pixelFontSm);
	    title.setForeground(COL_ACCENT);

	    JPanel rightTop = new JPanel();
	    rightTop.setOpaque(false);
	    rightTop.setLayout(new BoxLayout(rightTop, BoxLayout.Y_AXIS));

	    JLabel infoLabel = new JLabel("info found on");
	    infoLabel.setFont(pixelFontXs);
	    infoLabel.setForeground(COL_TEXT);

	    JLabel nasaLink = new JLabel("<html><u>NASA.gov</u></html>");
	    nasaLink.setFont(pixelFontXs);
	    nasaLink.setForeground(COL_ACCENT);
	    nasaLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    nasaLink.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            openLink("https://science.nasa.gov/exoplanets/how-we-find-and-characterize/");
	        }
	    });

	    rightTop.add(infoLabel);
	    rightTop.add(nasaLink);

	    top.add(backButton, BorderLayout.WEST);
	    top.add(title, BorderLayout.CENTER);
	    top.add(rightTop, BorderLayout.EAST);

	    JPanel grid = new JPanel(new GridLayout(2, 3, 24, 24));
	    grid.setOpaque(false);
	    grid.setBorder(BorderFactory.createEmptyBorder(30, 40, 0, 40));

	    grid.add(createMethodCard(
	        "RADIAL\nVELOCITY",
	        "images/radial_velocity.png",
	        "Radial Velocity",
	        "Radial velocity detects exoplanets by measuring the tiny wobble of a star caused by the gravitational pull of an orbiting planet. Scientists observe shifts in the star's spectrum due to the Doppler effect."
	    ));

	    grid.add(createMethodCard(
	        "TRANSITS",
	        "images/transits.png",
	        "Transits",
	        "The transit method detects planets when they pass in front of their host star from our point of view. This causes a slight dip in the star's brightness. Repeated dips can reveal a planet's size and orbit."
	    ));

	    grid.add(createMethodCard(
	        "GRAVITATIONAL\nMICROLENSING",
	        "images/microlensing.png",
	        "Gravitational Microlensing",
	        "Microlensing happens when a foreground star bends and magnifies the light of a background star. A planet orbiting the foreground star can create an extra signal in that magnification pattern."
	    ));

	    grid.add(createMethodCard(
	        "DIRECT\nIMAGING",
	        "images/direct_imaging.png",
	        "Direct Imaging",
	        "Direct imaging captures actual pictures of exoplanets by blocking out the bright light from their host stars. This is most effective for large planets far from their stars."
	    ));

	    grid.add(createMethodCard(
	        "CORONAGRAPH",
	        "images/coronagraph.png",
	        "Coronagraph",
	        "A coronagraph is an instrument used to block a star's light so nearby faint objects, like exoplanets, can be observed more clearly."
	    ));

	    grid.add(createMethodCard(
	        "STARSHADE",
	        "images/starshade.png",
	        "Starshade",
	        "A starshade is a large spacecraft positioned far from a telescope to block starlight before it enters the telescope, helping scientists directly observe exoplanets."
	    ));

	    page.add(top, BorderLayout.NORTH);
	    page.add(grid, BorderLayout.CENTER);

	    return page;
	}
	
	private JPanel createMethodCard(String displayTitle, String imagePath, String detailTitle, String detailText) {
	    JPanel card = new JPanel();
	    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
	    card.setBackground(BG_PANEL);
	    card.setBorder(BorderFactory.createLineBorder(COL_BORDER, 2));
	    card.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    JLabel title = new JLabel("<html>" + displayTitle.replace("\n", "<br>") + "</html>");
	    title.setFont(pixelFontSm);
	    title.setForeground(COL_ACCENT);
	    title.setAlignmentX(Component.CENTER_ALIGNMENT);
	    title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	    JLabel imageLabel = buildCardImageLabel(imagePath);
	    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    card.add(Box.createVerticalStrut(10));
	    card.add(title);
	    card.add(Box.createVerticalGlue());
	    card.add(imageLabel);
	    card.add(Box.createVerticalStrut(20));

	    MouseAdapter clicker = new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            showMethodDetail(detailTitle, detailText, imagePath);
	        }

	        @Override
	        public void mouseEntered(MouseEvent e) {
	            card.setBorder(BorderFactory.createLineBorder(COL_ACCENT, 3));
	            card.setBackground(BG_CARD);
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            card.setBorder(BorderFactory.createLineBorder(COL_BORDER, 2));
	            card.setBackground(BG_PANEL);
	        }
	    };

	    card.addMouseListener(clicker);
	    title.addMouseListener(clicker);
	    imageLabel.addMouseListener(clicker);

	    return card;
	}
	
	private JLabel buildCardImageLabel(String imagePath) {
	    java.net.URL url = getClass().getResource(imagePath);

	    if (url != null) {
	        ImageIcon icon = new ImageIcon(url);
	        Image scaled = icon.getImage().getScaledInstance(110, 80, Image.SCALE_SMOOTH);
	        return new JLabel(new ImageIcon(scaled));
	    }

	    JLabel placeholder = new JLabel("image");
	    placeholder.setPreferredSize(new Dimension(110, 80));
	    placeholder.setMaximumSize(new Dimension(110, 80));
	    placeholder.setHorizontalAlignment(SwingConstants.CENTER);
	    placeholder.setVerticalAlignment(SwingConstants.CENTER);
	    placeholder.setFont(pixelFontXs);
	    placeholder.setForeground(COL_TEXT);
	    placeholder.setBorder(BorderFactory.createLineBorder(new Color(0xff, 0x99, 0xdd), 2));
	    return placeholder;
	}
	
	private JPanel buildInfoDetail() {
	    JPanel page = new JPanel(new BorderLayout());
	    page.setBackground(BG_BASE);
	    page.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    page.setOpaque(false);

	    JButton backButton = retroButton("<< back to methods");
	    backButton.addActionListener(e -> centerLayout.show(centerPanel, "INFO_HOME"));

	    detailTitleLabel = new JLabel("METHOD TITLE");
	    detailTitleLabel.setFont(pixelFontSm);
	    detailTitleLabel.setForeground(COL_ACCENT);

	    JPanel top = new JPanel(new BorderLayout());
	    top.setOpaque(false);
	    top.add(backButton, BorderLayout.WEST);
	    top.add(detailTitleLabel, BorderLayout.CENTER);

	    detailImageLabel = new JLabel();
	    detailImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

	    detailTextArea = new JTextArea();
	    detailTextArea.setEditable(false);
	    detailTextArea.setLineWrap(true);
	    detailTextArea.setWrapStyleWord(true);
	    detailTextArea.setFont(pixelFontXs);
	    detailTextArea.setForeground(COL_TEXT);
	    detailTextArea.setBackground(BG_PANEL);
	    detailTextArea.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(COL_BORDER, 2),
	        BorderFactory.createEmptyBorder(12, 12, 12, 12)
	    ));

	    JPanel center = new JPanel();
	    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
	    center.setOpaque(false);

	    detailImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    detailTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);

	    center.add(Box.createVerticalStrut(20));
	    center.add(detailImageLabel);
	    center.add(Box.createVerticalStrut(20));
	    center.add(detailTextArea);

	    page.add(top, BorderLayout.NORTH);
	    page.add(center, BorderLayout.CENTER);

	    return page;
	}
	
	private void showMethodDetail(String title, String text, String imagePath) {
	    detailTitleLabel.setText(title);
	    detailTextArea.setText(text);

	    java.net.URL url = getClass().getResource(imagePath);
	    if (url != null) {
	        ImageIcon icon = new ImageIcon(url);
	        Image scaled = icon.getImage().getScaledInstance(240, 180, Image.SCALE_SMOOTH);
	        detailImageLabel.setIcon(new ImageIcon(scaled));
	        detailImageLabel.setText("");
	    } else {
	        detailImageLabel.setIcon(null);
	        detailImageLabel.setText("[ image not found ]");
	        detailImageLabel.setForeground(COL_TEXT);
	        detailImageLabel.setFont(pixelFontSm);
	    }

	    centerLayout.show(centerPanel, "INFO_DETAIL");
	}
	
	private JButton retroButton(String text) {
	    JButton btn = new JButton(text);
	    btn.setFont(pixelFontXs);
	    btn.setForeground(COL_TEXT);
	    btn.setBackground(BG_CARD);
	    btn.setFocusPainted(false);
	    btn.setBorder(BorderFactory.createLineBorder(COL_BORDER, 2));
	    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    return btn;
	}
	
	private void openLink(String url) {
	    try {
	        if (Desktop.isDesktopSupported()) {
	            Desktop.getDesktop().browse(new URI(url));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
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

	    JLabel arrow = new JLabel(active ? "\u25BA" : "  ");
	    arrow.setFont(pixelFontXs);
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
	    		
	    		if (text.equals("RADIUS")) {
	    			centerLayout.show(centerPanel, "RADIUS_PANEL");
	    		} else if (text.equals("MASS")){
	    			centerLayout.show(centerPanel,  "MASS_PANEL");
	    		} else if (text.equals("ORBIT PERIOD")) {
	    			centerLayout.show(centerPanel,  "ORBIT_PANEL");
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

	    JPanel resultsBody = new JPanel();
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
						break;
					case "HOST STAR":
						results = qe.filterByHostStar(query);
						break;
					case "METHOD":
						results = qe.filterByDiscoveryMethod(query);
						break;
					case "RADIUS":
						String[] parts = query.split(",");
						double min = Double.parseDouble(parts[0].trim());
						double max = Double.parseDouble(parts[1].trim());
						results = qe.filterByRadius(min, max); // or mass depending on implementation
						break;
					case "MASS":
						String[] parts2 = query.split(", ");
						double min2 = Double.parseDouble(parts2[0].trim());
						double max2 = Double.parseDouble(parts2[1].trim());
						results = qe.filterByMass(min2, max2);
						break;
					case "ORBIT PERIOD":
						parts = query.split(",");
						min = Double.parseDouble(parts[0].trim());
						max = Double.parseDouble(parts[1].trim());
						results = qe.filterByOrbitalPeriod(min, max);
						break;
					case "YEAR":
						int year = Integer.parseInt(query.trim());
						results = qe.filterByYear(year);
						break;
					default:
						results = qe.filterByName(query);
						}
				// update results count
					resultsCountLabel.setText(Integer.toString(results.size()));
					rc.removeAll();
					JScrollPane scrollPane = new JScrollPane(buildResults(results));
					styleScrollPane(scrollPane);

					rc.add(scrollPane);
					rc.revalidate();
					rc.repaint(); }
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
		bar.add(searchPromptLabel); // this ispdated label
		bar.add(field);
		bar.add(searchButton);

		return bar;}
	
	
	public JPanel buildStatCards(List<Exoplanet> planets) {
		// 1 row 3 columns 8px horizontal gap, 0 vertical gap
		JPanel row = new JPanel(new GridLayout(1, 3, 8, 0));
		row.setBackground(BG_BASE);;
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		
		
		
		// PLANETS LOADED stays number
		row.add(statCard("PLANETS LOADED", Integer.toString(planets.size()), COL_TEXT));

		// RESULTS FOUND → placeholder
		JPanel resultsCard = statCard("RESULTS FOUND", "...", COL_GREEN);
		this.resultsCountLabel = (JLabel) resultsCard.getComponent(1);
		row.add(resultsCard);

		// PAGE
		row.add(statCard("PAGE", "67", COL_TEXT));		
		
		return row;
	}
	
	// single stat box
	private JPanel statCard(String label, String value, Color valueColor) {
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
	
	public JPanel buildResultsHeader() {
		JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(BG_BASE);
        hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        hdr.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0x1a, 0x1a, 0x4a)));

        JLabel left = new JLabel("\u25BC RESULTS");
        left.setFont(pixelFontXs);
        left.setForeground(COL_BORDER);

        JLabel right = new JLabel("placeholder");
        right.setFont(pixelFontXs);
        right.setForeground(COL_ACCENT);

        hdr.add(left, BorderLayout.WEST);
        hdr.add(right, BorderLayout.EAST);
        return hdr;
	}
	
		public JPanel buildResults(List<Exoplanet> p) {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBackground(BG_BASE);
			panel.setOpaque(false);

			for (Exoplanet i : p) {
				String strP = i.toString();
				panel.add(buildPlanetRow(strP));
				panel.add(Box.createVerticalStrut(5));
			}
			if (p.size() == 0) {
				panel = noResultsFound();
			}
			
			return panel;
		}

		// a single result row
		private JPanel buildPlanetRow(String text) {
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
			row.setBackground(BG_PANEL);
			row.setOpaque(false);
			row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
			row.setBorder(BorderFactory.createLineBorder(new Color(0x1a, 0x1a, 0x4a), 2));

			JLabel star = new JLabel("\u2605");
			star.setFont(pixelFontSm);
			star.setForeground(COL_ACCENT);

			JLabel label = new JLabel(text);
			label.setFont(pixelFontXs);
			label.setForeground(COL_TEXT);

			row.add(star);
			row.add(Box.createHorizontalStrut(5));
			row.add(label);
			return row;
		}
		
		private JPanel noResultsFound() {
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
		
		public void updateResultCount(int count) {
			this.resultsCountLabel.setText(String.valueOf(count));
		}
		
		public void styleScrollPane(JScrollPane scrollPane) {
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

		public void show() {
			window.setVisible(true);
		}
		
		

	}
