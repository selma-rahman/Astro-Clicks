
// https://www.youtube.com/watch?v=ZPAVxdaYB6A
// https://stackoverflow.com/questions/73014523/how-to-draw-image-pixel-by-piexel-in-swing
package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RocketAnimation extends JPanel {

    // position of rocket
    private int x = 250;
    private int y = 20;

    // flame animation frame
    private int flameFrame = 0;

    // pixel size (controls overall scale)
    private final int s = 7;

    public RocketAnimation() {
        setOpaque(false);

        // make panel wide enough for full animation
        setPreferredSize(new Dimension(900, 130));

        // timer to update animation
        Timer timer = new Timer(60, new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // move rocket to the right
                x += 5;

                // cycle flame animation
                flameFrame = (flameFrame + 1) % 3;

                // reset when off screen
                if (x > getWidth() + 120) {
                    x = -140;
                }

                repaint(); // redraw
            }
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // thisi is to keep pixel-art sharp
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

      // to draw our rockey
        drawRocket(g2, x, y);

        // so our astro follows is attached to rocket
        int ax = x - 70;
        int ay = y + 10 + (int)(Math.sin(x * 0.05) * 6); // slight float

        drawAstronaut(g2, ax, ay);
    }

    // helper to draw one pixel square
    private void pixel(Graphics2D g2, int px, int py, Color color) {
        g2.setColor(color);
        g2.fillRect(px, py, s, s);
    }

    // our cool floacting rocket
    private void drawRocket(Graphics2D g2, int rx, int ry) {

        Color body = new Color(224, 224, 255);
        Color blue = new Color(123, 123, 255);
        Color green = new Color(0, 255, 136);
        Color orange = new Color(255, 120, 0);
        Color yellow = new Color(255, 230, 80);

        // flame animation
        if (flameFrame == 0) {
            pixel(g2, rx - 3*s, ry + 3*s, orange);
            pixel(g2, rx - 2*s, ry + 3*s, yellow);
        } else if (flameFrame == 1) {
            pixel(g2, rx - 4*s, ry + 3*s, orange);
            pixel(g2, rx - 3*s, ry + 2*s, yellow);
            pixel(g2, rx - 3*s, ry + 4*s, yellow);
        } else {
            pixel(g2, rx - 3*s, ry + 2*s, orange);
            pixel(g2, rx - 3*s, ry + 4*s, orange);
            pixel(g2, rx - 2*s, ry + 3*s, yellow);
        }

        // engine
        pixel(g2, rx, ry + 2*s, blue);
        pixel(g2, rx, ry + 3*s, blue);
        pixel(g2, rx, ry + 4*s, blue);

        // body
        for (int col = 1; col <= 8; col++) {
            pixel(g2, rx + col*s, ry + 1*s, body);
            pixel(g2, rx + col*s, ry + 2*s, body);
            pixel(g2, rx + col*s, ry + 3*s, body);
            pixel(g2, rx + col*s, ry + 4*s, body);
            pixel(g2, rx + col*s, ry + 5*s, body);
        }

        // outline
        for (int col = 2; col <= 7; col++) {
            pixel(g2, rx + col*s, ry, blue);
            pixel(g2, rx + col*s, ry + 6*s, blue);
        }

        // nose
        pixel(g2, rx + 9*s, ry + 2*s, blue);
        pixel(g2, rx + 10*s, ry + 3*s, blue);
        pixel(g2, rx + 9*s, ry + 4*s, blue);

        // window
        pixel(g2, rx + 4*s, ry + 2*s, green);
        pixel(g2, rx + 5*s, ry + 2*s, green);
        pixel(g2, rx + 4*s, ry + 3*s, green);
        pixel(g2, rx + 5*s, ry + 3*s, green);

        // fins
        pixel(g2, rx + 2*s, ry - 1*s, blue);
        pixel(g2, rx + 3*s, ry - 2*s, blue);

        pixel(g2, rx + 2*s, ry + 7*s, blue);
        pixel(g2, rx + 3*s, ry + 8*s, blue);
    }

    // our mini astronaut 
    private void drawAstronaut(Graphics2D g2, int ax, int ay) {

        int size = s;

        Color suit = new Color(224, 224, 255);
        Color visor = new Color(123, 123, 255);
        Color accent = new Color(0, 255, 136);

        // helmet
        pixel(g2, ax, ay, suit);
        pixel(g2, ax + size, ay, suit);
        pixel(g2, ax, ay + size, suit);
        pixel(g2, ax + size, ay + size, suit);

        // visor
        pixel(g2, ax + size, ay + size, visor);

        // body our our pixel rocket
        pixel(g2, ax, ay + 2*size, suit);
        pixel(g2, ax + size, ay + 2*size, suit);
        pixel(g2, ax, ay + 3*size, suit);
        pixel(g2, ax + size, ay + 3*size, suit);

        // roket arms
        pixel(g2, ax - size, ay + 2*size, accent);
        pixel(g2, ax + 2*size, ay + 2*size, accent);

        // legs
        pixel(g2, ax, ay + 4*size, accent);
        pixel(g2, ax + size, ay + 4*size, accent);

        // line to rocket
        g2.setColor(new Color(0, 255, 180, 120));
        g2.drawLine(ax + 2*size, ay + 2*size, x, y + 2*size);
    }
}