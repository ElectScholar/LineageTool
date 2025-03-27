package com.lineagetool;

import java.awt.Dimension;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application class for LineageTool
 */
public class App {
    public static void main(String[] args) {
        // Set system look and feel with better error handling
        setupLookAndFeel();

        // Initialize data
        FileRead fileRead = new FileRead("lineagetool/lineage.txt");
        LineageService lineageService = fileRead.getLineageService();
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and configure viewer
                LineageViewer viewer = new LineageViewer(lineageService);
                viewer.setPreferredSize(new Dimension(1200, 800));
                
                // Initialize root nodes
                for (String rootNode : fileRead.getRootNodes()) {
                    viewer.addRootNode(rootNode);
                }
                
                // Build and layout graph
                viewer.buildGraph();
                viewer.getGraphManager().executeLayoutWithAnimation();
                
                // Setup initial view
                viewer.getGraphManager().collapseAllNodes();
                viewer.getGraphManager().setAutoLayout(true);
                
                // Show the viewer
                viewer.pack();
                viewer.setLocationRelativeTo(null);
                viewer.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("Error initializing LineageViewer:");
                e.printStackTrace();
            }
        });
    }

    private static void setupLookAndFeel() {
        try {
            // Use system look and feel for better native integration
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Improve font rendering on macOS
            System.setProperty("apple.awt.antialiasing", "true");
            System.setProperty("apple.awt.rendering", "quality");
            
        } catch (Exception e) {
            System.err.println("Could not set system look and feel:");
            e.printStackTrace();
        }
    }
}