package karger;

import com.mxgraph.model.mxCell;
import karger.MainWindow;
import karger.KargerGraph;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;

import java.util.Map;
import java.util.*;

public class SidePanels {

    protected KargerGraph xGraph;

    protected String startEdge; // newly added start node of the edge
    protected String endEdge;   // newly added end node of the edge

    protected String nodeName;  // newly added node

    public DefaultListModel<String> algorithmModel = new DefaultListModel<String>();
    public JList<String> algorithmChoice;

    /**
     * Getting nodes and edges from the adjacency list and display them into the list.
     */
    public void crateList(){
        xGraph = new KargerGraph();
        xGraph.getAdjacencyList();

        System.out.println("i'm here \n");

        try{

            HashMap<mxCell,LinkedList<mxCell>> aList = xGraph.getAdjacencyList();

            if(aList != null) {
                System.out.println("Adjacency list 3: " + aList);
                System.out.println("something is there \n");

                if(aList != null){
                    for (mxCell key: aList.keySet()) {
                        System.out.println("key : " + key);
                        System.out.println("value : " + aList.get(key));
                    }
                }
            }
            else
                System.out.println("it's null \n");

        }catch (Throwable any) {
            System.out.println("side panels: " + any);
        }

    }


    /**
     * Add node from the input dialog to the node list.
     * @param model - node model.
     */
    public void addNode(DefaultListModel<String> model){

        nodeName = JOptionPane.showInputDialog(null, "Node name");

        if(nodeName != null){
            try {
                model.addElement(nodeName);

            } catch (Exception ex) {
                System.out.println(ex);
            }

        }

    }


    /**
     * Remove selected item from node/edge list.
     * @param choice - which item should be removed.
     */
    public void removeItem(JList<String> choice){


        System.out.println("Selected item: " + choice.getSelectedValue());

         //first entry cannot be removed
        if((choice.getSelectedValue() != "NODE LIST") && (choice.getSelectedValue() != "EDGE LIST")){

            try {
                DefaultListModel<String> modelX = (DefaultListModel<String>)choice.getModel();
                modelX.remove(choice.getSelectedIndex());

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }


    }


    /**
     * Add an edge from input dialog to the edge list.
     * @param edgeModel - edge model.
     */
    public void addEdge(DefaultListModel<String> edgeModel){

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

            startEdge = xField.getText();
            endEdge = yField.getText();

            System.out.println("start node: " + startEdge);
            System.out.println("end node: " + endEdge);

            strEdge = startEdge + " - " + endEdge;

            try {
                edgeModel.addElement(strEdge);

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }


    public JList<String> setAlgorithm(){

        try {
            algorithmModel.addElement("         ALGORITHM");
            algorithmModel.addElement("  ");
            algorithmModel.addElement("  1|    let G = (V,E)");
            algorithmModel.addElement("  2|");
            algorithmModel.addElement("  3|    void KargerAlgorithm() {");
            algorithmModel.addElement("  4|      while (V.length > 2)");
            algorithmModel.addElement("  5|         choose nodes to be merged");
            algorithmModel.addElement("  6|         mergeCells(v1,v2)");
            algorithmModel.addElement("  7|    }");
            algorithmModel.addElement("  8|");
            algorithmModel.addElement("  9|    void mergeCells(v1, v2) {");
            algorithmModel.addElement("10|       go through all vertices");
            algorithmModel.addElement("11|         adjacent to v2");
            algorithmModel.addElement("12|       create/update weighted edge");
            algorithmModel.addElement("13|       find edge");
            algorithmModel.addElement("14|       merge nodes");
            algorithmModel.addElement("15|       update graph");
            algorithmModel.addElement("16|    }");

          // algorithmChoice = new JList<String>(algorithmModel);

            algorithmChoice = new JList<String>(algorithmModel);

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return algorithmChoice;

    }


    public void setAlgorithmItem(int index, JList<String> algorithmChoice){

        algorithmChoice.setSelectedIndex(1);
    }


}
