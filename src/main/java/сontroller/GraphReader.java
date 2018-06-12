package сontroller;

import model.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class GraphReader {
    private File fileToRead;
    private Graph graph;
    private ArrayList<ArrayList<GraphEdge>> adjacencyList;
    private int flowFrom,flowTo;
    private int start;
    private FFGraph ffGraph;
    private DGraph dGraph;
    private int[][] adjacencyMatrix;
    private TSPGraph tspGraph;
    private HungaryGraph hungaryGraph;
    public GraphReader(String fileToReadPath){
        try{
            this.fileToRead = new File(fileToReadPath);
        }
        catch (Exception e){
            System.out.println(
                    e.getCause()
            );
        }
    }
    public void readDGraph() throws FileNotFoundException {
        Scanner scanner = new Scanner(fileToRead);
        int vertices=0, edges=0;
        int from,to,weight;
        boolean isOriented;
        vertices=scanner.nextInt();
        edges = scanner.nextInt();
        isOriented = scanner.nextBoolean();
        adjacencyList = new ArrayList<>();
        for(int i=0;i<vertices;i++){
            adjacencyList.add(new ArrayList<>());
        }
        this.graph = new DefaultGraph("Main DGraph");
        for(int i=0;i<vertices;i++){
            graph.addNode("N"+i);
        }
        for(int i=0;i<edges;i++){
            from = scanner.nextInt();
            to = scanner.nextInt();
            from--;
            to--;
            weight = scanner.nextInt();
            if(graph.getNode(from)==null){
                this.graph.addNode("N"+from);
                this.graph.getNode("N"+from).addAttribute("ui.label","N"+from);
            }
            GraphEdge edgeFrom = new GraphEdge(from,to,weight);
            adjacencyList.get(from).add(edgeFrom);
            if(graph.getNode(to)==null) {
                this.graph.addNode("N"+to);
                this.graph.getNode("N"+to).addAttribute("ui.label","N"+to);
            }
            if(!isOriented){
                GraphEdge edgeTo = new GraphEdge(to,from,weight);
                adjacencyList.get(to).add(edgeTo);
            }
            System.out.println("from N"+from+" to N"+to+" weight: "+weight);
            this.graph.addEdge("from N"+from+" to N"+to,from,to,isOriented);
            this.graph.getEdge("from N"+from+" to N"+to).addAttribute("ui.label",weight);
            if(isOriented)graph.getEdge("from N"+from+" to N"+to).addAttribute("ui.style","arrow-shape: arrow;");
            //this.graph.getEdge("from N"+from+" to N"+to).addAttribute("layout.weight",weight);
        }
        start = scanner.nextInt();
        start--;
        dGraph = new DGraph(adjacencyList,isOriented,start);
    }
    public void readFFGraph() throws FileNotFoundException {
        Scanner scanner = new Scanner(fileToRead);
        int vertices=0, edges=0;
        int from,to,weight;
        boolean isOriented;
        vertices=scanner.nextInt();
        edges = scanner.nextInt();
        isOriented = scanner.nextBoolean();
        adjacencyMatrix = new int[vertices][vertices];
        this.graph = new DefaultGraph("Main FFGraph");
        for(int i=0;i<vertices;i++){
            graph.addNode("N"+i);
        }
        for(int i=0;i<edges;i++){
            from = scanner.nextInt();
            to = scanner.nextInt();
            from--;
            to--;
            weight = scanner.nextInt();
            if(graph.getNode(from)==null){
                this.graph.addNode("N"+from);
                this.graph.getNode("N"+from).addAttribute("ui.label","N"+from);
            }
            adjacencyMatrix[from][to]=weight;
            if(graph.getNode(to)==null) {
                this.graph.addNode("N"+to);
                this.graph.getNode("N"+to).addAttribute("ui.label","N"+to);
            }
            if(!isOriented){
                adjacencyMatrix[to][from]=weight;
                //this.graph.addEdge("from N"+to+" to N"+from,(Node)graph.getNode("N"+to),graph.getNode("N"+from));
            }
            System.out.println("from N"+from+" to N"+to+" weight: "+weight);
            this.graph.addEdge("from N"+from+" to N"+to,from,to,!isOriented);
            this.graph.getEdge("from N"+from+" to N"+to).addAttribute("ui.label","0/"+weight);
            //this.graph.getEdge("from N"+from+" to N"+to).addAttribute("layout.weight",weight);
        }
        flowFrom = scanner.nextInt();
        flowTo = scanner.nextInt();
        flowFrom--;
        flowTo--;
        ffGraph = new FFGraph(adjacencyMatrix,isOriented,flowFrom,flowTo);
    }
    public void readTSPGraph() throws FileNotFoundException {
        Scanner scanner = new Scanner(fileToRead);
        int vertices, edges=0;
        int from,to,weight;
        boolean isOriented=false;
        vertices=scanner.nextInt();
        this.graph = new DefaultGraph("Main TSPGraph");
        ArrayList<GraphVertice> graphVertices = new ArrayList<>();
        for(int i=0;i<vertices;i++){
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            graphVertices.add(new GraphVertice(x,y));
            graph.addNode("N"+i);
        }
        start = scanner.nextInt();
        start--;
        tspGraph = new TSPGraph(graphVertices,start);
        for(int i=0;i<vertices;i++) {
            for (int j = i + 1; j < vertices; j++) {
                from = i;
                to = j;
                weight = tspGraph.getAdjacencyMatrix()[i][j];
                if (graph.getNode(from) == null) {
                    this.graph.addNode("N" + from);
                    this.graph.getNode("N" + from).addAttribute("ui.label", "N" + from);
                }
                GraphEdge edgeFrom = new GraphEdge(from, to, weight);
                if (graph.getNode(to) == null) {
                    this.graph.addNode("N" + to);
                    this.graph.getNode("N" + to).addAttribute("ui.label", "N" + to);
                }
                GraphEdge edgeTo = new GraphEdge(to, from, weight);
                System.out.println("from N" + from + " to N" + to + " weight: " + weight);
                this.graph.addEdge("from N" + from + " to N" + to, from, to, false);
                this.graph.getEdge("from N" + from + " to N" + to).addAttribute("ui.label", weight);
            }
        }
    }

    public void readHungaryGraph() throws FileNotFoundException {
        Scanner scanner = new Scanner(fileToRead);
        int vertices;
        boolean isOriented=true;
        vertices=scanner.nextInt();
        //this.graph = new DefaultGraph("Main TSPGraph");
        adjacencyMatrix = new int[vertices+1][vertices+1];
        for(int i=1;i<=vertices;i++){
            for(int j=1;j<=vertices;j++){
                adjacencyMatrix[i][j]=scanner.nextInt();
            }
        }
        hungaryGraph = new HungaryGraph(adjacencyMatrix,vertices);
        /*for(int i=0;i<vertices;i++) {
            for (int j = i + 1; j < vertices; j++) {
                from = i;
                to = j;
                weight = tspGraph.getAdjacencyMatrix()[i][j];
                if (graph.getNode(from) == null) {
                    this.graph.addNode("N" + from);
                    this.graph.getNode("N" + from).addAttribute("ui.label", "N" + from);
                }
                GraphEdge edgeFrom = new GraphEdge(from, to, weight);
                if (graph.getNode(to) == null) {
                    this.graph.addNode("N" + to);
                    this.graph.getNode("N" + to).addAttribute("ui.label", "N" + to);
                }
                GraphEdge edgeTo = new GraphEdge(to, from, weight);
                System.out.println("from N" + from + " to N" + to + " weight: " + weight);
                this.graph.addEdge("from N" + from + " to N" + to, from, to, false);
                this.graph.getEdge("from N" + from + " to N" + to).addAttribute("ui.label", weight);
            }
        }*/
    }

    public TSPGraph getTSPGraph(){
        return tspGraph;
    }
    public Graph getGraphicalGraph() {
        return graph;
    }
    public FFGraph getFFGraph(){
        return ffGraph;
    }

    public DGraph getdGraph() {
        return dGraph;
    }

    public HungaryGraph getHungaryGraph() {
        return hungaryGraph;
    }
}
