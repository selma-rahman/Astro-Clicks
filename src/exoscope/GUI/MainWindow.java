package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.io.InputStream;
import java.awt.event.*;
import logic.QueryEngine;
import model.Exoplanet;

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
	
	private String currentSearchType = "PLANET NAME"; // default search
	
	private Font pixelFont; // large
	public Font pixelFontSm; // medium
	public static Font pixelFontXs; // small
	
	protected JFrame window;
	protected JPanel sidebar;
	
	private JPanel main;
	private JPanel infoHomePanel;
	private JPanel homePanel; // for homepagee
	private JPanel infoDetailPanel;
	
	private JLabel detailTitleLabel;
	private JTextPane detailTextArea;
	private JLabel detailImageLabel;
	private JLabel searchPromptLabel;
	
	protected boolean infoMode = false;
	
	JPanel centerPanel; // removed private for graphics
	CardLayout centerLayout; // removed private for graphics
	
//	private JPanel cards;
//	private CardLayout cardLayout;
	
	
	//private JLabel resultsCountLabel = new JLabel("0");;
	private JLabel resultsCountLabel; //make it direct
	private JPanel resultsBody; 
	
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

	    homePanel = buildHomePanel(); // to build a seprate home panell
	    main = buildMain(this.planets);
	    infoHomePanel = buildInfoHome();
	    infoDetailPanel = buildInfoDetail();

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
		
		sidebar.add(sidebarSection("// TO NAVIGATE"));
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
	    
	    JButton homeButton = retroButton("HOME");
	    homeButton.addActionListener(e ->
	        centerLayout.show(centerPanel, "HOME")
	    );

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
	    
	    
	    JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
	    leftButtons.setOpaque(false);

	    leftButtons.add(backButton);
	    leftButtons.add(homeButton);

	    top.add(leftButtons, BorderLayout.WEST); // homebutton and backbutton
	    top.add(title, BorderLayout.CENTER);
	    top.add(rightTop, BorderLayout.EAST);

	   // top.add(backButton, BorderLayout.WEST);
	    //top.add(title, BorderLayout.CENTER);
	    //top.add(rightTop, BorderLayout.EAST);

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
	    detailTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    top.add(detailTitleLabel, BorderLayout.CENTER);
	    top.add(backButton, BorderLayout.WEST);
	    Dimension btnSize = backButton.getPreferredSize();
	    top.add(Box.createRigidArea(btnSize), BorderLayout.EAST);

	    detailImageLabel = new JLabel();
	    detailImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

	    
	    detailTextArea = new JTextPane();
	    SimpleAttributeSet attributes = new SimpleAttributeSet();
	    StyleConstants.setLineSpacing(attributes,0.7f);
	    detailTextArea.setParagraphAttributes(attributes, false);
	    detailTextArea.setEditable(false);
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
	    		
//	    		// Detect if "METHOD" is selected, and update the search bar
//	            if ("METHOD".equals(text)) {
//	                updateSearchBarWithDropdown();  // Switch to dropdown
//	            } else {
//	                updateSearchBarWithTextField();  // Keep default text field for others
//	            }
	    		
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

	    //JPanel resultsBody = new JPanel();
	    resultsBody = new JPanel();
	    resultsBody.setLayout(new BoxLayout(resultsBody, BoxLayout.Y_AXIS));
	    resultsBody.setOpaque(false);

	    resultsSection.add(buildResultsHeader());
	    resultsSection.add(Box.createVerticalStrut(6));
	    resultsSection.add(resultsBody);

//	    main.add(buildSearchBar());
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
	
	// Modify the buildSearchBar method to accept either JTextField or JComboBox
//	private JPanel buildSearchBar(JComponent component) {
//	    JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
//
//	    // Set the preferred size for the component (dropdown or text field)
//	    component.setPreferredSize(new Dimension(200, 30));  // Adjust the size as necessary
//
//	    bar.add(component);
//
//	    JButton searchButton = new JButton("SEARCH");
//	    searchButton.setFont(pixelFontXs);
//	    searchButton.setBackground(COL_ACCENT); 
//	    searchButton.setForeground(BG_BASE);
//	    searchButton.setOpaque(true);
//	    searchButton.setBorderPainted(false);
//
//	    searchButton.addActionListener(e -> {
//	        String query = ((JTextField)component).getText();  // Handle textfield-based search
//	        performSearch(query);
//	    });
//
//	    bar.setBackground(BG_PANEL);
//	    bar.setBorder(BorderFactory.createLineBorder(COL_BORDER, 3));
//	    bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));  // Ensure enough space for the component
//
//	    return bar;
//	}

	
	
	

	
	// Example method to perform search (for illustration purposes)
//	private void performSearch(String query) {
//	    // Logic to perform search based on the query (could be dropdown or text field)
//	    System.out.println("Searching for: " + query);
//	}
	
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
	
	protected JPanel buildResultsHeader() {
		JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(BG_BASE);
        hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        hdr.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0x1a, 0x1a, 0x4a)));

        JLabel left = new JLabel("\u25BC RESULTS");
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
		/*
				String strP = i.toString();
				panel.add(buildPlanetRow(strP));
				panel.add(Box.createVerticalStrut(5));
			}
			if (p.size() == 0) {
				panel = noResultsFound();
			}
			return panel;
		}
		*/
	
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
		    startButton.addActionListener(e -> centerLayout.show(centerPanel, "PLANET_PANEL"));

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
							((PlanetDetailPanel) c).show(planet);
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

		/* making a new row so that only the name is click-able label rows
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
		*/
		
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
		
		/*public void updateResultCount(int count) {
			this.resultsCountLabel.setText(String.valueOf(count));
		}*/
		
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
		    resultsBody.revalidate();
		    resultsBody.repaint();
		}
		

		public void show() {
			window.setVisible(true);
		}
		
		
		
		

	}
