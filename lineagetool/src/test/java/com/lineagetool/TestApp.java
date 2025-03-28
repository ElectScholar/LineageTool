package com.lineagetool;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Test application class for LineageTool development
 */
public class TestApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileRead fileRead = new FileRead("lineagetool/lineage.txt");
        LineageService lineageService = fileRead.getLineageService();
        
                // Example usage
        SwingUtilities.invokeLater(() -> {
            LineageVisualizationApp app = new LineageVisualizationApp(fileRead);
            app.setVisible(true);
        });
        // Test code here
        System.out.println("Testing LineageTool functionality:");
        
        // Print all root nodes
        System.out.println("\nRoot nodes:");
        List<Node<Person>> heads = lineageService.getHeads();

        for (Node<Person> head : heads) {
            System.out.println(head.val.getName());
        }

        //search for a person
        System.out.println("\nSearching for a person:");
        Node<Person> person = lineageService.getNode("Esau");
        System.out.println(person.val.getName());
        System.out.println("Children: ");
        for (Node<Person> child : person.next) {
            System.out.println(child.val.getName());
        }


        
    }
}