package com.lineagetool;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import com.lineagetool.graph.LineageGraphManager;
import com.lineagetool.ui.LineageEventHandler;
import com.lineagetool.ui.LineageInfoPanel;
import com.lineagetool.ui.LineageToolbar;

public class LineageViewer extends AbstractLineageViewer{
    private LineageGraphManager graphManager;
    private LineageEventHandler eventHandler;
    private LineageToolbar toolbar;
    //private LineageSearchPanel searchPanel;
    private LineageInfoPanel infoPanel;

    public LineageViewer(LineageService lineageService) {
        super(lineageService, new ArrayList<>(Collections.singletonList("Isaac")));
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        graphManager = new LineageGraphManager(lineageService);
        eventHandler = new LineageEventHandler(graphManager, this);
        toolbar = new LineageToolbar(this, graphManager);
        //searchPanel = new LineageSearchPanel(this, graphManager);
        infoPanel = new LineageInfoPanel(this);

        customizeFrame();
        layoutComponents();
        setupEventHandlers();
        graphManager.buildGraph();
    }

    @Override
    protected void setupEventHandlers() {
        eventHandler.setupHandlers();
    }

    @Override
    protected void buildGraph() {
        if (graphManager != null) {
            graphManager.buildGraph();
        } else {
            throw new IllegalStateException("GraphManager not initialized");
        }
    }

    private void customizeFrame() {
        setTitle("Lineage Viewer");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(graphManager.getGraphComponent(), BorderLayout.CENTER);
        //add(searchPanel, BorderLayout.SOUTH);
    }

    public LineageGraphManager getGraphManager() { return graphManager; }
    public LineageInfoPanel getInfoPanel() { return infoPanel; }
    public LineageService getLineageService() { return lineageService; }
}