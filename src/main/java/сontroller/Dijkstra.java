package сontroller;

import model.DGraph;
import org.graphstream.graph.Graph;
import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.util.ArrayList;
import java.util.TreeSet;

public class Dijkstra {
    DGraph graph;
    Graph graphicalGraph;
    private int vertices;
    private static final int INF = 1000000000;
    private int start;
    public Dijkstra(DGraph graph, Graph graphicalGraph) {
        this.graph = graph;
        this.graphicalGraph=graphicalGraph;
        this.vertices = graph.getAdjacencyList().size();
        this.start=graph.getStart();
    }
    private final long RELAX_SLEEP=1000;
    private final long ITERATION_SLEEP=1500;
    private class Pair implements Comparable<Pair>{
        int distance,vertice;

        public Pair(int distance, int vertice) {
            this.distance = distance;
            this.vertice = vertice;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "distance=" + distance +
                    ", vertice=" + vertice +
                    '}';
        }

        @Override
        public int compareTo(Pair o) {
            int deltaDistance = this.distance-o.distance;
            return (deltaDistance!=0)?deltaDistance:this.vertice-o.vertice;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (distance != pair.distance) return false;
            return vertice == pair.vertice;
        }

        @Override
        public int hashCode() {
            int result = distance;
            result = 31 * result + vertice;
            return result;
        }
    }
    public ArrayList<Integer> getDistances() throws InterruptedException {
        ArrayList<Integer> results = new ArrayList<>();
        TreeSet<Pair> set = new TreeSet<>();
        for(int i=0;i<vertices;i++){
            results.add((i!=start)?INF:0);
            set.add(new Pair((i!=start)?INF:0,i));
            graphicalGraph.getNode("N"+i).addAttribute("ui.label","N"+i+":"+((i!=start)?INF:0));
        }
        for(int i=0;i<vertices;i++){
            try {
                Thread.sleep(ITERATION_SLEEP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pair p = set.first();
            set.remove(p);
            int v = p.vertice;
            graphicalGraph.getNode("N"+v).addAttribute("ui.selected");
            for(int j=0;j<graph.getAdjacencyList().get(v).size();j++){
                int to=graph.getAdjacencyList().get(v).get(j).getTo(), d=graph.getAdjacencyList().get(v).get(j).getWeight();
                graphicalGraph.getNode("N"+to).addAttribute("ui.clicked");
                Thread.sleep(RELAX_SLEEP);
                if(results.get(v)+d< results.get(to)){
                    set.remove(new Pair(results.get(to),to));
                    results.set(to,results.get(v)+d);
                    graphicalGraph.getNode("N"+to).setAttribute("ui.label","N"+to+":"+(results.get(v)+d));
                    Thread.sleep(RELAX_SLEEP);
                    set.add(new Pair(results.get(to),to));
                }
                graphicalGraph.getNode("N"+to).removeAttribute("ui.clicked");
            }
            graphicalGraph.getNode("N"+v).addAttribute("ui.style","fill-color: #00FF00, #100051;");
            graphicalGraph.getNode("N"+v).removeAttribute("ui.selected");
        }
        return results;
    }
}
