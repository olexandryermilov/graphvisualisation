package model;

import java.util.ArrayList;

public class MyGraph {
    protected ArrayList<ArrayList<GraphEdge>> adjacencyList;
    protected boolean isOriented;
    protected int[][] adjacencyMatrix;
    public MyGraph(int[][] adjacencyMatrix, boolean isOriented) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.isOriented=isOriented;
    }
    public MyGraph() {

    }

    public MyGraph(ArrayList<ArrayList<GraphEdge>> adjacencyList, boolean isOriented) {
        this.adjacencyList = adjacencyList;
        this.isOriented = isOriented;
    }
    public ArrayList<ArrayList<GraphEdge>> getAdjacencyList() {
        return adjacencyList;
    }

    public boolean isOriented() {
        return isOriented;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }
}
