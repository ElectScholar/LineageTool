package com.lineagetool;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;  // Add this import

// ...rest of the file remains the same...

public class LineageViewer extends JFrame {
    private mxGraph graph;
    private Object parent;
    private JTextArea infoPanel;
    private LineageService lineageService;
    private mxGraphComponent graphComponent;
    private List<String> rootNodes = new ArrayList<>();
    private Map<String, Object> vertexMap = new HashMap<>();  // Add this as a class field

    public LineageViewer(LineageService lineageService) {
        this.lineageService = lineageService;
        setTitle("Lineage Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create graph
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        graphComponent = new mxGraphComponent(graph);
        
        // Enable moving of vertices
        graph.setCellsMovable(true);
        graph.setCellsResizable(false);
        graph.setAllowDanglingEdges(false);
        
        // Info panel
        infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        infoPanel.setLineWrap(true);
        infoPanel.setWrapStyleWord(true);

        // Default root nodes
        rootNodes.add("Jacob");
        
        // Build tree
        buildGraph();
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            graphComponent, new JScrollPane(infoPanel));
        splitPane.setDividerLocation(600);
        add(splitPane, BorderLayout.CENTER);
        
        // Add mouse listener for selection
        graphComponent.getGraphControl().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null) {
                    String personName = graph.getLabel(cell);
                    updateInfoPanel(personName);
                }
            }
        });
    }

    private void buildGraph() {
        graph.getModel().beginUpdate();
        try {
            // Clear existing graph and vertex map
            graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
            vertexMap.clear();
            
            // Build trees for each root node
            double xOffset = 0;
            for (String rootName : rootNodes) {
                Node<Person> rootPerson = lineageService.getNode(rootName);
                if (rootPerson != null) {
                    Object vertex = buildGraphRecursive(rootPerson, null);
                    // Position root nodes horizontally
                    graph.getModel().setGeometry(vertex, 
                        new mxGeometry(xOffset, 0, 100, 40));
                    xOffset += 200; // Space between root nodes
                }
            }
            
            // Apply hierarchical layout
            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.setInterRankCellSpacing(50);
            layout.execute(parent);
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private Object buildGraphRecursive(Node<Person> personNode, Object parentVertex) {
        String personName = personNode.val.getName();
        Object vertex;
        
        // Check if we already created this vertex
        if (vertexMap.containsKey(personName)) {
            vertex = vertexMap.get(personName);
            // Add edge if there's a parent and we haven't connected them yet
            if (parentVertex != null) {
                Object[] edges = graph.getEdgesBetween(parentVertex, vertex);
                if (edges.length == 0) {
                    graph.insertEdge(parent, null, "", parentVertex, vertex,
                        "strokeColor=#666666");
                }
            }
        } else {
            // Create new vertex
            vertex = graph.insertVertex(parent, null, personName,
                0, 0, 100, 40, "rounded=1;strokeColor=#666666;fillColor=#f5f5f5");
            vertexMap.put(personName, vertex);
            
            if (parentVertex != null) {
                graph.insertEdge(parent, null, "", parentVertex, vertex,
                    "strokeColor=#666666");
            }

            // Process children only for new vertices to avoid cycles
            for (Node<Person> child : personNode.next) {
                buildGraphRecursive(child, vertex);
            }
        }
        
        return vertex;
    }

    private void updateInfoPanel(String personName) {
        if (personName == null) {
            infoPanel.setText("No details available.");
            return;
        }
        
        Node<Person> personNode = lineageService.getNode(personName);
        if (personNode == null) {
            infoPanel.setText("No details available for: " + personName);
            return;
        }
        
        Person person = personNode.val;
        StringBuilder sb = new StringBuilder("Name: " + person.getName() + "\n\n");
        for (String detail : person.getDetails()) {
            sb.append(detail).append("\n");
        }
        infoPanel.setText(sb.toString());
    }

    public void addRootNode(String personName) {
        if (!rootNodes.contains(personName) && lineageService.getNode(personName) != null) {
            rootNodes.add(personName);
            buildGraph();
        }
    }
}