/**
 * This class contains the main application window.
 * @author Katerina Pilatova (xpilat05)
 * @date 2017
 */

package karger;

import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

public class MainWindow {

   private JFrame frame;
   private JMenuBar menuBar;
   private JMenu menu;

   private JPanel topPanel;
   private JPanel leftPanel;
   private JPanel rightPanel;
   private JPanel centerPanel;
   private JPanel bottomPanel;

   private KargerGraph graph;
   private mxGraphComponent gc;

   // Class constructor
   public MainWindow() {

      // Create graph interface
      this.graph = new KargerGraph();

      // Colours
      Color menuColor = new Color(161, 1, 21);
      Color textColor = new Color(240, 239, 234);
      Color panelColor = new Color(215, 44, 22);
      Color sidePanelColor = new Color(192, 178, 181);

      // Set window options, title, size, position
      this.frame = new JFrame();
      this.frame.setPreferredSize(new Dimension(1000,800));

      this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.frame.setTitle("Karger Algorithm for Finding Minimal Cut");
      this.frame.setSize(1000, 800);
      this.frame.setLocationRelativeTo(null); // center of screen

      // Create top menu bar, menu
      this.menuBar = new JMenuBar();
      this.menu = new JMenu("Menu");

      // Create menu items
      JMenuItem menuCreateGraph = new JMenuItem("Create Graph");
      JMenuItem menuLoadGraph = new JMenuItem("Load Graph");
      JMenuItem menuSaveGraph = new JMenuItem("Save Graph");

      // Add items to menu
      this.menu.add(menuCreateGraph);
      this.menu.add(menuLoadGraph);
      this.menu.add(menuSaveGraph);
      this.menuBar.add(this.menu);

      // Set menu background colour
      this.menuBar.setBackground(menuColor);
      this.menu.setForeground(textColor);

      // Set window menu bar, layout
      this.frame.setJMenuBar(menuBar);
      this.frame.setLayout(new BorderLayout());

      // Create the layout
      this.topPanel = new JPanel();
      this.topPanel.setBackground(panelColor);
      this.topPanel.setPreferredSize(new Dimension(1000, 100));
      this.frame.add(this.topPanel, BorderLayout.NORTH);

      this.leftPanel = new JPanel();
      this.leftPanel.setBackground(sidePanelColor);
      this.leftPanel.setPreferredSize(new Dimension(200, 800));
      this.leftPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.black));
      this.frame.add(this.leftPanel, BorderLayout.WEST);

      this.centerPanel = new JPanel();
      this.centerPanel.setBackground(Color.WHITE);
      this.centerPanel.setPreferredSize(new Dimension(600, 600));
      this.centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
      this.frame.add(this.centerPanel, BorderLayout.CENTER);

      this.rightPanel = new JPanel();
      this.rightPanel.setBackground(sidePanelColor);
      this.rightPanel.setPreferredSize(new Dimension(200, 800));
      this.rightPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.black));
      this.frame.add(this.rightPanel, BorderLayout.EAST);

      this.bottomPanel = new JPanel();
      this.bottomPanel.setBackground(panelColor);
      this.bottomPanel.setPreferredSize(new Dimension(1000, 100));
      this.frame.add(this.bottomPanel, BorderLayout.SOUTH);

      // Add graph to panel
      this.centerPanel.add(this.graph.getGraphComponent());

   }

   public void show () {
       // Show panel in application window
       this.frame.pack();
       this.frame.setVisible(true);
   }

}
