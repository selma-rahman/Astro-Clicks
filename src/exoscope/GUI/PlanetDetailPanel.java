package GUI;

import model.Exoplanet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlanetDetailPanel extends JPanel {
	private MainWindow main;
	
	// label updated when show() gets called
	private JLabel nameLabel;
	private JLabel hostStarLabel;
	private JLabel yearLabel;
	private JLabel methodLabel;
	private JLabel radiusLabel;
	private JLabel massLabel;
	private JLabel orbitLabel;
	private JLabel distanceLabel;
	
	private String key;
	
	// canvas to draw the planet graphic
	private PlanetCanvas planetCanvas;
	
	public PlanetDetailPanel(MainWindow main) {
		this.main = main;
		setOpaque(false);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		buildLayout();
	}
	
	private void buildLayout() {
		//back button at top
		JButton backBtn = main.retroButton("<< back to results");
		backBtn.addActionListener(e -> {	
    		if (key.equals("HOME")) {
    		    main.centerLayout.show(main.centerPanel, "HOME");

    		} else if (key.equals("PLANET NAME")) {
    			main.centerLayout.show(main.centerPanel, "PLANET_PANEL");

    		} else if (key.equals("RADIUS")) {
    		    main.centerLayout.show(main.centerPanel, "RADIUS_PANEL");

    		} else if (key.equals("MASS")) {
    		    main.centerLayout.show(main.centerPanel, "MASS_PANEL");

    		} else if (key.equals("ORBIT PERIOD")) {
    		    main.centerLayout.show(main.centerPanel, "ORBIT_PANEL");

    		} else if (key.equals("HOST STAR")) {
    		    main.centerLayout.show(main.centerPanel, "HOST_PANEL");
    		} else if (key.equals("METHOD")) {
    		    main.centerLayout.show(main.centerPanel, "METHOD_PANEL");
    		} else if (key.equals("YEAR")) {
    		    main.centerLayout.show(main.centerPanel, "YEAR_PANEL");
    		} else {
    		    main.centerLayout.show(main.centerPanel, "MAIN");
    		}
    	
    	// to refresh sidebar 
    	main.window.remove(main.sidebar);
    	main.sidebar = main.buildSidebar();
    	main.window.add(main.sidebar, BorderLayout.WEST);
    	main.window.revalidate();
    	main.window.repaint();
    	
		});

		
		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.setOpaque(false);
		top.add(backBtn);
		
		// left side for the planet graphic
		planetCanvas = new PlanetCanvas();
		planetCanvas.setPreferredSize(new Dimension(320, 320));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setOpaque(false);
		leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
		leftPanel.add(planetCanvas);
		
		// right side for the planet info fields
		nameLabel = styledLabel("...");
		hostStarLabel = styledLabel("...");
		yearLabel = styledLabel("...");
		methodLabel = styledLabel("...");
		radiusLabel = styledLabel("...");
		massLabel = styledLabel("...");
		orbitLabel = styledLabel("...");
		distanceLabel = styledLabel("...");
		
		
	    JLabel littleEarthSymbol = new JLabel("⊕");
	    littleEarthSymbol.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		littleEarthSymbol.setForeground(MainWindow.COL_TEXT);
		
		// duplicate symbol bc it can't get reused for some reason????
		JLabel littleEarthSymbol2 = new JLabel("⊕");
		littleEarthSymbol2.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
		littleEarthSymbol2.setForeground(MainWindow.COL_TEXT);
		
		
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setOpaque(false);
		
		infoPanel.add(fieldRow("NAME",  nameLabel));
		infoPanel.add(Box.createVerticalStrut(12));
		infoPanel.add(fieldRow("HOST STAR",  hostStarLabel));
		infoPanel.add(Box.createVerticalStrut(12));
		infoPanel.add(fieldRow("DISCOVERY YEAR",  yearLabel));
		infoPanel.add(Box.createVerticalStrut(12));
		infoPanel.add(fieldRow("DISC. METHOD", methodLabel));
		infoPanel.add(Box.createVerticalStrut(12));
		infoPanel.add(massradiusRow("RADIUS (EARTH=1)", radiusLabel,littleEarthSymbol));
		infoPanel.add(Box.createVerticalStrut(12));
		infoPanel.add(massradiusRow("MASS (EARTH=1)", massLabel, littleEarthSymbol2));
		infoPanel.add(Box.createVerticalStrut(12));
		infoPanel.add(fieldRow("ORBITAL PERIOD", orbitLabel));
		infoPanel.add(Box.createVerticalStrut(12));
		infoPanel.add(fieldRow("DISTANCE (PC)", distanceLabel));
		
		JPanel center = new JPanel(new BorderLayout());
		center.setOpaque(false);
		center.add(leftPanel, BorderLayout.WEST);
		center.add(infoPanel, BorderLayout.CENTER);
		
		JPanel south = new JPanel();
		south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
		south.setOpaque(false);
		south.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));;
		south.add(buildUnitsGuide());
		
		add(top, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);	
		add(south, BorderLayout.SOUTH);
	}
	
	// box for info on the units and such
	private JPanel buildUnitsGuide() {
		JPanel guide = new JPanel();
		guide.setLayout(new BoxLayout(guide, BoxLayout.Y_AXIS));
		guide.setBackground(MainWindow.BG_PANEL);
		guide.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MainWindow.COL_BORDER, 1), BorderFactory.createEmptyBorder(10, 14, 10, 14)));
		JLabel title = new JLabel("// UNITS GUIDE");
		title.setFont(main.pixelFontXs);
		title.setForeground(MainWindow.COL_BORDER);
		title.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel r = new JLabel("<html>R<span style='font-family:Arial Unicode MS'>⊕</span> = radius relative to Earth &nbsp;(1 R<span style='font-family:Arial Unicode MS'>⊕</span> = 6,371 km)</html>");
		JLabel m = new JLabel("<html>M<span style='font-family:Arial Unicode MS'>⊕</span> = mass relative to Earth &nbsp;(1 M<span style='font-family:Arial Unicode MS'>⊕</span> = 5.97 × 10²⁴ kg)</html>");
		JLabel d = new JLabel("pc = parsecs (1 pc = 3.26 light years = ~19 trillion miles)");
		JLabel o = new JLabel("days = length of one full orbit around the host star");
		
		for (JLabel l : new JLabel[] {r, m, d, o}) {
			l.setFont(main.pixelFontXs);
			l.setForeground(MainWindow.COL_MUTED);
			l.setAlignmentX(Component.LEFT_ALIGNMENT);
			l.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
		}
		
		guide.add(title);
		guide.add(Box.createVerticalStrut(6));
		guide.add(r);
		guide.add(m);
		guide.add(d);
		guide.add(o);
		
		return guide;
		}
	
	// build label/value pair row
	private JPanel fieldRow(String fieldName, JLabel valueLabel) {
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
		row.setOpaque(false);
		row.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,  0,  1,  0,  new Color(0x1a, 0x1a, 0x4a)),
				BorderFactory.createEmptyBorder(0, 0, 6, 0)));
		
		JLabel key = new JLabel(fieldName);
		key.setFont(main.pixelFontXs);
		key.setForeground(MainWindow.COL_BORDER);
		key.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		row.add(key);
		row.add(Box.createVerticalStrut(3));
		row.add(valueLabel);
		return row;
	}
	
	// build label/value/symbol pair row
		private JPanel massradiusRow(String fieldName, JLabel valueLabel, JLabel symbol) {
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
			row.setOpaque(false);
			row.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,  0,  1,  0,  new Color(0x1a, 0x1a, 0x4a)),
					BorderFactory.createEmptyBorder(0, 0, 6, 0)));
			
			JLabel key = new JLabel(fieldName);
			key.setFont(main.pixelFontXs);
			key.setForeground(MainWindow.COL_BORDER);
			key.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			
			 JPanel valueSymbolPair = new JPanel();
			 valueSymbolPair.setLayout(new BoxLayout(valueSymbolPair, BoxLayout.X_AXIS));
			 valueSymbolPair.setOpaque(false);
			 valueSymbolPair.setAlignmentX(Component.LEFT_ALIGNMENT);
			    
			 valueSymbolPair.add(valueLabel);
			 valueSymbolPair.add(symbol); 

			 row.add(key);
			 row.add(Box.createVerticalStrut(3));
			 row.add(valueSymbolPair);
			 
			return row;
		}
	
	
	
	private JLabel styledLabel(String text) {
		JLabel l = new JLabel(text);
		l.setFont(main.pixelFontXs);
		l.setForeground(MainWindow.COL_TEXT);
		return l;
	}
	
	// called when a planet name is clicked, updates all the fields and redraws the graphic 
	public void show(Exoplanet planet, String search) {
		
		this.key = search;
		
		nameLabel.setText(planet.getName());
		hostStarLabel.setText(planet.getHostStar());
		yearLabel.setText(planet.getYear() !=null  ? String.valueOf(planet.getYear()) : "unknown");
		methodLabel.setText(planet.getDiscoveryMethod());
		radiusLabel.setText(planet.getRadius() != null ? String.valueOf(planet.getRadius()) + " R" : "unknown");
		massLabel.setText(planet.getMass() != null ? String.valueOf(planet.getMass()) + " M" : "unknown");
		orbitLabel.setText(planet.getOrbitalPeriod() != null ? String.valueOf(planet.getOrbitalPeriod()) + " days" : "unknown");
		distanceLabel.setText(planet.getDistance() != null ? String.valueOf(planet.getDistance()) + " pc" : "unknown");
		
		// pass planet data to the canvas so it can draw a unique graphic
		planetCanvas.setPlanet(planet);	
	}
	
	// okay procedure to draw the planet graphic
	// draw a unique pixel art planet based on the planet's actual data
	// radius: planet size, discovery method, color palette
	// orbital period: ring presence, mass, number of surface details
	static class PlanetCanvas extends JPanel {
		
		private Exoplanet planet;
		
		public PlanetCanvas() {
			setOpaque(false);
		}
		
		public void setPlanet(Exoplanet p) {
			this.planet = p;
			repaint(); // trigger a redraw with the new planet
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (planet == null) return;
			
			Graphics2D g2 = (Graphics2D) g;
			// smooth circles for the (exception to the pixel rule bc looks better)
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			int cx = getWidth() / 2;
			int cy = getHeight() / 2;
			
			// planet size
			// earth radius = 1.0, Jupiter ~ 11, use  log scale so giant planet aren't huge
			double r = planet.getRadius() != null ? planet.getRadius() : 5.0;
			int planetRadius = (int) Math.max(40,  Math.min(120,  40 + Math.log(r + 1) * 28));
			
			// planet color, based on discovery method
			Color baseColor, highlightColor, shadowColor;
			String method = planet.getDiscoveryMethod() != null ? planet.getDiscoveryMethod(): "";
			switch (method.toLowerCase()) {
				case "transit":
					baseColor = new Color(0x00, 0x9f, 0xff); // blue
					highlightColor = new Color(0x66, 0xcc, 0xff);
					shadowColor = new Color(0x00, 0x3a, 0x80);
					break;
					
				case "radial velocity":
					baseColor = new Color(0xff, 0x45, 0x00); // orange
					highlightColor = new Color(0xff, 0xc2, 0x66);
					shadowColor = new Color(0x66, 0x1a, 0x00);
					break;
					
				case "imaging":
					baseColor = new Color(0x00, 0xff, 0x7f); //green
					highlightColor = new Color(0x66, 0xff, 0xb3);
					shadowColor = new Color(0x00, 0x66, 0x33);
					break;
					
				case "microlensing":
					baseColor = new Color(0xd4, 0x3a, 0x7a); // pink
					highlightColor = new Color(0xff, 0x88, 0xbb);
					shadowColor = new Color(0x7a, 0x1a, 0x3a);
					break;
					
				default:
					baseColor = new Color(0x7a, 0x3a, 0xd4); //purple
					highlightColor = new Color(0xbb, 0x88, 0xff);
					shadowColor = new Color(0x3a, 0x1a, 0x7a);
					break;
			}
			
			// rings, shown if orbital period > 100 days
			boolean hasRings = planet.getOrbitalPeriod() != null && planet.getOrbitalPeriod() > 100;
			if (hasRings) {
				g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 80));
				g2.setStroke(new BasicStroke(6));
				g2.drawOval(cx - planetRadius - 30,  cy - planetRadius / 3,  (planetRadius + 30) * 2,  planetRadius / 3 * 2);;
				g2.setStroke(new BasicStroke(3));
				g2.setColor(new Color(highlightColor.getRed(), highlightColor.getGreen(), highlightColor.getBlue(), 60));
				g2.drawOval(cx - planetRadius - 18,  cy - planetRadius / 4,  (planetRadius + 18) * 2,  planetRadius / 4 * 2);
			}
			
			// planet body
			g2.setColor(baseColor);
			g2.fillOval(cx - planetRadius,  cy - planetRadius,  planetRadius * 2,  planetRadius * 2);
			
			// clip everything from here to the planet circle so shadow/highlight can't leak out
			Shape planetClip = new java.awt.geom.Ellipse2D.Double(cx - planetRadius, cy - planetRadius, planetRadius * 2, planetRadius * 2);
			Shape oldClip = g2.getClip(); // save the old clip so it can be restored after
			g2.setClip(planetClip);
			
			//shadow on the right side
			g2.setColor(shadowColor);
			g2.fillOval(cx,  cy - planetRadius,  planetRadius,  planetRadius * 2);
			
			//highlight on upper left
			g2.setColor(highlightColor);
			g2.fillOval(cx - planetRadius + 8, cy - planetRadius + 8, planetRadius / 2, planetRadius / 2);
			
			//surface details, pixel dots based on mass
			//more mass planet = more surface features
			int details = planet.getMass() != null ? (int) Math.min(12,  planet.getMass() / 20 + 3) : 4;
			java.util.Random rand = new java.util.Random(planet.getName().hashCode()); // seed by the name so the same planet always looks the same
			g2.setColor(shadowColor);
			for (int i = 0; i < details; i++) {
				// keep dots within the planet circle
				double angle = rand.nextDouble() * Math.PI; //left half only, lit side
				double dist = rand.nextDouble() * (planetRadius - 12);
				int dx = (int) (Math.cos(angle) * dist);
				int dy = (int) (Math.sin(angle) * dist * 1.5 - dist * 0.3);
				int dotSize = rand.nextInt(6) + 3;
				g2.fillOval(cx + dx - dotSize / 2, cy + dy - dotSize / 2, dotSize, dotSize);			
			}
			// restore clip before drawing the border, otherwise the border gets clipped too
			g2.setClip(oldClip);

			// pixel border around planet
			g2.setColor(new Color(0x1a, 0x1a, 0x4a));
			g2.setStroke(new BasicStroke(2));
			g2.drawOval(cx - planetRadius, cy - planetRadius, planetRadius * 2, planetRadius * 2);

			
			// pixel border around planet
			g2.setColor(new Color(0x1a, 0x1a, 0x4a));
			g2.setStroke(new BasicStroke(2));
			g2.drawOval(cx - planetRadius,  cy - planetRadius,  planetRadius * 2,  planetRadius * 2);
			
			// small star field  maybe ....
		}
	}
}