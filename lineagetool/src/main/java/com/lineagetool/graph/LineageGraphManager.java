package com.lineagetool.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingConstants;

import com.lineagetool.LineageService;
import com.lineagetool.LineageViewerStyles;
import com.lineagetool.Node;
import com.lineagetool.Person;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public class LineageGraphManager {
    private final mxGraph graph;
    private final Object parent;
    private final Map<String, Object> vertexMap;
    private final List<Object> searchHighlighted;
    private final List<Object> currentlyHighlighted;
    private final LineageService lineageService;
    private final mxGraphComponent graphComponent;
    private boolean useAutoLayout;

    public LineageGraphManager(LineageService lineageService) {
        this.lineageService = lineageService;
        this.graph = new mxGraph();
        this.parent = graph.getDefaultParent();
        this.vertexMap = new HashMap<>();
        this.searchHighlighted = new ArrayList<>();
        this.currentlyHighlighted = new ArrayList<>();
        this.graphComponent = new mxGraphComponent(graph);
        this.useAutoLayout = true;
        configureGraphSettings();
    }

    private void applyLayout() {
        if (!useAutoLayout) {
            graph.refresh();
            return;
        }

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setInterRankCellSpacing(50);
        layout.setIntraCellSpacing(30);
        layout.setParallelEdgeSpacing(10);
        layout.setOrientation(SwingConstants.NORTH);
        layout.execute(parent);
        graph.refresh();
    }

    private void configureGraphSettings() {
        // Graph behavior settings
        graph.setCellsMovable(true);
        graph.setCellsResizable(false);
        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setAutoSizeCells(true);
        graph.setEdgeLabelsMovable(false);
        graph.setCellsBendable(true);
        graph.setConnectableEdges(false);
        graph.setDisconnectOnMove(false);

        // Edge styling
        Map<String, Object> edgeStyle = new HashMap<>();
        edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
        edgeStyle.put(mxConstants.STYLE_ROUNDED, true);
        // Removed invalid STYLE_ORTHOGONAL_LOOP as it is not a valid field in mxConstants
        edgeStyle.put(mxConstants.STYLE_ROUTING_CENTER_X, true);
        edgeStyle.put(mxConstants.STYLE_ROUTING_CENTER_Y, true);
        graph.getStylesheet().putCellStyle("defaultEdgeStyle", edgeStyle);

        // Component settings
        graphComponent.setTripleBuffered(true);
        graphComponent.setAntiAlias(true);
        graphComponent.setGridVisible(true);
        graphComponent.setGridColor(new Color(240, 240, 240));
    }

    public void buildGraph() {
        graph.getModel().beginUpdate();
        try {
            buildGraphContent();
            applyLayout();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    private void buildGraphContent() {
        graph.removeCells(graph.getChildVertices(parent));
        vertexMap.clear();
        
        for (Node<Person> rootPerson : lineageService.getHeads()) {
            buildGraphRecursive(rootPerson, null);
        }
    }

    private Object buildGraphRecursive(Node<Person> personNode, Object parentVertex) {
        String personName = personNode.val.getName();
        Object vertex = vertexMap.get(personName);

        if (vertex == null) {
            vertex = graph.insertVertex(parent, null, 
                personName + LineageViewerStyles.COLLAPSE_ICON,
                0, 0, 120, 40, LineageViewerStyles.STYLE_EXPANDED);
            vertexMap.put(personName, vertex);

            if (parentVertex != null) {
                graph.insertEdge(parent, null, "", parentVertex, vertex,
                    "defaultEdgeStyle");
            }

            for (Node<Person> child : personNode.next) {
                buildGraphRecursive(child, vertex);
            }
        }

        return vertex;
    }
// Add these methods to LineageGraphManager class
public void collapseAllNodes() {
    graph.getModel().beginUpdate();
    try {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        for (Object vertex : vertices) {
            if (vertex instanceof mxCell) {
                mxCell cell = (mxCell) vertex;
                if (graph.getOutgoingEdges(cell).length > 0) {
                    setCollapsed(cell, true);
                }
            }
        }
    } finally {
        graph.getModel().endUpdate();
    }
}

public void expandAllNodes() {
    graph.getModel().beginUpdate();
    try {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        for (Object vertex : vertices) {
            if (vertex instanceof mxCell) {
                mxCell cell = (mxCell) vertex;
                setCollapsed(cell, false);
            }
        }
    } finally {
        graph.getModel().endUpdate();
    }
}

    public void expandNode(mxCell cell) {
        if (cell != null && graph.isCellCollapsed(cell)) {
            setCollapsed(cell, false);
        }
    }

    public void collapseNode(mxCell cell) {
        if (cell != null && !graph.isCellCollapsed(cell)) {
            setCollapsed(cell, true);
        }
    }
    public void setCollapsed(mxCell cell, boolean collapsed) {
        if (cell == null) return;
        
        graph.getModel().beginUpdate();
        try {
            String cellValue = extractPersonName(cell);
            cell.setValue(cellValue + (collapsed ? 
                LineageViewerStyles.EXPAND_ICON : 
                LineageViewerStyles.COLLAPSE_ICON));
            cell.setStyle(collapsed ? 
                LineageViewerStyles.STYLE_COLLAPSED : 
                LineageViewerStyles.STYLE_EXPANDED);
            
            List<Object> descendants = new ArrayList<>();
            getDescendants(cell, descendants);
            
            for (Object descendant : descendants) {
                graph.getModel().setVisible(descendant, !collapsed);
            }
            
            graph.getModel().setCollapsed(cell, collapsed);
            executeLayoutWithAnimation();
        } finally {
            graph.getModel().endUpdate();
        }
    }

    public void executeLayoutWithAnimation() {
        if (!useAutoLayout) {
            graph.refresh();
            return;
        }

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setInterRankCellSpacing(50);
        layout.setIntraCellSpacing(30);
        layout.setParallelEdgeSpacing(10);
        layout.setOrientation(SwingConstants.NORTH);
        
        List<Object> visibleElements = new ArrayList<>();
        Object[] vertices = graph.getChildVertices(parent);
        
        for (Object vertex : vertices) {
            if (graph.getModel().isVisible(vertex)) {
                visibleElements.add(vertex);
                for (Object edge : graph.getEdges(vertex)) {
                    if (graph.getModel().isVisible(edge)) {
                        visibleElements.add(edge);
                    }
                }
            }
        }
        
        if (!visibleElements.isEmpty()) {
            layout.execute(parent);
        }
        
        graph.refresh();
    }

    public void getDescendants(mxCell cell, List<Object> descendants) {
        Object[] outgoing = graph.getOutgoingEdges(cell);
        for (Object edge : outgoing) {
            mxCell edgeCell = (mxCell) edge;
            mxCell target = (mxCell) edgeCell.getTarget();
            
            descendants.add(edge);
            descendants.add(target);
            
            getDescendants(target, descendants);
        }
    }

    public String extractPersonName(mxCell cell) {
        String value = cell.getValue().toString();
        int iconIndex = value.indexOf(LineageViewerStyles.COLLAPSE_ICON);
        if (iconIndex == -1) {
            iconIndex = value.indexOf(LineageViewerStyles.EXPAND_ICON);
        }
        return iconIndex == -1 ? value : value.substring(0, iconIndex);
    }

    // Public accessors
    public mxGraphComponent getGraphComponent() { return graphComponent; }
    public mxGraph getGraph() { return graph; }
    public void setAutoLayout(boolean enable) { useAutoLayout = enable; }
    public boolean isAutoLayout() { return useAutoLayout; }
}