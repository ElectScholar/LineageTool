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
            viewer.addRootNode("Adah");
            viewer.addRootNode("Basemath");
            viewer.addRootNode("Isaac");
            viewer.addRootNode("Oholibamah");
            viewer.addRootNode("Timna");
            viewer.setVisible(true);
        });
    }
}
