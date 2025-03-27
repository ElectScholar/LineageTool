package com.lineagetool.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.lineagetool.LineageViewer;
import com.lineagetool.graph.LineageGraphManager;

public class LineageToolbar extends JPanel {
    private final LineageViewer viewer;
    private final LineageGraphManager graphManager;
    
    // View Controls
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton fitButton;
    private JButton resetViewButton;
    
    // Layout Controls
    private JButton collapseAllButton;
    private JButton expandAllButton;
    private JToggleButton autoLayoutButton;
    
    // Search Controls
    private JToggleButton searchModeButton;
    private JButton clearSearchButton;
    
    // Display Controls
    private JToggleButton showGridButton;
    private JComboBox<String> layoutDirectionBox;

    public LineageToolbar(LineageViewer viewer, LineageGraphManager graphManager) {
        this.viewer = viewer;
        this.graphManager = graphManager;
        initializeToolbar();
    }

    private void initializeToolbar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(new Color(240, 240, 240));

        createViewControls();
        createLayoutControls();
        createSearchControls();
        createDisplayControls();
        addSeparators();
        setupActions();
    }

    private void createViewControls() {
        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        viewPanel.setOpaque(false);
        
        zoomInButton = createStyledButton("Zoom In", "👁️+");
        zoomOutButton = createStyledButton("Zoom Out", "👁️-");
        fitButton = createStyledButton("Fit View", "🔍");
        resetViewButton = createStyledButton("Reset View", "↺");
        
        viewPanel.add(zoomInButton);
        viewPanel.add(zoomOutButton);
        viewPanel.add(fitButton);
        viewPanel.add(resetViewButton);
        
        add(viewPanel);
    }

    private void createLayoutControls() {
        JPanel layoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        layoutPanel.setOpaque(false);
        
        collapseAllButton = createStyledButton("Collapse All", "▼");
        expandAllButton = createStyledButton("Expand All", "▲");
        autoLayoutButton = createStyledToggleButton("Auto Layout", "📊");
        
        layoutPanel.add(collapseAllButton);
        layoutPanel.add(expandAllButton);
        layoutPanel.add(autoLayoutButton);
        
        add(layoutPanel);
    }

    private void createSearchControls() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setOpaque(false);
        
        searchModeButton = createStyledToggleButton("Search Mode", "🔍");
        clearSearchButton = createStyledButton("Clear", "✖");
        
        searchPanel.add(searchModeButton);
        searchPanel.add(clearSearchButton);
        
        add(searchPanel);
    }

    private void createDisplayControls() {
        JPanel displayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        displayPanel.setOpaque(false);
        
        showGridButton = createStyledToggleButton("Show Grid", "⊞");
        layoutDirectionBox = new JComboBox<>(new String[]{"Top to Bottom", "Left to Right"});
        layoutDirectionBox.setPreferredSize(new Dimension(120, 35));
        
        displayPanel.add(showGridButton);
        displayPanel.add(layoutDirectionBox);
        
        add(displayPanel);
    }

    private JButton createStyledButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        styleButton(button);
        return button;
    }

    private JToggleButton createStyledToggleButton(String text, String icon) {
        JToggleButton button = new JToggleButton(icon + " " + text);
        styleButton(button);
        return button;
    }

    private void styleButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        setupButtonHoverEffect(button);
    }

    private void setupButtonHoverEffect(AbstractButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        });
    }

    private void addSeparators() {
        Component[] components = getComponents();
        for (int i = 0; i < components.length - 1; i++) {
            add(new JSeparator(JSeparator.VERTICAL) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(1, 35);
                }
            });
        }
    }

    private void setupActions() {
        // View Controls
        zoomInButton.addActionListener(e -> graphManager.getGraphComponent().zoomIn());
        zoomOutButton.addActionListener(e -> graphManager.getGraphComponent().zoomOut());
        fitButton.addActionListener(e -> graphManager.getGraphComponent().zoomAndCenter());
        //resetViewButton.addActionListener(e -> resetView());
        
        // Layout Controls
        collapseAllButton.addActionListener(e -> graphManager.collapseAllNodes());
        expandAllButton.addActionListener(e -> graphManager.expandAllNodes());
        autoLayoutButton.addActionListener(e -> graphManager.setAutoLayout(autoLayoutButton.isSelected()));
    }
        /* Search Controls
        searchModeButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                viewer.getSearchField().requestFocusInWindow();
            }
        });
        clearSearchButton.addActionListener(e -> {
            viewer.getSearchField().setText("");
            graphManager.clearSearch();
        });
        8?
        
        // Display Controls
        //showGridButton.addActionListener(e -> 
            //graphManager.getGraphComponent().setGridVisible(showGridButton.isSelected()));
        //layoutDirectionBox.addActionListener(e -> 
            ///updateLayoutDirection((String)layoutDirectionBox.getSelectedItem()));
    }

    private void resetView() {
        graphManager.getGraphComponent().zoomActual();
        graphManager.setAutoLayout(true);
        autoLayoutButton.setSelected(true);
        graphManager.executeLayoutWithAnimation();
    }
    /*
    private void updateLayoutDirection(String direction) {
        boolean isVertical = direction.equals("Top to Bottom");
        graphManager.setLayoutOrientation(isVertical);
        graphManager.executeLayoutWithAnimation();
    }*/
}