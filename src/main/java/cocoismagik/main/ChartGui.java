package cocoismagik.main;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

public class ChartGui extends JFrame{

    private DefaultMutableTreeNode root;
    private JTree tree;
    private JTextField dataField;

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

        // Top bar for editing
        JPanel topBar = new JPanel(new BorderLayout(5, 5));
        dataField = new JTextField();
        JButton updateBtn = new JButton("Update");

        topBar.add(new JLabel("Data:"), BorderLayout.WEST);
        topBar.add(dataField, BorderLayout.CENTER);
        topBar.add(updateBtn, BorderLayout.EAST);
        topBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(topBar, BorderLayout.NORTH);

        // Bottom panel with Add and Remove buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addChildBtn = new JButton("Add Child Node");
        JButton removeNodeBtn = new JButton("Remove Node");
        bottomPanel.add(addChildBtn);
        bottomPanel.add(removeNodeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Tree selection updates field
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                Node n = (Node) selected.getUserObject();
                dataField.setText(n.data);
            }
        });

        // Update selected node data
        updateBtn.addActionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                Node n = (Node) selected.getUserObject();
                n.data = dataField.getText();
                ((DefaultTreeModel) tree.getModel()).nodeChanged(selected);
            }
        });

        // Add new child node
        addChildBtn.addActionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Node("New Node"));
                selected.add(newNode);
                ((DefaultTreeModel) tree.getModel()).reload(selected);
                tree.expandPath(new TreePath(selected.getPath()));
            }
        });

        // Remove node and its subtree
        removeNodeBtn.addActionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null && selected != root) {
                MutableTreeNode parent = (MutableTreeNode) selected.getParent();
                if (parent != null) {
                    ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(selected);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Root node cannot be removed.");
            }
        });

        setVisible(true);
    }
}
