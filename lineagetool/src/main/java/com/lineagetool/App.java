package com.lineagetool;

import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        LineagePopulate populate = new LineagePopulate();
        LineageService lineageService = populate.getLineageService();
        
        SwingUtilities.invokeLater(() -> {
            LineageViewer viewer = new LineageViewer(lineageService);
            viewer.setVisible(true);
        });
    }
}
