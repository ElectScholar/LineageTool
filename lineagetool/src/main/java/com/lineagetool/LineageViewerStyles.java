package com.lineagetool;

import java.awt.Color;

public final class LineageViewerStyles {
    // Updated Colors from the image palette
    private static Color DARK_GREEN = new Color(33, 43, 39);   // Darkest circle
    private static Color MID_GREEN = new Color(65, 79, 73);    // Second circle
    private static Color LIGHT_GREEN = new Color(113, 129, 123); // Third circle
    private static Color PALE_GREEN = new Color(175, 186, 183); // Fourth circle
    private static Color ALMOST_WHITE = new Color(230, 235, 233); // Lightest circle

    // Background gradient effect colors
    private static Color BACKGROUND_START = new Color(60, 70, 65, 180);
    private static Color BACKGROUND_END = new Color(180, 190, 185, 150);

    // Vertex Styles - updated with new palette
    public static String STYLE_EXPANDED = "rounded=1;strokeColor=" + colorToHex(MID_GREEN) + 
        ";fillColor=" + colorToHex(PALE_GREEN) + ";fontColor=" + colorToHex(DARK_GREEN);
    
    public static String STYLE_COLLAPSED = "rounded=1;strokeColor=" + colorToHex(MID_GREEN) + 
        ";fillColor=" + colorToHex(LIGHT_GREEN) + ";fontColor=" + colorToHex(DARK_GREEN);
    
    public static String SEARCH_HIGHLIGHT_STYLE = "rounded=1;strokeColor=" + colorToHex(DARK_GREEN) + 
        ";strokeWidth=2;fillColor=" + colorToHex(ALMOST_WHITE) + ";fontColor=" + colorToHex(DARK_GREEN);
    
    public static String HIGHLIGHT_STYLE = "rounded=1;strokeColor=" + colorToHex(MID_GREEN.darker()) + 
        ";strokeWidth=3;fillColor=" + colorToHex(ALMOST_WHITE) + ";fontColor=" + colorToHex(DARK_GREEN);
    
    // Edge Styles
    public static String EDGE_STYLE_NORMAL = "edgeStyle=orthogonalEdgeStyle;rounded=1;orthogonalLoop=1;" +
        "strokeWidth=2;strokeColor=" + colorToHex(MID_GREEN);
    
    public static String EDGE_STYLE_HIGHLIGHT = "edgeStyle=orthogonalEdgeStyle;rounded=1;orthogonalLoop=1;" +
        "strokeWidth=3;strokeColor=" + colorToHex(DARK_GREEN);

    // Icons
    public static final String EXPAND_ICON = "+";
    public static final String COLLAPSE_ICON = "-";

    // Scroll and Zoom Settings
    public static final double SCROLL_SPEED = 5.0;
    public static final double ZOOM_FACTOR = 1.2;

    /**
     * Updates the color palette used throughout the application.
     * 
     * @param darkGreen darkest color in the palette
     * @param midGreen medium dark color
     * @param lightGreen medium light color
     * @param paleGreen light color
     * @param almostWhite lightest color
     */
    public static void updateColorPalette(Color darkGreen, Color midGreen, Color lightGreen, Color paleGreen, Color almostWhite) {
        DARK_GREEN = darkGreen;
        MID_GREEN = midGreen;
        LIGHT_GREEN = lightGreen;
        PALE_GREEN = paleGreen;
        ALMOST_WHITE = almostWhite;
        
        // Update styles with new colors
        STYLE_EXPANDED = "rounded=1;strokeColor=" + colorToHex(MID_GREEN) + 
            ";fillColor=" + colorToHex(PALE_GREEN) + ";fontColor=" + colorToHex(DARK_GREEN);
        
        STYLE_COLLAPSED = "rounded=1;strokeColor=" + colorToHex(MID_GREEN) + 
            ";fillColor=" + colorToHex(LIGHT_GREEN) + ";fontColor=" + colorToHex(DARK_GREEN);
        
        SEARCH_HIGHLIGHT_STYLE = "rounded=1;strokeColor=" + colorToHex(DARK_GREEN) + 
            ";strokeWidth=2;fillColor=" + colorToHex(ALMOST_WHITE) + ";fontColor=" + colorToHex(DARK_GREEN);
        
        HIGHLIGHT_STYLE = "rounded=1;strokeColor=" + colorToHex(MID_GREEN.darker()) + 
            ";strokeWidth=3;fillColor=" + colorToHex(ALMOST_WHITE) + ";fontColor=" + colorToHex(DARK_GREEN);
            
        EDGE_STYLE_NORMAL = "edgeStyle=orthogonalEdgeStyle;rounded=1;orthogonalLoop=1;" +
            "strokeWidth=2;strokeColor=" + colorToHex(MID_GREEN);
        
        EDGE_STYLE_HIGHLIGHT = "edgeStyle=orthogonalEdgeStyle;rounded=1;orthogonalLoop=1;" +
            "strokeWidth=3;strokeColor=" + colorToHex(DARK_GREEN);
    }

    /**
     * Converts a Color to its hex representation for use in styles.
     * 
     * @param color the color to convert
     * @return hex string representation
     */
    private static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    // Prevent instantiation
    private LineageViewerStyles() {
        throw new AssertionError("Cannot be instantiated");
    }
}