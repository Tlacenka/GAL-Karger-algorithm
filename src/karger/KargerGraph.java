/**
 * This class implements the graph interface.
 * @author Katerina Pilatova (xpilat05)
 * @date 2017
 */


/**
 * - How and where will we edit graph? We will need coordinates to update the current one.
 *   - such as creating/removing/moving a new edge, vertex
 * - Which values do we need to send between windows and graph when storing/loading?
 * - Where will the reactions on buttons be implemented?
 *   - it will probably call operations of this class with instance in MainWin
 *   - initial buttons: 1 step further/back, 1 run/reset, run the whole algorithm
 *   - so does the karger algorithm go here as well? making the step etc
 */

package karger;

import java.util.Map;
import java.util.*;

import java.lang.Boolean;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxRectangle;

import org.w3c.dom.Document;

public class KargerGraph {

    private mxGraph graph; // graph
    public mxGraphComponent gc; // graph component (wrapper)

    private String runCounter;
    private String bestResult;

    private Object parent;
    private int vertex_size;

    private HashMap<mxCell,LinkedList<mxCell>> adjacencyList;

    public KargerGraph() {

        this.runCounter = "0";
        this.bestResult = "-";
        this.vertex_size = 60;

        // Create a graph
        this.graph = new mxGraph();

        this.adjacencyList = new HashMap<mxCell,LinkedList<mxCell>>();

        // Add graph style
        Map<String, Object> edgestyle = this.graph.getStylesheet().getDefaultEdgeStyle();
        edgestyle.put(mxConstants.STYLE_ENDARROW, "none");
        edgestyle.put(mxConstants.STYLE_STROKEWIDTH, 3);
        edgestyle.put(mxConstants.STYLE_STROKECOLOR, "#A10115");
        
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

        // Add all vertices
        for (Object v : this.graph.getChildVertices(this.parent)) {
            mxCell vertex = (mxCell)v;
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

        // Update adjacency list
        this.createAdjacencyList();

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

    /**
     * Resets all runs, re-creates the whole graph.
     */
    public void resetAlgorithm() {
        System.out.println("Reset algorithm.");
        return;
    }

    /**
     * Goes back one step in the algorithm.
     */
    public void undoStep() {
        return;
    }

    /**
     * Performs one step instead of the user.
     */
    public void nextStep() {
        return;
    }

    /**
     * Finishes current run of the algorithm.
     */
    public void finishRun() {
        return;
    }

    /**
     * Finishes the whole algorithm.
     */
    public void finishAlgorithm() {
        return;
    }
}
