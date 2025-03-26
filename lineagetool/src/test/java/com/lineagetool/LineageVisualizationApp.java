package com.lineagetool;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class LineageVisualizationApp extends JFrame {
    private mxGraph graph;
    private mxGraphComponent graphComponent;
    private LineageService lineageService;
    private Map<String, Object> vertexMap;
    private JTextArea detailsPanel;

    private Node<Person> baseRoot;

    public LineageVisualizationApp(FileRead fileRead) {
        this.lineageService = fileRead.getLineageService();
        this.vertexMap = new HashMap<>();
        this.baseRoot = lineageService.getNode("Isaac");

        
        setTitle("Interactive Lineage Graph");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initComponents();
        setupLayout();
        
        // Initially populate the graph
        populateGraph();
    }

    private void initComponents() {
        // Initialize graph
        graph = new mxGraph();
        graph.setAllowDanglingEdges(false);
        graph.setAllowLoops(false);
        
        // Create graph component
        graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);
        
        // Add mouse listener for vertex interaction
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null) {
                    String personName = graph.getLabel(cell);
                    updateDetailsPanel(findPersonByName(personName));
                }
            }
        });

        // Details panel
        detailsPanel = new JTextArea();
        detailsPanel.setEditable(false);
        detailsPanel.setLineWrap(true);
        detailsPanel.setWrapStyleWord(true);
        
        // Buttons
        JButton addPersonButton = new JButton("Add Person");
        addPersonButton.addActionListener(e -> showAddPersonDialog());
        
        JButton removePersonButton = new JButton("Remove Person");
        removePersonButton.addActionListener(e -> removeSelectedPerson());
    }

    private void setupLayout() {
        // Main panel with split layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Left side - Graph
        splitPane.setLeftComponent(graphComponent);
        
        // Right side - Details and Buttons
        JPanel rightPanel = new JPanel(new BorderLayout());
        JScrollPane detailsScrollPane = new JScrollPane(detailsPanel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addPersonButton = new JButton("Add Person");
        addPersonButton.addActionListener(e -> showAddPersonDialog());
        
        JButton removePersonButton = new JButton("Remove Person");
        removePersonButton.addActionListener(e -> removeSelectedPerson());
        
        buttonPanel.add(addPersonButton);
        buttonPanel.add(removePersonButton);
        
        rightPanel.add(detailsScrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        splitPane.setRightComponent(rightPanel);
        
        // Set initial divider location
        splitPane.setDividerLocation(800);
        
        add(splitPane);
    }

    private void populateGraph() {
        // Begin graph update
        graph.getModel().beginUpdate();
        try {
            // Clear existing graph
            graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
            vertexMap.clear();
            
            // Recursively add nodes
            if (baseRoot != null) {
                addNodeToGraph(baseRoot, null);
            }
            
            // Apply hierarchical layout
            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.execute(graph.getDefaultParent());
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private Object addNodeToGraph(Node<Person> node, Object parentVertex) {
        Person person = node.val;
        
        // Create vertex for this person
        Object vertex = graph.insertVertex(
            graph.getDefaultParent(), 
            null, 
            person.getName(), 
            0, 0, 100, 50, 
            "fillColor=#E0F8E0;fontStyle=1"
        );
        
        // Store in vertex map for later reference
        vertexMap.put(person.getName(), vertex);
        
        // Add edge from parent if parent exists
        if (parentVertex != null) {
            graph.insertEdge(graph.getDefaultParent(), null, "", parentVertex, vertex);
        }
        
        // Recursively add children
        for (Node<Person> child : node.next) {
            addNodeToGraph(child, vertex);
        }
        
        return vertex;
    }

    private void updateDetailsPanel(Person person) {
        if (person != null) {
            StringBuilder details = new StringBuilder();
            details.append("Name: ").append(person.getName()).append("\n");
            details.append("Title: ").append(person.getDetails()[0]).append("\n");
            details.append("Lifespan: ").append(person.getDetails()[1]).append("\n");
            details.append("Scripture: ").append(person.getDetails()[2]).append("\n");
            details.append("Description: ").append(person.getDetails()[3]);
            
            detailsPanel.setText(details.toString());
        }
    }

    private void showAddPersonDialog() {
        // Create dialog for adding a new person
        JDialog addPersonDialog = new JDialog(this, "Add New Person", true);
        addPersonDialog.setLayout(new GridLayout(6, 2));
        
        JTextField nameField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField lifespanField = new JTextField();
        JTextField scriptureField = new JTextField();
        JTextArea descriptionArea = new JTextArea();
        JTextField parentField = new JTextField();
        
        addPersonDialog.add(new JLabel("Name:"));
        addPersonDialog.add(nameField);
        addPersonDialog.add(new JLabel("Title:"));
        addPersonDialog.add(titleField);
        addPersonDialog.add(new JLabel("Lifespan:"));
        addPersonDialog.add(lifespanField);
        addPersonDialog.add(new JLabel("Scripture:"));
        addPersonDialog.add(scriptureField);
        addPersonDialog.add(new JLabel("Description:"));
        addPersonDialog.add(new JScrollPane(descriptionArea));
        addPersonDialog.add(new JLabel("Parent Name (optional):"));
        addPersonDialog.add(parentField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String[] details = {
                titleField.getText(),
                lifespanField.getText(),
                scriptureField.getText(),
                descriptionArea.getText()
            };
            
            Person newPerson = new Person(nameField.getText(), details);
            Node<Person> newNode = new Node<>(newPerson);
            
            // Add to lineage service
            if (parentField.getText().isEmpty()) {
                lineageService.addFirst(newNode);
            } else {
                lineageService.addChild(newNode, parentField.getText());
            }
            
            // Repopulate graph
            populateGraph();
            
            addPersonDialog.dispose();
        });
        
        addPersonDialog.add(saveButton);
        
        addPersonDialog.setSize(400, 500);
        addPersonDialog.setLocationRelativeTo(this);
        addPersonDialog.setVisible(true);
    }

    private void removeSelectedPerson() {
        // Get currently displayed person from details panel
        String personName = detailsPanel.getText().split("\n")[0].replace("Name: ", "");
        
        if (!personName.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to remove " + personName + "?",
                "Confirm Removal", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                lineageService.removePerson(personName);
                
                // Repopulate graph
                populateGraph();
                
                // Clear details panel
                detailsPanel.setText("");
            }
        }
    }

    private Person findPersonByName(String name) {
        if (baseRoot == null) return null;
        
        Node<Person> foundNode = findPersonNode(baseRoot, name);
        return foundNode != null ? foundNode.val : null;
    }

    private Node<Person> findPersonNode(Node<Person> currentNode, String name) {
        // Check current node
        if (currentNode.val.getName().equals(name)) {
            return currentNode;
        }

        // Recursively search children
        for (Node<Person> child : currentNode.next) {
            Node<Person> found = findPersonNode(child, name);
            if (found != null) {
                return found;
            }
        }

        return null;
    }
}