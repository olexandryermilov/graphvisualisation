package model;

public class HungaryGraph {
    public int[][] adjacencyMatrix;
    public int size;
    public HungaryGraph(int[][] adjacencyMatrix, int size){
        this.adjacencyMatrix=adjacencyMatrix;
        this.size=size;
    }
}
