/**
 * This class implements the graph interface.
 * @author Katerina Pilatova (xpilat05), Michal Tabasek (xtabas02)
 * @date 2017
 */

package karger;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;


public class AboutWindow {

    public void showAboutDialog(){
        Font textFont = new Font("Calibri", Font.PLAIN, 15);
        JEditorPane textArea = new JEditorPane("text/html", "");
        textArea.setText("<h2>Karger Algorithm for Finding Minimal Cut</h2>" +
                "<p>This application is a school project for the subject <b>Graph Algorithms (GAL)</b> held " +
                "at Faculty of Information Technology, Brno University of Technology in the academic year 2017/2018.</p>" +
                "<p>The project collaborators are Katerina Pilatova (<i>xpilat05</i>) and Michal Tabasek (<i>xtabas02</i>).");

        JScrollPane scrollPane = new JScrollPane(textArea);
        //textArea.setLineWrap(true);
        //textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 500, 200 ) );
        JOptionPane.showMessageDialog(null, scrollPane, "About",
                JOptionPane.YES_NO_OPTION);
    }

}
