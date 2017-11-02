/**
 * This class implements the main window and its components.
 * @author Michal Tabasek (xtabas02)
 * @date 2017
 */


package karger;


public class MainWin extends javax.swing.JFrame {

    /**
     * Creates new form MainWin
     */
    public MainWin() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        controlPanel = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        nextStepButton = new javax.swing.JButton();
        stepBackButton = new javax.swing.JButton();
        finishButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        simulationPanel = new javax.swing.JPanel();
        editorPanel = new javax.swing.JPanel();
        listPanel = new javax.swing.JScrollPane();
        addNewEntry = new javax.swing.JButton();
        removeEntry = new javax.swing.JButton();
        algorithmPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newFileMenu = new javax.swing.JMenuItem();
        openFileMenu = new javax.swing.JMenuItem();
        saveFileMenu = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        userGuideHelp = new javax.swing.JMenuItem();
        aboutHelp = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        controlPanel.setBackground(new java.awt.Color(70, 70, 70));

        playButton.setBackground(new java.awt.Color(255, 255, 255));
        playButton.setText(">");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        nextStepButton.setBackground(new java.awt.Color(255, 255, 255));
        nextStepButton.setText(">>");
        nextStepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextStepButtonActionPerformed(evt);
            }
        });

        stepBackButton.setBackground(new java.awt.Color(255, 255, 255));
        stepBackButton.setText("<<");
        stepBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepBackButtonActionPerformed(evt);
            }
        });

        finishButton.setBackground(new java.awt.Color(255, 255, 255));
        finishButton.setText("Finish");
        finishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });

        resetButton.setBackground(new java.awt.Color(255, 255, 255));
        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(resetButton)
                .addGap(12, 12, 12)
                .addComponent(stepBackButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextStepButton, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(finishButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playButton)
                    .addComponent(nextStepButton)
                    .addComponent(stepBackButton)
                    .addComponent(finishButton)
                    .addComponent(resetButton))
                .addContainerGap())
        );

        simulationPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout simulationPanelLayout = new javax.swing.GroupLayout(simulationPanel);
        simulationPanel.setLayout(simulationPanelLayout);
        simulationPanelLayout.setHorizontalGroup(
            simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );
        simulationPanelLayout.setVerticalGroup(
            simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
        );

        listPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        addNewEntry.setBackground(new java.awt.Color(255, 255, 255));
        addNewEntry.setText("Add new");

        removeEntry.setBackground(new java.awt.Color(255, 255, 255));
        removeEntry.setText("Remove");

        javax.swing.GroupLayout editorPanelLayout = new javax.swing.GroupLayout(editorPanel);
        editorPanel.setLayout(editorPanelLayout);
        editorPanelLayout.setHorizontalGroup(
            editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listPanel)
            .addGroup(editorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addNewEntry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(removeEntry)
                .addContainerGap())
        );
        editorPanelLayout.setVerticalGroup(
            editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editorPanelLayout.createSequentialGroup()
                .addComponent(listPanel)
                .addGap(18, 18, 18)
                .addGroup(editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewEntry)
                    .addComponent(removeEntry))
                .addGap(26, 26, 26))
        );

        algorithmPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setBorder(null);

        jList1.setBackground(new java.awt.Color(240, 240, 240));
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "ALGORITHM" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout algorithmPanelLayout = new javax.swing.GroupLayout(algorithmPanel);
        algorithmPanel.setLayout(algorithmPanelLayout);
        algorithmPanelLayout.setHorizontalGroup(
            algorithmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );
        algorithmPanelLayout.setVerticalGroup(
            algorithmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, algorithmPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenuBar1.setBackground(new java.awt.Color(70, 70, 70));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jMenuBar1.setForeground(new java.awt.Color(239, 239, 239));

        fileMenu.setForeground(new java.awt.Color(235, 235, 235));
        fileMenu.setText("File");
        fileMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileMenuMouseClicked(evt);
            }
        });

        newFileMenu.setText("New file");
        fileMenu.add(newFileMenu);

        openFileMenu.setText("Open file");
        openFileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileMenuActionPerformed(evt);
            }
        });
        fileMenu.add(openFileMenu);

        saveFileMenu.setText("Save file");
        saveFileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileMenuActionPerformed(evt);
            }
        });
        fileMenu.add(saveFileMenu);

        jMenuBar1.add(fileMenu);

        helpMenu.setForeground(new java.awt.Color(235, 235, 235));
        helpMenu.setText("Help");
        helpMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                helpMenuMouseClicked(evt);
            }
        });
        helpMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuActionPerformed(evt);
            }
        });

        userGuideHelp.setText("User guide");
        userGuideHelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userGuideHelpMouseClicked(evt);
            }
        });
        userGuideHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userGuideHelpActionPerformed(evt);
            }
        });
        helpMenu.add(userGuideHelp);

        aboutHelp.setText("About");
        aboutHelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aboutHelpMouseClicked(evt);
            }
        });
        aboutHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutHelpActionPerformed(evt);
            }
        });
        helpMenu.add(aboutHelp);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(editorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(simulationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(algorithmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(simulationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(algorithmPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }

    private void helpMenuActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void helpMenuMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void fileMenuMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void userGuideHelpActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        UserGuideWindow userGuideWin = new UserGuideWindow();
        userGuideWin.setVisible(true);
    }

    private void userGuideHelpMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void aboutHelpMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void aboutHelpActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        AboutWindow aboutWin = new AboutWindow();
        aboutWin.setVisible(true);
    }

    private void openFileMenuActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        OpenFileWindow openFileWin = new OpenFileWindow();
        openFileWin.setVisible(true);
    }

    private void saveFileMenuActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        SaveFileWindow saveFileWin = new SaveFileWindow();
        saveFileWin.setVisible(true);
    }


    // Control panel action

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void nextStepButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void stepBackButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }







    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JMenuItem aboutHelp;
    private javax.swing.JButton addNewEntry;
    private javax.swing.JPanel algorithmPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton finishButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane listPanel;
    private javax.swing.JMenuItem newFileMenu;
    private javax.swing.JButton nextStepButton;
    private javax.swing.JMenuItem openFileMenu;
    private javax.swing.JButton playButton;
    private javax.swing.JButton removeEntry;
    private javax.swing.JButton resetButton;
    private javax.swing.JMenuItem saveFileMenu;
    private javax.swing.JPanel simulationPanel;
    private javax.swing.JButton stepBackButton;
    private javax.swing.JMenuItem userGuideHelp;
}
