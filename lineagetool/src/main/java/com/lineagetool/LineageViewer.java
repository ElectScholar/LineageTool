package com.lineagetool;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.JDialog;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Point;

public class LineageViewer extends JFrame {
    private mxGraph graph;
    private Object parent;
    private JDialog infoDialog;
    private JTextArea infoPanel;
    private LineageService lineageService;
    private mxGraphComponent graphComponent;
    private List<String> rootNodes = new ArrayList<>();
    private Map<String, Object> vertexMap = new HashMap<>();

    private static final String STYLE_EXPANDED = "rounded=1;strokeColor=#666666;fillColor=#f5f5f5";
    private static final String STYLE_COLLAPSED = "rounded=1;strokeColor=#666666;fillColor=#e0e0e0";
    private static final String EXPAND_ICON = "+";
    private static final String COLLAPSE_ICON = "-";
    private static final double SCROLL_SPEED = 5.0; // Adjust this value to change scroll speed
    private static final double ZOOM_FACTOR = 1.2;  // Adjust for zoom sensitivity
    private static final String HIGHLIGHT_STYLE = "rounded=1;strokeColor=#FF0000;strokeWidth=3;fillColor=#f5f5f5";
    private static final String HIGHLIGHT_EDGE_STYLE = "strokeColor=#FF0000;strokeWidth=2";
    private List<Object> currentlyHighlighted = new ArrayList<>();

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
        graph.setCellsEditable(false);
        graph.setAutoSizeCells(true);
        
        // Create info dialog instead of split pane
        infoDialog = new JDialog(this, false); // false for non-modal
        infoDialog.setUndecorated(true); // Remove window decorations
        
        infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        infoPanel.setLineWrap(true);
        infoPanel.setWrapStyleWord(true);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setPreferredSize(new Dimension(250, 150)); // Small fixed size
        infoDialog.add(scrollPane);
        infoDialog.pack();
        
        // Add the graph component directly to frame
        add(graphComponent, BorderLayout.CENTER);

        // Default root nodes
        rootNodes.add("Isaac");
        
        // Build tree
        buildGraph();
        
        // Add mouse listener for selection
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null && cell instanceof mxCell) {
                    mxCell clickedCell = (mxCell) cell;
                    if (e.getClickCount() == 2) {  // Double click to collapse/expand
                        toggleCollapse(clickedCell);
                    } else if (e.getClickCount() == 1) {  // Single click for info
                        String personName = graph.getLabel(cell).replace(EXPAND_ICON, "").replace(COLLAPSE_ICON, "").trim();
                        updateInfoPanel(personName);
                        highlightPathToRoot(clickedCell);
                    }
                } else {
                    hideInfoPanel();
                }
            }
        });

        // Add scroll and zoom control
        graphComponent.getGraphControl().addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    // Zoom when Ctrl is pressed
                    if (e.getWheelRotation() < 0) {
                        graphComponent.zoomIn();
                    } else {
                        graphComponent.zoomOut();
                    }
                } else {
                    // Enhanced scrolling without Ctrl
                    int scrollAmount = (int)(e.getUnitsToScroll() * SCROLL_SPEED);
                    if (e.isShiftDown()) {
                        // Horizontal scroll when Shift is pressed
                        graphComponent.getHorizontalScrollBar().setValue(
                            graphComponent.getHorizontalScrollBar().getValue() + 
                            scrollAmount);
                    } else {
                        // Vertical scroll
                        graphComponent.getVerticalScrollBar().setValue(
                            graphComponent.getVerticalScrollBar().getValue() + 
                            scrollAmount);
                    }
                }
                e.consume();
            }
        });
        
        // Enable scroll optimization
        graphComponent.setTripleBuffered(true);
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

        if (vertexMap.containsKey(personName)) {
            vertex = vertexMap.get(personName);
            if (parentVertex != null) {
                Object[] edges = graph.getEdgesBetween(parentVertex, vertex);
                if (edges.length == 0) {
                    graph.insertEdge(parent, null, "", parentVertex, vertex,
                        "strokeColor=#666666");
                }
            }
        } else {
            // Add collapse/expand indicator to vertices with children
            String label = personName;
            if (!personNode.next.isEmpty()) {
                label += " " + COLLAPSE_ICON;
            }
            
            vertex = graph.insertVertex(parent, null, label,
                0, 0, 100, 40, STYLE_EXPANDED);
            vertexMap.put(personName, vertex);
            
            if (parentVertex != null) {
                graph.insertEdge(parent, null, "", parentVertex, vertex,
                    "strokeColor=#666666");
            }

            // Create child vertices
            for (Node<Person> child : personNode.next) {
                buildGraphRecursive(child, vertex);
            }
        }
        
        return vertex;
    }

    private void toggleCollapse(mxCell cell) {
        graph.getModel().beginUpdate();
        try {
            boolean collapsed = !graph.isCellCollapsed(cell);
            graph.getModel().setCollapsed(cell, collapsed);
            
            // Get all connected edges and child vertices
            List<Object> descendants = new ArrayList<>();
            getDescendants(cell, descendants);
            
            // Toggle visibility
            for (Object descendant : descendants) {
                if (descendant instanceof mxCell) {
                    mxCell mxDescendant = (mxCell) descendant;
                    if (mxDescendant.isVertex()) {
                        graph.getModel().setVisible(mxDescendant, !collapsed);
                        // Also hide edges connected to this vertex
                        for (Object edge : graph.getEdges(mxDescendant)) {
                            mxCell edgeCell = (mxCell) edge;
                            mxCell source = (mxCell) edgeCell.getSource();
                            if (source == cell || !descendants.contains(source)) {
                                graph.getModel().setVisible(edge, !collapsed);
                            }
                        }
                    }
                }
            }
            
            // Update the cell style and label
            String label = cell.getValue().toString();
            label = label.replace(EXPAND_ICON, "").replace(COLLAPSE_ICON, "").trim();
            cell.setValue(label + " " + (collapsed ? EXPAND_ICON : COLLAPSE_ICON));
            cell.setStyle(collapsed ? STYLE_COLLAPSED : STYLE_EXPANDED);
            
            // Refresh layout
            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.setInterRankCellSpacing(50);
            layout.execute(parent);
            
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private void getDescendants(mxCell cell, List<Object> descendants) {
        Object[] outgoing = graph.getOutgoingEdges(cell);
        for (Object edge : outgoing) {
            mxCell target = (mxCell) ((mxCell) edge).getTarget();
            if (!descendants.contains(target)) {
                descendants.add(target);
                descendants.add(edge);
                getDescendants(target, descendants);
            }
        }
    }

    private void updateInfoPanel(String personName) {
        personName = personName.replace(EXPAND_ICON, "").replace(COLLAPSE_ICON, "").trim();
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
        
        // Position dialog near mouse
        Point mouseLocation = getMousePosition(true);
        if (mouseLocation != null) {
            infoDialog.setLocation(
                mouseLocation.x + 20,  // Offset from cursor
                mouseLocation.y + 20
            );
        }
        
        infoDialog.setVisible(true);
    }

    private void hideInfoPanel() {
        infoDialog.setVisible(false);
    }

    public void addRootNode(String personName) {
        if (!rootNodes.contains(personName) && lineageService.getNode(personName) != null) {
            rootNodes.add(personName);
            buildGraph();
        }
    }

    private void highlightPathToRoot(mxCell cell) {
        // Clear previous highlighting
        clearHighlights();
        
        graph.getModel().beginUpdate();
        try {
            mxCell current = cell;
            while (current != null && current.isVertex()) {
                // Highlight the current vertex
                String oldStyle = current.getStyle();
                current.setStyle(HIGHLIGHT_STYLE);
                currentlyHighlighted.add(current);
                
                // Find parent edge and vertex
                Object[] incomingEdges = graph.getIncomingEdges(current);
                if (incomingEdges.length > 0) {
                    mxCell edge = (mxCell) incomingEdges[0];
                    edge.setStyle(HIGHLIGHT_EDGE_STYLE);
                    currentlyHighlighted.add(edge);
                    current = (mxCell) edge.getSource();
                } else {
                    current = null;
                }
            }
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private void clearHighlights() {
        graph.getModel().beginUpdate();
        try {
            for (Object cell : currentlyHighlighted) {
                if (cell instanceof mxCell) {
                    mxCell mxCell = (mxCell) cell;
                    if (mxCell.isVertex()) {
                        boolean isCollapsed = graph.isCellCollapsed(mxCell);
                        mxCell.setStyle(isCollapsed ? STYLE_COLLAPSED : STYLE_EXPANDED);
                    } else {
                        mxCell.setStyle("strokeColor=#666666");
                    }
                }
            }
            currentlyHighlighted.clear();
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }
}