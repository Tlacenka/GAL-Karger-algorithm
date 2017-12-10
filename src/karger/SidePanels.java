package karger;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;

import com.mxgraph.swing.mxGraphComponent;

import java.awt.event.MouseWheelEvent;


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

    private int vertex_size = 60;

    protected Object sourceVertex = null;
    protected Object targetVertex = null;

    private int yPosition = 30;
    private int xPosition = 30;
    private int xGap = 20;
    private int yGap = 20;


    public DefaultListModel<String> algorithmModel = new DefaultListModel<String>();
    public JList<String> algorithmChoice;

    DefaultListModel<String> nModel = new DefaultListModel<String>();
    mxGraph nGraph;
    HashMap<mxCell,LinkedList<mxCell>> adjList;

    protected HashMap<mxCell,LinkedList<mxCell>> myList;



    /**
     * Add node from the input dialog to the node list.
     * @param model - node model
     * @param adjacencyList - list of existing vertices
     * @param graph - current graph we work with
     * @return true if action was successful
     */
    public boolean addNode(DefaultListModel<String> model, HashMap<mxCell,LinkedList<mxCell>> adjacencyList, mxGraph graph){

        addedNode = JOptionPane.showInputDialog(null, "Node name");

        if(addedNode != null){
            try {
                myList = adjacencyList;

                // vertex with the same name as existing one cannot be added
                for (mxCell vertex: this.myList.keySet()) {

                    if(vertex.getValue().equals(addedNode)){
                        warningDialog("Vertex with value like this already exists.", "Warning");
                        return false;
                    }

                }

                model.addElement(addedNode);

                // adds cells to the model in a single step
                graph.getModel().beginUpdate();
                try
                {
                    Object v1 = graph.insertVertex(graph.getDefaultParent(), null, addedNode, xPosition, yPosition, this.vertex_size, this.vertex_size);
                }
                finally
                {
                    // update the display
                    graph.getModel().endUpdate();
                    yPosition += yGap;
                    xPosition += xGap;
                }

                return true;

            } catch (Exception ex) {
                System.out.println(ex);
            }

        }

        return false;
    }



    /**
     * Add an edge between two vertices specified by the input dialog.
     * @param edgeModel - current edge model we work with
     * @param graph - current graph we work with
     * @return true if action was successful
     */
    public boolean addEdge(DefaultListModel<String> edgeModel, mxGraph graph, HashMap<mxCell,LinkedList<mxCell>> adjacencyList){

        String strEdge = null;
        boolean response = false;

        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("source:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("target:"));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please enter source and target nodes", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            addedSrc = xField.getText();
            addedDst = yField.getText();
            strEdge = addedSrc + " - " + addedDst;

            try {

                // go through all vertices in the graph and check if the vertices from the input exist
                for (Object v : graph.getChildVertices(graph.getDefaultParent())) {
                    mxCell vertex = (mxCell)v;

                    if(vertex.getValue().equals(addedSrc)){
                        //System.out.println("SRC: " + vertex.getValue());
                        sourceVertex = vertex;
                    }

                    if(vertex.getValue().equals(addedDst)){
                        //System.out.println("DST: " + vertex.getValue());
                        targetVertex = vertex;
                    }
                }


                // one of the vertices doesn't exist
                if(sourceVertex == null || targetVertex == null){

                    if(sourceVertex == null)
                        response = desicionDialog("Do you want to create this vertex?", "Vertex " + addedSrc + " doesn't exist");

                    if(targetVertex == null)
                        response = desicionDialog("Do you want to create this vertex?", "Vertex " + addedDst + " doesn't exist");

                    if(response){
                        if(sourceVertex == null)
                            nModel.addElement(addedSrc);
                        if(targetVertex == null)
                            nModel.addElement(addedDst);

                        // adds cells to the model in a single step
                        graph.getModel().beginUpdate();
                        try
                        {
                            Object v1;

                            if(sourceVertex == null){
                                v1 = graph.insertVertex(graph.getDefaultParent(), null, addedSrc, xPosition, yPosition, this.vertex_size, this.vertex_size);
                                sourceVertex = v1;
                                yPosition += yGap;
                                xPosition += xGap;
                            }

                            else if(targetVertex == null){
                                v1 = graph.insertVertex(graph.getDefaultParent(), null, addedDst, xPosition, yPosition, this.vertex_size, this.vertex_size);
                                targetVertex = v1;
                                yPosition += yGap;
                                xPosition += xGap;
                            }

                        }
                        finally
                        {
                            // update the display
                            graph.getModel().endUpdate();
                        }
                    }
                }


                // check if edge like this already exists
                for(int i = 0; i < edgeModel.getSize(); i++){

                    String testStr = addedSrc + " - " + addedDst;

                    if(edgeModel.get(i).toString().equals(testStr)){
                        warningDialog("Edge with between these vertices already exists.", "Warning");
                        return false;
                    }
                }



                // both vertices exist or user did want to create non-existent vertex
                if(sourceVertex != null && targetVertex != null){

                    graph.getModel().beginUpdate();
                    try
                    {
                        // graph.insertEdge(parent, null, '', source, target, style);
                        Object edgeX = graph.insertEdge(graph.getDefaultParent(), null, "", sourceVertex, targetVertex);

                        // add edge to the edge list
                        edgeModel.addElement(strEdge);
                    }
                    finally
                    {
                        // update the display
                        graph.getModel().endUpdate();
                        return true;
                    }

                }

                return false;

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        return false;
    }



    /**
     * Remove selected item from node/edge list.
     * @param choice - vertex or edge which shall be removed
     * @param isNode - if true - node must be removed, otherwise, an edge must be removed
     * @param graph - the current graph we work with
     * @return true if action was successful
     */
    public boolean removeItem(JList<String> choice, boolean isNode, mxGraph graph){

        try {

            if(isNode){

                // value isn't selected
                if(choice.getSelectedValue() == null){
                    warningDialog("No selected node", "Error message");
                    return false;
                }

                System.out.println("REMOVE node ");
                removedNode = choice.getSelectedValue();

                findObjects(graph, false);

                graph.getModel().beginUpdate();
                try {
                    // remove a vertex
                    // the related edge is removed as well
                    graph.removeCells(new Object[]{sourceVertex});
                } finally {
                    graph.getModel().endUpdate();
                }


            }else {

                // value isn't selected
                if(choice.getSelectedValue() == null){
                    warningDialog("No selected edge", "Error message");
                    return false;
                }

                String[] splits = choice.getSelectedValue().split("[\\s-]+");

                removedSrc = splits[0];
                removedDst = splits[1];

                findObjects(graph, true);

                graph.getModel().beginUpdate();
                try {
                    Object[] edgeR = graph.getEdgesBetween(sourceVertex, targetVertex);
                    graph.removeCells(edgeR);
                } finally {
                    graph.getModel().endUpdate();
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



    /**
     * Get vertices/edge as an object from a graph.
     * @param graph - the graph we work with
     * @param isEdge - distinguished between vertices and edges
     */
    public void findObjects(mxGraph graph, boolean isEdge){

        for (Object v : graph.getChildVertices(graph.getDefaultParent())) {
            mxCell vertex = (mxCell)v;


            if(isEdge){

                if(vertex.getValue().equals(removedSrc)){
                    System.out.println("SRC found: " + vertex.getValue());

                    sourceVertex = vertex;
                }

                if(vertex.getValue().equals(removedDst)){
                    System.out.println("DST found: " + vertex.getValue());

                    targetVertex = vertex;
                }

            }else {

                if(vertex.getValue().equals(removedNode)){
                    System.out.println("NODE found: " + vertex.getValue());

                    sourceVertex = vertex;
                }
            }

        }
    }



    // currently not used
    /**
     * Highlight current step from algorithm
     * @param index - current step (element which should be selected)
     * @param algorithmChoice - jList with algorithm
     */
    public void setAlgorithmItem(int index, JList<String> algorithmChoice){
        algorithmChoice.setSelectedIndex(index);
    }



    // currently not used
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


    /**
     * Error/Warning etc. dialog
     * @param message - message to be shown
     * @param dialogType - Error/Warning etc. It will be shown in the dialog title
     */
    public void warningDialog(String message, String dialogType){
        JOptionPane.showMessageDialog(new JFrame(), message, dialogType,
                JOptionPane.ERROR_MESSAGE);
    }



    public boolean desicionDialog(String message, String problem){
        int dialogResult = JOptionPane.showConfirmDialog (null, message, problem, JOptionPane.OK_CANCEL_OPTION);

        if(dialogResult == JOptionPane.YES_OPTION){
           return true;
        }else
            return false;
    }


    public void getNodeModel(DefaultListModel<String> model){
        nModel = model;
    }


    public void getGraph(mxGraph graph){
        nGraph = graph;
    }


    public void getAdjacencyList(HashMap<mxCell,LinkedList<mxCell>> adjacencyList){
        adjList = adjacencyList;
    }
}
