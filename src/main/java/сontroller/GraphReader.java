package сontroller;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.AdjacencyListGraph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphReader {
    private File fileToRead;
    private Graph graph;
    private ArrayList<ArrayList<Integer[]>> adjacencyList;
    private int flowFrom,flowTo;
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
    public void readGraph() throws FileNotFoundException {
        Scanner scanner = new Scanner(fileToRead);
        int vertices=0, edges=0;

        int from,to,weight;
        boolean isOriented;
        vertices=scanner.nextInt();
        edges = scanner.nextInt();
        isOriented = scanner.nextBoolean();
        adjacencyList = new ArrayList<>();
        for(int i=0;i<vertices;i++){
            adjacencyList.add(new ArrayList<Integer[]>());
        }
        this.graph = new DefaultGraph("Main FFGraph");
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
            Integer[] edge = {to,weight};
            adjacencyList.get(from).add(edge);
            if(graph.getNode(to)==null) {
                this.graph.addNode("N"+to);
                this.graph.getNode("N"+to).addAttribute("ui.label","N"+to);
            }
            if(isOriented){
                edge[0]=from;
                adjacencyList.get(to).add(edge);
            }
            System.out.println("from N"+from+" to N"+to+" weight: "+weight);
            this.graph.addEdge("from N"+from+" to N"+to,from,to,isOriented);
            this.graph.getEdge("from N"+from+" to N"+to).addAttribute("ui.label","0/"+weight);
            this.graph.getEdge("from N"+from+" to N"+to).addAttribute("layout.weight",weight);

        }
        flowFrom = scanner.nextInt();
        flowFrom--;
        flowTo = scanner.nextInt();
        flowTo--;
        //todo: handle errors while reading
    }
    public Graph getGraphicalGraph() {
        return graph;
    }
    public ArrayList<ArrayList<Integer[]>> getAdjacencyList(){
        return adjacencyList;
    }
}
