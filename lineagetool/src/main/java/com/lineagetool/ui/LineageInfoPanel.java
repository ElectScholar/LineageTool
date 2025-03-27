package com.lineagetool.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import com.lineagetool.Person;

public class LineageInfoPanel extends JDialog {
    private final JPanel mainPanel;
    private final JPanel detailsPanel;
    private final JPanel familyPanel;
    private final JPanel buttonPanel;
    
    // Info components
    private final JLabel nameLabel;
    private final JLabel generationLabel;
    private final JTextArea descriptionArea;
    private final JList<String> childrenList;
    private final JLabel spouseLabel;
    private final JLabel parentLabel;
    
    // Action buttons
    private final JButton focusButton;
    private final JButton expandButton;
    private final JButton traceLineageButton;
    private Person currentPerson;

    public LineageInfoPanel(JFrame parent) {
        super(parent, false);
        setUndecorated(true);

        // Initialize panels
        mainPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel = new JPanel(new GridBagLayout());
        familyPanel = new JPanel(new BorderLayout(5, 5));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Initialize components
        nameLabel = createStyledLabel("");
        generationLabel = createStyledLabel("");
        descriptionArea = createDescriptionArea();
        childrenList = createChildrenList();
        spouseLabel = createStyledLabel("");
        parentLabel = createStyledLabel("");
        
        focusButton = createActionButton("Focus", "🔍");
        expandButton = createActionButton("Expand", "▼");
        traceLineageButton = createActionButton("Trace Lineage", "↝");

        setupPanels();
        setupWindow();
        setupEventHandlers();
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JTextArea createDescriptionArea() {
        JTextArea area = new JTextArea(3, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBackground(new Color(245, 245, 245));
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return area;
    }

    private JList<String> createChildrenList() {
        JList<String> list = new JList<>();
        list.setVisibleRowCount(4);
        list.setFont(new Font("Arial", Font.PLAIN, 12));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }

    private JButton createActionButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        return button;
    }

    private void setupPanels() {
        // Setup details panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 2, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        detailsPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(nameLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        detailsPanel.add(new JLabel("Generation:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(generationLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        detailsPanel.add(new JLabel("Parent:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(parentLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        detailsPanel.add(new JLabel("Spouse:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(spouseLabel, gbc);

        // Setup family panel
        familyPanel.setBorder(BorderFactory.createTitledBorder("Children"));
        familyPanel.add(new JScrollPane(childrenList), BorderLayout.CENTER);

        // Setup button panel
        buttonPanel.add(focusButton);
        buttonPanel.add(expandButton);
        buttonPanel.add(traceLineageButton);

        // Combine panels
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        mainPanel.add(familyPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private void setupWindow() {
        setBackground(new Color(250, 250, 250));
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        addWindowFocusListener(new WindowAdapter() {
            public void windowLostFocus(WindowEvent e) {
                setVisible(false);
            }
        });
    }

    private void setupEventHandlers() {
        focusButton.addActionListener(e -> focusOnPerson());
        expandButton.addActionListener(e -> toggleExpand());
        traceLineageButton.addActionListener(e -> traceLineage());
        
        childrenList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedChild = childrenList.getSelectedValue();
                    if (selectedChild != null) {
                        showPersonByName(selectedChild);
                    }
                }
            }
        });
    }

    public void updateInfo(Person person) {
        if (person == null) return;
        
        currentPerson = person;
        nameLabel.setText(person.getName());
        String[] details = person.getDetails();
        generationLabel.setText(details[0]);
        descriptionArea.setText(details[2]);
        
        // Update family information
        //updateFamilyInfo(person);
        
        pack();
        revalidate();
        repaint();
    }
/*
    private void updateFamilyInfo(Person person) {
        Node<Person> node = person.getNode();
        
        // Update parent info
        if (node.parent != null) {
            parentLabel.setText(node.parent.val.getName());
        } else {
            parentLabel.setText("None");
        }
        
        // Update spouse info
        spouseLabel.setText(person.getSpouse() != null ? 
            person.getSpouse().getName() : "None");
        
        // Update children list
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Node<Person> child : node.next) {
            model.addElement(child.val.getName());
        }
        childrenList.setModel(model);
    }
*/
    private void focusOnPerson() {
        if (currentPerson != null) {
            // Implementation depends on your graph manager
            firePropertyChange("FOCUS_PERSON", null, currentPerson.getName());
        }
    }

    private void toggleExpand() {
        if (currentPerson != null) {
            firePropertyChange("TOGGLE_EXPAND", null, currentPerson.getName());
        }
    }

    private void traceLineage() {
        if (currentPerson != null) {
            firePropertyChange("TRACE_LINEAGE", null, currentPerson.getName());
        }
    }

    private void showPersonByName(String name) {
        firePropertyChange("SHOW_PERSON", null, name);
    }
}