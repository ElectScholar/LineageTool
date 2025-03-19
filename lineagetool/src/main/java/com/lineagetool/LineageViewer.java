package com.lineagetool;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * A graphical user interface (GUI) for visualizing and interacting with a lineage tree.
 * The `LineageViewer` class displays a tree structure of a lineage and provides
 * functionality to sort and view details of individuals in the lineage.
 */
public class LineageViewer extends JFrame {
    private JTree lineageTree;
    private JTextArea infoPanel;
    private LineageService lineageService;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;

    /**
     * Constructs a `LineageViewer` instance.
     *
     * @param lineageService The {@code LineageService} instance used to manage the lineage data.
     */
    public LineageViewer(LineageService lineageService) {
        this.lineageService = lineageService;
        setTitle("Lineage Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Build tree model
        root = buildTree();
        treeModel = new DefaultTreeModel(root);
        lineageTree = new JTree(treeModel);
        lineageTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        lineageTree.addTreeSelectionListener(e -> updateInfoPanel());

        // Info panel
        infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        infoPanel.setLineWrap(true);
        infoPanel.setWrapStyleWord(true);

        // Button to sort entire tree
        JButton sortButton = new JButton("Sort Entire Tree");
        sortButton.addActionListener(e -> sortAndRefresh());

        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(lineageTree), BorderLayout.CENTER);
        leftPanel.add(sortButton, BorderLayout.SOUTH); // Add button below tree

        add(leftPanel, BorderLayout.WEST);
        add(new JScrollPane(infoPanel), BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Builds the root of the lineage tree.
     *
     * @return The root {@code DefaultMutableTreeNode} of the lineage tree.
     *         If no data is available, a placeholder node is returned.
     */
    private DefaultMutableTreeNode buildTree() {
        Node<Person> rootPerson = lineageService.getNode("Jacob"); // Root of the lineage
        if (rootPerson == null) return new DefaultMutableTreeNode("No Data");

        return buildTreeRecursive(rootPerson);
    }

    /**
     * Recursively builds the lineage tree starting from a given node.
     *
     * @param personNode The {@code Node<Person>} representing the current person in the lineage.
     * @return A {@code DefaultMutableTreeNode} representing the current person and their descendants.
     */
    private DefaultMutableTreeNode buildTreeRecursive(Node<Person> personNode) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(personNode.val.getName());

        for (Node<Person> child : personNode.next) {
            treeNode.add(buildTreeRecursive(child));
        }
        return treeNode;
    }

    /**
     * Updates the information panel with details of the selected person in the tree.
     */
    private void updateInfoPanel() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) lineageTree.getLastSelectedPathComponent();
        if (selectedNode == null) return;

        String personName = selectedNode.getUserObject().toString();
        Node<Person> personNode = lineageService.getNode(personName);
        if (personNode == null) {
            infoPanel.setText("No details available.");
            return;
        }

        Person person = personNode.val;
        StringBuilder sb = new StringBuilder("Name: " + person.getName() + "\n\n");
        for (String detail : person.getDetails()) {
            sb.append(detail).append("\n");
        }
        infoPanel.setText(sb.toString());
    }

    /**
     * Sorts the entire lineage tree and refreshes the GUI to reflect the changes.
     */
    private void sortAndRefresh() {
        Node<Person> rootPerson = lineageService.getNode("Jacob");
        if (rootPerson == null) {
            JOptionPane.showMessageDialog(this, "Root node not found.");
            return;
        }

        sortAllNodes(rootPerson);

        // Refresh UI
        root.removeAllChildren();
        rebuildTree(root, rootPerson);
        treeModel.reload();
    }

    /**
     * Recursively sorts all nodes in the lineage tree.
     *
     * @param node The {@code Node<Person>} representing the current node to sort.
     */
    private void sortAllNodes(Node<Person> node) {
        if (node.next.isEmpty()) return;

        node.next = Sorting.sortChildren(node.next); // Sort children
        for (Node<Person> child : node.next) {
            sortAllNodes(child); // Recursively sort each subtree
        }
    }

    /**
     * Rebuilds the lineage tree in the GUI after sorting.
     *
     * @param treeNode The {@code DefaultMutableTreeNode} representing the current tree node in the GUI.
     * @param node     The {@code Node<Person>} representing the current node in the lineage.
     */
    private void rebuildTree(DefaultMutableTreeNode treeNode, Node<Person> node) {
        for (Node<Person> child : node.next) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child.val.getName());
            treeNode.add(childNode);
            rebuildTree(childNode, child);
        }
    }
}