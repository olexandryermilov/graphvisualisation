import model.DGraph;
import model.FFGraph;
import model.HungaryGraph;
import model.TSPGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.Viewer;
import сontroller.*;

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
    private static void solveTSP() throws InterruptedException {
        GraphReader graphReader = new GraphReader(filePath);
        try {
            graphReader.readTSPGraph();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Graph graphicalGraph = graphReader.getGraphicalGraph();
        try {
            addStyleSheet(graphicalGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Viewer viewer = graphicalGraph.display();
        Graph treeGraph = new DefaultGraph("TreeGraph");
        try {
            addStyleSheet(treeGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Viewer viewer2= treeGraph.display();
        TSPGraph graph = graphReader.getTSPGraph();
        TSPSolver tsp = new TSPSolver(graph,graphicalGraph,treeGraph);
            tsp.solve();
    }

    private static void solveHungary(){
        GraphReader graphReader = new GraphReader(filePath);
        try {
            graphReader.readHungaryGraph();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*Graph graphicalGraph = graphReader.getGraphicalGraph();
        try {
            addStyleSheet(graphicalGraph);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Viewer viewer = graphicalGraph.display();*/
        HungaryGraph graph = graphReader.getHungaryGraph();
        HungarySolver hungary = new HungarySolver(graph);
            hungary.solve();

    }

    public static void main(String[] argc) throws InterruptedException {
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
                if(algo.equals("TSP")){
                    solveTSP();
                }
                else{
                    if(algo.equals("Hungary")){
                        solveHungary();
                    }
                    else{
                        System.out.println("Bad algorithm request");
                    }
                }
            }
        }

    }
}
