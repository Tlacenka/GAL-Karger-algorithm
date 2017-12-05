package karger;

import javax.swing.*;
import java.awt.Dimension;



public class AboutWindow {

    public void showAboutDialog(){
        JTextArea textArea = new JTextArea("Karger Algorithm for Finding Minimal Cut \n\n" +
                "This application is a school project for the subject Graph Algorithms held " +
                "at Faculty of Information Technology, Brno University of Technology. \n" +
                "The project collaborators are Katerina Pilatova and Michal Tabasek.");

        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 500, 200 ) );
        JOptionPane.showMessageDialog(null, scrollPane, "About",
                JOptionPane.YES_NO_OPTION);
    }

}
