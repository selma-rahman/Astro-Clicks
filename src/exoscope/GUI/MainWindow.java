package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.InputStream;
import java.awt.event.*;

public class MainWindow {
	// color palette, hex codes
	// BG = background
	// COL = foreground/accent colors
	private static final Color BG_BASE = new Color(0x05, 0x05, 0x10); // black navy ish
	private static final Color BG_PANEL = new Color(0x07, 0x07, 0x18); // lighter, for sidebar and cards
	private static final Color BG_CARD = new Color(0x0a, 0x0a, 0x2a); // active or highlight stuff
	private static final Color COL_BORDER = new Color(0x3a, 0x3a, 0xff); // bright blue/purple for borders
	private static final Color COL_TEXT = new Color(0xe0, 0xe0, 0xff); // softer white/blue for main text color
	private static final Color COL_MUTED = new Color(0x5a, 0x5a, 0x9a); // dimmer purple/grey for inactive items
	private static final Color COL_ACCENT = new Color(0x7b, 0x7b, 0xff); // blue/purple for titles and things to be highlights
	private static final Color COL_GREEN = new Color(0x00, 0xff, 0x88); // neon green, for the result count
	
	private Font pixelFont; // large
	private Font pixelFontSm; // medium
	private Font pixelFontXs; // small
	
	private JFrame window;
	
	public MainWindow() {
		loadFont();
		buildWindow();
	}
	
	// loads the retroyy pixel font from the font folder/ still need to put in
	// defaults to plain monospaced if the file isn't found
	
	private void loadFont() {
		try {
			InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P.ttf"); // REMEMBER TO DOWNLOAD THIS FONT
			if ( is != null) {
				Font base = Font.createFont(Font.TRUETYPE_FONT, is);
				GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
				pixelFont = base.deriveFont(10f);
				pixelFontSm = base.deriveFont(7f);
				pixelFontXs = base.deriveFont(5f);
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
	
	private void buildWindow() {
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
		root.setBackground(BG_BASE);;
		root.setBorder(BorderFactory.createLineBorder(COL_BORDER, 3));
		root.add(buildTitleBar(), BorderLayout.NORTH);
		root.add(buildSidebar(), BorderLayout.WEST);
		root.add(buildMain(), BorderLayout.CENTER);
		
		window.setContentPane(root);;
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
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setBackground(BG_PANEL);
		sidebar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, COL_BORDER), BorderFactory.createEmptyBorder(14, 10, 14, 10)));
		sidebar.setPreferredSize(new Dimension(200, 0));
		
		sidebar.add(sidebarSection("// SEARCH BY"));
		sidebar.add(sidebarItem("PLANET NAME" , true));
		sidebar.add(sidebarItem("HOST STAR", false));
		sidebar.add(sidebarItem("METHOD", false));
		sidebar.add(sidebarItem("RADIUS / MASS", false));
		sidebar.add(sidebarItem("ORBIT PERIOD", false));
		sidebar.add(sidebarItem("YEAR", false));
		sidebar.add(Box.createVerticalStrut(6));
		sidebar.add(sidebarSection("// INFO"));
		sidebar.add(sidebarItem("DISC. METHODS", false));
		
		return sidebar;
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
	private JPanel buildMain() {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		main.setBackground(BG_BASE);
		main.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		main.setOpaque(false); // let the star field show through
		
		main.add(buildSearchBar());
		main.add(Box.createVerticalStrut(12));
		main.add(buildStatCards());
		main.add(Box.createVerticalStrut(12));
		main.add(Box.createVerticalStrut(8));
		main.add(buildResultsHeader());
		main.add(buildPlaceholderResults());
		
		return main;
	}
	
	// search bar top of main area
	// need to replace this jtextfield 
	private JPanel buildSearchBar() {
		JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
		bar.setBackground(BG_PANEL);
		bar.setBorder(BorderFactory.createLineBorder(COL_BORDER, 3));
		bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		
		JLabel prompt = new JLabel("?");
		prompt.setFont(pixelFontSm);
		prompt.setForeground(COL_BORDER);
		
		JLabel text = new JLabel("ENTER PLANET NAME:");
		text.setFont(pixelFontXs);
		text.setForeground(COL_MUTED);
		
		bar.add(prompt);
		bar.add(text);
		return bar;
	}
	
	private JPanel buildStatCards() {
		// 1 row 3 columns 8px horizontal gap, 0 vertical gap
		JPanel row = new JPanel(new GridLayout(1, 3, 8, 0));
		row.setBackground(BG_BASE);;
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		
		row.add(statCard("PLANETS LOADED", "...", COL_TEXT));
		row.add(statCard("RESULTS FOUND", "...", COL_GREEN));
		row.add(statCard("PAGE", "...", COL_TEXT));
		
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
	
	private JPanel buildResultsHeader() {
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
	
	// placeholder rows for now
		private JPanel buildPlaceholderResults() {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBackground(BG_BASE);

			String[] placeholders = {
				"super mario vroom  |  weeee  |  2016",
				"princess peach  |  eweeee  |  2016",
				"big bowser boom  |  weee |  2016",
			};

			for (String p : placeholders) {
				panel.add(buildPlanetRow(p));
				panel.add(Box.createVerticalStrut(5));
			}
			return panel;
		}

		// a single result row
		private JPanel buildPlanetRow(String text) {
			JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
			row.setBackground(BG_PANEL);
			row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
			row.setBorder(BorderFactory.createLineBorder(new Color(0x1a, 0x1a, 0x4a), 2));

			JLabel star = new JLabel("\u2605");
			star.setFont(pixelFontSm);
			star.setForeground(COL_ACCENT);

			JLabel label = new JLabel(text);
			label.setFont(pixelFontXs);
			label.setForeground(COL_TEXT);

			row.add(star);
			row.add(label);
			return row;
		}

		public void show() {
			window.setVisible(true);
		}

	}
	
	/*

	private JFrame window;
	
	public MainWindow() {
		window = new JFrame();
		window.setTitle("Welcome to Exoscope");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(800,500);
		window.setLocationRelativeTo(null);
		window.setLayout(new BorderLayout());
				
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		panel1.setBackground(Color.PINK);
				
		panel1.setPreferredSize(new Dimension(200,200));
		
		JLabel label = new JLabel("Begin");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Serif", Font.BOLD, 36));

		
		window.add(panel1, BorderLayout.SOUTH);
		panel1.add(label, BorderLayout.NORTH);
		
		JButton button2 = new JButton("Update Label Text");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText("End");
			}
		});
		
		panel1.add(button2);
		
	}
	
	public void show() {
		window.setVisible(true);
	}
	*/

