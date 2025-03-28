package com.lineagetool;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class AbstractLineageViewer extends JFrame {
    protected LineageService lineageService;
    protected List<String> rootNodes;

    public AbstractLineageViewer(LineageService lineageService, List<String> initialRootNodes) {
        this.lineageService = lineageService;
        this.rootNodes = initialRootNodes;
        
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Lineage Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    // Abstract methods to be implemented by subclasses
    protected abstract void buildGraph();
    protected abstract void initializeComponents();
    protected abstract void setupEventHandlers();

    // Common utility methods can be added here
    protected void addRootNode(String personName) {
        if (!rootNodes.contains(personName) && lineageService.getNode(personName) != null) {
            rootNodes.add(personName);
            buildGraph();
        }
    }
}
