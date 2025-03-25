package com.lineagetool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class LineageViewer extends AbstractLineageViewer implements GraphOperations {
    private mxGraph graph;
    private Object parent;
    private mxGraphComponent graphComponent;
    private JDialog infoDialog;
    private JTextArea infoPanel;
    private JTextField searchField;

    private Map<String, Object> vertexMap = new HashMap<>();
    private List<Object> searchHighlighted = new ArrayList<>();
    private List<Object> currentlyHighlighted = new ArrayList<>();

    public LineageViewer(LineageService lineageService) {
        super(lineageService, new ArrayList<>(Collections.singletonList("Isaac")));
        initializeComponents();
        setupEventHandlers();
        buildGraph();
        collapseAllNodes();
    }

    @Override
    protected void initializeComponents() {
        // Graph initialization
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        graphComponent = new mxGraphComponent(graph);
        
        configureGraph();
        createInfoDialog();
        
        // Add graph component to frame
        add(graphComponent, BorderLayout.CENTER);
        
        // Add search panel
        add(createSearchPanel(), BorderLayout.NORTH);
    }

    private void configureGraph() {
        graph.setCellsMovable(true);
        graph.setCellsResizable(false);
        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setAutoSizeCells(true);
        graphComponent.setTripleBuffered(true);
    }

    private void createInfoDialog() {
        infoDialog = new JDialog(this, false);
        infoDialog.setUndecorated(true);
        
        infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        infoPanel.setLineWrap(true);
        infoPanel.setWrapStyleWord(true);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        infoDialog.add(scrollPane);
        infoDialog.pack();
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton clearButton = new JButton("Clear");
        
        clearButton.addActionListener(e -> {
            searchField.setText("");
            expandAllNodes();
            clearSearchHighlights();
        });
        
        searchPanel.add(searchField);
        searchPanel.add(clearButton);
        return searchPanel;
    }

    @Override
    protected void setupEventHandlers() {
        // Mouse listener for cell interactions
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell instanceof mxCell) {
                    mxCell clickedCell = (mxCell) cell;
                    handleCellClick(clickedCell, e);
                } else {
                    hideInfoPanel();
                }
            }
        });

        // Scroll and zoom control
        graphComponent.getGraphControl().addMouseWheelListener(this::handleMouseWheel);

        // Search on Enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchNodes(searchField.getText());
                }
            }
        });
    }

    private void handleCellClick(mxCell cell, MouseEvent e) {
        if (e.getClickCount() == 2) {  // Double click to collapse/expand
            toggleCollapse(cell);
        } else if (e.getClickCount() == 1) {  // Single click for info
            String personName = extractPersonName(cell);
            updateInfoPanel(personName);
            highlightPathToRoot(cell);
        }
    }

    private String extractPersonName(mxCell cell) {
        return cell.getValue().toString()
            .replace(LineageViewerStyles.EXPAND_ICON, "")
            .replace(LineageViewerStyles.COLLAPSE_ICON, "")
            .trim();
    }

    private void handleMouseWheel(MouseWheelEvent e) {
        if (e.isControlDown()) {
            // Zoom when Ctrl is pressed
            if (e.getWheelRotation() < 0) {
                graphComponent.zoomIn();
            } else {
                graphComponent.zoomOut();
            }
        } else {
            // Enhanced scrolling without Ctrl
            int scrollAmount = (int)(e.getUnitsToScroll() * LineageViewerStyles.SCROLL_SPEED);
            if (e.isShiftDown()) {
                // Horizontal scroll when Shift is pressed
                graphComponent.getHorizontalScrollBar().setValue(
                    graphComponent.getHorizontalScrollBar().getValue() + scrollAmount);
            } else {
                // Vertical scroll
                graphComponent.getVerticalScrollBar().setValue(
                    graphComponent.getVerticalScrollBar().getValue() + scrollAmount);
            }
        }
        e.consume();
    }

    @Override
    protected void buildGraph() {
        graph.getModel().beginUpdate();
        try {
            // Clear existing graph
            graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
            vertexMap.clear();
            
            // Build trees for each root node
            for (String rootName : rootNodes) {
                Node<Person> rootPerson = lineageService.getNode(rootName);
                if (rootPerson != null) {
                    buildGraphRecursive(rootPerson, null);
                }
            }
            
            applyHierarchicalLayout();
            
            // Collapse non-root nodes
            collapseNonRootNodes();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private void applyHierarchicalLayout() {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setInterRankCellSpacing(50);
        layout.execute(parent);
    }

    private void collapseNonRootNodes() {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        for (Object vertex : vertices) {
            if (vertex instanceof mxCell) {
                mxCell cell = (mxCell) vertex;
                if (!isRootCell(cell) && graph.getOutgoingEdges(cell).length > 0) {
                    setCollapsed(cell, true);
                }
            }
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
                    graph.insertEdge(parent, null, "", parentVertex, vertex, "strokeColor=#666666");
                }
            }
        } else {
            String label = createVertexLabel(personName, personNode);
            
            vertex = graph.insertVertex(parent, null, label, 0, 0, 100, 40, LineageViewerStyles.STYLE_EXPANDED);
            vertexMap.put(personName, vertex);
            
            if (parentVertex != null) {
                graph.insertEdge(parent, null, "", parentVertex, vertex, "strokeColor=#666666");
            }
    
            for (Node<Person> child : personNode.next) {
                buildGraphRecursive(child, vertex);
            }
        }
        
        return vertex;
    }

    private String createVertexLabel(String personName, Node<Person> personNode) {
        return personName + (personNode.next.isEmpty() ? "" : LineageViewerStyles.COLLAPSE_ICON);
    }

    @Override
    public void toggleCollapse(mxCell cell) {
        boolean collapsed = !graph.isCellCollapsed(cell);
        setCollapsed(cell, collapsed);
    }

    @Override
    public void getDescendants(mxCell cell, List<Object> descendants) {
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

    @Override
    public void highlightPathToRoot(mxCell cell) {
        clearHighlights();
        
        graph.getModel().beginUpdate();
        try {
            mxCell current = cell;
            while (current != null && current.isVertex()) {
                current.setStyle(LineageViewerStyles.HIGHLIGHT_STYLE);
                currentlyHighlighted.add(current);
                
                Object[] incomingEdges = graph.getIncomingEdges(current);
                if (incomingEdges.length > 0) {
                    mxCell edge = (mxCell) incomingEdges[0];
                    edge.setStyle(LineageViewerStyles.HIGHLIGHT_EDGE_STYLE);
                    currentlyHighlighted.add(edge);
                    current = (mxCell) edge.getSource();
                } else {
                    break;
                }
            }
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    @Override
    public void clearHighlights() {
        graph.getModel().beginUpdate();
        try {
            for (Object cell : currentlyHighlighted) {
                if (cell instanceof mxCell) {
                    mxCell mxCell = (mxCell) cell;
                    if (mxCell.isVertex()) {
                        // Don't clear if cell is search highlighted
                        if (!searchHighlighted.contains(mxCell)) {
                            boolean isCollapsed = graph.isCellCollapsed(mxCell);
                            mxCell.setStyle(isCollapsed ? LineageViewerStyles.STYLE_COLLAPSED : LineageViewerStyles.STYLE_EXPANDED);
                        }
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

    @Override
    public void searchNodes(String searchTerm) {
        clearSearchHighlights();
        searchTerm = searchTerm.toLowerCase().trim();
        
        if (searchTerm.isEmpty()) {
            expandAllNodes();
            return;
        }

        graph.getModel().beginUpdate();
        try {
            // First, collapse all nodes
            collapseAllNodes();
            
            // Find matching nodes
            Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
            Set<mxCell> pathNodes = new HashSet<>();
            
            for (Object vertex : vertices) {
                if (vertex instanceof mxCell) {
                    mxCell cell = (mxCell) vertex;
                    String cellValue = extractPersonName(cell).toLowerCase();
                    
                    if (cellValue.contains(searchTerm)) {
                        // Add path to root for this node
                        collectPathToRoot(cell, pathNodes);
                        cell.setStyle(LineageViewerStyles.SEARCH_HIGHLIGHT_STYLE);
                        searchHighlighted.add(cell);
                    }
                }
            }
            
            // Expand only the nodes in the path
            pathNodes.forEach(node -> graph.getModel().setCollapsed(node, false));
            
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    @Override
    public void collectPathToRoot(mxCell cell, Set<mxCell> pathNodes) {
        mxCell current = cell;
        while (current != null && current.isVertex()) {
            pathNodes.add(current);
            Object[] incomingEdges = graph.getIncomingEdges(current);
            if (incomingEdges.length > 0) {
                current = (mxCell) ((mxCell) incomingEdges[0]).getSource();
            } else {
                break;
            }
        }
    }

    @Override
    public void collapseAllNodes() {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        for (Object vertex : vertices) {
            if (vertex instanceof mxCell) {
                mxCell cell = (mxCell) vertex;
                if (graph.getOutgoingEdges(cell).length > 0) {
                    graph.getModel().setCollapsed(cell, true);
                }
            }
        }
    }

    @Override
    public void expandAllNodes() {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        for (Object vertex : vertices) {
            if (vertex instanceof mxCell) {
                mxCell cell = (mxCell) vertex;
                if (graph.getOutgoingEdges(cell).length > 0) {
                    graph.getModel().setCollapsed(cell, false);
                }
            }
        }
    }

    @Override
    public void clearSearchHighlights() {
        graph.getModel().beginUpdate();
        try {
            for (Object cell : searchHighlighted) {
                if (cell instanceof mxCell) {
                    mxCell mxCell = (mxCell) cell;
                    if (mxCell.isVertex()) {
                        boolean isCollapsed = graph.isCellCollapsed(mxCell);
                        mxCell.setStyle(isCollapsed ? LineageViewerStyles.STYLE_COLLAPSED : LineageViewerStyles.STYLE_EXPANDED);
                    }
                }
            }
            searchHighlighted.clear();
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private void updateInfoPanel(String personName) {
        if (personName == null || personName.isEmpty()) {
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

    private boolean isRootCell(mxCell cell) {
        String cellValue = extractPersonName(cell);
        return rootNodes.contains(cellValue);
    }

    private void setCollapsed(mxCell cell, boolean collapsed) {
        String cellValue = extractPersonName(cell);
        cell.setValue(cellValue + (collapsed ? 
            LineageViewerStyles.EXPAND_ICON : 
            LineageViewerStyles.COLLAPSE_ICON));
        cell.setStyle(collapsed ? 
            LineageViewerStyles.STYLE_COLLAPSED : 
            LineageViewerStyles.STYLE_EXPANDED);
        
        // Hide/show child cells and their edges
        Object[] childCells = graph.getChildCells(cell, true, true);
        for (Object childCell : childCells) {
            graph.getModel().setVisible(childCell, !collapsed);
        }
        
        Object[] edges = graph.getEdges(cell);
        for (Object edge : edges) {
            mxCell edgeCell = (mxCell) edge;
            if (edgeCell.getTarget().getParent() != cell.getParent()) {
                graph.getModel().setVisible(edge, !collapsed);
            }
        }
        
        graph.getModel().setCollapsed(cell, collapsed);
    }
}