/**
 * This class contains the main application window.
 * @author Katerina Pilatova (xpilat05)
 * @date 2017
 */

package karger;

import javax.imageio.ImageIO;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Component;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxPoint;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;


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
   private JButton manualSteps;

   private JTextArea runTracker;
   private JTextArea resultTracker;
   private JTextArea resultTitle;
   private JTextArea otherResultsTitle;

   private JPanel panel1;
   private JPanel panel2;

   private JScrollPane nodePanel;
   private JScrollPane edgePanel;
   private JScrollPane algorithmPanel;
   private JScrollPane resultsPanel;

   private JPanel nodeButtonPanel;
   private JPanel edgeButtonPanel;
   private JPanel resultContentPanel;
   private JPanel bestResultPanel;
   private JPanel otherResultsPanel;

   private JButton addNodeButton;
   private JButton removeNodeButton;
   private JButton addEdgeButton;
   private JButton removeEdgeButton;

   boolean addedNode;
   boolean isPhase1;

   private KargerGraph graph;
   private mxGraphComponent gc;
   private ArrayList<KargerGraph> graphResults;
   private KargerGraph graphBestResult;
   private ArrayList<mxCell> manualVertices;

   protected int clickCounter = 0;


   protected SidePanels xSidePanels;

   DefaultListModel<String> nodeModel = new DefaultListModel<String>();
   JList<String> nodeChoice;

   DefaultListModel<String> edgeModel = new DefaultListModel<String>();
   JList<String> edgeChoice;

   DefaultListModel<String> algorithmModel = new DefaultListModel<String>();
   public JList<String> algorithmChoice;

   private boolean firstTimeCreatedModel = false;

   protected HashMap<mxCell,LinkedList<mxCell>> aList;

  // boolean gotNode = false;
  // boolean gotEdge = false;


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
      FINISH,
      MANUAL_STEPS
   }

   // Class constructor
   public MainWindow() {

      // Create graph interface
      this.graph = new KargerGraph();
      this.aList = graph.getAdjacencyList();
      this.manualVertices = new ArrayList<mxCell>();

      xSidePanels = new SidePanels();
      xSidePanels.getNodeModel(nodeModel);
      xSidePanels.getAdjacencyList(graph.getAdjacencyList());
      xSidePanels.getGraph(graph.xGetGraph());

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

      this.isPhase1 = true;

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


      // create models for the first time
       this.firstTimeCreatedModel = true;

        updateNodeList();     // show node lists
        updateEdgeList();     // show edge list

       this.firstTimeCreatedModel = false;


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
               //xSidePanels = new SidePanels();
               //System.out.println("Add node list: " + graph.getAdjacencyList());

               try{
                  addedNode = xSidePanels.addNode(nodeModel, graph.getAdjacencyList(), graph.xGetGraph());

                  if(addedNode == true){
                     graph.createAdjacencyList();

                     // first of all, the list must be cleared
                     graph.getCurOrder().clear();

                     // then we can "update" the list with changed values
                     int i = 0;
                     for (Object eO: graph.xGetGraph().getChildEdges(graph.xGetGraph().getDefaultParent())) {
                        graph.getCurOrder().add(i);
                        i++;
                     }
                  }


               }catch (Exception ex) {
                  System.out.println(ex);
               }
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
               //System.out.println("REMOVE NODE \n");
               //xSidePanels = new SidePanels();
               boolean isRemoved = xSidePanels.removeItem(nodeChoice, true, graph.xGetGraph());

               if(isRemoved){
                  graph.createAdjacencyList();
                  edgeModel.clear();
                  updateEdgeList();

                  // first of all, the list must be cleared
                  graph.getCurOrder().clear();

                  // then we can "update" the list with changed values
                  int i = 0;
                  for (Object eO: graph.xGetGraph().getChildEdges(graph.xGetGraph().getDefaultParent())) {
                     graph.getCurOrder().add(i);
                     i++;
                  }

               }

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
               //xSidePanels = new SidePanels();
               boolean gotEdge = xSidePanels.addEdge(edgeModel, graph.xGetGraph(), graph.getAdjacencyList());

               if(gotEdge == true){

                  graph.createAdjacencyList();

                  // first of all, the list must be cleared
                  graph.getCurOrder().clear();

                  // then we can "update" the list with changed values
                  int i = 0;
                  for (Object eO: graph.xGetGraph().getChildEdges(graph.xGetGraph().getDefaultParent())) {
                     graph.getCurOrder().add(i);
                     i++;
                  }

               }




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
               //xSidePanels = new SidePanels();
               boolean isRemoved = xSidePanels.removeItem(edgeChoice, false, graph.xGetGraph());

               if(isRemoved){
                  graph.createAdjacencyList();
                  edgeModel.clear();
                  updateEdgeList();

                  // first of all, the list must be cleared
                  graph.getCurOrder().clear();

                  // then we can "update" the list with changed values
                  int i = 0;
                  for (Object eO: graph.xGetGraph().getChildEdges(graph.xGetGraph().getDefaultParent())) {
                     graph.getCurOrder().add(i);
                     i++;
                  }
               }

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


         //System.out.println("GET algorithm list \n");
         //graph.getAlgorithmList(algorithmChoice);

      } catch (Exception ex) {
         System.out.println(ex);
      }


      this.algorithmPanel = new JScrollPane(algorithmChoice);
      this.algorithmPanel.setPreferredSize(new Dimension(350, 350));
      this.algorithmPanel.setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.black));

       //algorithmChoice.setSelectedIndex(1);

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
      this.manualSteps = new JButton();


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

         img = ImageIO.read(getClass().getResource("images/manualSteps.png"));
         this.manualSteps.setIcon(new ImageIcon(img));
         this.manualSteps.setPreferredSize(new Dimension(button_width, button_height));
         this.manualSteps.setToolTipText("Manual steps");
         this.manualSteps.addActionListener(new ButtonListener(this, ButtonEventType.MANUAL_STEPS));

      } catch (Exception ex) {
         System.out.println(ex);
      }

      this.bottomPanel.add(this.resetButton);
      // Undo not supported
      //this.bottomPanel.add(this.undoButton);
      this.bottomPanel.add(this.stepButton);
      this.bottomPanel.add(this.runButton);
      this.bottomPanel.add(this.finishButton);
      this.bottomPanel.add(this.manualSteps);

      // Add text areas to top panel
      this.topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 110, 30));

      // Create text areas
      this.runTracker = new JTextArea("Total Runs: " + this.graph.getRunCounter());
      this.resultTracker = new JTextArea("Best Result: " + this.graph.getBestResultCut());

      // Set text color, size
      this.runTracker.setForeground(textColor);
      this.runTracker.setBackground(panelColor);
      this.runTracker.setFont(titleFont);
      this.runTracker.setEditable(false);
      this.runTracker.setPreferredSize(new Dimension(225, 30));
      this.resultTracker.setForeground(textColor);
      this.resultTracker.setBackground(panelColor);
      this.resultTracker.setFont(titleFont);
      this.resultTracker.setEditable(false);
      this.resultTracker.setPreferredSize(new Dimension(225, 30));

      // Add text areas
      this.topPanel.add(this.runTracker);
      this.topPanel.add(this.resultTracker);

      // Add graph to panel
      //this.centerPanel.add(this.graph.getGraphComponent());

      this.resultContentPanel = new JPanel();
      this.resultContentPanel.setBackground(Color.white);
      this.resultContentPanel.setBorder(null);
      this.resultContentPanel.setLayout(new BoxLayout(this.resultContentPanel, BoxLayout.Y_AXIS));

      // Prepare results
      this.resultTitle = new JTextArea("Best Result");
      this.resultTitle.setFont(titleFont);
      this.resultTitle.setForeground(panelColorTest);
      this.resultTitle.setPreferredSize(new Dimension(650, 30));
      this.resultTitle.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));

      // Results panels
      this.bestResultPanel = new JPanel();
      this.bestResultPanel.setBorder(null);
      this.bestResultPanel.setBackground(Color.white);
      this.bestResultPanel.setLayout(new BoxLayout(this.bestResultPanel, BoxLayout.Y_AXIS));
      this.otherResultsPanel = new JPanel();
      this.otherResultsPanel.setBorder(null);
      this.otherResultsPanel.setBackground(Color.white);
      this.otherResultsPanel.setLayout(new BoxLayout(this.otherResultsPanel, BoxLayout.Y_AXIS));

      this.otherResultsTitle = new JTextArea("Other Results");
      this.otherResultsTitle.setFont(titleFont);
      this.otherResultsTitle.setForeground(panelColorTest);
      this.otherResultsTitle.setPreferredSize(new Dimension(650, 30));
      this.otherResultsTitle.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));

      // Add to panel
      this.resultContentPanel.add(this.graph.getGraphComponent());

      // Prepare scrolling
      this.resultsPanel = new JScrollPane(this.resultContentPanel);
      this.resultsPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      this.resultsPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      this.resultsPanel.setBorder(null);
      this.resultsPanel.setPreferredSize(new Dimension(675, 720));
      this.resultsPanel.revalidate();

      // user can scroll by mouse wheel
      this.resultsPanel.setWheelScrollingEnabled(true);

      // speed up the mouse scrolling
      this.resultsPanel.getVerticalScrollBar().setUnitIncrement(16);

      // Add scrolling to panel
      this.centerPanel.add(this.resultsPanel);

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

               // Reset buttons
               clickCounter = 0;
               //algorithmChoice.setSelectedIndex(0);
               algorithmChoice.clearSelection();
               this.mainwindow.undoButton.setEnabled(false);
               this.mainwindow.resetButton.setEnabled(false);
               this.mainwindow.graph.resetAlgorithm();
               this.mainwindow.stepButton.setEnabled(true);
               this.mainwindow.finishButton.setEnabled(true);
               this.mainwindow.runButton.setEnabled(true);
               this.mainwindow.manualSteps.setEnabled(true);

               // Reset trackers
               this.mainwindow.runTracker.setText("Total Runs: " + this.mainwindow.graph.getRunCounter());
               this.mainwindow.resultTracker.setText("Best Result: " + this.mainwindow.graph.getBestResultCut());

               // Remove results, display only original graph
               this.mainwindow.graph.getGraphComponent().zoom(1.0);
               this.mainwindow.resultContentPanel.remove(this.mainwindow.resultTitle);
               this.mainwindow.resultContentPanel.remove(this.mainwindow.bestResultPanel);
               this.mainwindow.resultContentPanel.remove(this.mainwindow.otherResultsTitle);
               this.mainwindow.resultContentPanel.remove(this.mainwindow.otherResultsPanel);

               // Remove stored results
               this.mainwindow.bestResultPanel.removeAll();
               this.mainwindow.otherResultsPanel.removeAll();

               break;
            case UNDO:
               this.mainwindow.undoButton.setEnabled(false);
               this.mainwindow.graph.undoStep();
               break;
            case NEXT_STEP:
               this.mainwindow.graph.nextStep();
               this.mainwindow.undoButton.setEnabled(true);
               this.mainwindow.resetButton.setEnabled(true);
               if (this.mainwindow.graph.isRunFinished()) {
                  this.mainwindow.runTracker.setText("Total Runs: " + this.mainwindow.graph.getRunCounter());
                  this.mainwindow.resultTracker.setText("Best Result: " + this.mainwindow.graph.getBestResultCut());
                  this.mainwindow.stepButton.setEnabled(false);
                  this.mainwindow.manualSteps.setEnabled(false);
                  this.mainwindow.runButton.setEnabled(false);
               }

               break;
            case RUN:
               this.mainwindow.undoButton.setEnabled(false);
               this.mainwindow.stepButton.setEnabled(false);
               this.mainwindow.runButton.setEnabled(false);
               this.mainwindow.manualSteps.setEnabled(false);
               this.mainwindow.graph.finishRun();
               this.mainwindow.resetButton.setEnabled(true);
               this.mainwindow.runTracker.setText("Total Runs: " + this.mainwindow.graph.getRunCounter());
               this.mainwindow.resultTracker.setText("Best Result: " + this.mainwindow.graph.getBestResultCut());
               break;
            case FINISH:
               this.mainwindow.finishButton.setEnabled(false);
               this.mainwindow.runButton.setEnabled(false);
               this.mainwindow.undoButton.setEnabled(false);
               this.mainwindow.stepButton.setEnabled(false);
               this.mainwindow.manualSteps.setEnabled(false);
               this.mainwindow.graph.finishAlgorithm();
               this.mainwindow.resetButton.setEnabled(true);
               this.mainwindow.runTracker.setText("Total Runs: " + this.mainwindow.graph.getRunCounter());
               this.mainwindow.resultTracker.setText("Best Result: " + this.mainwindow.graph.getBestResultCut());

               // Display zoomed out graph, results below it
               this.mainwindow.graph.getGraphComponent().zoom(0.7);
               this.mainwindow.graph.xGetGraph().getView().setTranslate(new mxPoint(100, 0));
               this.mainwindow.graph.getGraphComponent().refresh();
               this.mainwindow.graph.getGraphComponent().setAlignmentX(Component.CENTER_ALIGNMENT);

               this.mainwindow.resultContentPanel.add(Box.createRigidArea(new Dimension(0,15)));
               this.mainwindow.resultContentPanel.add(this.mainwindow.resultTitle);

               // Display best result
               this.mainwindow.graphBestResult = new KargerGraph();
               this.mainwindow.graphBestResult.XMLToGraph(this.mainwindow.graph.getBestResult().getEncodedGraph());
               this.mainwindow.graphBestResult.xGetGraph().getView().setTranslate(new mxPoint(120, 0));
               this.mainwindow.bestResultPanel.add(Box.createRigidArea(new Dimension(0,15)));
               this.mainwindow.bestResultPanel.add(this.mainwindow.graphBestResult.getGraphComponent());
               this.mainwindow.bestResultPanel.add(Box.createRigidArea(new Dimension(0,15)));

               this.mainwindow.resultContentPanel.add(bestResultPanel);
               this.mainwindow.resultContentPanel.add(this.mainwindow.otherResultsTitle);

               // Display other results
               this.mainwindow.graphResults = new ArrayList<KargerGraph>();

               for (KargerGraph.KargerRecord r : this.mainwindow.graph.getResults()) {
                   
                   if (r == this.mainwindow.graph.getBestResult()) {
                      continue;
                   }
                   
                   KargerGraph tmp = new KargerGraph();
                   tmp.XMLToGraph(r.getEncodedGraph());
                   tmp.xGetGraph().getView().setTranslate(new mxPoint(120, 0));
                   this.mainwindow.graphResults.add(tmp);
                   this.mainwindow.otherResultsPanel.add(Box.createRigidArea(new Dimension(0,15)));
                   this.mainwindow.otherResultsPanel.add(tmp.getGraphComponent());
                   
               }
               this.mainwindow.otherResultsPanel.add(Box.createRigidArea(new Dimension(0,15)));
               this.mainwindow.resultContentPanel.add(this.mainwindow.otherResultsPanel);
               break;

            case MANUAL_STEPS:

               clickCounter++;

               switch (clickCounter){
                  case 22:
                     algorithmChoice.setSelectedIndex(7);      // finish
                     clickCounter = 21;
                     break;
                  case 1:
                     algorithmChoice.setSelectedIndex(1);
                     break;
                  case 2:
                     algorithmChoice.setSelectedIndex(3);
                     break;
                  case 3:
                     algorithmChoice.setSelectedIndex(4);

                     int j = 0;
                     for (Object eK: graph.xGetGraph().getChildEdges(graph.xGetGraph().getDefaultParent())) {
                        j++;
                     }

                     if(j == 1){
                        clickCounter = 21;
                     }


                     break;
                  case 5:
                     algorithmChoice.setSelectedIndex(6);
                     break;
                  case 6:
                     algorithmChoice.setSelectedIndex(9);
                     break;
                  case 4:
                  case 7:

                     if (this.mainwindow.isPhase1) {

                        // Disable all buttons except for reset before phase 2 is finished
                        this.mainwindow.undoButton.setEnabled(false);
                        this.mainwindow.stepButton.setEnabled(false);
                        this.mainwindow.runButton.setEnabled(false);
                        this.mainwindow.finishButton.setEnabled(false);

                        this.mainwindow.manualVertices = this.mainwindow.graph.stepPhase1();
                        this.mainwindow.isPhase1 = false;

                     } else {

                        if ((this.mainwindow.manualVertices != null) &&
                                (this.mainwindow.manualVertices.size() == 2)) {
                           this.mainwindow.graph.stepPhase2(this.mainwindow.manualVertices);
                        }
                        this.mainwindow.manualVertices = new ArrayList<mxCell>();

                        // Enable all buttons after phase 2
                        this.mainwindow.undoButton.setEnabled(true);
                        this.mainwindow.stepButton.setEnabled(true);
                        this.mainwindow.runButton.setEnabled(true);
                        this.mainwindow.finishButton.setEnabled(true);
                        this.mainwindow.resetButton.setEnabled(true);

                  this.mainwindow.isPhase1 = true;

                  if (this.mainwindow.graph.isRunFinished()) {
                     this.mainwindow.runTracker.setText("Total Runs: " + this.mainwindow.graph.getRunCounter());
                     this.mainwindow.resultTracker.setText("Best Result: " + this.mainwindow.graph.getBestResultCut());
                     this.mainwindow.stepButton.setEnabled(false);
                     this.mainwindow.manualSteps.setEnabled(false);
                     this.mainwindow.runButton.setEnabled(false);
                  }

                        this.mainwindow.isPhase1 = true;
                     }

                     if(clickCounter == 4)
                        algorithmChoice.setSelectedIndex(5);

                     if(clickCounter == 7){
                        algorithmChoice.setSelectedIndex(13);
                        clickCounter = 2;    // bach to the while
                     }

               }

               break;
         }


         // some action was performed so the lists must be updated
         // in the first step, the "old" list must be cleared
         edgeModel.clear();
         nodeModel.clear();

       //  System.out.println("\nUpdate lists \n");

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



    /**
     * Update edge list based on current graph model.
     */
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

         //System.out.println("first time: " + this.firstTimeCreatedModel);
         if(firstTimeCreatedModel)
             this.edgeChoice = new JList<String>(edgeModel);


      } catch (Exception ex) {
         System.out.println(ex);
      }
   }

    /**
     * Update node list based on current adjacency list.
     */
   public void updateNodeList(){
      try {

         aList = graph.getAdjacencyList();

         if(aList != null){

            for (mxCell value: this.aList.keySet()) {
               nodeModel.addElement(value.getValue().toString());
               //System.out.println("Added element: " + value.getValue().toString());
            }

             //System.out.println("first time: " + this.firstTimeCreatedModel);
            if(firstTimeCreatedModel)
                nodeChoice = new JList<String>(nodeModel);
         }


      } catch (Exception ex) {
         System.out.println(ex);
      }
   }



   // currently not used (and never be?)
   public void disableEditorButtons(){

      this.addNodeButton.setEnabled(false);
      this.removeNodeButton.setEnabled(false);
      this.addEdgeButton.setEnabled(false);
      this.removeEdgeButton.setEnabled(false);

   }

  /* public JList<String> getAlgorithmList(){
       return this.algorithmChoice;
   }*/
}
