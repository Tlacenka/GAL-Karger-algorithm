/**
 * This class contains the main application window.
 * @author Katerina Pilatova (xpilat05)
 * @date 2017
 */

package karger;

import java.lang.reflect.Field;
import javax.imageio.ImageIO;

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
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.JComponent;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

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
   private JMenu help_menu;

   private JPanel topPanel;
   private JPanel leftPanel;
   private JPanel rightPanel;
   private JPanel centerPanel;
   private JPanel bottomPanel;

   private JButton resetButton;
   private JButton undoButton;
   private JButton stepButton;
   private JButton runButton;
   private JButton finishButton;

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

      int button_width = 70;
      int button_height = 70;

      // Set window options, title, size, position
      this.frame = new JFrame();
      this.frame.setPreferredSize(new Dimension(1000,800));

      this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.frame.setTitle("Karger Algorithm for Finding Minimal Cut");
      this.frame.setSize(1000, 800);
      this.frame.setLocationRelativeTo(null); // center of screen

      // Create top menu bar, menu, help menu
      this.menuBar = new JMenuBar();
      this.menu = new JMenu("Menu");
      this.help_menu = new JMenu("Help");

      // Create menu items
      JMenuItem menuCreateGraph = new JMenuItem("Create Graph");
      JMenuItem menuLoadGraph = new JMenuItem("Load Graph");
      JMenuItem menuSaveGraph = new JMenuItem("Save Graph");

      // Add items to menu
      this.menu.add(menuCreateGraph);
      this.menu.add(menuLoadGraph);
      this.menu.add(menuSaveGraph);
      this.menuBar.add(this.menu);

      // Create help menu items
      JMenuItem menuUserGuide = new JMenuItem("User Guide");
      JMenuItem menuAbout = new JMenuItem("About");

      // Add items to help menu
      this.help_menu.add(menuUserGuide);
      this.help_menu.add(menuAbout);
      this.menuBar.add(this.help_menu);

      // Set menu background colour
      this.menuBar.setBackground(menuColor);
      this.menu.setForeground(textColor);
      this.help_menu.setForeground(textColor);

      // Set window menu bar, layout
      this.frame.setJMenuBar(menuBar);
      this.frame.setLayout(new BorderLayout());

      // Create the layout - panels, their position and size
      this.topPanel = new JPanel();
      this.topPanel.setBackground(panelColor);
      this.topPanel.setPreferredSize(new Dimension(1000, 100));
      this.frame.add(this.topPanel, BorderLayout.NORTH);

      this.leftPanel = new JPanel();
      this.leftPanel.setBackground(sidePanelColor);
      this.leftPanel.setPreferredSize(new Dimension(250, 800));
      this.leftPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.black));
      this.frame.add(this.leftPanel, BorderLayout.WEST);

      this.centerPanel = new JPanel();
      this.centerPanel.setBackground(Color.WHITE);
      this.centerPanel.setPreferredSize(new Dimension(550, 500));
      this.centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
      this.frame.add(this.centerPanel, BorderLayout.CENTER);

      this.rightPanel = new JPanel();
      this.rightPanel.setBackground(sidePanelColor);
      this.rightPanel.setPreferredSize(new Dimension(250, 800));
      this.rightPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.black));
      this.frame.add(this.rightPanel, BorderLayout.EAST);

      this.bottomPanel = new JPanel();
      this.bottomPanel.setBackground(panelColor);
      this.bottomPanel.setPreferredSize(new Dimension(1000, 100));
      this.frame.add(this.bottomPanel, BorderLayout.SOUTH);

      // Add buttons to bottom panel
      this.bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

      this.resetButton = new JButton();
      this.undoButton = new JButton();
      this.stepButton = new JButton();
      this.runButton = new JButton();
      this.finishButton = new JButton();

      // Set button icons, border, size
      try {
         Image img = ImageIO.read(getClass().getResource("images/resetSmall.png"));
         this.resetButton.setIcon(new ImageIcon(img));
         this.resetButton.setPreferredSize(new Dimension(button_width, button_height));
         //this.resetButton.setBorder(new LineBorder(Color.black));
         this.resetButton.setToolTipText("Reset");

         img = ImageIO.read(getClass().getResource("images/stepBackSmall.png"));
         this.undoButton.setIcon(new ImageIcon(img));
         this.undoButton.setPreferredSize(new Dimension(button_width, button_height));
         //this.undoButton.setBorder(new LineBorder(Color.black));
         this.undoButton.setToolTipText("Undo");

         img = ImageIO.read(getClass().getResource("images/playSmall.png"));
         this.stepButton.setIcon(new ImageIcon(img));
         this.stepButton.setPreferredSize(new Dimension(button_width, button_height));
         //this.stepButton.setBorder(new LineBorder(Color.black));
         this.stepButton.setToolTipText("Next Step");

         img = ImageIO.read(getClass().getResource("images/nextStepSmall.png"));
         this.runButton.setIcon(new ImageIcon(img));
         this.runButton.setPreferredSize(new Dimension(button_width, button_height));
         //this.runButton.setBorder(new LineBorder(Color.black));
         this.runButton.setToolTipText("Finish Run");

         img = ImageIO.read(getClass().getResource("images/finishSmall.png"));
         this.finishButton.setIcon(new ImageIcon(img));
         this.finishButton.setPreferredSize(new Dimension(button_width, button_height));
         //this.finishButton.setBorder(new LineBorder(Color.black));
         this.finishButton.setToolTipText("Finish Algorithm");

      } catch (Exception ex) {
         System.out.println(ex);
      }

      this.bottomPanel.add(this.resetButton);
      this.bottomPanel.add(this.undoButton);
      this.bottomPanel.add(this.stepButton);
      this.bottomPanel.add(this.runButton);
      this.bottomPanel.add(this.finishButton);

      // Add graph to panel
      this.centerPanel.add(this.graph.getGraphComponent());

   }

   public void show () {
       // Show panel in application window
       this.frame.pack();
       this.frame.setVisible(true);
   }

}
