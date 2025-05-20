package com.lineagetool;

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

        FileRead fileRead = new FileRead("lineage.txt");
        LineageService lineageService = fileRead.getLineageService();
        
            // Create and show the integrated application
            LineageViewerFrame frame = new LineageViewerFrame(lineageService, "Adam");
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
    }
}