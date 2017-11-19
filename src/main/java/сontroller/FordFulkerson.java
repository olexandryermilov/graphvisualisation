package сontroller;

import model.FFGraph;
import org.graphstream.graph.Graph;
import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.util.ArrayList;

public class FordFulkerson {
    private FFGraph graph;
    private Graph graphicGraph;
    private int[][] flow;
    int vertices;
    int maxFlow;
    private final int INF = 1000000000;
    int flowFrom,flowTo;
    int[][] adjacencyMatrix;
    public FordFulkerson(FFGraph graph, Graph graphicGraph){
        this.graph = graph;
        this.graphicGraph = graphicGraph;
        vertices = graph.getAdjacencyMatrix().length;
        flowFrom = graph.getFlowFrom();
        flowTo = graph.getFlowTo();
        flow = new int[vertices][vertices];
        adjacencyMatrix = graph.getAdjacencyMatrix();
        try {
            solve();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void solve() throws InterruptedException {
        for (;;)
        {
            int[] from = new int[vertices];
            for(int i=0;i<from.length;i++)from[i]=-1;
            int[] q = new int[vertices];
            int h=0, t=0;
            q[t++] = 0;
            from[0] = 0;
            for (int cur; h<t;)
            {
                cur = q[h++];
                graphicGraph.getNode("N"+cur).setAttribute("ui.clicked");
                Thread.sleep(1000);
                for (int v=0; v<vertices; v++) {
                    if (from[v] == -1 && adjacencyMatrix[cur][v] - flow[cur][v] > 0) {
                        graphicGraph.getNode("N" + v).setAttribute("ui.selected");
                        Thread.sleep(750);
                        q[t++] = v;
                        from[v] = cur;
                        graphicGraph.getNode("N"+v).removeAttribute("ui.selected");
                    }
                }
                graphicGraph.getNode("N"+cur).removeAttribute("ui.clicked");
            }

            if (from[flowTo] == -1)
                break;
            int cf = INF;
            for (int cur=flowTo; cur!=0; )
            {
                int prev = from[cur];
                cf = Math.min (cf, adjacencyMatrix[prev][cur]-flow[prev][cur]);
                cur = prev;
            }

            for (int cur=flowTo; cur!=0; )
            {
                int prev = from[cur];
                flow[prev][cur] += cf;
                flow[cur][prev] -= cf;
                cur = prev;
            }

        }

        maxFlow = 0;
        for (int i=0; i<vertices; i++)
            if (adjacencyMatrix[0][i]!=0)
                maxFlow += flow[0][i];
        System.out.println(maxFlow);
    }
}
