package com.lineagetool;

import com.mxgraph.model.mxCell;
import java.util.List;
import java.util.Set;

public interface GraphOperations {
    void toggleCollapse(mxCell cell);
    void highlightPathToRoot(mxCell cell);
    void clearHighlights();
    void searchNodes(String searchTerm);
    void collapseAllNodes();
    void expandAllNodes();
    void clearSearchHighlights();
    void collectPathToRoot(mxCell cell, Set<mxCell> pathNodes);
    void getDescendants(mxCell cell, List<Object> descendants);
}
