package astarpathfinding;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

//this class modified from Thanasis1101's code
public class MainPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {

    private BufferedImage image;

    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private boolean zoomer;
    private boolean dragger;
    private boolean released;
    private double xOffset = 0;
    private double yOffset = 0;
    private int xDiff;
    private int yDiff;
    private Point startPoint;

    public MainPanel(BufferedImage _image) {

        this.image = _image;
        initComponent();

    }

    public void UpdateImage() {
       
        repaint();
    }

    private void initComponent() {
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    private AffineTransform Prevat = new AffineTransform();

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        AffineTransform at = Prevat;
        if (zoomer) {
            at = new AffineTransform();

            double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
            double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

            double zoomDiv = zoomFactor / prevZoomFactor;

            xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
            yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

            at.translate(Math.round(xOffset), Math.round(yOffset));
            at.scale(zoomFactor, zoomFactor);
            prevZoomFactor = zoomFactor;

            zoomer = false;
        }

        if (dragger) {
            at = new AffineTransform();
            at.translate(Math.round(xOffset + xDiff), Math.round(yOffset + yDiff));
            at.scale(zoomFactor, zoomFactor);

            if (released) {
                xOffset += xDiff;
                yOffset += yDiff;
                dragger = false;
            }

        }
        Prevat = at;
        g2.transform(at);
        // All drawings go here
        g2.drawImage(image, 0, 0, this);
        // g2.drawLine(XHighlight, YHighlight, XHighlight, YHighlight);

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        zoomer = true;

        //Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
            repaint();
        }
        //Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= 1.1;
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point curPoint = e.getLocationOnScreen();
        xDiff = curPoint.x - startPoint.x;
        yDiff = curPoint.y - startPoint.y;

        dragger = true;
        repaint();

    }

    private int XHighlight;
    private int YHighlight;

    @Override
    public void mouseMoved(MouseEvent e) {
        XHighlight = (int) Math.round((e.getX() - xOffset) / zoomFactor);
        YHighlight = (int) Math.round((e.getY() - yOffset) / zoomFactor);
//        xDiff = 0;
//        yDiff = 0;
//
//        dragger = true;
//        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        AStarPathfinding.StartAndEnd((int) Math.round((e.getX() - xOffset) / zoomFactor), (int) Math.round((e.getY() - yOffset) / zoomFactor));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        released = false;
        startPoint = MouseInfo.getPointerInfo().getLocation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        released = true;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
