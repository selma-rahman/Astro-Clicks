package GUI;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.*;
import java.awt.event.*;
import java.awt.Desktop;
import java.net.URI;

public class DiscoveryMethodsPanel {

    private final MainWindow mainWindow;

    private JLabel    detailTitleLabel;
    private JTextPane detailTextArea;
    private JLabel    detailImageLabel;

    public DiscoveryMethodsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public JPanel buildInfoHome() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(MainWindow.BG_BASE);
        page.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        page.setOpaque(false);

        //top bar
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JButton homeButton = mainWindow.retroButton("HOME");
        homeButton.addActionListener(e ->
            mainWindow.centerLayout.show(mainWindow.centerPanel, "HOME")
        );

        JLabel title = new JLabel("DISCOVERY METHODS", SwingConstants.CENTER);
        title.setFont(mainWindow.pixelFontSm);
        title.setForeground(MainWindow.COL_ACCENT);

        JPanel rightTop = new JPanel();
        rightTop.setOpaque(false);
        rightTop.setLayout(new BoxLayout(rightTop, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("info found on");
        infoLabel.setFont(MainWindow.pixelFontXs);
        infoLabel.setForeground(MainWindow.COL_TEXT);

        JLabel nasaLink = new JLabel("<html><u>NASA.gov</u></html>");
        nasaLink.setFont(MainWindow.pixelFontXs);
        nasaLink.setForeground(MainWindow.COL_ACCENT);
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
        leftButtons.add(homeButton);

        top.add(leftButtons, BorderLayout.WEST);
        top.add(title,       BorderLayout.CENTER);
        top.add(rightTop,    BorderLayout.EAST);

        //method card grid
        JPanel grid = new JPanel(new GridLayout(2, 3, 24, 24));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(30, 40, 0, 40));

        grid.add(createMethodCard(
            "RADIAL\nVELOCITY",
            "images/radial_velocity.png",
            "Radial Velocity",
            "Radial velocity detects exoplanets by measuring the tiny wobble of a star caused by the " +
            "gravitational pull of an orbiting planet. Scientists observe shifts in the star's spectrum " +
            "due to the Doppler effect."
        ));

        grid.add(createMethodCard(
            "TRANSITS",
            "images/transits.png",
            "Transits",
            "The transit method detects planets when they pass in front of their host star from our " +
            "point of view. This causes a slight dip in the star's brightness. Repeated dips can " +
            "reveal a planet's size and orbit."
        ));

        grid.add(createMethodCard(
            "GRAVITATIONAL\nMICROLENSING",
            "images/microlensing.png",
            "Gravitational Microlensing",
            "Microlensing happens when a foreground star bends and magnifies the light of a background " +
            "star. A planet orbiting the foreground star can create an extra signal in that " +
            "magnification pattern."
        ));

        grid.add(createMethodCard(
            "DIRECT\nIMAGING",
            "images/direct_imaging.png",
            "Direct Imaging",
            "Direct imaging captures actual pictures of exoplanets by blocking out the bright light " +
            "from their host stars. This is most effective for large planets far from their stars."
        ));

        grid.add(createMethodCard(
            "CORONAGRAPH",
            "images/coronagraph.png",
            "Coronagraph",
            "A coronagraph is an instrument used to block a star's light so nearby faint objects, " +
            "like exoplanets, can be observed more clearly."
        ));

        grid.add(createMethodCard(
            "STARSHADE",
            "images/starshade.jpeg",
            "Starshade",
            "A starshade is a large spacecraft positioned far from a telescope to block starlight " +
            "before it enters the telescope, helping scientists directly observe exoplanets."
        ));

        page.add(top,  BorderLayout.NORTH);
        page.add(grid, BorderLayout.CENTER);

        return page;
    }

    public JPanel buildInfoDetail() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(MainWindow.BG_BASE);
        page.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        page.setOpaque(false);

        JButton backButton = mainWindow.retroButton("<< back to methods");
        backButton.addActionListener(e ->
            mainWindow.centerLayout.show(mainWindow.centerPanel, "INFO_HOME")
        );

        detailTitleLabel = new JLabel("METHOD TITLE");
        detailTitleLabel.setFont(mainWindow.pixelFontSm);
        detailTitleLabel.setForeground(MainWindow.COL_ACCENT);
        detailTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(detailTitleLabel,                           BorderLayout.CENTER);
        top.add(backButton,                                 BorderLayout.WEST);
        top.add(Box.createRigidArea(backButton.getPreferredSize()), BorderLayout.EAST);

        detailImageLabel = new JLabel();
        detailImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailTextArea = new JTextPane();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(attributes, 0.7f);
        detailTextArea.setParagraphAttributes(attributes, false);
        detailTextArea.setEditable(false);
        detailTextArea.setFont(MainWindow.pixelFontXs);
        detailTextArea.setForeground(MainWindow.COL_TEXT);
        detailTextArea.setBackground(MainWindow.BG_PANEL);
        detailTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainWindow.COL_BORDER, 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        detailTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.add(Box.createVerticalStrut(20));
        center.add(detailImageLabel);
        center.add(Box.createVerticalStrut(20));
        center.add(detailTextArea);

        page.add(top,    BorderLayout.NORTH);
        page.add(center, BorderLayout.CENTER);

        return page;
    }

    private JPanel createMethodCard(String displayTitle, String imagePath,
                                    String detailTitle,  String detailText) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(MainWindow.BG_PANEL);
        card.setBorder(BorderFactory.createLineBorder(MainWindow.COL_BORDER, 2));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel title = new JLabel("<html>" + displayTitle.replace("\n", "<br>") + "</html>");
        title.setFont(mainWindow.pixelFontSm);
        title.setForeground(MainWindow.COL_ACCENT);
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
                showMethodCard(detailTitle, detailText, imagePath);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(MainWindow.COL_ACCENT, 3));
                card.setBackground(MainWindow.BG_CARD);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(MainWindow.COL_BORDER, 2));
                card.setBackground(MainWindow.BG_PANEL);
            }
        };

        card.addMouseListener(clicker);
        title.addMouseListener(clicker);
        imageLabel.addMouseListener(clicker);

        return card;
    }

    /** Populates the detail panel and flips to it. Formerly showMethodDetail in MainWindow. */
    public void showMethodCard(String title, String text, String imagePath) {
        detailTitleLabel.setText(title);
        detailTextArea.setText(text);

        java.net.URL url = getClass().getResource("/" + imagePath);
        if (url != null) {
            ImageIcon icon   = new ImageIcon(url);
            Image     scaled = icon.getImage().getScaledInstance(240, -1, Image.SCALE_SMOOTH);
            detailImageLabel.setIcon(new ImageIcon(scaled));
            detailImageLabel.setText("");
        } else {
            detailImageLabel.setIcon(null);
            detailImageLabel.setText("[ image not found ]");
            detailImageLabel.setForeground(MainWindow.COL_TEXT);
            detailImageLabel.setFont(mainWindow.pixelFontSm);
        }

        mainWindow.centerLayout.show(mainWindow.centerPanel, "INFO_DETAIL");
    }

    private JLabel buildCardImageLabel(String imagePath) {
        java.net.URL url = getClass().getResource("/" + imagePath);

        if (url != null) {
            ImageIcon icon   = new ImageIcon(url);
            Image     scaled = icon.getImage().getScaledInstance(110, -1, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaled));
        }

        JLabel placeholder = new JLabel("image");
        placeholder.setPreferredSize(new Dimension(110, 80));
        placeholder.setMaximumSize(new Dimension(110, 80));
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setVerticalAlignment(SwingConstants.CENTER);
        placeholder.setFont(MainWindow.pixelFontXs);
        placeholder.setForeground(MainWindow.COL_TEXT);
        placeholder.setBorder(BorderFactory.createLineBorder(new Color(0xff, 0x99, 0xdd), 2));
        return placeholder;
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
}