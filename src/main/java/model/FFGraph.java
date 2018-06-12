package model;

public class FFGraph extends MyGraph {

    private int flowFrom,flowTo;
    public FFGraph(int[][] adjacencyMatrix, boolean isOriented,int flowFrom, int flowTo) {
        super(adjacencyMatrix,isOriented);
        this.flowFrom = flowFrom;
        this.flowTo = flowTo;
    }

    public int getFlowFrom() {
        return flowFrom;
    }

    public int getFlowTo() {
        return flowTo;
    }
}
