/**
 * This class implements the graph interface.
 * @author Katerina Pilatova (xpilat05)
 * @date 2017
 */

package karger;

import java.util.Map;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.Timer;
import java.util.TimerTask;

import java.lang.Boolean;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JList;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.model.mxGeometry;

import org.w3c.dom.Document;

public class KargerGraph {

    private mxGraph graph; // graph
    public mxGraphComponent gc; // graph component (wrapper)

    private String runCounter;
    private int maxRuns;
    private int stepCounter;
    private KargerRecord bestResult;
    private String bestResultCut;

    private ArrayList<KargerRecord> runs; // runs
    private ArrayList<Integer> curOrder;
    private ArrayList<mxCell> graphEdges; // ordered edges

    private Object parent;
    private int vertex_size; // width = height of vertex
    private int vertex_scaling; // by how much is vertex expanded when new node is merged into it

    private HashMap<mxCell,LinkedList<mxCell>> adjacencyList;

    /** Basic idea
     * - create ordered list of edges
     * - each time there is a run, store the order of removed edges somewhere?
     * - after run is finished, store V1, V2, value and edges to KargerResult
     * - keep the best result yet on the screen (and in best.xml just the 2 nodes)
     * - when algorithm is finished, find best result and display the graph
     * - figure out a way to randomly shuffle for one next run
     * - just go one by one when doing the whole thing
     * - find out if one can shuffle without repeating itself in a random way
     **/

    public KargerGraph() {

        this.runCounter = "0";
        this.bestResultCut = "-";
        this.vertex_size = 60;
        this.vertex_scaling = 10;
        this.stepCounter = 0;
        this.runs = new ArrayList<KargerRecord>();
        this.graphEdges = new ArrayList<mxCell>();
        this.bestResult = null;
        this.maxRuns = 0;

        // Create a graph
        this.graph = new mxGraph();

        this.adjacencyList = new HashMap<mxCell,LinkedList<mxCell>>();

        // Add graph style
        Map<String, Object> edgestyle = this.graph.getStylesheet().getDefaultEdgeStyle();
        edgestyle.put(mxConstants.STYLE_ENDARROW, "none");
        edgestyle.put(mxConstants.STYLE_STROKEWIDTH, 3);
        edgestyle.put(mxConstants.STYLE_STROKECOLOR, "#A10115");
        edgestyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        edgestyle.put(mxConstants.STYLE_FONTSIZE, "15");
        edgestyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        edgestyle.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
        
        Map<String, Object> vertstyle = this.graph.getStylesheet().getDefaultVertexStyle();
        vertstyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertstyle.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_ELLIPSE);
        vertstyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
        vertstyle.put(mxConstants.STYLE_FILLCOLOR, "#F0EFEA");
        vertstyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        vertstyle.put(mxConstants.STYLE_FONTSIZE, "15");
        vertstyle.put(mxConstants.STYLE_STROKECOLOR, "#D72C16");
        vertstyle.put(mxConstants.STYLE_GRADIENTCOLOR, "white");

        // Create default parent object
        this.parent = graph.getDefaultParent();

        // Create default graph
        this.createDefaultGraph();

        // Create adjacency list from it
        this.createAdjacencyList();

        // Create default edge order and randomize it
        this.curOrder = new ArrayList();
        int i = 0;
        for (Object e: this.graph.getChildVertices(this.parent)) {
            this.curOrder.add(i);
            i++;
        }
        this.shuffleEdges();

        // Wrap it in a component
        this.gc = new mxGraphComponent(this.graph);
        //this.gc.setEnabled(false); // disable graph editing ad hoc
        
        // Set background color, remove default border
        this.gc.getViewport().setOpaque(true);
        this.gc.getViewport().setBackground(Color.white);
        this.gc.setBorder(null);

        // Override mouse actions
        this.gc.getGraphControl().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent m) {

                // Find out if there is a cell based on mouse location
                mxCell cell = getCellFromMouse(m);
                if (cell == null) {
                    return;
                }

                // TODO: Specify what to do
            }
        });

    }

    // Class for storing results of individual runs
    class KargerRecord {

        // V1 u V2 = V
        private String V1; // First set of vertices
        private String V2; // Second set of vertices
        private int cut; // cut value
        private ArrayList<Integer> edgeOrder; // Order of removed edges - last stayed

        // Class constructor
        public KargerRecord(String V1, String V2, int cut,
                            ArrayList<Integer> edgeOrder) {

            this.V1 = V1;
            this.V2 = V2;
            this.cut = cut;
            this.edgeOrder = edgeOrder;
        }

        // Get edge order
        public ArrayList<Integer> getOrder() {
            return this.edgeOrder;
        }

        // Get V1
        public String getV1() {
            return this.V1;
        }

        // Get V2
        public String getV2() {
            return V2;
        }

        // Get cut value
        public int getCut() {
            return this.cut;
        }

        // Set cut value
        public void setCut(int cut) {
            this.cut = cut;
        }
    }

    /**
     * Compute n!
     */
    public int factorial(int n) {

        int result = 1;

        if ((n == 0) || (n == 1)) {
            return 1;
        }
        

        for (int i = 2; i <= n; i++) {
            result *= i;
        }

        return result;
    }

    /**
     * Shuffle edges until they are uniquely ordered.
     */
    public void shuffleEdges() {
        //System.out.print(this.curOrder);

        Boolean keepShuffling = true;
        Boolean isUnique = true;

        // Total number of possible runs is |E|!
        if (Integer.parseInt(this.runCounter) >= this.maxRuns) {
            return;
        }

        do {
            Collections.shuffle(this.curOrder);
            //System.out.print(this.curOrder);

            // Make sure it is unique
            for (KargerRecord r: this.runs) {
                if (this.curOrder.equals(r)) {
                    isUnique = false;
                    break;
                }
            }

            if (isUnique) {
                break;
            }
        } while (keepShuffling);
    }

    

    /**
     * Get cell from mouse coordinates.
     */
    private mxCell getCellFromMouse(MouseEvent m) {
        Object o = this.gc.getCellAt(m.getX(), m.getY());

        // If mouse is not on a cell, end action
        if ((o == null) || !(o instanceof mxCell)) {
            return null;
        } else {
            return (mxCell)o;
        }
    }

    /**
     * Create default graph to be displayed at the start
     */
    private void createDefaultGraph() {
        // Add some vertices and edges
        this.graph.getModel().beginUpdate();
        {
           Object vA = graph.insertVertex(parent, null, "A", 100, 180, vertex_size, vertex_size);
           Object vB = graph.insertVertex(parent, null, "B", 10, 250, vertex_size, vertex_size);
           Object vC = graph.insertVertex(parent, null, "C", 180, 320, vertex_size, vertex_size);
           Object vD = graph.insertVertex(parent, null, "D", 300, 180, vertex_size, vertex_size);
           Object vE = graph.insertVertex(parent, null, "E", 250, 100, vertex_size, vertex_size);
           Object vF = graph.insertVertex(parent, null, "F", 300, 20, vertex_size, vertex_size);
           Object vG = graph.insertVertex(parent, null, "G", 400, 120, vertex_size, vertex_size);
           Object vH = graph.insertVertex(parent, null, "H", 400, 250, vertex_size, vertex_size);
           Object eAB = graph.insertEdge(parent, null, "", vA, vB);
           Object eBC = graph.insertEdge(parent, null, "", vB, vC);
           Object eAC = graph.insertEdge(parent, null, "", vA, vC);
           Object eAD = graph.insertEdge(parent, null, "", vA, vD);
           Object eDE = graph.insertEdge(parent, null, "", vD, vE);
           Object eEF = graph.insertEdge(parent, null, "", vE, vF);
           Object eFG = graph.insertEdge(parent, null, "", vF, vG);
           Object eDG = graph.insertEdge(parent, null, "", vD, vG);
           Object eDH = graph.insertEdge(parent, null, "", vD, vH);
        }
        this.graph.getModel().endUpdate();
    }

    /**
     * Create adjacency list to graph.
     */
    public void createAdjacencyList() {

        // Remove previous adjacency list - TODO somehow empty it?
        this.adjacencyList = new HashMap<mxCell,LinkedList<mxCell>>();

        // Add all vertices
        for (Object v : this.graph.getChildVertices(this.parent)) {
            mxCell vertex = (mxCell)v;
            this.adjacencyList.put(vertex, new LinkedList<mxCell>());
        }

        // Link adjacent vertices, add edges into a list
        for (Object e : this.graph.getChildEdges(this.parent)) {
            mxCell edge = (mxCell)e;

            // Add edge to list of edges
            this.graphEdges.add((mxCell)edge);

            mxCell src = (mxCell)edge.getSource();
            mxCell dst = (mxCell)edge.getTarget();

            // Check that all edges are between two vertices
            if ((src == null) || (dst == null)) {
                throw new IllegalArgumentException("Each edge must be between 2 vertices.");
            }

            this.adjacencyList.get(src).add(dst);
            this.adjacencyList.get(dst).add(src);
        }

        // Get max runs - TODO set maximum edges to say 6
        this.maxRuns = this.factorial(this.graphEdges.size());
        //System.out.print(this.maxRuns);

    }

    /**
     * Prompts for the updated graph component.
     * @return Graph component object.
     */
    public mxGraphComponent getGraphComponent() {
        return this.gc;
    }

    /**
     * Prompts for the current run counter.
     * @return Run counter.
     */
    public String getRunCounter() {
        return this.runCounter;
    }

    /**
     * Prompts for the best result.
     * @return Best result (object).
     */
    public KargerRecord getBestResult() {
        return this.bestResult;
    }

    /**
     * Prompts for the best result (string).
     */
    public String getBestResultCut() {
        return this.bestResultCut;
    }

    /**
     * Creates a new graph by removing everything from the current one.
     */
    public void createEmptyGraph() {
        this.graph.getModel().beginUpdate();
        {
            this.graph.removeCells(this.graph.getChildCells(this.graph.getDefaultParent(), true, true));
        }
        this.graph.getModel().endUpdate();
    }

    /**
     * Loads graph from file.
     * @param filepath Path to file containing encoded graph.
     */
    public void loadGraph(String filepath) {

        // Empty current graph if one exists
        this.createEmptyGraph();

        Document graph_file;

        // Decode graph and store it to graph variable
        try {
            graph_file = mxXmlUtils.parseXml(mxUtils.readFile(filepath));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "File could not be loaded.");
            return;
        }

        mxCodec codec = new mxCodec(graph_file);
        codec.decode(graph_file.getDocumentElement(), graph.getModel());

        // Update default parent
        this.parent = graph.getDefaultParent();

        // Update adjacency list
        this.createAdjacencyList();

        System.out.println("Adj list created.");

        return;
    }

    /**
     * Returns encoded graph to be stored to file.
     * @return Encoded graph.
     */
    public void saveGraph(String filepath) {

        // Take current graph and encode it
        mxCodec codec = new mxCodec();
        String encodedGraph = mxXmlUtils.getXml(codec.encode(this.graph.getModel()));

        try {
            mxUtils.writeFile(encodedGraph, filepath);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "File could not be saved.");
            return;
        }
    }

    private void mergeCells(mxCell v1, mxCell v2) {

        int edge_val = 0;

        // Print out their values
        //System.out.println((String)v1.getValue() + " and " + (String)v2.getValue()); 

        // Merge the cells

        // Make the merged node bigger
        this.graph.getModel().beginUpdate();
        {
            v1.setGeometry(new mxGeometry(v1.getGeometry().getX(), v1.getGeometry().getY(),
                                          v1.getGeometry().getWidth() + 10,
                                          v1.getGeometry().getHeight() + 10));
        }
        this.graph.getModel().endUpdate();

        // New node is in alphabetical order
        String val = (String)v1.getValue() + (String)v2.getValue();
        char[] val_array = val.toCharArray();
        Arrays.sort(val_array);

        this.graph.getModel().beginUpdate();
        {
            this.graph.getModel().setValue(v1, new String(val_array));
        }
        this.graph.getModel().endUpdate();

        // Go through all vertices adjacent to v2
        for (Object v_obj : this.adjacencyList.get(v2)) {
            mxCell v = (mxCell)v_obj;

            // Skip v1
            if (v == v1) {
                continue;
            }

            // Find edge and redirect it
            for (Object e : this.graph.getChildEdges(this.parent)) {
                mxCell edge = (mxCell)e;
                mxCell src = (mxCell)edge.getSource();
                mxCell dst = (mxCell)edge.getTarget();

                // Redirect edge, update adjacency list
                if ((src == v) && (dst == v2)) {
                    //System.out.println("redirecting edge " + (String)edge.getSource().getValue() + " - " + (String)edge.getTarget().getValue());
                    this.graph.getModel().beginUpdate();
                    {
                        v2.removeEdge(edge, false);
                        edge.setTarget(v1);
                    }
                    this.graph.getModel().endUpdate();
                    //System.out.println("redirected to edge " + (String)edge.getSource().getValue() + " - " + (String)edge.getTarget().getValue());
                    // If there is no edge between v and v1, update adjacency
                    if (!this.multipleEdges(edge)) {
                        this.adjacencyList.get(v1).add(v);
                        this.adjacencyList.get(v).add(v1);
                    }
                } else if ((src == v2) && (dst == v)) {
                    this.graph.getModel().beginUpdate();
                    {
                        v2.removeEdge(edge, true);
                        edge.setSource(v1);
                    }
                    this.graph.getModel().endUpdate();

                    // If there is no edge between v and v1, update adjacency
                    if (!this.multipleEdges(edge)) {
                        this.adjacencyList.get(v1).add(v);
                        this.adjacencyList.get(v).add(v1);
                    }
                }
            }
        }

        this.gc.refresh();

        // Remove v2, contracted edge
        this.adjacencyList.remove(v2);
        this.graph.getModel().beginUpdate();
        {
            mxCell removingEdge = this.graphEdges.get(this.curOrder.get(this.stepCounter));
            //System.out.println("Removing edge " + (String)removingEdge.getSource().getValue() + " - " + (String)removingEdge.getTarget().getValue());
            this.graph.removeCells(new Object[] {this.graphEdges.get(this.curOrder.get(this.stepCounter))});

            //System.out.println("Removing node " + (String)v2.getValue());
            //System.out.println("Edge count " + Integer.toString(v2.getEdgeCount()));

            this.graph.removeCells(new Object[] {v2});
        }
        this.graph.getModel().endUpdate();
        this.graphEdges.set(this.curOrder.get(this.stepCounter), null);

        // Print out edges - TODO DEBUG
        for (Object e : this.graph.getChildEdges(this.parent)) {
            mxCell edge = (mxCell)e;
            mxCell src = (mxCell)edge.getSource();
            mxCell dst = (mxCell)edge.getTarget();
            //System.out.println("hello edges " + (String)src.getValue() + " - " + (String)dst.getValue());
        }

        this.gc.refresh();

    }
 
    /**
     * Handle multiple edges
     */
    public Boolean multipleEdges(mxCell edge) {

        Boolean found = false;

        // Handle multiple edges
        for (Object e2 : this.graph.getChildEdges(this.parent)) {
            mxCell edge2 = (mxCell)e2;
            mxCell src2 = (mxCell)edge2.getSource();
            mxCell dst2 = (mxCell)edge2.getTarget();

            if (edge == edge2) {
                continue;
            }

            if (((edge.getSource() == edge2.getSource()) &&
                 (edge.getTarget() == edge2.getTarget())) ||
                ((edge.getSource() == edge2.getTarget()) &&
                 (edge.getTarget() == edge2.getSource()))) {

                System.out.println("multiple edge " + (String)edge.getSource().getValue() + " - " + (String)edge.getTarget().getValue());

                // Store sum of edges' values
                int val1 = this.getEdgeValue(edge);
                int val2 = this.getEdgeValue(edge2);

                this.graph.getModel().beginUpdate();
                {
                    edge.setValue(Integer.toString(val1 + val2));
                }
                this.graph.getModel().endUpdate();

                System.out.println("updated value of edge " + (String)edge.getSource().getValue() + " - " + (String)edge.getTarget().getValue());

                // Store edge to graphEdges for the nth time
                if ( this.graphEdges.indexOf(edge2) == -1) {
                    System.out.println("Fuck my life 2");
                }

                this.graphEdges.set(this.graphEdges.indexOf(edge2), edge);

                // Remove edge2
                this.graph.getModel().beginUpdate();
                {
                    this.graph.removeCells(new Object[] {edge2});
                }
                this.graph.getModel().endUpdate();

                System.out.println("still existing edge " + (String)edge.getSource().getValue() + " - " + (String)edge.getTarget().getValue());

                found = true;
                break;

            }
        }

        return found;

    }

    /**
     * Get integer value of edge.
     */
    public int getEdgeValue(mxCell edge) {
        return ((String)edge.getValue() == "") ? 1 : Integer.parseInt((String)edge.getValue());
    }

    /**
     * Subclass for a timer used when highlighting cells before merging.
     */
     class HighlightBeforeMerging extends TimerTask {
        private final mxCell v1;
        private final mxCell v2;

        HighlightBeforeMerging(mxCell v1, mxCell v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @Override
        public void run() {
            // Merge cells
            mergeCells(v1, v2);
        }
     }

    /**
     * Resets all runs, re-creates the whole graph.
     */
    public void resetAlgorithm() {
        if (this.stepCounter > 0) {
            this.loadGraph("./examples/reset.xml");
            this.stepCounter = 0;
            this.runCounter = "0";
            this.bestResultCut = "-";
            this.runs = new ArrayList<KargerRecord>();
            this.shuffleEdges();
        }
    }

    /**
     * Goes back one step in the algorithm.
     */
    public void undoStep() {

        if (this.stepCounter > 0) {
            this.loadGraph("./examples/undo.xml");

            // Update step counter
            this.stepCounter = this.stepCounter - 1;
        }
    }

    /**
     * Performs one step instead of the user.
     */
    public void nextStep() {

        // If this is first step, save for resetting
        if (this.stepCounter == 0) {
            this.saveGraph("./examples/reset.xml");
        }

        // Save current algorithm
        this.saveGraph("./examples/undo.xml");


        // Choose cells to be merged
        // maybe save all edges in ArrayList if getChildEdges isn't ordered?

        // 3 - wouldn't make sense to have empty set of vertices
        if (this.adjacencyList.keySet().toArray().length < 3) {
            return;
        }

        // TODO fix
        // In order[stepCounter] is index of edge to be removed
        System.out.println("step " + this.stepCounter);
        System.out.println("edge at index " + this.curOrder.get(this.stepCounter));
        int edgeIndex = this.curOrder.get(this.stepCounter);
        mxCell v1 = (mxCell)this.graphEdges.get(edgeIndex).getSource();
        mxCell v2 = (mxCell)this.graphEdges.get(edgeIndex).getTarget();

        this.graph.getModel().beginUpdate();
        try {
            this.graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#D72C16", new Object[] {v1});
            this.graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#D72C16", new Object[] {v2});
        } finally {
            this.graph.getModel().endUpdate();
        }

        // v1 is the one being merged to
        if (v1.getGeometry().getX() >  v2.getGeometry().getX()) {
            mxCell v_swap = v1;
            v1 = v2;
            v2 = v_swap;
        }

        this.mergeCells(v1,v2);

        //this.graph.getModel().beginUpdate();
        //try {
            //(new Timer(true)).schedule(new HighlightBeforeMerging(v1, v2), 2*1000);
        //} finally {
            //this.graph.getModel().endUpdate();
        //}

        // Change colour back
        this.graph.getModel().beginUpdate();
        try {
            this.graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#F0EFEA", new Object[] {v1});
        } finally {
            this.graph.getModel().endUpdate();
        }

        // Update step counter
        this.stepCounter += 1;

    }

    /**
     * Finishes current run of the algorithm.
     */
    public void finishRun() {

        if (Integer.parseInt(this.runCounter) >= this.maxRuns) {
            return;
        }

        // Go on until there are 2 nodes left
        while (this.adjacencyList.keySet().toArray().length >= 3) {
            this.nextStep();
        }

        // Update results obtained by the lat run
        this.updateResults();

        // Update run counter
        this.runCounter = Integer.toString(Integer.parseInt(this.runCounter) + 1);

        // Shuffle edge order
        this.shuffleEdges();
    }

    /**
     * Update results - store/update list of results
     */
    public void updateResults() {
        // Last two vertices
        mxCell v1 = (mxCell)this.adjacencyList.keySet().toArray()[0];
        mxCell v2 = (mxCell)this.adjacencyList.keySet().toArray()[1];

        String V1 = (String)v1.getValue();
        String V2 = (String)v2.getValue();

        // V1 will be the lexiographically lesser one
        if (V1.compareTo(V2) > 0) {
            String swap = V1;
            V1 = V2;
            V2 = swap;
        }

        // Find out if these sets have been there before
        KargerRecord sameResult = this.getResult(V1, V2);
        KargerRecord newRecord = null;

        // Get edge value in the graph
        Object e_obj = this.graph.getChildEdges(this.parent)[0];
        mxCell edge = (mxCell)e_obj;
        int cut_val = Integer.parseInt((String)edge.getValue());

        // If result set is unique, Add new record
        if (sameResult == null) {
            newRecord = new KargerRecord(V1, V2, cut_val, this.curOrder);
            this.runs.add(newRecord);
        } else {
            // Otherwise, compare the two and save the better value
            if (cut_val < sameResult.getCut()) {
                sameResult.setCut(cut_val);
            }
        }

        // Update best result
        if (this.bestResultCut.equals("-") || Integer.parseInt(this.bestResultCut) > cut_val) {
            this.bestResultCut = Integer.toString(cut_val);
            this.bestResult = (sameResult == null) ? newRecord : sameResult;
        }
    }

    /**
     * Return result with these sets of vertices if it exists
     */
    public KargerRecord getResult(String V1, String V2) {

        for (KargerRecord r: this.runs) {
            if ((r.getV1().compareTo(V1) == 0) && (r.getV2().compareTo(V2) == 0)) {
                return r;
            }
        }

        return null;
    }

    /**
     * Finishes the whole algorithm.
     */
    public void finishAlgorithm() {

        // Run the whole thing maximum number of times
        while (Integer.parseInt(this.runCounter) < this.maxRuns) {
            this.finishRun();
        }

        // Display the best result - load from best.xml (TODO) or create graph based on best result (bleh)

        return;
    }


    public HashMap<mxCell,LinkedList<mxCell>> getAdjacencyList(){

        System.out.println("Adjacency list 25: " + this.adjacencyList);

        return adjacencyList;
    }
}
