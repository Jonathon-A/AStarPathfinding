package astarpathfinding;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class MainFrame extends JFrame {

    private MainPanel mainPanel;
    private JLabel infoLabel;
    private BufferedImage image;

    public MainFrame(BufferedImage _image) {
        image = _image;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setTitle("A*Pathfinder");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        mainPanel = new MainPanel(image);
        mainPanel.setBounds(50, 50, width - 100, height - 240);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 6, false));
        this.add(mainPanel);
        mainPanel.setVisible(true);

        infoLabel = new JLabel("Scroll to zoom. Click and drag to move. Click on start and end points to find path. Contrast slider:", JLabel.CENTER);
        infoLabel.setFont(new Font(infoLabel.getFont().getFontName(), Font.PLAIN, 20));
        infoLabel.setBounds(50, height - 180, width / 2 , 80);
        this.add(infoLabel);
        infoLabel.setVisible(true);

        JSlider ContrastSlider = new JSlider();
        ContrastSlider.setFont(new Font(infoLabel.getFont().getFontName(), Font.PLAIN, 16));
        ContrastSlider.setBounds(width / 2 + 50, height - 180, width / 2 - 100, 80);
        ContrastSlider.setMajorTickSpacing(15);
        ContrastSlider.setMinorTickSpacing(1);
        ContrastSlider.setPaintTrack(true);
        ContrastSlider.setPaintTicks(true);
        ContrastSlider.setPaintLabels(true);
        ContrastSlider.setMaximum(255);
        ContrastSlider.setMinimum(0);
        ContrastSlider.setValue(150);

        ContrastSlider.addChangeListener((final ChangeEvent e) -> {
            AStarPathfinding.UpdateImageContrast(ContrastSlider.getValue());
        });

        this.add(ContrastSlider);

    }

    public void UpdateImage() {
       
        mainPanel.UpdateImage();
    }

}
