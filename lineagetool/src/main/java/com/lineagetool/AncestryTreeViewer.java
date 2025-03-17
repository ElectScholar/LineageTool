package com.lineagetool;
// Driver class
import javax.swing.SwingUtilities;

public class AncestryTreeViewer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LineagePopulate populator = new LineagePopulate();
            LineageService service = populator.getLineageService();
            new AncestryTreeGUI(service);
        });
    }
}