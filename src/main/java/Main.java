import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import сontroller.GraphReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    final static String filePath = "graph.txt";
    public static void main(String[] argc){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        GraphReader graphReader = new GraphReader(filePath);
        try {
            graphReader.readGraph();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Graph graphicalGraph = graphReader.getGraphicalGraph();
        graphicalGraph.addAttribute("ui.stylesheet","graph {\n" +
                "canvas-color: white;\n" +
                "fill-mode: gradient-radial;\n" +
                "fill-color: #002, #004;\n" +
                //"padding: 20px;\n" +
                "}\n" +
                "node {\n" +
                "shape: circle;\n" +
                //"size-mode: dyn-size;\n" +
                "size: 37px;\n" +
                "fill-mode: gradient-radial;\n" +
                "fill-color: #FFFC, #FFF0;\n" +
                "stroke-mode: none;\n" +
                "text-size: 11px;\n"+
                "text-color: #FFFFFF;\n"+
                "}\n" +
                "node:clicked {\n" +
                "fill-color: #F00A, #F000;\n" +
                "}\n" +
                "node:selected {\n" +
                "fill-color: #00FA, #00F0;\n" +
                "}\n" +
                "edge {\n" +
                "shape: cubic-curve;\n" +
                "size: 1px;\n" +
                "fill-color: #FFF3;\n" +
                "fill-mode: plain;\n" +
                "arrow-shape: none;\n" +
                "text-size: 14px;\n"+
                "text-color: #FFFFFF;\n"+
                "text-alignment: above;\n"+
                "}\n"/* +
                "sprite {\n" +
                "shape: circle;\n" +
                "fill-mode: gradient-radial;\n" +
                "fill-color: #FFF8, #FFF0;\n" +
                "}"*/);
        ArrayList<ArrayList<Integer[]>> adjacencyList = graphReader.getAdjacencyList();
        Viewer viewer = graphicalGraph.display();
        //graphicalGraph.getNode(0).
    }
}
