package com.lineagetool;

import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
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