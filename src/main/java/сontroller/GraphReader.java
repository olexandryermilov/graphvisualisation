package сontroller;

import model.DGraph;
import model.FFGraph;
import model.GraphEdge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;


import java.io.*;
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
            //this.graph.getEdge("from N"+from+" to N"+to).addAttribute("layout.weight",weight);
        }
        start = scanner.nextInt();
        start--;
        dGraph = new DGraph(adjacencyList,isOriented,start);
        //todo: handle errors while reading
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
            GraphEdge edgeFrom = new GraphEdge(from,to,weight);
            adjacencyMatrix[from][to]=weight;
            if(graph.getNode(to)==null) {
                this.graph.addNode("N"+to);
                this.graph.getNode("N"+to).addAttribute("ui.label","N"+to);
            }
            if(!isOriented){
                GraphEdge edgeTo = new GraphEdge(to,from,weight);
                adjacencyMatrix[to][from]=weight;
            }
            System.out.println("from N"+from+" to N"+to+" weight: "+weight);
            this.graph.addEdge("from N"+from+" to N"+to,from,to,isOriented);
            this.graph.getEdge("from N"+from+" to N"+to).addAttribute("ui.label","0/"+weight);
            this.graph.getEdge("from N"+from+" to N"+to).addAttribute("layout.weight",weight);
        }
        flowFrom = scanner.nextInt();
        flowTo = scanner.nextInt();
        flowFrom--;
        flowTo--;
        ffGraph = new FFGraph(adjacencyMatrix,isOriented,flowFrom,flowTo);
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
}
