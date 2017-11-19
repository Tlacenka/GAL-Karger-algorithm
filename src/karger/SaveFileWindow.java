/**
 * This class implements the "Save File" window
 * @author Michal Tabasek (xtabas02)
 * @date 2017
 */


package karger;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SaveFileWindow extends JFrame {
    
    public SaveFileWindow(){
        File f;
        showSaveChooser();
    }
    
    /*
        show save dialog using jfilechooser
    */
    public void showSaveChooser(){
        JFileChooser c = new JFileChooser();
        
        // Demonstrate "Save" dialog:
        int rVal = c.showSaveDialog(SaveFileWindow.this);
      
        if (rVal == JFileChooser.APPROVE_OPTION) {
            System.out.println(c.getSelectedFile().getName());
        }
      
        if (rVal == JFileChooser.CANCEL_OPTION) {         
            System.out.println("CANCEL"); 
        }
        
        //return(c.getSelectedFile().getName());
    }
   
}
