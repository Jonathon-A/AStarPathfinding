package astarpathfinding;

public class Node implements Comparable<Node> {

    private int X;
    private int Y;

    private int Gcost; //Distance from starting node
    private int Hcost; //Distance from ending node

    private boolean Wall = false;
    private boolean Closed = false;

    private Node Parent;

    public void SetCords(int X, int Y) {
        this.X = X;
        this.Y = Y;

    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void setGcost(int Gcost) {
        this.Gcost = Gcost;
    }

    public void setHcost(int Hcost) {
        this.Hcost = Hcost;
    }

    public void setWall(boolean Closed) {
        this.Wall = Closed;
    }

    public void setClosed(boolean Closed) {
        this.Closed = Closed;
    }

    public void setParent(Node Parent) {
        this.Parent = Parent;
    }

    public boolean isWall() {
        return Wall;
    }

    public boolean isClosed() {
        return Closed;
    }

    public Node getParent() {
        return Parent;
    }

    public int getGcost() {
        return Gcost;
    }

    public int getHcost() {
        return Hcost;
    }

    public int getFcost() {
        return Gcost + Hcost;
    }

    @Override
    public int compareTo(Node t) {
     
        return (t.getFcost() < this.getFcost() || (t.getFcost() == this.getFcost() && t.getHcost() < this.getHcost())) ? 1 : -1;
      
    }

}
