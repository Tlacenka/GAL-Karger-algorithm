/**
 * This class implements the "Open File" window and its components.
 * @author Michal Tabasek (xtabas02)
 * @date 2017
 */


package karger;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
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

public class OpenFileWindow extends JFrame {
    
    public OpenFileWindow(){    
        File f; 
        showOpenChooser();  
    }
    
     /*
        show open dialog using jfilechooser
    */
    public void showOpenChooser(){
        JFileChooser chooser = new JFileChooser();
        
        // set filter for xml files
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        
        // set selected filter
        chooser.setFileFilter(xmlfilter);
        
        // Demonstrate "Open" dialog:
        int rVal = chooser.showOpenDialog(OpenFileWindow.this);
        
        // file is selected
        if (rVal == JFileChooser.APPROVE_OPTION) {
            System.out.println(chooser.getSelectedFile().getName());    // print file name
        }
      
        if (rVal == JFileChooser.CANCEL_OPTION) {         
            System.out.println("CANCEL"); 
            dispose();
        }
        
        //return(chooser.getSelectedFile().getName());
    }
   
}
