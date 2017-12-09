import model.DGraph;
import model.FFGraph;
import org.graphstream.graph.Graph;

import org.graphstream.ui.view.Viewer;
import сontroller.Dijkstra;
import сontroller.FordFulkerson;
import сontroller.GraphReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class Main {
    final static String filePath = "graph.txt";
    final static String configPath = "config.txt";
    private static void addStyleSheet(Graph graph) throws IOException {
        graph.setAttribute("ui.stylesheet","url('file:///"+new File(".").getCanonicalPath().replaceAll("\\u002F","\\u005C")+"/src/main/java/visualizer-stylesheet.css')");

    }
    private static void solveDijkstra(){
        GraphReader graphReader = new GraphReader(filePath);
        try {
            graphReader.readDGraph();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Graph graphicalGraph = graphReader.getGraphicalGraph();
        try {
            addStyleSheet(graphicalGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Viewer viewer = graphicalGraph.display();
        DGraph graph = graphReader.getdGraph();
        try {
            System.out.println(new Dijkstra(graph,graphicalGraph).getDistances());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void solveFF(){
        GraphReader graphReader = new GraphReader(filePath);
        try {
            graphReader.readFFGraph();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Graph graphicalGraph = graphReader.getGraphicalGraph();
        try {
            addStyleSheet(graphicalGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Viewer viewer = graphicalGraph.display();
        FFGraph graph = graphReader.getFFGraph();
        FordFulkerson ff = new FordFulkerson(graph,graphicalGraph);
    }
    public static void main(String[] argc){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(configPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String algo = scanner.next();
        if(algo.equals("Dijkstra")){
            solveDijkstra();
        }
        else{
            if(algo.equals("FF"))
                solveFF();
            else{
                System.out.println("Bad algorithm request");
            }
        }

    }
}
