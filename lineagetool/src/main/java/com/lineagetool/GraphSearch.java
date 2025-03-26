package com.lineagetool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class GraphSearch {
    private final mxGraph graph;
    private final Map<Integer, mxCell> idToCell;
    private final Set<Integer> visited;

    public GraphSearch(mxGraph graph) {
        this.graph = graph;
        this.idToCell = new HashMap<>();
        this.visited = new HashSet<>();
        mapCellIds();
    }

    private void mapCellIds() {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        for (Object vertex : vertices) {
            if (vertex instanceof mxCell) {
                mxCell cell = (mxCell) vertex;
                String label = cell.getValue().toString();
                // Extract ID from label format "Name#ID"
                int hashIndex = label.lastIndexOf('#');
                if (hashIndex != -1) {
                    try {
                        int id = Integer.parseInt(label.substring(hashIndex + 1));
                        idToCell.put(id, cell);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid ID format in cell: " + label);
                    }
                }
            }
        }
    }

    public List<mxCell> findPathBetweenIds(int startId, int endId) {
        visited.clear();
        mxCell startCell = idToCell.get(startId);
        mxCell endCell = idToCell.get(endId);
        
        if (startCell == null || endCell == null) {
            return Collections.emptyList();
        }

        List<mxCell> path = new ArrayList<>();
        if (dfs(startCell, endCell, path)) {
            return path;
        }
        return Collections.emptyList();
    }

    private boolean dfs(mxCell current, mxCell target, List<mxCell> path) {
        int currentId = extractId(current);
        if (visited.contains(currentId)) {
            return false;
        }

        visited.add(currentId);
        path.add(current);

        if (current == target) {
            return true;
        }

        // Search through outgoing edges
        Object[] outgoing = graph.getOutgoingEdges(current);
        for (Object edge : outgoing) {
            mxCell targetCell = (mxCell) ((mxCell) edge).getTarget();
            if (dfs(targetCell, target, path)) {
                return true;
            }
        }

        // Search through incoming edges
        Object[] incoming = graph.getIncomingEdges(current);
        for (Object edge : incoming) {
            mxCell sourceCell = (mxCell) ((mxCell) edge).getSource();
            if (dfs(sourceCell, target, path)) {
                return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    private int extractId(mxCell cell) {
        String label = cell.getValue().toString();
        int hashIndex = label.lastIndexOf('#');
        if (hashIndex != -1) {
            try {
                return Integer.parseInt(label.substring(hashIndex + 1));
            } catch (NumberFormatException e) {
                System.err.println("Invalid ID format in cell: " + label);
            }
        }
        return -1;
    }

    // Utility method to find a cell by name
    public mxCell findCellByName(String name) {
        for (mxCell cell : idToCell.values()) {
            String label = cell.getValue().toString();
            if (label.startsWith(name + "#")) {
                return cell;
            }
        }
        return null;
    }

    // Get all cells with matching name (handles duplicates)
    public List<mxCell> findAllCellsByName(String name) {
        List<mxCell> matches = new ArrayList<>();
        for (mxCell cell : idToCell.values()) {
            String label = cell.getValue().toString();
            if (label.startsWith(name + "#")) {
                matches.add(cell);
            }
        }
        return matches;
    }
}