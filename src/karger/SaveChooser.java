/**
 * This class implements the save options.
 * @author Michal Tabasek (xtabas02)
 * @date 2017
 */


package karger;


public class SaveChooser extends javax.swing.JPanel implements java.beans.Customizer {

    private Object bean;

    /** Creates new customizer saveChooser */
    public SaveChooser() {
        initComponents();
    }

    public void setObject(Object bean) {
        this.bean = bean;
    }

    /** This method is called from within the constructor to
     *  initialize the form.
     */
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());

    }
}
