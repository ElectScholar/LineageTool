/*package com.lineagetool.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import com.lineagetool.LineageViewer;
import com.lineagetool.graph.LineageGraphManager;

public class LineageSearchPanel extends JPanel {
    private final LineageViewer viewer;
    private final LineageGraphManager graphManager;
    private final JTextField searchField;
    private final JComboBox<String> searchTypeCombo;
    private final JButton clearButton;
    private final JButton previousMatch;
    private final JButton nextMatch;
    private final JLabel matchCounter;
    private int currentMatchIndex = 0;
    private int totalMatches = 0;

    public LineageSearchPanel(LineageViewer viewer, LineageGraphManager graphManager) {
        this.viewer = viewer;
        this.graphManager = graphManager;
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setBorder(createPanelBorder());
        setBackground(new Color(245, 245, 245));

        // Initialize components
        searchField = createSearchField();
        searchTypeCombo = createSearchTypeCombo();
        previousMatch = createNavigationButton("◀");
        nextMatch = createNavigationButton("▶");
        clearButton = createClearButton();
        matchCounter = createMatchCounter();

        // Layout components
        layoutComponents();
        setupEventHandlers();
    }

    private Border createPanelBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }

    private JTextField createSearchField() {
        JTextField field = new JTextField(25);
        field.setPreferredSize(new Dimension(200, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        return field;
    }

    private JComboBox<String> createSearchTypeCombo() {
        JComboBox<String> combo = new JComboBox<>(new String[]{
            "Name", "Generation", "Lineage Path", "Descendants"
        });
        combo.setPreferredSize(new Dimension(120, 30));
        return combo;
    }

    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setEnabled(false);
        button.setPreferredSize(new Dimension(40, 30));
        button.setFocusPainted(false);
        return button;
    }

    private JButton createClearButton() {
        JButton button = new JButton("✖");
        button.setPreferredSize(new Dimension(40, 30));
        button.setFocusPainted(false);
        button.setToolTipText("Clear Search");
        return button;
    }

    private JLabel createMatchCounter() {
        JLabel label = new JLabel("0 of 0");
        label.setPreferredSize(new Dimension(60, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private void layoutComponents() {
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchContainer.setOpaque(false);
        
        searchContainer.add(new JLabel("Search:"));
        searchContainer.add(searchField);
        searchContainer.add(searchTypeCombo);
        searchContainer.add(previousMatch);
        searchContainer.add(nextMatch);
        searchContainer.add(matchCounter);
        searchContainer.add(clearButton);
        
        add(searchContainer);
    }

    private void setupEventHandlers() {
        searchField.getDocument().addDocumentListener(new SearchDocumentListener());
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });

        clearButton.addActionListener(e -> {
            searchField.setText("");
            resetSearch();
        });

        previousMatch.addActionListener(e -> navigateMatches(-1));
        nextMatch.addActionListener(e -> navigateMatches(1));
        
        searchTypeCombo.addActionListener(e -> {
            if (!searchField.getText().isEmpty()) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        
        if (searchTerm.isEmpty()) {
            resetSearch();
            return;
        }

        switch (searchType) {
            case "Name":
                graphManager.searchByName(searchTerm);
                break;
            case "Generation":
                graphManager.searchByGeneration(searchTerm);
                break;
            case "Lineage Path":
                graphManager.searchLineagePath(searchTerm);
                break;
            case "Descendants":
                graphManager.searchDescendants(searchTerm);
                break;
        }

        updateMatchStatus();
    }

    private void navigateMatches(int direction) {
        if (totalMatches > 0) {
            currentMatchIndex = (currentMatchIndex + direction + totalMatches) % totalMatches;
            graphManager.highlightMatch(currentMatchIndex);
            updateMatchCounter();
        }
    }

    private void updateMatchStatus() {
        totalMatches = graphManager.getMatchCount();
        currentMatchIndex = 0;
        updateMatchCounter();
        updateNavigationButtons();
    }

    private void updateMatchCounter() {
        matchCounter.setText(String.format("%d of %d", 
            totalMatches > 0 ? currentMatchIndex + 1 : 0, totalMatches));
    }

    private void updateNavigationButtons() {
        boolean hasMatches = totalMatches > 0;
        previousMatch.setEnabled(hasMatches);
        nextMatch.setEnabled(hasMatches);
    }

    private void resetSearch() {
        currentMatchIndex = 0;
        totalMatches = 0;
        updateMatchCounter();
        updateNavigationButtons();
        graphManager.clearSearch();
    }

    // Inner class for handling document changes
    private class SearchDocumentListener implements javax.swing.event.DocumentListener {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
    }

    public JTextField getSearchField() {
        return searchField;
    }
}
*/