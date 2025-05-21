package cocoismagik.main;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
//import java.awt.event.*;

public class ChartGui extends JFrame{

    private DefaultMutableTreeNode root;
    private JTree tree;
    private JTextField dataField, data2Field;

    public ChartGui() {
        setTitle("Taxonomy Maker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        root = new DefaultMutableTreeNode(new Node("Root Node"));
        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode(new Node("Child A", "Type B"));
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(new Node("Child B", "Type C"));
        root.add(child1);
        root.add(child2);

        tree = new JTree(root);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        add(new JScrollPane(tree), BorderLayout.CENTER);

        // Top bar for editing data and data2 — now in a single row
        JPanel topBar = new JPanel(new BorderLayout(5, 5));
        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dataField = new JTextField(15);
        data2Field = new JTextField(15);
        JButton updateBtn = new JButton("Update");

        fieldsPanel.add(new JLabel("Data 1:"));
        fieldsPanel.add(dataField);
        fieldsPanel.add(new JLabel("Data 2:"));
        fieldsPanel.add(data2Field);
        fieldsPanel.add(updateBtn);

        topBar.add(fieldsPanel, BorderLayout.CENTER);
        topBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(topBar, BorderLayout.NORTH);

        // Bottom panel with Add and Remove buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addChildBtn = new JButton("Add Child Node");
        JButton removeNodeBtn = new JButton("Remove Node");
        bottomPanel.add(addChildBtn);
        bottomPanel.add(removeNodeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Tree selection updates fields
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                Node n = (Node) selected.getUserObject();
                dataField.setText(n.data);
                data2Field.setText(n.data2);
            }
        });

        // Update selected node
        updateBtn.addActionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                Node n = (Node) selected.getUserObject();
                n.data = dataField.getText();
                n.data2 = data2Field.getText();
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

        // Remove selected node (except root)
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
