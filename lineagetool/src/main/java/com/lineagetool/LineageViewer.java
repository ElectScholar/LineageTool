package com.lineagetool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
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

    //key data structures
    private final Map<String, Object> vertexMap = new HashMap<>();
    private final List<Object> searchHighlighted = new ArrayList<>();
    private final List<Object> currentlyHighlighted = new ArrayList<>();

    // Toolbar buttons
    protected JButton zoomInButton, zoomOutButton, collapseAllButton, expandAllButton;
    protected JToggleButton searchModeButton;

    //variable to determine original node
    private String originalNode;

    public void setOriginalNode(String node) {
        this.originalNode = node;
    }
    public LineageViewer(LineageService lineageService, String original) {
        super(lineageService, new ArrayList<>(Collections.singletonList(original)));
        setOriginalNode(original);
        initializeComponents();
        setupEventHandlers();
        buildGraph();
    }

    @Override
    protected void initializeComponents() {
        customizeFrame();
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
        graphComponent.setGridVisible(false);
        // use dynamic colors:
        Color backgroundColor = new Color(175, 186, 183); // Use PALE_GREEN from palette
        graphComponent.getViewport().setBackground(backgroundColor);
    }

    private void createUIComponents() {
        createToolbar();
        createSearchPanel();
        createInfoDialog();
    }

    protected void customizeFrame() {
        setUndecorated(true);
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
        toolbarPanel.setBorder(new LineBorder(new Color(0x49, 0x62, 0x46), 3, true)); // Axolotl border
        toolbarPanel.setBackground(new Color(0x97, 0xb6, 0xb1)); // Summer Green background

        // Create buttons
        zoomInButton = createStyledButton("Zoom In", null);
        zoomOutButton = createStyledButton("Zoom Out", null);
        collapseAllButton = createStyledButton("Collapse All", null);
        expandAllButton = createStyledButton("Expand All", null);
        searchModeButton = new JToggleButton("Search Mode");
        styleToggleButton(searchModeButton);

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

    /**
     * Update the createStyledButton method to use the new color palette.
     */
    private JButton createStyledButton(String text, javax.swing.Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        
        // Use new palette colors
        Color textColor = new Color(230, 235, 233); // ALMOST_WHITE
        Color borderColor = new Color(113, 129, 123); // LIGHT_GREEN
        
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setContentAreaFilled(true);
                button.setBackground(new Color(65, 79, 73)); // MID_GREEN
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setContentAreaFilled(false);
            }
        });

        return button;
    }
    
    private void styleToggleButton(JToggleButton button) {

        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        
        // Use new palette colors
        Color textColor = new Color(230, 235, 233); // ALMOST_WHITE
        Color borderColor = new Color(113, 129, 123); // LIGHT_GREEN
        Color hoverColor = new Color(65, 79, 73); // MID_GREEN
        
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 1));

        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        button.setForeground(new Color(0x49, 0x62, 0x46)); // Axolotl text
        button.setBorder(BorderFactory.createLineBorder(new Color(0x49, 0x62, 0x46), 1)); // Axolotl border
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(0xac, 0xcf, 0xc5)); // Jet Stream hover
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(UIManager.getColor("Button.background"));
                }
            }
        });
        
        button.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                button.setBackground(new Color(0xac, 0xcf, 0xc5)); // Jet Stream selected
            } else {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        });
    }

    private void createInfoDialog() {
        infoDialog = new JDialog(this, false);
        infoDialog.setUndecorated(true);
        
        infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        infoPanel.setLineWrap(true);
        infoPanel.setWrapStyleWord(true);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x49, 0x62, 0x46), 2), // Axolotl border
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        infoPanel.setBackground(new Color(0xdf, 0xea, 0xe9)); // Mystic background
        infoPanel.setForeground(new Color(0x49, 0x62, 0x46)); // Axolotl text
        
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        infoDialog.add(scrollPane);
        infoDialog.pack();
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(0x97, 0xb6, 0xb1)); // Summer Green background
        
        searchField = new JTextField(20);
        searchField.setBackground(new Color(0xdf, 0xea, 0xe9)); // Mystic background
        searchField.setForeground(new Color(0x49, 0x62, 0x46)); // Axolotl text
        searchField.setBorder(BorderFactory.createLineBorder(new Color(0x49, 0x62, 0x46), 1)); // Axolotl border
        
        JButton clearButton = createStyledButton("Clear", null);
        
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

    private void searchAndFocusLineage(String searchTerm) {
    graph.getModel().beginUpdate();
    try {
        collapseExceptLineage(searchTerm);
        
        // Find and focus on the searched cell
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        for (Object vertex : vertices) {
            if (vertex instanceof mxCell) {
                mxCell cell = (mxCell) vertex;
                String cellValue = extractPersonName(cell).toLowerCase();
                if (cellValue.equals(searchTerm.toLowerCase())) {
                    // Get cell geometry
                    graphComponent.scrollCellToVisible(cell);
                    mxRectangle mxBounds = graphComponent.getGraph().getCellBounds(cell);
                    Rectangle bounds = new Rectangle((int) mxBounds.getX(), (int) mxBounds.getY(), (int) mxBounds.getWidth(), (int) mxBounds.getHeight());
                    if (bounds != null) {
                        // Center the viewport on the cell
                        Rectangle viewRect = graphComponent.getViewport().getViewRect();
                        int centerX = bounds.x + (bounds.width - viewRect.width) / 2;
                        int centerY = bounds.y + (bounds.height - viewRect.height) / 2;
                        graphComponent.getViewport().setViewPosition(new Point(centerX, centerY));
                    }
                    break;
                }
            }
        }
        
        graph.refresh();
    } finally {
        graph.getModel().endUpdate();
    }
}

    private void collapseExceptLineage(String searchTerm) {
        graph.getModel().beginUpdate();
        try {
            // First make everything invisible and collapsed
            Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
            for (Object vertex : vertices) {
                if (vertex instanceof mxCell) {
                    mxCell cell = (mxCell) vertex;
                    setCollapsed(cell, true);
                    graph.getModel().setVisible(cell, false);
                }
            }
    
            // Find the searched node
            mxCell searchedCell = null;
            for (Object vertex : vertices) {
                if (vertex instanceof mxCell) {
                    mxCell cell = (mxCell) vertex;
                    String cellValue = extractPersonName(cell).toLowerCase();
                    if (cellValue.equals(searchTerm.toLowerCase())) {
                        searchedCell = cell;
                        break;
                    }
                }
            }
    
            if (searchedCell != null) {
                // Make the searched node visible
                graph.getModel().setVisible(searchedCell, true);
                
                // Collect and show only path to root
                Set<mxCell> pathToRoot = new HashSet<>();
                collectPathToRoot(searchedCell, pathToRoot);
                
                // Make path to root visible and highlight edges
                for (mxCell node : pathToRoot) {
                    graph.getModel().setVisible(node, true);
                    // Make edges to ancestors visible
                    Object[] incomingEdges = graph.getIncomingEdges(node);
                    if (incomingEdges.length > 0) {
                        mxCell edge = (mxCell) incomingEdges[0];
                        graph.getModel().setVisible(edge, true);
                        edge.setStyle(LineageViewerStyles.EDGE_STYLE_HIGHLIGHT);
                    }
                }
    
                // Highlight the searched node
                searchedCell.setStyle(LineageViewerStyles.SEARCH_HIGHLIGHT_STYLE);
                searchHighlighted.add(searchedCell);
            }
    
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private void setupSearchHandler() {
        if (searchField != null) {
            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String searchTerm = searchField.getText().trim();
                        expandAllNodes();
                        if (!searchTerm.isEmpty()) {
                            clearSearchHighlights();
                            searchAndFocusLineage(searchTerm);
                        } else {
                            expandAllNodes();
                        }
                    }
                }
            });
        }
    }

    private void handleCellClick(mxCell cell, MouseEvent e) {
        if (e.getClickCount() == 2) {  // Double click to collapse/expand
            toggleCollapse(cell);
        }
        else if (e.getClickCount() == 1) {  // Single click for info
            String personName = extractPersonName(cell);
            updateInfoPanel(personName, e); // Pass the MouseEvent here
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
        layout.setInterRankCellSpacing(100);
        layout.setParallelEdgeSpacing(50);  // Add space between parallel edges
        layout.setIntraCellSpacing(10);     // Add space within ranks
        layout.execute(parent);
        graph.refresh();
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
                graph.insertEdge(parent, null, "", parentVertex, vertex, LineageViewerStyles.EDGE_STYLE_NORMAL);
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
                        mxCell.setStyle(LineageViewerStyles.EDGE_STYLE_NORMAL);
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
            //collapseAllNodes();
            
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
                    //&& graph.getOutgoingEdges(cell).length > 0
                        setCollapsed(cell, true);
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
            Object parent = graph.getDefaultParent();
            Object[] vertices = graph.getChildVertices(parent);
            for (Object vertex : vertices) {
                if (vertex instanceof mxCell) {
                    expandRecursively((mxCell) vertex);
                }
            }
            graph.refresh();
        } finally {
            graph.getModel().endUpdate();
        }
    }
    
    private void expandRecursively(mxCell cell) {
        // Expand the current cell if it's collapsed
        if (cell.isCollapsed()) {
            setCollapsed(cell, false);
        }
    
        // Get connected outgoing edges (assumes direction = parent -> child)
        Object[] edges = graph.getOutgoingEdges(cell);
        for (Object edge : edges) {
            mxCell target = (mxCell) graph.getModel().getTerminal(edge, false); // false = target
            if (target != null && target.isVertex()) {
                expandRecursively(target);
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

    private void updateInfoPanel(String personName, MouseEvent e) {
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
        
        // Get the point of the click relative to the graph component
        Point clickPointInComponent = e.getPoint();
        // Convert that point to screen coordinates
        SwingUtilities.convertPointToScreen(clickPointInComponent, graphComponent.getGraphControl());

        // Position dialog near the converted mouse location
        infoDialog.setLocation(
            clickPointInComponent.x + 20,  // Offset from cursor, adjusted for better placement
            clickPointInComponent.y + 20
        );
        
        infoDialog.setVisible(true);
    }

    private void hideInfoPanel() {
        infoDialog.setVisible(false);
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
            graph.getModel().setVisible(descendant, !collapsed);
        }
        
        graph.getModel().setCollapsed(cell, collapsed);
    }


    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    public JTextField getSearchField() {
        return searchField;
    }
}