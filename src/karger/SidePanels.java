package karger;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxIGraphModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class SidePanels {

    protected KargerGraph xGraph;

    // newly added node/edge
    protected String addedNode;
    protected String addedSrc;
    protected String addedDst;


    // removed node/edge
    protected String removedNode;
    protected String removedSrc;
    protected String removedDst;

    int vertex_size = 60;

    protected Object eX = null;
    protected Object eY = null;


    public DefaultListModel<String> algorithmModel = new DefaultListModel<String>();
    public JList<String> algorithmChoice;

    protected HashMap<mxCell,LinkedList<mxCell>> myList;



    /**
     * Add node from the input dialog to the node list.
     * @param model - node model.
     */
    public boolean addNode(DefaultListModel<String> model, HashMap<mxCell,LinkedList<mxCell>> adjacencyList, mxGraph graph){

        addedNode = JOptionPane.showInputDialog(null, "Node name");

        if(addedNode != null && adjacencyList != null){
            try {
                myList = adjacencyList;
                System.out.println("Add node list 2: " + myList);
                model.addElement(addedNode);

                // adds cells to the model in a single step
                graph.getModel().beginUpdate();
                try
                {
                    Object v1 = graph.insertVertex(graph.getDefaultParent(), null, addedNode, 20, 20, 60, 60);
                }
                finally
                {
                    // update the display
                    graph.getModel().endUpdate();
                }

                return true;

            } catch (Exception ex) {
                System.out.println(ex);
            }

        }

        return false;

    }



    /**
     * Add an edge from input dialog to the edge list.
     * @param edgeModel - edge model.
     */
    public boolean addEdge(DefaultListModel<String> edgeModel, mxGraph graph){

        String strEdge = null;



        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("start:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("end:"));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please enter start and end nodes", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            addedSrc = xField.getText();
            addedDst = yField.getText();

            System.out.println("start node: " + addedSrc);
            System.out.println("end node: " + addedDst);


            strEdge = addedSrc + " - " + addedDst;

            try {
                edgeModel.addElement(strEdge);

                for (Object v : graph.getChildVertices(graph.getDefaultParent())) {
                    mxCell vertex = (mxCell)v;

                    System.out.println("Graph vertex: " + vertex.getValue());

                    if(vertex.getValue().equals(addedSrc)){
                        System.out.println("SRC found: " + vertex.getValue());

                        eX = vertex;
                    }

                    if(vertex.getValue().equals(addedDst)){
                        System.out.println("DST found: " + vertex.getValue());

                        eY = vertex;
                    }

                }

                if(eX != null && eY != null){

                    graph.getModel().beginUpdate();
                    try
                    {
                        // graph.insertEdge(parent, null, '', source, target, style);
                        Object edgeX = graph.insertEdge(graph.getDefaultParent(), null, "", eX, eY);
                    }
                    finally
                    {
                        // update the display
                        graph.getModel().endUpdate();
                    }

                }

                return true;

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        return false;
    }



    /**
     * Remove selected item from node/edge list.
     * @param choice - which item should be removed.
     */
    public boolean removeItem(JList<String> choice, boolean isNode, mxGraph graph){

        try {

            if(choice.getSelectedValue() == null){
                return false;
            }

            if(isNode){

                System.out.println("REMOVE node ");

                removedNode = choice.getSelectedValue();

                System.out.println("Node " + removedNode);

                findObjects(graph, false);

                graph.getModel().beginUpdate();
                try {
                    // Remove a vertex. The related edge is removed as well.
                    graph.removeCells(new Object[]{eX});
                } finally {
                    graph.getModel().endUpdate();

                    //return true;
                }


            }else {
                System.out.println("REMOVE edge ");

                String[] splits = choice.getSelectedValue().split("[\\s-]+");

                removedSrc = splits[0];
                removedDst = splits[1];

                // System.out.println("Src " + removedSrc);
                // System.out.println("Dsc " + removedDst);

                findObjects(graph, true);

                System.out.println("REMOVE");

                graph.getModel().beginUpdate();
                try {
                    Object[] edgeR = graph.getEdgesBetween(eX, eY);
                    graph.removeCells(edgeR);

                } finally {
                    graph.getModel().endUpdate();

                    //return true;
                }

            }


            DefaultListModel<String> modelX = (DefaultListModel<String>)choice.getModel();
            modelX.remove(choice.getSelectedIndex());

            return true;

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return false;
    }



    public void findObjects(mxGraph graph, boolean isEdge){

        System.out.println("FIND OBJECTS \n ");

        for (Object v : graph.getChildVertices(graph.getDefaultParent())) {
            mxCell vertex = (mxCell)v;

            //System.out.println("Graph vertex: " + vertex.getValue());

            if(isEdge){

                System.out.println("is edge \n ");

                if(vertex.getValue().equals(removedSrc)){
                    System.out.println("SRC found: " + vertex.getValue());

                    eX = vertex;
                }

                if(vertex.getValue().equals(removedDst)){
                    System.out.println("DST found: " + vertex.getValue());

                    eY = vertex;
                }

            }else {

                System.out.println("is node: " + vertex.getValue() + " ==" + removedNode);

                if(vertex.getValue().equals(removedNode)){
                    System.out.println("NODE found: " + vertex.getValue());

                    eX = vertex;
                }
            }

        }
    }



    // unused
    public void setAlgorithmItem(int index, JList<String> algorithmChoice){
        algorithmChoice.setSelectedIndex(1);
    }



    protected void zoomByMouseWheel(MouseWheelEvent e, mxGraphComponent gc){
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
                gc.zoomIn();
            }
            else {
                gc.zoomOut();
            }
        }
    }


}
