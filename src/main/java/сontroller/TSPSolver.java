package сontroller;

import model.DGraph;
import model.TSPGraph;
import org.graphstream.graph.Graph;
import scala.Int;

import java.util.ArrayList;
import java.util.TreeSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.StringTokenizer;

public class TSPSolver {
    TSPGraph graph;
    int count;
    Graph graphicalGraph;
    private int vertices;
    private static final int INF = 1000000000;
    private int start;
    Graph treeGraph;
    int level = 0;

    public TSPSolver(TSPGraph graph, Graph graphicalGraph, Graph treeGraph) {
        this.graph = graph;
        this.graphicalGraph = graphicalGraph;
        this.vertices = graph.getSize();
        this.start = graph.getStart();
        this.treeGraph = treeGraph;
    }

    private final long RELAX_SLEEP = 1000;
    private final long ITERATION_SLEEP = 1500;

    private class Pair implements Comparable<Pair> {
        int distance, vertice;

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
            int deltaDistance = this.distance - o.distance;
            return (deltaDistance != 0) ? deltaDistance : this.vertice - o.vertice;
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


    private void addNode(int level, int number, List<Integer> prev) {
        long t = System.nanoTime();
        if (prev.size() == 0) prev.add(0);
        if ((treeGraph.getNode(getTreeNodeName(level, number, prev))) == null) {
            treeGraph.addNode(getTreeNodeName(level, number, prev));
            treeGraph.getNode(getTreeNodeName(level, number, prev)).addAttribute("ui.label","N"+number);
            if (level == 0)
                treeGraph.getNode(getTreeNodeName(0, 0, new LinkedList<>())).addAttribute("ui.style", "fill-color:#FFFF00, #100051;");
        }
    }

    private String getTreeNodeName(int level, int number, List<Integer> prev) {
        String t = "";
        if (prev.size() == 0) prev.add(0);
        for (int i = 0; i < prev.size(); i++) t += prev.get(i);//t+=i
        return "L" + level + "N" + number + "P" + t;
    }

    public void solve() throws InterruptedException {
        for (int i = 0; i < vertices; i++) {
            graphicalGraph.getNode("N" + i).addAttribute("ui.label", "N" + i);
        }
        int x = 0;
        x = reduce(graph.getAdjacencyMatrix(), x, 9999, 9999);

        lobject l1 = new lobject(graph.getSize(), -1);
        //The root of the tree starting at city 0.
        l1.city = 0;
        l1.cost = x;
        l1.matrix = graph.getAdjacencyMatrix();
        l1.st.push(0);
        l1.remainingcity = new int[graph.getSize() - 1];
        l1.city_left_to_expand = graph.getSize() - 1;

        for (int o = 0; o < graph.getSize() - 1; o++) {
            l1.remainingcity[o] = o + 1;
        }
        //variable for keeping track of the number of expanded nodes
        //Stack DS for maintaining the nodes in the tree
        Stack<lobject> s = new Stack<>();
        //Pushing the root onto the stack
        s.push(l1);
        addNode(level++, l1.city, new LinkedList<>());
        //treeGraph.getNode(getTreeNodeName(0,0)).addAttribute("ui.style","fill-color:#FFFF00, #100051;");
        //Temporary variable for storing the best solution found so far
        lobject temp_best_solution = new lobject(graph.getSize(), -1);
        int current_best_cost = 10000000;
        //Stack iterations including backtracking
        // Starting the Tree Traversal - Traverses until stack gets empty i.e, all nodes have been expanded
        lobject prev = l1;
        while (!s.empty()) {
            // Variable Initialization
            List main = new ArrayList();
            lobject hell = s.pop();
            //[ 0, 3, 2, 5, 1, 4, 0 ]
            //Expand Stack only if the node is not a leaf node and if its cost is better than the best so far
            if (hell.city_left_to_expand == 0) {
                //Comparing the cost of this node to the best and updating if necessary
                if (hell.cost <= current_best_cost) {
                    temp_best_solution = hell;
                    current_best_cost = temp_best_solution.cost;
                }
            } else {
                if (hell.cost <= current_best_cost) {
                    // Expanding the latest node popped from stack
                    //addNode(level,hell.city);
                    count++;
                    //addEdge(level-1,temp_best_solution.city,level,hell.city);
                    expand(main, hell);

                    //Determing the order in which the expanded nodes should be pushed onto the stack
                    int[] arrow = new int[main.size()];
                    for (int pi = 0; pi < main.size(); pi++) {
                        lobject help = (lobject) main.get(pi);
                        arrow[pi] = help.cost;
                    }
                    // Sorting nodes in decreasing order based on their costs
                    int[] tempppp = decreasing_sort(arrow);
                    for (int pi = 0; pi < tempppp.length; pi++) {
                        // Pushing the node objects into stack in decreasing order
                        lobject l = (lobject) main.get(tempppp[pi]);
                        s.push(l);
                        //addNode(level, l.city);
                    }

                }
            }
        }

        // Checking if a solution is found
        if (temp_best_solution.st.size() == graph.getSize() && temp_best_solution.cost < 9000) {
            // Printing Tour Cost
            for (int st_i = 1; st_i < temp_best_solution.st.size(); st_i++) {
                addNode(st_i, temp_best_solution.st.get(st_i), temp_best_solution.st.subList(0, st_i));
                addEdge(st_i - 1, temp_best_solution.st.get(st_i - 1), temp_best_solution.st.subList(0, st_i), st_i, temp_best_solution.st.get(st_i),
                        temp_best_solution.st.subList(0, st_i + 1), "#00FF00");
                String n1 = getTreeNodeName(st_i - 1, temp_best_solution.st.get(st_i - 1), temp_best_solution.st.subList(0, st_i));
                String n2 = getTreeNodeName(st_i, temp_best_solution.st.get(st_i), temp_best_solution.st.subList(0, st_i + 1));
                treeGraph.getNode(n2).setAttribute("ui.style","fill-color: #00FF00, #100051;");
                treeGraph.getEdge("From " + n1 + " to " + n2).setAttribute("ui.style", "fill-color: #00FF00, #100051;");
            }
            System.out.println();
            System.out.println(current_best_cost);
            System.out.println();
            // Printing Optimal Tour
            List<Integer> ans = new ArrayList<>();
            System.out.print("[ ");
            for (int st_i = 0; st_i < temp_best_solution.st.size(); st_i++) {
                Integer k = temp_best_solution.st.get(st_i);
                ans.add(k);
                if (st_i > 0)
                    graphicalGraph.getEdge("from N" + Math.min(k, ans.get(ans.size() - 2)) + " to N" + Math.max(k, ans.get(ans.size() - 2))).addAttribute("ui.style", "fill-color: #00FF00, #100051;");
                System.out.print(k);
                System.out.print(", ");
            }
            graphicalGraph.getEdge("from N" + Math.min(0, ans.get(ans.size() - 1)) + " to N" + Math.max(0, ans.get(ans.size() - 1))).addAttribute("ui.style", "fill-color: #00FF00, #100051;");

            System.out.print("0 ");
            System.out.print("]");
            System.out.println();
        } else {
            System.out.println("\nNo Solution.\n");
        }
        for(int i=0;i<treeGraph.getNodeCount();i++){
            if(treeGraph.getNode(i).getDegree()==0)treeGraph.removeNode(i);

        }
//        treeGraph.removeNode("L4N1P0123");
  //      treeGraph.removeNode("L5N4P01234");
    }

    /*
	Min Function - Reduced the passed array with the value passed
	Input - Array to be reduced, minimum value with which to reduce
	Return - Reduced array
	*/

    public static int[] min(int[] array, int min) {
        // Recurse through array and reduce with the passed "min" value
        for (int j = 0; j < array.length; j++) {
            array[j] = array[j] - min;
        }
        // Return reduced array
        return array;
    }

	/*
	Minimum Function - Calculates the minimum value with which a matrix can be reduced
	Input - Array for which minimum value is to be calculated
	Return - Minimum value
	*/

    public static int minimum(int[] array) {
        // Declaring default as something lesser than infinity but higher than valid values
        int min = 9000;
        // Recursing through array to find minimum value
        for (int i = 0; i < array.length; i++) {
            // If value is valid i.e. less than infinity, reset min with that value
            if (array[i] < min) {
                min = array[i];
            }
        }
        // Check if min value is unchanged i.e. we met an infinity array
        if (min == 9000) {
            // Return 0 as nothing to reduce
            return 0;
        }
        // Else return the min value
        else {
            return min;
        }
    }

	/*
	lobject - Class which stores required data
	*/

    public static class lobject implements Cloneable {
        int city;
        int cost;
        int[][] matrix;
        int[] remainingcity;
        int city_left_to_expand;
        Stack<Integer> st;
        int prevcity;

        lobject(int number, int prevcity) {
            matrix = new int[number][number];
            st = new Stack<>();
            this.prevcity = prevcity;
        }
    }

	/*
	Method to print the contents of object on screen
	Return - Void
	*/

    public static void output(lobject l1) {
        System.out.println("============================================");
        System.out.println("============================================");
        System.out.println("This city is :" + l1.city);
        System.out.println("The node cost function:" + l1.cost);
        // Printing remaining cities
        System.out.println("The remaining cities to be expanded from this node");
        for (int h = 0; h < l1.remainingcity.length; h++) {
            System.out.print(l1.remainingcity[h]);
            System.out.print(" ");
        }
        System.out.println();
        System.out.println("The number of possible remaining expansions from this node are" + l1.city_left_to_expand);
        System.out.println();
        System.out.println("============================================");
        System.out.println("============================================");
    }


    private void addEdge(int level1, int node1, List<Integer> prev1, int level2, int node2, List<Integer> prev2, String color) {
        String n1 = getTreeNodeName(level1, node1, prev1);
        String n2 = getTreeNodeName(level2, node2, prev2);
        if (treeGraph.getEdge("From " + n1 + " to " + n2) == null) {
            treeGraph.addEdge("From " + n1 + " to " + n2, n1, n2, true);
            treeGraph.getEdge("From " + n1 + " to " + n2).addAttribute("ui.style", "fill-color: " + color + ", #100051;");
        }
    }

    private void addEdge(int level1, int node1, List<Integer> prev1, int level2, int node2, List<Integer> prev2) {
        addEdge(level1, node1, prev1, level2, node2, prev2, "#FFFFFF");
    }

    public List<Integer> stackToList(Stack<Integer> st){
        List<Integer> res = new ArrayList<>();
        for(int i=0;i<st.size();i++){
            res.add(st.get(i));
        }
        return res;
    }
    public void expand(List l, lobject o) throws InterruptedException {
        // Number of cities to be traversed
        int length = o.remainingcity.length;
        for (int i = 0; i < length; i++) {
            // Variable Initialization
            if (o.remainingcity[i] == 0) continue;
            int cost = o.cost;
            int city = o.city;
            Stack<Integer> st = new Stack<>();
            for (int st_i = 0; st_i < o.st.size(); st_i++) {
                Integer k = o.st.get(st_i);
                st.push(k);
            }
            st.push(o.remainingcity[i]);
            // Fetching matrix contents into a temporary matrix for reduction
            int[][] temparray = new int[o.matrix.length][o.matrix.length];
            for (int i_1 = 0; i_1 < temparray.length; i_1++) {
                for (int i_2 = 0; i_2 < temparray.length; i_2++) {
                    temparray[i_1][i_2] = o.matrix[i_1][i_2];
                }
            }
            //Adding the value of edge (i,j) to the cost
            cost = cost + temparray[city][o.remainingcity[i]];
            //Making the ith row and jth column to be infinity
            for (int j = 0; j < temparray.length; j++) {
                temparray[city][j] = 9999;
                temparray[j][o.remainingcity[i]] = 9999;
            }
            //Making (j,0) to be infinity
            temparray[o.remainingcity[i]][0] = 9999;
            //Reducing this matrix according to the rules specified
            int cost1 = reduce(temparray, cost, city, o.remainingcity[i]);
            // Updating object contents corresponding to current city tour
            //addNode(level,o.remainingcity[i]);
            //addEdge(level-1,city,level,o.remainingcity[i]);
            //[ 0, 3, 2, 5, 1, 4, 0 ]
            lobject finall = new lobject(o.matrix.length, o.city);

            finall.city = o.remainingcity[i];
            finall.cost = cost1;
            finall.matrix = temparray;
            int[] temp_array = new int[o.remainingcity.length];
            // Limiting the expansion in case of backtracking
            for (int i_3 = 0; i_3 < temp_array.length; i_3++) {
                temp_array[i_3] = o.remainingcity[i_3];
            }
            temp_array[i] = 0;
            finall.remainingcity = temp_array;
            finall.city_left_to_expand = o.city_left_to_expand - 1;
            finall.st =st;
            if(o.st.contains(finall.city))continue;
            else {
                System.out.println(finall.st);
                addNode(graph.getSize() - finall.city_left_to_expand - 1, finall.city, stackToList(finall.st));
                addEdge(graph.getSize() - finall.city_left_to_expand - 2, o.city, stackToList(o.st),
                        graph.getSize() - finall.city_left_to_expand - 1, finall.city, stackToList(finall.st));
            }
            l.add(finall);
            Thread.sleep(300);
        }
        Thread.sleep(500);
    }

	/*
	Reduce - Reduces the passed Matrix with minimum value possible
	Input - 2D Array to be reduced, Previous Step's Cost, Row to be processed, Column to be processed
	Return - Cost of Reduction
	*/

    public static int reduce(int[][] array, int cost, int row, int column) {
        // Variables
        // Arrays to store rows and columns to be reduced
        int[] array_to_reduce = new int[array.length];
        int[] reduced_array;
        // Variable to store updated cost
        int new_cost = cost;
        // Loop for reducing rows
        for (int i = 0; i < array.length; i++) {
            // If the row matches current city, do not reduce
            if (i == row) continue;
            // If the row is not corresponding current city, try to reduce
            for (int j = 0; j < array.length; j++) {
                // Fetch the row to be reduced
                array_to_reduce[j] = array[i][j];
            }
            // Check if current row can be reduced
            if (minimum(array_to_reduce) != 0) {
                // Updating new cost
                new_cost = minimum(array_to_reduce) + new_cost;
                // Reducing the row
                reduced_array = min(array_to_reduce, minimum(array_to_reduce));
                // Pushing the reduced row back into original array
                for (int k = 0; k < array.length; k++) {
                    array[i][k] = reduced_array[k];
                }
            }
        }
        // Loop for reducing columns
        for (int i = 0; i < array.length; i++) {
            // If column matches current city, do not reduce
            if (i == column) continue;
            // If column does not match current city, try to reduce
            for (int j = 0; j < array.length; j++) {
                // Fetching column to be reduced
                array_to_reduce[j] = array[j][i];
            }
            // Check if current column can be reduced
            if (minimum(array_to_reduce) != 0) {
                // Updating current cost
                new_cost = minimum(array_to_reduce) + new_cost;
                // Reducing the column
                reduced_array = min(array_to_reduce, minimum(array_to_reduce));
                // Pushing the reduced column back into original array
                for (int k = 0; k < array.length; k++) {
                    array[k][i] = reduced_array[k];
                }
            }
        }
        // Reduction done, return the new cost
        return new_cost;
    }

	/*
	Decreasing Sort - Sorts the passed array in decreasing order and returns back the index
	Input - Array to be sorted in decreasing order
	Return - Sorted array
	*/

    public static int[] decreasing_sort(int[] temp) {
        int[] y = new int[temp.length];
        // Retreiving Array contents
        for (int j = 0; j < temp.length; j++) {
            y[j] = temp[j];
        }
        int x;
        // Sorting
        for (int i = 0; i < temp.length - 1; i++) {
            if (temp[i] < temp[i + 1]) {
                x = temp[i];
                temp[i] = temp[i + 1];
                temp[i + 1] = x;
            }
        }
        int[] to_be_returned = new int[temp.length];
        // Putting sorted contents into array to be returned
        for (int j = 0; j < temp.length; j++) {
            for (int j1 = 0; j1 < temp.length; j1++) {
                if (temp[j] == y[j1]) {
                    to_be_returned[j] = j1;
                }
            }
        }
        return to_be_returned;
    }
}
