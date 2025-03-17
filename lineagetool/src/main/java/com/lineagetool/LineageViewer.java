package com.lineagetool;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class LineageViewer extends JFrame {
    private JTree lineageTree;
    private JTextArea infoPanel;
    private LineageService lineageService;

    public LineageViewer(LineageService lineageService) {
        this.lineageService = lineageService;
        setTitle("Lineage Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Build tree model
        DefaultMutableTreeNode root = buildTree();
        lineageTree = new JTree(root);
        lineageTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        lineageTree.addTreeSelectionListener(e -> updateInfoPanel());
        
        // Info panel
        infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        infoPanel.setLineWrap(true);
        infoPanel.setWrapStyleWord(true);
        
        // Layout
        add(new JScrollPane(lineageTree), BorderLayout.WEST);
        add(new JScrollPane(infoPanel), BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode buildTree() {
        Node<Person> rootPerson = lineageService.getNode("Jacob"); // Root of the lineage
        if (rootPerson == null) return new DefaultMutableTreeNode("No Data");
        
        return buildTreeRecursive(rootPerson);
    }
    
    private DefaultMutableTreeNode buildTreeRecursive(Node<Person> personNode) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(personNode.val.getName());
        
        for (Node<Person> child : personNode.next) {
            treeNode.add(buildTreeRecursive(child));
        }
        return treeNode;
    }
    
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
    
}