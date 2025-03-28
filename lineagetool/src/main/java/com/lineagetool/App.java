package com.lineagetool;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application class for LineageTool
 */
public class App {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileRead fileRead = new FileRead("lineagetool/lineage.txt");
        LineageService lineageService = fileRead.getLineageService();
        
        SwingUtilities.invokeLater(() -> {
            LineageViewer viewer = new LineageViewer(lineageService);
            
            for (String rootNode : fileRead.getRootNodes()) {
                viewer.addRootNode(rootNode);
            }
            
            viewer.setVisible(true);
        });
    }
}