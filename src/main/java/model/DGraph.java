package model;

import java.util.ArrayList;

public class DGraph extends MyGraph {
    private int start;
    public DGraph(ArrayList<ArrayList<GraphEdge>> adjacencyList, boolean isOriented, int start) {
        super(adjacencyList, isOriented);
        this.start = start;
    }

    public int getStart() {
        return start;
    }
}
