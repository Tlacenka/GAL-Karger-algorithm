package karger;


import javax.swing.*;
import java.awt.*;

public class UserGuideWindow {

    public void showGuideDialog(){

        JTextArea textArea = new JTextArea("Karger Algorithm for Finding Minimal Cut \n\n" +
                "In computer science and graph theory, Karger's algorithm is a randomized algorithm to compute " +
                "a minimum cut of a connected graph. It was invented by David Karger and first published in 1993.\n\n" +
                "The main window of this application is divided into four main sections. \n" +
                "- the graph editor on the left, \n" +
                "- the algorithm panel on the right, \n" +
                "- the control panel on the bottom, \n" +
                "- the panel with the simulation on the center," +
                "- the panel with the results on the top. \n\n" +
                "How to control this application\n" +
                "The control panel contains five buttons: \n" +
                "- play/pause button - \n" +
                "- next step button - \n" +
                "- step back button - \n" +
                "- finish button - \n" +
                "- reset button - ");

        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 500, 200 ) );
        JOptionPane.showMessageDialog(null, scrollPane, "User Guide",
                JOptionPane.YES_NO_OPTION);

    }

}
