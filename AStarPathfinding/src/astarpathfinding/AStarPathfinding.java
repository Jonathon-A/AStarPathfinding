package astarpathfinding;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public class AStarPathfinding {

    //Directory of project on user's computer
    final String UserDirectory = System.getProperty("user.dir") + "\\dist\\";
    private BufferedImage Maze = null;
    final private JFrame GUIWindow = new JFrame("A*Pathfinder");

    public static void main(String[] args) {
        AStarPathfinding Main = new AStarPathfinding();

        Main.GetImage();
        Main.WindowSetup();

    }

    private Node[][] AllNodes;
    private BufferedImage InputMaze = null;

    private void GetImage() {
        Scanner input = new Scanner(System.in);

        do {

            System.out.println("Image name:");
            //String name = "Hard.jpg";
            String name = input.next();

            try {
                InputMaze = CreateBufferedImage(name);
            } catch (Exception e) {
                InputMaze = null;
            }

        } while (InputMaze == null);

        Maze = new BufferedImage(InputMaze.getWidth(), InputMaze.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Maze.getGraphics().drawImage(InputMaze, 0, 0, null);

        AllNodes = new Node[Maze.getWidth()][Maze.getHeight()];

        for (int i = 0; i < Maze.getWidth(); i++) {
            for (int j = 0; j < Maze.getHeight(); j++) {
                int num = 150;
                AllNodes[i][j] = new Node();
                if (Maze.getRGB(i, j) < new Color(num, num, num).getRGB()) {
                    Maze.setRGB(i, j, new Color(0, 0, 0).getRGB());
                    AllNodes[i][j].setWall(true);
                } else {
                    Maze.setRGB(i, j, new Color(255, 255, 255).getRGB());
                }
                AllNodes[i][j].SetCords(i, j);

            }
        }
    }

    private BufferedImage CreateBufferedImage(final String ImagePath) throws IOException {
        //Returns buffered image created from image specified from given file path
        return ImageIO.read(new File(UserDirectory + "Mazes\\" + ImagePath));
    }
    private final JLabel MazePanel = new JLabel();

    private void WindowSetup() {

        GUIWindow.setVisible(true);
        GUIWindow.setLayout(null);
        GUIWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLayeredPane MazeBorder = new JLayeredPane();

        //Setup GUI's window background properties
        MazePanel.setLocation(6, 6);
        MazePanel.setSize(new Dimension(Maze.getWidth(), Maze.getHeight()));

        MazeBorder.setSize(new Dimension(Maze.getWidth() + 12, Maze.getHeight() + 12));
        MazeBorder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 6, false));

        MazePanel.setIcon(new ImageIcon(Maze));
        MazeBorder.add(MazePanel);

        GUIWindow.setSize(new Dimension(Maze.getWidth() + 30, Maze.getHeight() + 59 + 80));

        GUIWindow.add(MazeBorder);

        MazePanel.addMouseListener(new MouseAdapter() {

            //When mouse is released
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                //Mouse held is set to false
                StartAndEnd(e.getX(), e.getY());

            }
        });

        End = new Point(Maze.getWidth(), Maze.getHeight());

        // ContrastSlider.setBackground(AssetsColorArray[0]);
        ContrastSlider.setFont(new Font("Tahoma", Font.BOLD, 12));
        ContrastSlider.setBounds(0, Maze.getHeight() + 12, Maze.getWidth(), 80);
        ContrastSlider.setMajorTickSpacing(50);
        ContrastSlider.setMinorTickSpacing(1);
        ContrastSlider.setPaintTrack(true);
        ContrastSlider.setPaintTicks(true);
        ContrastSlider.setPaintLabels(true);
        ContrastSlider.setMaximum(255);
        ContrastSlider.setMinimum(0);
        ContrastSlider.setValue(150);

        ContrastSlider.addChangeListener((final ChangeEvent e) -> {
            Maze.getGraphics().drawImage(InputMaze, 0, 0, null);
            for (int i = 0; i < Maze.getWidth(); i++) {
                for (int j = 0; j < Maze.getHeight(); j++) {
                    int num = ContrastSlider.getValue();
                    AllNodes[i][j] = new Node();
                    if (Maze.getRGB(i, j) < new Color(num, num, num).getRGB()) {
                        Maze.setRGB(i, j, new Color(0, 0, 0).getRGB());
                        AllNodes[i][j].setWall(true);
                    } else {
                        Maze.setRGB(i, j, new Color(255, 255, 255).getRGB());
                    }
                    AllNodes[i][j].SetCords(i, j);

                }
            }
            MazePanel.repaint();
            AtStart = true;
            Over = false;

            Start = new Point(0, 0);
            End = new Point(0, 0);
        });

        GUIWindow.add(ContrastSlider);

    }

    private final JSlider ContrastSlider = new JSlider();

    private boolean AtStart = true;

    private Point Start = new Point(0, 0);
    private Point End = new Point(0, 0);
    private boolean Over = false;

    private void StartAndEnd(int X, int Y) {
        if (!Over) {

            int Offset = 1;
            if (AtStart) {

                for (int i = X - Offset; i < X + Offset; i++) {
                    for (int j = Y - Offset; j < Y + Offset; j++) {
                        try {
                            if (Maze.getRGB(i, j) != new Color(0, 0, 0).getRGB()) {
                                Maze.setRGB(i, j, new Color(0, 255, 0).getRGB());
                                MazePanel.repaint();
                            }

                        } catch (Exception e) {
                            //  System.out.println(e);
                        }
                    }
                }
                Start = new Point(X, Y);
                AtStart = false;

            } else {
                for (int i = X - Offset; i < X + Offset; i++) {
                    for (int j = Y - Offset; j < Y + Offset; j++) {
                        try {
                            if (Maze.getRGB(i, j) != new Color(0, 0, 0).getRGB()) {
                                Maze.setRGB(i, j, new Color(255, 0, 0).getRGB());
                                MazePanel.repaint();

                            }
                        } catch (Exception e) {
                            //  System.out.println(e);
                        }
                    }
                }

                End = new Point(X, Y);
                Over = true;
                //  MazePanel.removeMouseListener(MazePanel.getMouseListeners()[0]);

                Thread T1 = new Thread(() -> {

                    long startTime = System.currentTimeMillis();

                    AStarPathfinder();

                    long endTime = System.currentTimeMillis();

                    System.out.println("That took " + (endTime - startTime) + " milliseconds");

                });
                T1.start();

            }
        }
    }

    private final PriorityQueue<Node> Open = new PriorityQueue<>();

    private void AStarPathfinder() {
        /*
        This method finds the shortest path between the start and end node that avoids obstacles
        It uses the A* search algorithm
         */

        //Calculate F Cost of start node
        AllNodes[Start.x][Start.y].setGcost(0);
        AllNodes[Start.x][Start.y].setHcost(GetNodeDistance(AllNodes[Start.x][Start.y], AllNodes[End.x][End.y]));
        //Adds start node to open set
        Open.add(AllNodes[Start.x][Start.y]);
        Maze.setRGB(Start.x, Start.y, Color.CYAN.getRGB());

        boolean FoundPath = false;

        while (!Open.isEmpty()) {

//            Time Delay
//            try {
//                TimeUnit.MILLISECONDS.sleep((long) (1));
//            } catch (InterruptedException ex) {
//                Logger.getLogger(AStarPathfinding.class.getName()).log(Level.SEVERE, null, ex);
            //            }
            //Finds node from open set with lowest F cost
            Node Current = Open.poll();
            //Adds current node to closed set
            Current.setClosed(true);
            Maze.setRGB(Current.getX(), Current.getY(), Color.BLUE.getRGB());
            MazePanel.repaint();
            //If the current node is the end node
            if (Current == AllNodes[End.x][End.y]) {
                //Stops loop because path has been found
                FoundPath = true;
                break;
            } else {
                //Looks at neigbouring nodes to current node
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (!(i == 0 && j == 0)) {
                            int Xcord = Current.getX() + i;
                            int Ycord = Current.getY() + j;
                            //If node coordinates are within bounds
                            if (Xcord >= 0 && Xcord < Maze.getWidth() && Ycord >= 0 && Ycord < Maze.getHeight()) {
                                //If the neigbouring node is traversable and is not in the closed set
                                if (!(AllNodes[Xcord][Ycord].isWall() || AllNodes[Xcord][Ycord].isClosed())) {
                                    //Calculate G Cost of start node
                                    int Gcost = Current.getGcost() + GetNodeDistance(Current, AllNodes[Xcord][Ycord]);
                                    //If the neigbouring node is in the open set
                                    if (!Open.contains(AllNodes[Xcord][Ycord])) {
                                        //Calculates F Cost of start node
                                        AllNodes[Xcord][Ycord].setGcost(Gcost);
                                        AllNodes[Xcord][Ycord].setHcost(GetNodeDistance(AllNodes[Xcord][Ycord], AllNodes[End.x][End.y]));
                                        //Sets neigbouring node parent to current node
                                        AllNodes[Xcord][Ycord].setParent(Current);
                                        //Adds neigbouring node to open set
                                        Open.add(AllNodes[Xcord][Ycord]);
                                        Maze.setRGB(Xcord, Ycord, Color.CYAN.getRGB());
                                        MazePanel.repaint();
                                    }//Otherwise if the G cost is lower than the neigbouring node's previous G cost
                                    else if (Gcost < AllNodes[Xcord][Ycord].getGcost()) {
                                        //Calculates F Cost of start node
                                        AllNodes[Xcord][Ycord].setGcost(Gcost);
                                        AllNodes[Xcord][Ycord].setHcost(GetNodeDistance(AllNodes[Xcord][Ycord], AllNodes[End.x][End.y]));
                                        //Sets neigbouring node parent to current node
                                        AllNodes[Xcord][Ycord].setParent(Current);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //If path is found
        if (FoundPath) {
            //Loops through parents of nodes leading from the end node to the start node
            Node Current = AllNodes[End.x][End.y];
            while (Current != AllNodes[Start.x][Start.y]) {
                Maze.setRGB(Current.getX(), Current.getY(), new Color(0, 255, 0).getRGB());
                MazePanel.repaint();
                Current = Current.getParent();
            }
            Maze.setRGB(Start.x, Start.y, new Color(0, 255, 0).getRGB());
            MazePanel.repaint();
            System.out.println("Path found");
        } //If path is not found
        else {
            System.out.println("No path found");
        }

    }

    private int GetNodeDistance(Node NodeA, Node NodeB) {
        /*
        This method finds the distance between two nodes (sum of edges between them)
        It then returns this distance
         */

        //Finds vector displacement between both nodes
        int XDistance = Math.abs(NodeA.getX() - NodeB.getX());
        int YDistance = Math.abs(NodeA.getY() - NodeB.getY());
        //Calculates distance between both nodes (distance of 10 between nodes next to each other and 14 between nodes diagonal to each other)
        if (XDistance > YDistance) {
            return 14 * YDistance + 10 * (XDistance - YDistance);
        } else {
            return 14 * XDistance + 10 * (YDistance - XDistance);
        }
    }

}
