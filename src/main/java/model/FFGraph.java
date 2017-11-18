package model;

import java.util.ArrayList;

public class FFGraph extends MyGraph{

    private int flowFrom,flowTo;
    public FFGraph(int[][] adjacencyMatrix, boolean isOriented,int flowFrom, int flowTo) {
        super(adjacencyMatrix,isOriented);
        this.flowFrom = flowFrom;
        this.flowTo = flowTo;
    }
}
