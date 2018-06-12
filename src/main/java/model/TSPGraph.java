package model;

import java.util.ArrayList;
import java.util.Stack;

public class TSPGraph extends MyGraph {
    private int start;
    private int size;
    public int city;
    public int cost;
    public int[][] matrix ;
    public int[] remainingcity;
    public int city_left_to_expand;
    public Stack<Integer> st;
    public TSPGraph(int number) {
        matrix = new int[number][number];
        st = new Stack<Integer>();
    }
    public TSPGraph(ArrayList<GraphVertice> verticeList, int start) {
        super();
        size = verticeList.size();
        adjacencyMatrix = new int[size][size];
        isOriented = false;
        for (int i = 0; i < verticeList.size(); i++) {
            for (int j = 0; j < verticeList.size(); j++) {
                adjacencyMatrix[i][j] = (int)Math.sqrt(verticeList.get(i).squaredDistance(verticeList.get(j)));
            }
        }
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public int getStart() {
        return start;
    }
}
