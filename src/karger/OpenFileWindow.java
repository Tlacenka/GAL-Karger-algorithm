/**
 * This class implements the "Open File" window and its components.
 * @author Michal Tabasek (xtabas02), Katerina Pilatova (xpilat05)
 * @date 2017
 */


package karger;

import java.lang.String;
import java.lang.NullPointerException;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class OpenFileWindow extends JFrame {
    
    public OpenFileWindow(){    
    }
    
    /**
     * Show open dialog using JFileChooser
     * @return Path to file
     */
    public String showOpenChooser(){
        JFileChooser chooser = new JFileChooser("./examples");
        int rVal = 0;
        
        // set filter for xml files
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        
        // set selected filter
        chooser.setFileFilter(xmlfilter);
        
        // Demonstrate "Open" dialog:
        rVal = chooser.showOpenDialog(OpenFileWindow.this);

        // Get file content
        if ((rVal == JFileChooser.APPROVE_OPTION) && (chooser.getSelectedFile() != null)) {
            System.out.println(chooser.getSelectedFile().getPath());
        } else {
            System.out.println("Action cancelled."); 
            dispose();
            return "";
        }
        return(chooser.getSelectedFile().getPath());
    }
   
}
