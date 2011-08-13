import sun.tools.tree.ThisExpression;

import javax.swing.*;

/**
 * Created by Pål Arne Hoff.
 * Date: 11.08.11
 * Time: 16:07
 */
public class MainGUI {
    private JTabbedPane tabbedPane1;
    private JComboBox accountComboBox;
    private JTable transactionTable;
    private JButton importCSVButton;
    private JButton saveToQIFButton;

    public MainGUI() {
    }

    public JTable getTransactionTable() {
        return transactionTable;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }
}
