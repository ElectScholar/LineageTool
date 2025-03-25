package com.lineagetool;

public final class LineageViewerStyles {
    // Vertex Styles
    public static final String STYLE_EXPANDED = "rounded=1;strokeColor=#666666;fillColor=#f5f5f5";
    public static final String STYLE_COLLAPSED = "rounded=1;strokeColor=#666666;fillColor=#e0e0e0";
    public static final String SEARCH_HIGHLIGHT_STYLE = 
        "rounded=1;strokeColor=#0000FF;strokeWidth=2;fillColor=#E6E6FF";
    public static final String HIGHLIGHT_STYLE = "rounded=1;strokeColor=#FF0000;strokeWidth=3;fillColor=#f5f5f5";
    public static final String HIGHLIGHT_EDGE_STYLE = "strokeColor=#FF0000;strokeWidth=2";

    // Icons
    public static final String EXPAND_ICON = "+";
    public static final String COLLAPSE_ICON = "-";

    // Scroll and Zoom Settings
    public static final double SCROLL_SPEED = 5.0;
    public static final double ZOOM_FACTOR = 1.2;

    // Prevent instantiation
    private LineageViewerStyles() {
        throw new AssertionError("Cannot be instantiated");
    }
}
