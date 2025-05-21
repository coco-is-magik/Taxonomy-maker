package cocoismagik.main;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
//import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChartGui extends JFrame{

    private DefaultMutableTreeNode root;
    private JTree tree;
    private JTextField dataField, data2Field;
    private JTextArea data3Area;

    public ChartGui() {
        setTitle("Taxonomy Maker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        root = new DefaultMutableTreeNode(new Node("Root Node", "Type A"));
        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode(new Node("Child A", "Type B"));
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(new Node("Child B", "Type C"));
        root.add(child1);
        root.add(child2);

        tree = new JTree(root);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        add(new JScrollPane(tree), BorderLayout.CENTER);

        // Top bar for data editing
        JPanel topBar = new JPanel(new BorderLayout(10, 10));
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        dataField = new JTextField(20);
        data2Field = new JTextField(20);
        JButton updateBtn = new JButton("Update");
        data3Area = new JTextArea(5, 30);
        data3Area.setLineWrap(true);
        data3Area.setWrapStyleWord(true);

        // Row 1: Data 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 5);
        fieldsPanel.add(new JLabel("Binomial Name:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(dataField, gbc);

        // Row 2: Data 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel("Clade:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(data2Field, gbc);

        // Row 3: Update Button (same width as text fields)
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(updateBtn, gbc);

        // Layout with Data3 area
        topBar.add(fieldsPanel, BorderLayout.WEST);
        topBar.add(new JScrollPane(data3Area), BorderLayout.CENTER);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(topBar, BorderLayout.NORTH);

        // Bottom controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addChildBtn = new JButton("Add Child Node");
        JButton removeNodeBtn = new JButton("Remove Node");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(addChildBtn);
        bottomPanel.add(removeNodeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Selection listener
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                Node n = (Node) selected.getUserObject();
                dataField.setText(n.data);
                data2Field.setText(n.data2);
                data3Area.setText(n.data3);
            }
        });

        // Update button action
        updateBtn.addActionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                Node n = (Node) selected.getUserObject();
                n.data = dataField.getText();
                n.data2 = data2Field.getText();
                n.data3 = data3Area.getText();
                ((DefaultTreeModel) tree.getModel()).nodeChanged(selected);
            }
        });

        // Add child node
        addChildBtn.addActionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Node("New Node"));
                selected.add(newNode);
                ((DefaultTreeModel) tree.getModel()).reload(selected);
                tree.expandPath(new TreePath(selected.getPath()));
            }
        });

        // Remove node
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

        // Save to file using JFileChooser
        saveBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Tree");
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                    out.writeObject(root);
                    JOptionPane.showMessageDialog(this, "Tree saved successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to save tree.");
                }
            }
        });

        // Load from file using JFileChooser
        loadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Tree");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                    root = (DefaultMutableTreeNode) in.readObject();
                    tree.setModel(new DefaultTreeModel(root));
                    ((DefaultTreeModel) tree.getModel()).reload();
                    expandAll(tree);
                    JOptionPane.showMessageDialog(this, "Tree loaded successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to load tree.");
                }
            }
        });

        setVisible(true);
    }

    private void expandAll(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}
