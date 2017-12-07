/**
 * This class contains the main application window.
 * @author Katerina Pilatova (xpilat05)
 * @date 2017
 */

package karger;

import java.lang.reflect.Field;
import javax.imageio.ImageIO;

import javax.swing.*;
import javax.swing.border.LineBorder;

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

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;


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

   private JTextArea runTracker;
   private JTextArea resultTracker;

   private JPanel panel1;
   private JPanel panel2;

   private JScrollPane nodePanel;
   private JScrollPane edgePanel;
   private JScrollPane algorithmPanel;

   private JPanel nodeButtonPanel;
   private JPanel edgeButtonPanel;

   private JButton addNodeButton;
   private JButton removeNodeButton;
   private JButton addEdgeButton;
   private JButton removeEdgeButton;



   private KargerGraph graph;
   private mxGraphComponent gc;


   protected SidePanels xSidePanels;

   DefaultListModel<String> nodeModel = new DefaultListModel<String>();
   JList<String> nodeChoice;

   DefaultListModel<String> edgeModel = new DefaultListModel<String>();
   JList<String> edgeChoice;

   DefaultListModel<String> algorithmModel = new DefaultListModel<String>();
   public JList<String> algorithmChoice;

   protected HashMap<mxCell,LinkedList<mxCell>> aList;


   // Menu event enumeration type
   private enum MenuEventType {
       NEW,
       LOAD,
       SAVE,
       GUIDE,
       ABOUT
   }

   // Button event enumeration type
   private enum ButtonEventType {
      RESET,
      UNDO,
      NEXT_STEP,
      RUN,
      FINISH
   }

   // Class constructor
   public MainWindow() {

      // Create graph interface
      this.graph = new KargerGraph();
      this.aList = graph.getAdjacencyList();



      // Colours
      Color menuColor = new Color(125, 30, 30);
      Color textColor = new Color(240, 239, 234);
      Color panelColor = new Color(215, 44, 22);
      Color sidePanelColor = new Color(192, 178, 181);
      Color panelColorTest = new Color(70, 70, 70);

      // Fonts
      Font titleFont = new Font("Calibri", Font.BOLD, 25);
      Font smallTitleFont = new Font("Calibri", Font.BOLD, 18);
      Font algFont = new Font("Inconsolata", Font.PLAIN, 15);


      int button_width = 50;
      int button_height = 50;

      int sideButton_width = 50;
      int sideButton_height = 30;


      // Set window options, title, size, position
      this.frame = new JFrame();
      this.frame.setPreferredSize(new Dimension(1400,900));

      this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.frame.setTitle("Karger Algorithm for Finding Minimal Cut");
      this.frame.setSize(1400, 900);
      this.frame.setLocationRelativeTo(null); // center of screen

      // Create top menu bar, menu, help menu
      this.menuBar = new JMenuBar();
      this.menu = new JMenu("Menu");
      this.help_menu = new JMenu("Help");

      // Create menu items
      JMenuItem menuCreateGraph = new JMenuItem("Create Graph");
      menuCreateGraph.addActionListener(new MenuListener(this, MenuEventType.NEW));
      JMenuItem menuLoadGraph = new JMenuItem("Load Graph");
      menuLoadGraph.addActionListener(new MenuListener(this, MenuEventType.LOAD));
      JMenuItem menuSaveGraph = new JMenuItem("Save Graph");
      menuSaveGraph.addActionListener(new MenuListener(this, MenuEventType.SAVE));

      // Add items to menu
      this.menu.add(menuCreateGraph);
      this.menu.add(menuLoadGraph);
      this.menu.add(menuSaveGraph);
      this.menuBar.add(this.menu);

      // Create help menu items
      JMenuItem menuUserGuide = new JMenuItem("User Guide");
      menuUserGuide.addActionListener(new MenuListener(this, MenuEventType.GUIDE));
      JMenuItem menuAbout = new JMenuItem("About");
      menuAbout.addActionListener(new MenuListener(this, MenuEventType.ABOUT));

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
      this.topPanel.setPreferredSize(new Dimension(1400, 90));
      this.frame.add(this.topPanel, BorderLayout.NORTH);


      // Graph editor panel
      this.leftPanel = new JPanel();
      this.leftPanel.setBackground(sidePanelColor);
      this.leftPanel.setPreferredSize(new Dimension(350, 750));
      this.leftPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.black));

      // Node list and edge list panels
      this.panel1 = new JPanel();
      this.panel1.setBackground(sidePanelColor);
      this.panel1.setPreferredSize(new Dimension(150, 600));

      this.panel2 = new JPanel();
      this.panel2.setBackground(sidePanelColor);
      this.panel2.setPreferredSize(new Dimension(150, 600));

      // show node lists
      updateNodeList();

      // show edge list
      updateEdgeList();


      // Create panel for edges and nodes

      JTextArea edgeTitle = new JTextArea("Edge List");
      JTextArea nodeTitle = new JTextArea("Node List");

      edgeTitle.setForeground(textColor);
      edgeTitle.setBackground(sidePanelColor);
      edgeTitle.setFont(smallTitleFont);
      edgeTitle.setEditable(false);
      edgeTitle.setPreferredSize(new Dimension(140, 30));
      this.panel1.add(nodeTitle);

      nodeTitle.setForeground(textColor);
      nodeTitle.setBackground(sidePanelColor);
      nodeTitle.setFont(smallTitleFont);
      nodeTitle.setEditable(false);
      nodeTitle.setPreferredSize(new Dimension(140, 30));
      this.panel2.add(edgeTitle);

      this.nodePanel = new JScrollPane(nodeChoice);
      this.edgePanel = new JScrollPane(edgeChoice);

      this.nodePanel.setPreferredSize(new Dimension(140, 500));
      this.edgePanel.setPreferredSize(new Dimension(140, 500));

      this.nodeButtonPanel = new JPanel();
      this.edgeButtonPanel = new JPanel();

      this.nodeButtonPanel.setPreferredSize(new Dimension(140, 40));
      this.edgeButtonPanel.setPreferredSize(new Dimension(140, 40));

      this.nodeButtonPanel.setBackground(sidePanelColor);
      this.edgeButtonPanel.setBackground(sidePanelColor);


      // Add and remove button for node and edge lists
      this.addNodeButton     = new JButton();
      this.removeNodeButton  = new JButton();
      this.addEdgeButton     = new JButton();
      this.removeEdgeButton  = new JButton();


      // Adding/removing nodes/edges action
      try {
         Image img = ImageIO.read(getClass().getResource("images/addButton.png"));
         this.addNodeButton.setIcon(new ImageIcon(img));
         this.addNodeButton.setPreferredSize(new Dimension(sideButton_width, sideButton_height));
         this.addNodeButton.setBorder(BorderFactory.createLineBorder(Color.black));
         this.addNodeButton.setToolTipText("Add node");
         this.addNodeButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               xSidePanels = new SidePanels();
               xSidePanels.addNode(nodeModel);
            }
         });

         img = ImageIO.read(getClass().getResource("images/removeButton.png"));
         this.removeNodeButton.setIcon(new ImageIcon(img));
         this.removeNodeButton.setPreferredSize(new Dimension(sideButton_width, sideButton_height));
         this.removeNodeButton.setBorder(BorderFactory.createLineBorder(Color.black));
         this.removeNodeButton.setToolTipText("Remove node");
         this.removeNodeButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               xSidePanels = new SidePanels();
               xSidePanels.removeItem(nodeChoice, true);
            }
         });

         img = ImageIO.read(getClass().getResource("images/addButton.png"));
         this.addEdgeButton.setIcon(new ImageIcon(img));
         this.addEdgeButton.setPreferredSize(new Dimension(sideButton_width, sideButton_height));
         this.addEdgeButton.setBorder(BorderFactory.createLineBorder(Color.black));
         this.addEdgeButton.setToolTipText("Add edge");
         this.addEdgeButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               xSidePanels = new SidePanels();
               xSidePanels.addEdge(edgeModel);
            }
         });

         img = ImageIO.read(getClass().getResource("images/removeButton.png"));
         this.removeEdgeButton.setIcon(new ImageIcon(img));
         this.removeEdgeButton.setPreferredSize(new Dimension(sideButton_width, sideButton_height));
         this.removeEdgeButton.setBorder(BorderFactory.createLineBorder(Color.black));
         this.removeEdgeButton.setToolTipText("Remove edge");
         this.removeEdgeButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               xSidePanels = new SidePanels();
               xSidePanels.removeItem(edgeChoice, false);
            }
         });


      } catch (Exception ex) {
         System.out.println(ex);
      }


      this.nodeButtonPanel.add(this.addNodeButton, BorderLayout.WEST);
      this.nodeButtonPanel.add(this.removeNodeButton, BorderLayout.EAST);

      this.edgeButtonPanel.add(this.addEdgeButton, BorderLayout.WEST);
      this.edgeButtonPanel.add(this.removeEdgeButton, BorderLayout.EAST);

      this.panel1.add(this.nodePanel, BorderLayout.CENTER);
      this.panel1.add(this.nodeButtonPanel, BorderLayout.SOUTH);

      this.panel2.add(this.edgePanel, BorderLayout.CENTER);
      this.panel2.add(this.edgeButtonPanel, BorderLayout.SOUTH);

      this.leftPanel.add(this.panel1, BorderLayout.WEST);
      this.leftPanel.add(this.panel2, BorderLayout.CENTER);

      this.frame.add(this.leftPanel, BorderLayout.WEST);



      // Simulation panel
      this.centerPanel = new JPanel();
      this.centerPanel.setBackground(Color.WHITE);
      this.centerPanel.setPreferredSize(new Dimension(750, 750));
      this.centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
      this.frame.add(this.centerPanel, BorderLayout.CENTER);


      // Algorithm panel
      this.rightPanel = new JPanel();
      this.rightPanel.setBackground(sidePanelColor);
      this.rightPanel.setPreferredSize(new Dimension(350, 750));
      this.rightPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.black));

      try {                                                                      // index
         algorithmModel.addElement("  ");                                        // 1
         algorithmModel.addElement("      1|  let G = (V,E)");                       // 2
         algorithmModel.addElement("      2|");                                      // 3
         algorithmModel.addElement("      3|  void KargerAlgorithm(G) {");           // 4
         algorithmModel.addElement("      4|    while (|V| > 2)");                   // 5
         algorithmModel.addElement("      5|       choose 2 adjacent nodes v1, v2"); // 6
         algorithmModel.addElement("      6|       mergeCells(v1, v2)");             // 7
         algorithmModel.addElement("      7|  }");                                   // 8
         algorithmModel.addElement("      8|");                                      // 9
         algorithmModel.addElement("      9|  void mergeCells(v1, v2) {");           // 10
         algorithmModel.addElement("    10|     for v in Adj[v2]:");                 // 11
         algorithmModel.addElement("    11|        redirect edge to v1");            // 12
         algorithmModel.addElement("    12|     rename v1");                         // 13
         algorithmModel.addElement("    13|     remove v2 and its edges");           // 14
         algorithmModel.addElement("    14|  }");                                    // 15

         algorithmChoice = new JList<String>(algorithmModel);
         algorithmChoice.setFont(algFont);
         algorithmChoice.setBackground(Color.white);

      } catch (Exception ex) {
         System.out.println(ex);
      }

     // algorithmChoice.setSelectedIndex(graph.algorithmItemIndex);

      this.algorithmPanel = new JScrollPane(algorithmChoice);
      this.algorithmPanel.setPreferredSize(new Dimension(350, 350));
      this.algorithmPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.black));

      // Add title Algorithm
      JTextArea algorithmTitle = new JTextArea("Algorithm");

      // Set its text color, size
      algorithmTitle.setForeground(textColor);
      algorithmTitle.setBackground(sidePanelColor);
      algorithmTitle.setFont(titleFont);
      this.rightPanel.add(algorithmTitle);

      this.rightPanel.add(this.algorithmPanel, BorderLayout.CENTER);
      this.frame.add(this.rightPanel, BorderLayout.EAST);

      this.rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));


      // Control panel
      this.bottomPanel = new JPanel();
      this.bottomPanel.setBackground(menuColor);
      this.bottomPanel.setPreferredSize(new Dimension(1200, 60));
      this.frame.add(this.bottomPanel, BorderLayout.SOUTH);

      // Add buttons to bottom panel
      this.bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

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
         this.resetButton.setToolTipText("Reset");
         this.resetButton.addActionListener(new ButtonListener(this, ButtonEventType.RESET));
         this.resetButton.setEnabled(false);

         img = ImageIO.read(getClass().getResource("images/stepBackSmall.png"));
         this.undoButton.setIcon(new ImageIcon(img));
         this.undoButton.setPreferredSize(new Dimension(button_width, button_height));
         this.undoButton.setToolTipText("Undo");
         this.undoButton.addActionListener(new ButtonListener(this, ButtonEventType.UNDO));
         this.undoButton.setEnabled(false);

         img = ImageIO.read(getClass().getResource("images/playSmall.png"));
         this.stepButton.setIcon(new ImageIcon(img));
         this.stepButton.setPreferredSize(new Dimension(button_width, button_height));
         this.stepButton.setToolTipText("Next Step");
         this.stepButton.addActionListener(new ButtonListener(this, ButtonEventType.NEXT_STEP));

         img = ImageIO.read(getClass().getResource("images/nextStepSmall.png"));
         this.runButton.setIcon(new ImageIcon(img));
         this.runButton.setPreferredSize(new Dimension(button_width, button_height));
         this.runButton.setToolTipText("Finish Run");
         this.runButton.addActionListener(new ButtonListener(this, ButtonEventType.RUN));

         img = ImageIO.read(getClass().getResource("images/finishSmall.png"));
         this.finishButton.setIcon(new ImageIcon(img));
         this.finishButton.setPreferredSize(new Dimension(button_width, button_height));
         this.finishButton.setToolTipText("Finish Algorithm");
         this.finishButton.addActionListener(new ButtonListener(this, ButtonEventType.FINISH));

      } catch (Exception ex) {
         System.out.println(ex);
      }

      this.bottomPanel.add(this.resetButton);
      this.bottomPanel.add(this.undoButton);
      this.bottomPanel.add(this.stepButton);
      this.bottomPanel.add(this.runButton);
      this.bottomPanel.add(this.finishButton);

      // Add text areas to top panel
      this.topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 30));

      // Create text areas
      this.runTracker = new JTextArea("Total Runs: " + this.graph.getRunCounter());
      this.resultTracker = new JTextArea("Best Result: " + this.graph.getBestResult());

      // Set text color, size
      this.runTracker.setForeground(textColor);
      this.runTracker.setBackground(panelColor);
      this.runTracker.setFont(titleFont);
      this.runTracker.setEditable(false);
      this.runTracker.setPreferredSize(new Dimension(200, 30));
      this.resultTracker.setForeground(textColor);
      this.resultTracker.setBackground(panelColor);
      this.resultTracker.setFont(titleFont);
      this.resultTracker.setEditable(false);
      this.resultTracker.setPreferredSize(new Dimension(200, 30));

      // Add text areas
      this.topPanel.add(this.runTracker);
      this.topPanel.add(this.resultTracker);

      // Add graph to panel
      this.centerPanel.add(this.graph.getGraphComponent());

   }

   /**
    * Class for performing menu actions.
    */
   private class MenuListener implements ActionListener {
      private final MainWindow mainwindow;
      private final MenuEventType eventType;

      // Class constructor
      public MenuListener(MainWindow mainwindow, MenuEventType eventType) {
         this.mainwindow = mainwindow;
         this.eventType = eventType;
      }

      // Performing menu actions
      @Override
      public void actionPerformed(ActionEvent e) {
         switch(this.eventType) {
            case NEW:
               this.mainwindow.newGraph();
               break;
            case LOAD:
               this.mainwindow.loadGraph();
               break;
            case SAVE:
               this.mainwindow.saveGraph();
               break;
            case GUIDE:
               UserGuideWindow xGuide = new UserGuideWindow();
               xGuide.showGuideDialog();
               break;
            case ABOUT:
               AboutWindow xAbout = new AboutWindow();
               xAbout.showAboutDialog();
               break;
         }
      }

   }

   /**
    * Class for performing bottom button actions.
    */
   private class ButtonListener implements ActionListener {
      private final MainWindow mainwindow;
      private final ButtonEventType eventType;

      // Class constructor
      public ButtonListener(MainWindow mainwindow, ButtonEventType eventType) {
         this.mainwindow = mainwindow;
         this.eventType = eventType;
      }

      // Performing button actions
      @Override
      public void actionPerformed(ActionEvent e) {
         switch(this.eventType) {
            case RESET:
               this.mainwindow.undoButton.setEnabled(false);
               this.mainwindow.resetButton.setEnabled(false);
               this.mainwindow.graph.resetAlgorithm();
               break;
            case UNDO:
               this.mainwindow.undoButton.setEnabled(false);
               this.mainwindow.graph.undoStep();
               break;
            case NEXT_STEP:
               this.mainwindow.graph.nextStep();
               this.mainwindow.undoButton.setEnabled(true);
               this.mainwindow.resetButton.setEnabled(true);
               break;
            case RUN:
               this.mainwindow.graph.finishRun();
               this.mainwindow.undoButton.setEnabled(true);
               this.mainwindow.resetButton.setEnabled(true);
               break;
            case FINISH:
               this.mainwindow.graph.finishAlgorithm();
               this.mainwindow.resetButton.setEnabled(true);
               break;
         }


         // some action was performed so the lists must be updated

         // in the first step, the "old" list must be cleared
         edgeModel.clear();
         nodeModel.clear();

         this.mainwindow.updateEdgeList();
         this.mainwindow.updateNodeList();
      }
   }

   // Create new graph
   public void newGraph() {
      this.graph.createEmptyGraph();
   }

   // Load graph from file
   public void loadGraph() {
      OpenFileWindow win = new OpenFileWindow();
      String filepath = win.showOpenChooser();
      this.graph.loadGraph(filepath);
   }

   // Save graph to file
   public void saveGraph() {
      SaveFileWindow win = new SaveFileWindow();
      String filepath = win.showSaveChooser();
      this.graph.saveGraph(filepath);
   }

   public void show () {
       // Show panel in application window
       this.frame.pack();
       this.frame.setVisible(true);
   }



   public void updateEdgeList(){
      try {

         Object xParent = graph.xGetGraph().getDefaultParent();

         for (Object e : graph.xGetGraph().getChildEdges(xParent)) {
            mxCell edge = (mxCell)e;
            mxCell src = (mxCell)edge.getSource();
            mxCell dst = (mxCell)edge.getTarget();

            // Check that all edges are between two vertices
            if ((src == null) || (dst == null)) {
               throw new IllegalArgumentException("Each edge must be between 2 vertices.");
            }

            this.edgeModel.addElement(src.getValue() + " - " + dst.getValue());

         }

         this.edgeChoice = new JList<String>(edgeModel);


      } catch (Exception ex) {
         System.out.println(ex);
      }
   }



   public void updateNodeList(){
      try {

         if(aList != null){

            for (mxCell value: this.aList.keySet()) {
               nodeModel.addElement(value.getValue().toString());
            }
         }

         nodeChoice = new JList<String>(nodeModel);

      } catch (Exception ex) {
         System.out.println(ex);
      }
   }
}
