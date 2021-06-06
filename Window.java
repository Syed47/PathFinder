import java.awt.Font;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

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
