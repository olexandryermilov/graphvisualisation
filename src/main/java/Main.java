import org.graphstream.graph.Graph;
import сontroller.GraphReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    final static String filePath = "graph.txt";
    public static void main(String[] argc){
        GraphReader graphReader = new GraphReader(filePath);
        try {
            graphReader.readGraph();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Graph graphicalGraph = graphReader.getGraphicalGraph();
        ArrayList<ArrayList<Integer[]>> adjacencyList = graphReader.getAdjacencyList();
        graphicalGraph.display();
    }
}
