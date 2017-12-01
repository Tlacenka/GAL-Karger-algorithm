/**
 * This class implements the "Save File" window
 * @author Michal Tabasek (xtabas02), Katerina Pilatova (xpilat05)
 * @date 2017
 */


package karger;

import java.lang.String;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class SaveFileWindow extends JFrame {
    
    public SaveFileWindow() {
    }
    
    /**
     * Show save dialog using JFileChooser
     * @return Path to file
     */
    public String showSaveChooser(){
        JFileChooser chooser = new JFileChooser();
        
        // Demonstrate "Save" dialog:
        int rVal = chooser.showSaveDialog(SaveFileWindow.this);
      
        if (rVal == JFileChooser.APPROVE_OPTION) {
            System.out.println(chooser.getSelectedFile().getPath());
        }
      
        if (rVal == JFileChooser.CANCEL_OPTION) {         
            System.out.println("Action cancelled."); 
        }
        
        return(chooser.getSelectedFile().getPath());
    }
   
}
