/**
 * This class implements the "Open File" window and its components.
 * @author Michal Tabasek (xtabas02), Katerina Pilatova (xpilat05)
 * @date 2017
 */


package karger;

import java.lang.String;

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
        
        // set filter for xml files
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        
        // set selected filter
        chooser.setFileFilter(xmlfilter);
        
        // Demonstrate "Open" dialog:
        int rVal = chooser.showOpenDialog(OpenFileWindow.this);
        
        // file is selected
        if (rVal == JFileChooser.APPROVE_OPTION) {
            System.out.println(chooser.getSelectedFile().getPath());
        }
      
        if (rVal == JFileChooser.CANCEL_OPTION) {         
            System.out.println("Action cancelled."); 
            dispose();
        }
        
        return(chooser.getSelectedFile().getPath());
    }
   
}
