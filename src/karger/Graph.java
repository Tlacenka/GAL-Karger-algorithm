/**
 * This class implements the graph interface.
 * @author Katerina Pilatova (xpilat05)
 * @date 2017
 */


package karger;

import java.util.Map;
import java.util.*;
import java.lang.Boolean;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.BorderLayout;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

public class Graph {

    private JFrame frame;
    private JPanel panel;

    public Graph() {

        // Create a frame with a panel for graph
        this.frame = new JFrame("Karger Algorithm");
        this.panel = new JPanel(new BorderLayout());

        this.frame.add(panel);

        // Create a graph
        mxGraph graph = new mxGraph();


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
        mxGraphComponent gc = new mxGraphComponent(graph);

        // Add graph to panel
        this.panel.add(gc); // create graph pannel
    }

    public void show() {

        // Show panel in application window
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.pack();
        this.frame.setSize(600,600);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

}
