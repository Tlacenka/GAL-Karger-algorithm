package karger;


import javax.swing.*;
import java.awt.*;

public class UserGuideWindow {

    public void showGuideDialog(){

        Font textFont = new Font("Calibri", Font.PLAIN, 15);
        JEditorPane textArea = new JEditorPane("text/html", "");
        textArea.setText("<h1>Karger Algorithm for Finding Minimal Cut</h1>" +
                "<p>In computer science and graph theory, Karger's algorithm is a randomized algorithm to compute " +
                "a minimum cut of a connected graph. It was invented by David Karger and first published in 1993.</p>" +
                "<p>The basic idea of the Karger Algorithm is that in each step, one edges is removed and the vertices" +
                " connected to this edge are merged into one. As this produces a multigraph, we decided to give edges" +
                " weights based on how many edges are connecting the given pair of vertices. The algorithm ends when" +
                " there are two vertices left, dividing the original set of vertices into two groups, and one edge" +
                " that represents the cut. In order to find minimal cut, trying out all possible orders of removing edges" +
                " is needed (altogether that makes |E|! runs).</p>" +
                "<h2>Algorithm Complexity</h2>" +
                "<p>As said above, trying out all possible edge orders can be completed in |E|! runs, in other words polynomial time" +
                " with regards to the number of edges.</p>" +
                "<p> One step of the algorithm for vertices v1 and v2 involves going through all adjacent nodes of v2, finding the edge " +
                "connecting them to v2 and redirecting it to v1. Consequently, multiple edges between two vertices are merged into one and" +
                "the edge weight is adjusted.</p>" +
                "<p>Therefore, one step is also completed in polynomial time with regards to both the number of vertices and edges.</p>" +
                "<h2> The application design</h2>" +
                "<p>The main window of this application is divided into four main sections:</p>" +
                "<ul><li> the <b>graph editor</b> on the left </li>" +
                "<li> the <b>algorithm panel</b> on the right (highlighting the current phase of the step in case of manual step)</li>" +
                "<li> the <b>control panel</b> at the bottom </li> \n" +
                "<li> the <b>simulation panel</b> in the centre </li>" +
                "<li> the <b>tracker panel</b> at the top </li></ul>" +
                "<p>At the very top of the window, there are 2 items. The <b>Menu</b> contains the options for " +
                "creating a new graph, loading a graph from file and saving the current graph into a file. " +
                "The second item is called <b>Help</b> and its menu contains this User Guide and a About section.</p>" +
                "<h2>How to control this application</h2>" +
                "<p>The control panel contains five buttons." +
                " Going from left to right, these are:</p>" +
                "<ul><li> <b>reset</b> button</li>" +
                "<li> <b>next step</b> button</li>" +
                "<li> <b>finish current run</b> button</li>" +
                "<li> <b>finish the whole algorithm</b> button</li>" +
                "<li> <b>manual step</b> button</li></ul>" +
                "<p><i>Note: As this is a randomised algorithm, user cannot pick which edge will be removed next.</i></p>" +
                "<p><b>Reset</b> button always returns the graph to its original state and enables starting over.</p>" +
                "<p> As the other button names suggest, user can choose to run the application in multiple ways." +
                " Either the user is concerned with only a single run of the algorithm (achievable by the <b>next step</b>, " +
                "<b>manual step</b> or <b>finish run</b> buttons) or all possible runs (<b>finish algorithm</b> button).</p>" +
                "<h3>Single run mode</h3>" +
                " <b>Next step</b> button completes one step (removes one edge) and displays the new graph." +
                " <b>Manual step</b> is a two-phase step. When the button is clicked for the first time, the two" +
                " vertices that are about to be merged are highlighted. After the second click of the button, " +
                "these vertices are merged and the step is complete. <b>Finish run</b> completes the current run of the algorithm " +
                "and displays the resulting graph.</p>" +
                "<h3> Multiple runs mode </h3>" +
                "<p><b>Finish algorithm</b> internally tries out all possible options and displays the original graph, " +
                "best result and the other result below it to the user in a scrollable window.</p>" +
                "<h2> Graph editor </h2>" +
                "<p>User can create a new graph (in order to remove the current graph, go <i>Menu > Create Graph</i>) or "+
                "edit the current one by adding/removing its vertices and edges.</p>" +
                "<p>In order to add an edge/vertex, click the <b>+</b> icon below the corresponding panel and choose a name for the new item. " +
                "Additionally, after adding a new vertex, user can change its location by dragging it elsewhere in the simulation panel.</p>" +
                "<p>When removing an edge or vertex, click on their name in the Graph Editor panel and then click on the <b>-</b> icon.</p>" +
                "<p><i>Note: This algorithm is implemented for consecutive graphs only. " +
                "Therefore, whenever graph becomes non-consecutive in the editing process, control buttons are disabled.</i></p>");

        textArea.setFont(textFont);
        JScrollPane scrollPane = new JScrollPane(textArea);
        //textArea.setLineWrap(true);
        //textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 600, 400 ) );
        JOptionPane.showMessageDialog(null, scrollPane, "User Guide",
                JOptionPane.YES_NO_OPTION);

    }

}
