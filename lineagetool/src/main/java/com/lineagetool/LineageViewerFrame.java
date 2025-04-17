package com.lineagetool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * LineageViewerFrame integrates the CustomFrame with LineageViewer
 * to create a cohesive application with custom styling.
 */
public class LineageViewerFrame extends CustomFrame {
    private LineageViewer lineageViewer;
    
    /**
     * Creates a new LineageViewerFrame with the specified LineageService.
     * 
     * @param lineageService the service providing lineage data
     */
    public LineageViewerFrame(LineageService lineageService, String originalNode) {
        super("Lineage Viewer", 1200, 800);
        
        // Apply the new color palette from the image
        initializeWithNewPalette();
        
        // Create the lineage viewer component
        lineageViewer = new LineageViewer(lineageService, originalNode) {
            @Override
            protected void customizeFrame() {
                // Override to prevent LineageViewer from modifying the frame
                // since we're using CustomFrame's styling
            }
        };
        
        // Add the lineage viewer's components to our custom frame
        initializeContent();
    }
    
    /**
     * Updates the color palette to match the image provided.
     */
    private void initializeWithNewPalette() {
        // Update the title bar colors based on the image palette
        Color darkGreen = new Color(33, 43, 39);  // Darkest circle
        Color midGreen = new Color(65, 79, 73);   // Second circle
        Color lightGreen = new Color(113, 129, 123); // Third circle
        Color paleGreen = new Color(175, 186, 183); // Fourth circle
        Color almostWhite = new Color(230, 235, 233); // Lightest circle
        
        // Update LineageViewerStyles with new palette
        LineageViewerStyles.updateColorPalette(darkGreen, midGreen, lightGreen, paleGreen, almostWhite);
        
        // Set the title bar to the darkest color
        setTitleBarColor(darkGreen);
        setTitleTextColor(almostWhite);
        
        // Set a drop shadow border
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(20, 20, 20), 1));
    }
    
    /**
     * Initialize content pane with LineageViewer components.
     */
    private void initializeContent() {
        // Get the content panel from the custom frame
        JPanel contentPanel = (JPanel) getContentPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Replace LineageViewer's toolbar with a styled version
        JPanel styledToolbar = createStyledToolbar();
        
        // Add components to content panel
        contentPanel.add(styledToolbar, BorderLayout.NORTH);
        contentPanel.add(lineageViewer.getGraphComponent(), BorderLayout.CENTER);
        contentPanel.add(createStyledSearchPanel(), BorderLayout.SOUTH);
    }
    
    /**
     * Creates a styled toolbar matching the new color palette.
     * 
     * @return styled toolbar panel
     */
    private JPanel createStyledToolbar() {
        // Get colors from updated LineageViewerStyles
        Color darkGreen = new Color(33, 43, 39);
        Color midGreen = new Color(65, 79, 73);
        Color paleGreen = new Color(175, 186, 183);
        
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, midGreen));
        toolbarPanel.setBackground(darkGreen);
        
        // Add LineageViewer's toolbar buttons with updated styling
        toolbarPanel.add(lineageViewer.zoomInButton);
        toolbarPanel.add(lineageViewer.zoomOutButton);
        toolbarPanel.add(lineageViewer.collapseAllButton);
        toolbarPanel.add(lineageViewer.expandAllButton);
        toolbarPanel.add(lineageViewer.searchModeButton);
        
        // Update button styling
        updateButtonStyling(toolbarPanel, paleGreen, darkGreen);
        
        return toolbarPanel;
    }
    
    /**
     * Creates a styled search panel.
     * 
     * @return styled search panel
     */
    private JPanel createStyledSearchPanel() {
        // Get colors from updated LineageViewerStyles
        Color darkGreen = new Color(33, 43, 39);
        Color lightGreen = new Color(113, 129, 123);
        Color almostWhite = new Color(230, 235, 233);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(darkGreen);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Style the search field
        lineageViewer.getSearchField().setBackground(lightGreen);
        lineageViewer.getSearchField().setForeground(almostWhite);
        lineageViewer.getSearchField().setBorder(BorderFactory.createLineBorder(lightGreen.darker(), 1));
        lineageViewer.getSearchField().setPreferredSize(new Dimension(200, 30));
        lineageViewer.getSearchField().setFont(new Font("Arial", Font.PLAIN, 14));
        
        searchPanel.add(lineageViewer.getSearchField());
        
        return searchPanel;
    }
    
    /**
     * Updates styling of all buttons in a panel.
     * 
     * @param panel the panel containing buttons
     * @param textColor the color for button text
     * @param backgroundColor the background color
     */
    private void updateButtonStyling(JPanel panel, Color textColor, Color backgroundColor) {
        java.awt.Component[] components = panel.getComponents();
        for (java.awt.Component comp : components) {
            if (comp instanceof javax.swing.AbstractButton) {
                javax.swing.AbstractButton button = (javax.swing.AbstractButton) comp;
                button.setForeground(textColor);
                button.setBackground(backgroundColor);
                button.setFont(new Font("Arial", Font.BOLD, 12));
                button.setBorder(BorderFactory.createLineBorder(textColor.darker(), 1));
                button.setFocusPainted(false);
            }
        }
    }

}
