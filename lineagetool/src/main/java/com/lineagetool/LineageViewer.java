package com.lineagetool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ItemEvent;
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
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class LineageViewer extends AbstractLineageViewer implements GraphOperations {
    // Graph components
    private mxGraph graph;
    private Object parent;
    protected mxGraphComponent graphComponent;

    // UI components
    private JDialog infoDialog;
    private JTextArea infoPanel;
    private JTextField searchField;
    private JPanel toolbarPanel;
    private final Map<String, Object> vertexMap = new HashMap<>();
    private final List<Object> searchHighlighted = new ArrayList<>();
    private final List<Object> currentlyHighlighted = new ArrayList<>();

    // Toolbar buttons
    private JButton zoomInButton, zoomOutButton, collapseAllButton, expandAllButton;
    private JToggleButton searchModeButton;

    public LineageViewer(LineageService lineageService) {
        super(lineageService, new ArrayList<>(Collections.singletonList("Isaac")));
        initializeComponents();
        setupEventHandlers();
        buildGraph();
    }

    @Override
    protected void initializeComponents() {
        initializeGraph();
        createUIComponents();
        layoutComponents();
    }

    private void initializeGraph() {
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        graphComponent = new mxGraphComponent(graph);
        configureGraphSettings();
    }

    private void configureGraphSettings() {
        graph.setCellsMovable(true);
        graph.setCellsResizable(false);
        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setAutoSizeCells(true);
        graph.setEdgeLabelsMovable(false);
        graph.setCellsBendable(true);
        graph.setConnectableEdges(false);
        graph.setDisconnectOnMove(false);
        graphComponent.setTripleBuffered(true);
        graphComponent.setAntiAlias(true);
        graphComponent.setGridVisible(true);
        graphComponent.setGridColor(new Color(240, 240, 240));
    }

    private void createUIComponents() {
        createToolbar();
        createSearchPanel();
        createInfoDialog();
        customizeFrame();
    }

    private void customizeFrame() {
        setTitle("Lineage Viewer");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.NORTH);
        add(graphComponent, BorderLayout.CENTER);
        add(createSearchPanel(), BorderLayout.SOUTH);
    }

    private void createToolbar() {
        toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        toolbarPanel.setBackground(new Color(240, 240, 240));

        // Create buttons
        zoomInButton = createStyledButton("Zoom In", null);
        zoomOutButton = createStyledButton("Zoom Out", null);
        collapseAllButton = createStyledButton("Collapse All", null);
        expandAllButton = createStyledButton("Expand All", null);
        searchModeButton = new JToggleButton("Search Mode");

        // Add button actions
        zoomInButton.addActionListener(e -> graphComponent.zoomIn());
        zoomOutButton.addActionListener(e -> graphComponent.zoomOut());
        collapseAllButton.addActionListener(e -> collapseAllNodes());
        expandAllButton.addActionListener(e -> expandAllNodes());

        searchModeButton.setFocusPainted(false);
        searchModeButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                searchField.requestFocusInWindow();
            }
        });

        // Add buttons to toolbar
        toolbarPanel.add(zoomInButton);
        toolbarPanel.add(zoomOutButton);
        toolbarPanel.add(collapseAllButton);
        toolbarPanel.add(expandAllButton);
        toolbarPanel.add(searchModeButton);
    }

    private JButton createStyledButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setContentAreaFilled(true);
                button.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setContentAreaFilled(false);
            }
        });

        return button;
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

    // Event handling methods
    @Override
    protected void setupEventHandlers() {
        if (graphComponent == null) return;
        setupMouseHandlers();
        setupSearchHandler();
    }

    private void setupMouseHandlers() {
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(e);
            }
        });
        graphComponent.getGraphControl().addMouseWheelListener(this::handleMouseWheel);
    }

    private void handleMouseRelease(MouseEvent e) {
        Object cell = graphComponent.getCellAt(e.getX(), e.getY());
        if (cell instanceof mxCell) {
            handleCellClick((mxCell)cell, e);
        } else {
            hideInfoPanel();
        }
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

    private void setupSearchHandler() {
        if (searchField != null) {
            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        searchNodes(searchField.getText());
                    }
                }
            });
        }
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



    @Override
    protected void buildGraph() {
        graph.getModel().beginUpdate();
        try {
            buildGraphContent();
            applyLayout();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private void buildGraphContent() {
        graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
        vertexMap.clear();
        for (String rootName : rootNodes) {
            Node<Person> rootPerson = lineageService.getNode(rootName);
            if (rootPerson != null) {
                buildGraphRecursive(rootPerson, null);
            }
        }
    }

    private void applyLayout() {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setInterRankCellSpacing(50);
        layout.execute(parent);
        graph.refresh();
    }

    private void applyHierarchicalLayout() {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setInterRankCellSpacing(50);
        layout.setParallelEdgeSpacing(50);  // Add space between parallel edges
        layout.setIntraCellSpacing(50);     // Add space within ranks
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
                    graph.insertEdge(parent, null, "", parentVertex, vertex, 
                        LineageViewerStyles.EDGE_STYLE_NORMAL);
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
                    edge.setStyle(LineageViewerStyles.EDGE_STYLE_HIGHLIGHT);
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
        graph.getModel().beginUpdate();
        try {
            // First pass - collapse all non-root nodes with children
            Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
            for (Object vertex : vertices) {
                if (vertex instanceof mxCell) {
                    mxCell cell = (mxCell) vertex;
                    String cellName = extractPersonName(cell);
                    // Explicitly check for Jacob and Esau
                    if (!isRootCell(cell) && graph.getOutgoingEdges(cell).length > 0) {
                        setCollapsed(cell, true);
                    }
                }
            }
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    @Override
    public void expandAllNodes() {
        graph.getModel().beginUpdate();
        try {
            Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
            for (Object vertex : vertices) {
                if (vertex instanceof mxCell) {
                    mxCell cell = (mxCell) vertex;
                    if (graph.isCellCollapsed(cell)) {
                        // Get all descendants
                        List<Object> descendants = new ArrayList<>();
                        getDescendants(cell, descendants);
                        
                        // Set visibility and style
                        for (Object descendant : descendants) {
                            graph.getModel().setVisible(descendant, true);
                        }
                        
                        // Update cell style and label
                        String cellValue = extractPersonName(cell);
                        cell.setValue(cellValue + LineageViewerStyles.COLLAPSE_ICON);
                        cell.setStyle(LineageViewerStyles.STYLE_EXPANDED);
                        graph.getModel().setCollapsed(cell, false);
                    }
                }
            }
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
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
                mouseLocation.x + 200,  // Offset from cursor
                mouseLocation.y + 200
            );
        }
        
        infoDialog.setVisible(true);
    }

    private void hideInfoPanel() {
        infoDialog.setVisible(false);
    }

    private boolean isRootCell(mxCell cell) {
        String cellValue = extractPersonName(cell);
        // Check if this node is in the rootNodes list from LineageService
        return lineageService.getRootNodes().contains(cellValue);
    }

    private void setCollapsed(mxCell cell, boolean collapsed) {
        // Don't return early for root nodes - we need to process their children
        String cellValue = extractPersonName(cell);
        
        // Update cell style and label
        cell.setValue(cellValue + (collapsed ? 
            LineageViewerStyles.EXPAND_ICON : 
            LineageViewerStyles.COLLAPSE_ICON));
        cell.setStyle(collapsed ? 
            LineageViewerStyles.STYLE_COLLAPSED : 
            LineageViewerStyles.STYLE_EXPANDED);
        
        // Get all descendants
        List<Object> descendants = new ArrayList<>();
        getDescendants(cell, descendants);
        
        // Set visibility for descendants
        for (Object descendant : descendants) {
            if (descendant instanceof mxCell) {
                mxCell descendantCell = (mxCell) descendant;
                // Only hide if the descendant isn't a direct child of a root
                if (!isDirectChildOfRoot(descendantCell)) {
                    graph.getModel().setVisible(descendant, !collapsed);
                }
            }
        }
        
        graph.getModel().setCollapsed(cell, collapsed);
    }

    private boolean isDirectChildOfRoot(mxCell cell) {
        Object[] incomingEdges = graph.getIncomingEdges(cell);
        if (incomingEdges.length > 0) {
            mxCell parentCell = (mxCell) ((mxCell) incomingEdges[0]).getSource();
            return isRootCell(parentCell);
        }
        return false;
    }

    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    public JTextField getSearchField() {
        return searchField;
    }
}