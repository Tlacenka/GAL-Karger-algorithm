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

    KargerGraph xGraph;


    public void crateList(){
        xGraph = new KargerGraph();
        xGraph.getAdjacencyList();

        System.out.println("i'm here \n");

        //  System.out.println("Adjacency list 31: " + xGraph.);
        try{

            HashMap<mxCell,LinkedList<mxCell>> aList = xGraph.getAdjacencyList();

            if(aList != null) {
                System.out.println("Adjacency list 3: " + aList);
                System.out.println("something is there \n");
            }
            else
                System.out.println("it's null \n");

        }catch (Throwable any) {
            System.out.println("side panels: " + any);
        }



       /* Map<String, Integer> map = new HashMap<String, Integer>();

        for (String key: map.keySet()) {
            System.out.println("key : " + key);
            System.out.println("value : " + map.get(key));
        }*/

        /*if(adjacencyList != null){
            for (mxCell key: adjacencyList.keySet()) {
                System.out.println("key : " + key);
                System.out.println("value : " + adjacencyList.get(key));
            }
        }*/



    }


    public void addNode(DefaultListModel<String> model){

        String nodeName = JOptionPane.showInputDialog(null, "Node name");

        if(nodeName != null){
            try {
                model.addElement(nodeName);

            } catch (Exception ex) {
                System.out.println(ex);
            }

        }

    }


    public void removeItem(DefaultListModel<String> model, JList<String> choice){

        try {
            DefaultListModel<String> modelX = (DefaultListModel<String>)choice.getModel();
            modelX.remove(choice.getSelectedIndex());

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }


    public void addEdge(DefaultListModel<String> edgeModel){

        String strEdge = null;
        String startNode = null;
        String endNode = null;


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

            startNode = xField.getText();
            endNode = yField.getText();

            System.out.println("start node: " + startNode);
            System.out.println("end node: " + endNode);

            strEdge = startNode + " - " + endNode;

            try {
                edgeModel.addElement(strEdge);

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }


    public void setAlgorithm(DefaultListModel<String> algorithmModel){

        try {
            algorithmModel.addElement(" 1|");
            algorithmModel.addElement(" 2|");
            algorithmModel.addElement(" 3|");
            algorithmModel.addElement(" 4|");
            algorithmModel.addElement(" 5|");
            algorithmModel.addElement(" 6|");
            algorithmModel.addElement(" 7|");
            algorithmModel.addElement(" 8|");
            algorithmModel.addElement(" 9|");
            algorithmModel.addElement("10|");
            algorithmModel.addElement("11|");
            algorithmModel.addElement("12|");
            algorithmModel.addElement("13|");
            algorithmModel.addElement("14|");
            algorithmModel.addElement("15|");

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }


}
