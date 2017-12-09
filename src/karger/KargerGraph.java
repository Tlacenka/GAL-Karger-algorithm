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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.model.mxGeometry;
import javax.swing.JList;

import org.w3c.dom.Document;

public class KargerGraph {

    private mxGraph graph; // graph
    public mxGraphComponent gc; // graph component (wrapper)

    private String runCounter;
    private int stepCounter;
    private String bestResult;

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
        this.bestResult = "-";
        this.vertex_size = 60;
        this.vertex_scaling = 10;
        this.stepCounter = 0;

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

   /* public mxIGraphModel getModelX() {
        return graph.getModel();
    }*/

    // Class for storing results of individual runs
    class KargerRecord {

        // V1 u V2 = V
        private JList<mxCell> V1; // First set of vertices
        private JList<mxCell> V2; // Second set of vertices
        private int cut; // cut value

        // Class constructor
        public KargerRecord(JList<mxCell> V1, JList <mxCell> V2, int cut) {

            this.V1 = V1;
            this.V2 = V2;
            this.cut = cut;
        }
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

            //System.out.println("Vertex: " + vertex.getValue());

            this.adjacencyList.put(vertex, new LinkedList<mxCell>());
        }

        // Link adjacent vertices
        for (Object e : this.graph.getChildEdges(this.parent)) {
            mxCell edge = (mxCell)e;
            mxCell src = (mxCell)edge.getSource();
            mxCell dst = (mxCell)edge.getTarget();

            // Check that all edges are between two vertices
            if ((src == null) || (dst == null)) {
                throw new IllegalArgumentException("Each edge must be between 2 vertices.");
            }

            this.adjacencyList.get(src).add(dst);
            this.adjacencyList.get(dst).add(src);
        }
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
     * @return Best result.
     */
    public String getBestResult() {
        return this.bestResult;
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
        System.out.println("Merge cells: " + (String)v1.getValue() + " and " + (String)v2.getValue());

        // Merge the cells

        // Redirect all edges from v2, remove v2 and connected edges
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
            if (v != v1) {

                // Create/update weighted edge
                if (this.adjacencyList.get(v).contains(v1)) {
                    
                    // Find edge
                    for (Object e : this.graph.getChildEdges(this.parent)) {
                        mxCell edge = (mxCell)e;
                        mxCell src = (mxCell)edge.getSource();
                        mxCell dst = (mxCell)edge.getTarget();

                        if (((src == v) && (dst == v1)) || ((src == v1) && (dst == v))) {
                            if (edge.getValue() != null) {
                                edge_val = ((String)edge.getValue() == "") ? 1 : Integer.parseInt((String)edge.getValue());
                                this.graph.getModel().beginUpdate();
                                {
                                    this.graph.getModel().setValue(edge, Integer.toString(edge_val + 1));
                                }
                                this.graph.getModel().endUpdate();
                            } else {
                                this.graph.getModel().beginUpdate();
                                {
                                    this.graph.getModel().setValue(edge, "2");
                                }
                                this.graph.getModel().endUpdate();
                            }
                            break;
                        }
                    }
                } else {
                    this.graph.getModel().beginUpdate();
                    {
                        Object e = this.graph.insertEdge(parent, null, "", v, v1);
                    }
                    this.graph.getModel().endUpdate();

                    this.adjacencyList.get(v).add(v1);
                    this.adjacencyList.get(v1).add(v);
                }

                this.adjacencyList.get(v).remove(v2);
            }
        }

        // Remove v2 and connected edges
        this.adjacencyList.remove(v2);

        this.graph.getModel().beginUpdate();
        {
            this.graph.removeCells(new Object[] {v2});
        }
        this.graph.getModel().endUpdate();
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
            
            // TODO disable undo button
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
            
            // TODO disable undo button
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
        // TODO cells based on random generator or user
        // 3 - wouldn't make sense to have empty set of vertices
        if (this.adjacencyList.keySet().toArray().length < 3) {
            return;
        }
        mxCell v1 = (mxCell)this.adjacencyList.keySet().toArray()[0];
        mxCell v2 = (mxCell)this.adjacencyList.keySet().toArray()[1];

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
        this.stepCounter = this.stepCounter + 1;

    }

    /**
     * Finishes current run of the algorithm.
     */
    public void finishRun() {
        while (this.adjacencyList.keySet().toArray().length >= 3) {
            this.nextStep();
        }

        // TODO update best result, number of runs
    }

    /**
     * Finishes the whole algorithm.
     */
    public void finishAlgorithm() {
        return;
    }


    public HashMap<mxCell,LinkedList<mxCell>> getAdjacencyList(){
        return this.adjacencyList;
    }

    public mxGraph xGetGraph(){
        return this.graph;
    }
}
