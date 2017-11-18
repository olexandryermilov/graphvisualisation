package model;

import java.util.ArrayList;

public class FFGraph {
    private ArrayList<ArrayList<GraphEdge>> adjacencyList;
    private int flowFrom,flowTo;
    private boolean isOriented;
    public FFGraph(ArrayList<ArrayList<GraphEdge>> adjacencyList, int flowFrom, int flowTo, boolean isOriented) {
        this.adjacencyList = adjacencyList;
        this.flowFrom = flowFrom;
        this.flowTo = flowTo;
        this.isOriented = isOriented;
    }
}
