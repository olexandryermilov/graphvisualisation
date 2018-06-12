package model;

import java.util.Objects;

public class GraphVertice  {
    private int x,y;

    public GraphVertice(int x, int y){
        this.x=x;
        this.y=y;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int square(int x){
        return x*x;
    }
    public int squaredDistance(GraphVertice other){
        return square(this.x-other.x)+square(this.y-other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphVertice that = (GraphVertice) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "GraphVertice{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
