package karger;

import javax.swing.*;


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


    public DefaultListModel<String> algorithmModel = new DefaultListModel<String>();
    public JList<String> algorithmChoice;



    /**
     * Add node from the input dialog to the node list.
     * @param model - node model.
     */
    public void addNode(DefaultListModel<String> model){

        addedNode = JOptionPane.showInputDialog(null, "Node name");

        if(addedNode != null){
            try {
                model.addElement(addedNode);

            } catch (Exception ex) {
                System.out.println(ex);
            }

        }

    }


    /**
     * Remove selected item from node/edge list.
     * @param choice - which item should be removed.
     */
    public void removeItem(JList<String> choice, boolean isNode){

        try {

            if(isNode){
                removedNode = choice.getSelectedValue();

              //  System.out.println("Node " + removedNode);

            }else {
                String[] splits = choice.getSelectedValue().split("[\\s-]+");

                removedSrc = splits[0];
                removedDst = splits[1];

               // System.out.println("Src " + removedSrc);
               // System.out.println("Dsc " + removedDst);

            }


            DefaultListModel<String> modelX = (DefaultListModel<String>)choice.getModel();
            modelX.remove(choice.getSelectedIndex());

        } catch (Exception ex) {
            System.out.println(ex);
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

            addedSrc = xField.getText();
            addedDst = yField.getText();

            System.out.println("start node: " + addedSrc);
            System.out.println("end node: " + addedDst);

            strEdge = addedSrc + " - " + addedDst;

            try {
                edgeModel.addElement(strEdge);

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }


    // unused
    public void setAlgorithmItem(int index, JList<String> algorithmChoice){

        algorithmChoice.setSelectedIndex(1);
    }


}
