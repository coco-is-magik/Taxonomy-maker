package cocoismagik.main;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

public class ChartGui extends JFrame{

    private DefaultMutableTreeNode root;
    private JTree tree;
    private JTextField dataField;
    private JButton addChildBtn;

    public ChartGui() {
        setTitle("Taxonomy Maker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        root = new DefaultMutableTreeNode(new Node("Root Node"));
        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode(new Node("Child A"));
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(new Node("Child B"));
        root.add(child1);
        root.add(child2);

        tree = new JTree(root);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        add(new JScrollPane(tree), BorderLayout.CENTER);

        JPanel detailPanel = new JPanel(new GridLayout(2, 2));
        dataField = new JTextField();
        JButton updateBtn = new JButton("Update");

        detailPanel.add(new JLabel("Data:"));
        detailPanel.add(dataField);
        detailPanel.add(new JLabel());
        detailPanel.add(updateBtn);
        add(detailPanel, BorderLayout.EAST);

        addChildBtn = new JButton("Add Child Node");
        add(addChildBtn, BorderLayout.SOUTH);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                Node n = (Node) selected.getUserObject();
                dataField.setText(n.data);
            }
        });

        updateBtn.addActionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                Node n = (Node) selected.getUserObject();
                n.data = dataField.getText();
                ((DefaultTreeModel) tree.getModel()).nodeChanged(selected);
            }
        });

        addChildBtn.addActionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Node("New Node"));
                selected.add(newNode);
                ((DefaultTreeModel) tree.getModel()).reload(selected);
                tree.expandPath(new TreePath(selected.getPath()));
            }
        });

        setVisible(true);
    }
}
