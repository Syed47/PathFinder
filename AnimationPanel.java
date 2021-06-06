import java.util.Random;
import java.util.Arrays;

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

class AnimationPanel extends JPanel {

    int x, y, width, height, r;
    Image image;
    private Record[] points; // the shortest path

    AnimationPanel(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        width = w;
        height = h - 40;
        this.points = null;
        Image temp = null;
        try {
            temp = ImageIO.read(new java.io.File("map.png"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error: map.png not loaded.\nClick OK to continue!");
        } finally {
            image = temp;
        }
        setBounds(x, y, width, height);
        r = 8;
    }

    public void setPoints(Record[] path) {
        this.points = Arrays.copyOf(path, path.length);
        for (Record r : points) {
            setRelativeGPS(r);
        }
    }

    private void setRelativeGPS(Record point) {
        final double MAXN = 53.41318, MAXW = -6.71261;
        final double MINN = 53.28271, MINW = -6.45509;
        point.WEST = map(point.gpsW, MAXW, MINW, 0, width);
        point.NORTH = map(point.gpsN, MAXN, MINN, 0, height);
    }

    private int map(double x, double a0, double a1, double b0, double b1) {
        return (int) (b0 + (b1 - b0) * ((x - a0) / (a1 - a0)));
    }

    protected void paintComponent(Graphics gfx) {
        super.paintComponent(gfx);
        gfx.drawImage(image, x, y, width, height, Color.BLACK, null);
        if (points != null) {
            for (int i = 1; i < points.length; i++) {
                Random RND = new Random();
                gfx.setColor(new Color(
                    RND.nextInt(200), 
                    RND.nextInt(200), 
                    RND.nextInt(255)));
                gfx.fillOval(points[i].WEST, points[i].NORTH, r, r);
                gfx.drawLine(
                    points[i - 1].WEST + r / 2, 
                    points[i - 1].NORTH + r / 2, 
                    points[i].WEST + r / 2,
                    points[i].NORTH + r / 2);
            }
        }
    }

    public void render() {
        repaint();
    }
}
