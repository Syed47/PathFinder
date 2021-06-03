// Author: Syed Baryalay
// Student Number: 19719431

/* shortest path
38, 29, 2, 31, 40, 34, 1, 25, 33, 20, 21, 22, 14, 
7, 3, 23, 39, 27, 6, 24, 17, 18, 37, 15, 4, 35, 26,
30, 28, 32, 19, 8, 11, 5, 9, 13, 36, 10, 16, 12

angry mins = 214.77 
*/

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class TSP {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Window window = new Window("TSP", 1000, 525);
                window.getComputeBtn().addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {    
                        window.draw(new PathFinder(window.pullInputData()).getPath());
                    }
                });
            }
        });
    }
}


class Window {

    private String title;
    private int width, height, margin;

    private JFrame frame;
    private JPanel sidePanel;
    private AnimationPanel animationPanel;

    private JTextArea tfInput, tfOutput;
    private JScrollPane scrollIn, scrollOut;
    private JButton btCompute;

    Window(String t, int w, int h) {
        title = t;
        margin = 10;
        width = w;
        height = h;

        initFrame();
        initSidePanel();
        initAnimationPanel();

        frame.add(sidePanel);
        frame.add(animationPanel);
        frame.setVisible(true);
    }

    public String pullInputData() {
        return tfInput.getText();
    }

    public void setOutput(String test) {
        tfOutput.setText(test);
    }

    public JButton getComputeBtn() {
        return btCompute;
    }

    public void draw(Record[] path) {
        animationPanel.setPoints(path);
        animationPanel.render();
        String pathIDs = "";
        for (int i = 1; i < path.length; i++) {
            pathIDs += path[i].id + ",";
        }
        setOutput(pathIDs);
    }

    private void initFrame() {
        frame = new JFrame();
        frame.setTitle(title);
        frame.setLayout(null);
        frame.setBounds(0, 0, width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initAnimationPanel() {
        animationPanel = new AnimationPanel(0, 0, width / 2, height);
    }

    private void initSidePanel() {
        sidePanel = new JPanel();
        tfInput = new JTextArea();
        tfOutput = new JTextArea();
        btCompute = new JButton();
        scrollIn = new JScrollPane(tfInput, 22, 32); // ! CONSTS
        scrollOut = new JScrollPane(tfOutput, 22, 32); // ! CONSTS
        sidePanel.setBackground(Color.ORANGE);
        sidePanel.setLayout(null);
        sidePanel.setBounds(width / 2, 0, width / 2, height);

        settfInputProperties();
        settfOutputProperties();
        setbtComputeProperties();

        sidePanel.add(scrollIn);
        sidePanel.add(scrollOut);
        sidePanel.add(btCompute);
    }

    private void settfInputProperties() {
        final int w = width / 2, h = 220;
        scrollIn.setBounds(0, 0, w, h);
        tfInput.setFont(new Font("SansSarif", Font.BOLD, 14));
        tfInput.setForeground(Color.GRAY);
        tfInput.setLineWrap(false);
    }

    private void settfOutputProperties() {
        final int w = width / 2, h = 140;
        scrollOut.setBounds(0, 220 + margin, w, h);
        tfOutput.setFont(new Font("SansSarif", Font.BOLD, 18));
        tfOutput.setForeground(Color.BLUE);
        tfInput.setLineWrap(true);
        tfInput.setWrapStyleWord(true);
        tfOutput.setEditable(false);
    }

    private void setbtComputeProperties() {
        final int w = 300, h = 100;
        btCompute.setBounds(100, 360+margin, w, h);
        btCompute.setFont(new Font("SansSarif", Font.BOLD, 24));
        btCompute.setBackground(Color.RED);
        btCompute.setText("Compute");
    }

    public int getMapWidth() {
        return animationPanel.width;
    }

    public int getMapHeight() {
        return animationPanel.height;
    }
}

class AnimationPanel extends JPanel {

    int x, y, width, height, r;
    Image image;
    private Record[] points; // the shortest path

    AnimationPanel(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        width = w;
        height = h-40;
        this.points = null;
        Image temp = null;
        try {
            temp = ImageIO.read(new java.io.File("map.png"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null, "Error: map.png not loaded.\nClick OK to continue!");
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
                gfx.setColor(new Color(RND.nextInt(200), RND.nextInt(200), RND.nextInt(255)));
                gfx.fillOval(points[i].WEST, points[i].NORTH, r, r);
                gfx.drawLine(
                    points[i-1].WEST+r/2, 
                    points[i-1].NORTH+r/2,
                    points[i].WEST+r/2, 
                    points[i].NORTH+r/2
                );
            }
        }
    }

    public void render() {
        repaint();
    }
}


final class PathFinder {

    private Record[] records;
    private ArrayList<Record> path;

    PathFinder(String data) {
        Record apachePizza = new Record(0, 0, 53.38197, -6.59274);
        this.records = parseData(data);
        this.path = new ArrayList<>();
        for (Record r : records) { this.path.add(r); }
        this.path = nearestNeighbours(apachePizza);
        System.out.println("NN = "+totalTime(this.path));
        this.path = twoOpt(this.path);
        System.out.println("2-opt = " + totalTime(this.path));
    }

    Record[] parseData(String data) {
        String[] tokens = data.trim().split("\n");
        Record[] local = new Record[tokens.length];
        for (String token : tokens) {
            String[] fields = token.split(",");
            int id = Integer.parseInt(fields[0]);
            int seconds = Integer.parseInt(fields[2]) * 60;//seconds
            double gpsN = Double.parseDouble(fields[3]);
            double gpsW = Double.parseDouble(fields[4]);
            local[id-1] = new Record(id, seconds, gpsN, gpsW);
        }
        return local;
    }

    Record[] getPath() {
        Record[] spath = new Record[path.size()];
        for (int i = 0; i < path.size(); i++) {
            spath[i] = path.get(i);
        }
        return spath;
    }
    
    private ArrayList<Record> nearestNeighbours(Record start) {
        ArrayList<Record> allNearestNeighbors = new ArrayList<>();
        start.setVisited(true);
        allNearestNeighbors.add(start);
        Record next = start, nearest = null;
        int visited = 0, all = records.length;
        while (visited < all) {
            int shortest = Integer.MAX_VALUE;
            for (int i = 0; i < records.length; i++) {
                if (!records[i].isVisited()) {
                    int time = next.timeTo(records[i]);
                    if (time < shortest) {
                        shortest = time;
                        nearest = records[i];
                    }
                }
            }
            nearest.setVisited(true);
            allNearestNeighbors.add(nearest);
            visited++;
            next = nearest;
        }
        return allNearestNeighbors;
    }


    private ArrayList<Record> twoOpt(ArrayList<Record> path) {
        int N = path.size();
        ArrayList<Record> best = new ArrayList<>();
        for (Record r : path) { best.add(r); }
        int maxIterations = 0;
        while (maxIterations < N/5) {
            for (int i = 1; i < N - 2; i++) {
                for (int j = i + 1; j < N + 1; j++) {
                    if (j - i == 1) continue;
                    ArrayList<Record> newPath = new ArrayList<>();
                    for (int k = 0; k < i; k++) {
                        newPath.add(best.get(k));
                    }
                    for (int k = j - 1; k >= i; k--) {
                        newPath.add(best.get(k));
                    }
                    for (int k = j; k < N; k++) {
                        newPath.add(best.get(k));
                    }
                    if (totalTime(newPath) < totalTime(best)) {
                        best = newPath;
                    }
                }
            }
            maxIterations++;
        }
        return best;
    }

    private int totalTime(ArrayList<Record> path) {
        int c = 0;
        for (int i = 1; i < path.size(); i++) {
            c +=  path.get(i-1).timeTo(path.get(i));
        }
        return c;
    }
}


class Record {

    int id;
    int seconds;
    double gpsN;
    double gpsW;

    int WEST, NORTH;
    boolean visited;

    Record(int i, int min, double gpsn, double gpsw) {
        id = i;
        seconds = min;
        gpsN = gpsn;
        gpsW = gpsw;
        visited = false;
    }

    // converts distance (km) into time (seconds) using 60kmph speed
    int timeTo(Record other) {
        double d = dist(other);
        int seconds = (int) ((d / 60) * 3600);
        return seconds;
    }
    // finds between two GPS points
    double dist(Record other) {
        final int Radius = 6371;
        double lat1 = other.gpsN;
        double lon1 = other.gpsW;
        double lat2 = this.gpsN;
        double lon2 = this.gpsW;
        double latDistance = radians(lat2 - lat1);
        double lonDistance = radians(lon2 - lon1);
        double angle = Math.sin(latDistance / 2) * 
                Math.sin(latDistance / 2) + 
                Math.cos(radians(lat1)) * 
                Math.cos(radians(lat2)) * 
                Math.sin(lonDistance / 2) * 
                Math.sin(lonDistance / 2);
        double unit = 2 * Math.atan2(Math.sqrt(angle), Math.sqrt(1 - angle));
        return Radius * unit;
    }
    
    private double radians(double value) {
        return value * Math.PI / 180;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean f) {
        visited = f;
    }
}
