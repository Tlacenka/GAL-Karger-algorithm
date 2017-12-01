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
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxRectangle;

public class KargerGraph {

    private JFrame frame;
    private JPanel panel;

    private mxGraph graph; // graph
    public mxGraphComponent gc; // graph component (wrapper)

    public KargerGraph() {

        // Create a graph
        this.graph = new mxGraph();
        //this.graph.setMinimumGraphSize(new mxRectangle(600,600));


        // Add graph style
        Map<String, Object> edgestyle = graph.getStylesheet().getDefaultEdgeStyle();
        edgestyle.put(mxConstants.STYLE_ENDARROW, "none");
        edgestyle.put(mxConstants.STYLE_STROKEWIDTH, 3);
        
        Map<String, Object> vertstyle = graph.getStylesheet().getDefaultVertexStyle();
        vertstyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertstyle.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_ELLIPSE);
        vertstyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
    

        // Create default parent object
        Object parent = graph.getDefaultParent();

        // Add some vertices and edges
        graph.getModel().beginUpdate();
        {
           Object vA = graph.insertVertex(parent, null, "A", 100, 180, 50, 50);
           Object vB = graph.insertVertex(parent, null, "B", 10, 250, 50, 50);
           Object vC = graph.insertVertex(parent, null, "C", 180, 320, 50, 50);
           Object vD = graph.insertVertex(parent, null, "D", 300, 180, 50, 50);
           Object vE = graph.insertVertex(parent, null, "E", 250, 100, 50, 50);
           Object vF = graph.insertVertex(parent, null, "F", 300, 20, 50, 50);
           Object vG = graph.insertVertex(parent, null, "G", 400, 120, 50, 50);
           Object vH = graph.insertVertex(parent, null, "H", 400, 250, 50, 50);
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
        graph.getModel().endUpdate();

        // Wrap it in a component
        this.gc = new mxGraphComponent(this.graph);
    }

    /**
     * Prompts for the updated graph component.
     * @return Graph component object.
     */
    public mxGraphComponent getGraphComponent() {
        return this.gc;
    }

    /**
     * Creates a new graph by removing everything from the current one.
     */
    private void createEmptyGraph() {
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

        // Load encoded graph from file
        // Decode graph and store it to graph variable
        return;
    }

    /**
     * Returns encoded graph to be stored to file.
     * @return Encoded graph.
     */
    public String getEncodedGraph() {
        // Take current graph and encode it
        // Return encoded graph to be stored
        return "";
    }

}
