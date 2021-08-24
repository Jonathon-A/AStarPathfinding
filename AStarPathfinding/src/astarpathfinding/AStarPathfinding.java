package astarpathfinding;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;

public abstract class AStarPathfinding {

 

    public static void main(String[] args) {

        WindowSetup();

    }

    private static Node[][] AllNodes;
    private static BufferedImage InputMazeImage = null;
    private static BufferedImage MazeImage = null;

    public static void SetImage(BufferedImage InputImage) {

        InputMazeImage = InputImage;

        MazeImage = new BufferedImage(InputMazeImage.getWidth(), InputMazeImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        AllNodes = new Node[MazeImage.getWidth()][MazeImage.getHeight()];

        UpdateImageContrast(150);

        myFrame.UpdateImage(MazeImage);
    }

    public static void WindowSetup() {
        myFrame = new MainFrame(MazeImage);
        myFrame.setVisible(true);
    }

//    private static BufferedImage CreateBufferedImage(final String ImagePath) throws IOException {
//        //Returns buffered image created from image specified from given file path
//        return ImageIO.read(new File(UserDirectory + "Mazes\\" + ImagePath));
//    }
    private static MainFrame myFrame = new MainFrame(MazeImage);

    private static int CurrentContrast = 150;

    public static void UpdateImageContrast(int Contrast) {
        if (InputMazeImage != null) {

            CurrentContrast = Contrast;
            MazeImage.getGraphics().drawImage(InputMazeImage, 0, 0, null);
            for (int i = 0; i < MazeImage.getWidth(); i++) {
                for (int j = 0; j < MazeImage.getHeight(); j++) {

                    AllNodes[i][j] = new Node();
                    AllNodes[i][j].Reset();
                    if (MazeImage.getRGB(i, j) < new Color(Contrast, Contrast, Contrast).getRGB()) {
                        MazeImage.setRGB(i, j, new Color(0, 0, 0).getRGB());
                        AllNodes[i][j].setWall(true);
                    } else {
                        MazeImage.setRGB(i, j, new Color(255, 255, 255).getRGB());
                    }
                    AllNodes[i][j].SetCords(i, j);
                }
            }
            Open.clear();
            myFrame.UpdateImage();
            AtStart = true;
            myFrame.UpdateString("");
        }

    }

    private static boolean AtStart = true;

    private static Point Start = new Point(0, 0);
    private static Point End = new Point(0, 0);

    public static void StartAndEnd(int Xcord, int Ycord) {

        if (InputMazeImage != null && Xcord >= 0 && Xcord < MazeImage.getWidth() && Ycord >= 0 && Ycord < MazeImage.getHeight()) {

            if (AtStart) {

                if (MazeImage.getRGB(Xcord, Ycord) != new Color(0, 0, 0).getRGB()) {
                    UpdateImageContrast(CurrentContrast);
                    MazeImage.setRGB(Xcord, Ycord, new Color(0, 128, 0).getRGB());
                    myFrame.UpdateImage();
                    Start = new Point(Xcord, Ycord);
                    AtStart = false;
                }

            } else {

                if (MazeImage.getRGB(Xcord, Ycord) != new Color(0, 0, 0).getRGB()) {
                    MazeImage.setRGB(Xcord, Ycord, new Color(255, 0, 0).getRGB());
                    myFrame.UpdateImage();
                    End = new Point(Xcord, Ycord);

                    //  MazePanel.removeMouseListener(MazePanel.getMouseListeners()[0]);
                    Thread T1 = new Thread(() -> {

                        long startTime = System.currentTimeMillis();

                        String EndStr = AStarPathfinder();

                        long endTime = System.currentTimeMillis();

                        myFrame.UpdateString(EndStr + "! That took " + (endTime - startTime) + " milliseconds.");

                    });
                    T1.start();
                 
                }

            }

        }
    }

    private static final PriorityQueue<Node> Open = new PriorityQueue<>();

    private static String AStarPathfinder() {
        /*
        This method finds the shortest path between the start and end node that avoids obstacles
        It uses the A* search algorithm
         */

        //Calculate F Cost of start node
        AllNodes[Start.x][Start.y].setGcost(0);
        AllNodes[Start.x][Start.y].setHcost(GetNodeDistance(AllNodes[Start.x][Start.y], AllNodes[End.x][End.y]));
        //Adds start node to open set
        Open.add(AllNodes[Start.x][Start.y]);
        MazeImage.setRGB(Start.x, Start.y, Color.CYAN.getRGB());
        myFrame.UpdateImage();

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
            MazeImage.setRGB(Current.getX(), Current.getY(), Color.BLUE.getRGB());

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
                            if (Xcord >= 0 && Xcord < MazeImage.getWidth() && Ycord >= 0 && Ycord < MazeImage.getHeight()) {
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
                                        MazeImage.setRGB(Xcord, Ycord, Color.CYAN.getRGB());

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
            myFrame.UpdateImage();
        }
        String EndStr = "";
        //If path is found
        if (FoundPath) {
            //Loops through parents of nodes leading from the end node to the start node
            Node Current = AllNodes[End.x][End.y];
            while (Current != AllNodes[Start.x][Start.y]) {
                MazeImage.setRGB(Current.getX(), Current.getY(), new Color(0, 255, 0).getRGB());

                Current = Current.getParent();
            }

            EndStr = "Path found";
        } //If path is not found
        else {
            EndStr = "No path found";
        }
        MazeImage.setRGB(Start.x, Start.y, new Color(0, 128, 0).getRGB());
        MazeImage.setRGB(End.x, End.y, new Color(255, 0, 0).getRGB());
        myFrame.UpdateImage();
        AtStart = true;

        return EndStr;
    }

    private static int GetNodeDistance(Node NodeA, Node NodeB) {
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
