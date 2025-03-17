package com.lineagetool;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class AncestryTreeGUI extends JFrame {
    private JPanel treePanel;
    private LineageService lineageService;
    private Map<String, Point> nodePositions;
    private Map<String, List<String>> parentChildConnections;
    private int nodeWidth = 150;
    private int nodeHeight = 70;
    private int horizontalGap = 30;
    private int verticalGap = 100;
    private Person selectedPerson;
    private JTextArea infoPanel;

    public AncestryTreeGUI(LineageService lineageService) {
        this.lineageService = lineageService;
        this.nodePositions = new HashMap<>();
        this.parentChildConnections = new HashMap<>();
        
        setTitle("Ancestry Tree Visualization");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create scrollable tree panel
        treePanel = new TreePanel();
        JScrollPane scrollPane = new JScrollPane(treePanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        // Info panel for displaying person details
        infoPanel = new JTextArea(10, 30);
        infoPanel.setEditable(false);
        infoPanel.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane infoScrollPane = new JScrollPane(infoPanel);
        
        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoScrollPane, BorderLayout.EAST);
        
        // Add toolbar with zoom controls
        JToolBar toolBar = new JToolBar();
        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");
        JButton resetButton = new JButton("Reset View");
        
        zoomInButton.addActionListener(e -> {
            ((TreePanel)treePanel).changeZoom(0.1);
        });
        
        zoomOutButton.addActionListener(e -> {
            ((TreePanel)treePanel).changeZoom(-0.1);
        });
        
        resetButton.addActionListener(e -> {
            ((TreePanel)treePanel).resetView();
        });
        
        toolBar.add(zoomInButton);
        toolBar.add(zoomOutButton);
        toolBar.add(resetButton);
        toolBar.setFloatable(false);
        
        mainPanel.add(toolBar, BorderLayout.NORTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Calculate positions for all nodes
        buildTree();
        
        // Set preferred size based on calculations
        treePanel.setPreferredSize(new Dimension(
            getMaxWidth() + nodeWidth + 100,
            getMaxHeight() + nodeHeight + 100
        ));
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private int getMaxWidth() {
        int max = 0;
        for (Point p : nodePositions.values()) {
            if (p.x > max) max = p.x;
        }
        return max;
    }
    
    private int getMaxHeight() {
        int max = 0;
        for (Point p : nodePositions.values()) {
            if (p.y > max) max = p.y;
        }
        return max;
    }
    
    private void buildTree() {
        // Since there's no getRoot() method, we'll start with getFirst() 
        // which should be the head of the doubly linked list
        Node<Person> startNode = lineageService.getFirst();
        if (startNode == null) {
            System.out.println("No nodes in the lineage service.");
            return;
        }
        
        // Build a map of parent-child relationships
        buildConnectionsMap(startNode);
        
        // Calculate positions
        calculateNodePositions(startNode);
    }
    
    private void buildConnectionsMap(Node<Person> startNode) {
        // Use BFS to traverse the tree and build connections
        Queue<Node<Person>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.add(startNode);
        
        while (!queue.isEmpty()) {
            Node<Person> current = queue.poll();
            String currentName = current.val.getName();
            
            if (visited.contains(currentName)) {
                continue;
            }
            
            visited.add(currentName);
            
            // Process children (next nodes)
            for (Node<Person> child : current.next) {
                String childName = child.val.getName();
                
                // Add to parent-child connections
                if (!parentChildConnections.containsKey(currentName)) {
                    parentChildConnections.put(currentName, new ArrayList<>());
                }
                parentChildConnections.get(currentName).add(childName);
                
                queue.add(child);
            }
        }
    }
    
    private void calculateNodePositions(Node<Person> startNode) {
        // First, assign levels (depths) to each node
        Map<String, Integer> nodeLevels = new HashMap<>();
        assignLevels(startNode, 0, nodeLevels, new HashSet<>());
        
        // Group nodes by level
        Map<Integer, List<String>> levelNodes = new HashMap<>();
        for (Map.Entry<String, Integer> entry : nodeLevels.entrySet()) {
            if (!levelNodes.containsKey(entry.getValue())) {
                levelNodes.put(entry.getValue(), new ArrayList<>());
            }
            levelNodes.get(entry.getValue()).add(entry.getKey());
        }
        
        // Calculate horizontal positions for each level
        int maxLevel = levelNodes.keySet().stream().max(Integer::compare).orElse(0);
        for (int level = 0; level <= maxLevel; level++) {
            List<String> nodesInLevel = levelNodes.getOrDefault(level, new ArrayList<>());
            int totalWidth = nodesInLevel.size() * nodeWidth + (nodesInLevel.size() - 1) * horizontalGap;
            int startX = 50;
            
            for (int i = 0; i < nodesInLevel.size(); i++) {
                String nodeName = nodesInLevel.get(i);
                int x = startX + i * (nodeWidth + horizontalGap);
                int y = 50 + level * (nodeHeight + verticalGap);
                nodePositions.put(nodeName, new Point(x, y));
            }
        }
        
        // Adjust positions to center parents above their children
        optimizePositions(nodeLevels, levelNodes, maxLevel);
    }
    
    private void assignLevels(Node<Person> node, int level, Map<String, Integer> nodeLevels, Set<String> visited) {
        if (node == null) return;
        
        String nodeName = node.val.getName();
        
        // Avoid cycles
        if (visited.contains(nodeName)) return;
        visited.add(nodeName);
        
        // Assign or update level
        if (!nodeLevels.containsKey(nodeName) || level > nodeLevels.get(nodeName)) {
            nodeLevels.put(nodeName, level);
        }
        
        // Process children (next nodes)
        for (Node<Person> child : node.next) {
            assignLevels(child, level + 1, nodeLevels, new HashSet<>(visited));
        }
    }
    
    private void optimizePositions(Map<String, Integer> nodeLevels, Map<Integer, List<String>> levelNodes, int maxLevel) {
        // Start from the bottom level and work up
        for (int level = maxLevel - 1; level >= 0; level--) {
            List<String> nodesInLevel = levelNodes.getOrDefault(level, new ArrayList<>());
            
            for (String nodeName : nodesInLevel) {
                List<String> children = parentChildConnections.getOrDefault(nodeName, new ArrayList<>());
                
                if (!children.isEmpty()) {
                    // Calculate average X position of children
                    int totalX = 0;
                    int validChildren = 0;
                    
                    for (String childName : children) {
                        if (nodePositions.containsKey(childName)) {
                            totalX += nodePositions.get(childName).x;
                            validChildren++;
                        }
                    }
                    
                    if (validChildren > 0) {
                        int avgX = totalX / validChildren;
                        // Center parent above children
                        Point pos = nodePositions.get(nodeName);
                        nodePositions.put(nodeName, new Point(avgX - nodeWidth / 2, pos.y));
                    }
                }
            }
        }
    }
    
    private class TreePanel extends JPanel {
        private int offsetX = 0;
        private int offsetY = 0;
        private double scale = 1.0;
        private Point dragStart;
        
        public TreePanel() {
            setBackground(Color.WHITE);
            
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dragStart = e.getPoint();
                    
                    // Check if a node was clicked
                    Point mousePoint = new Point((int)((e.getX() - offsetX) / scale), (int)((e.getY() - offsetY) / scale));
                    selectedPerson = null;
                    
                    for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
                        Point p = entry.getValue();
                        if (mousePoint.x >= p.x && mousePoint.x <= p.x + nodeWidth &&
                            mousePoint.y >= p.y && mousePoint.y <= p.y + nodeHeight) {
                            String personName = entry.getKey();
                            Node<Person> node = lineageService.getNode(personName);
                            if (node != null) {
                                selectedPerson = node.val;
                                updateInfoPanel(selectedPerson);
                                break;
                            }
                        }
                    }
                    repaint();
                }
                
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragStart != null) {
                        offsetX += e.getX() - dragStart.x;
                        offsetY += e.getY() - dragStart.y;
                        dragStart = e.getPoint();
                        repaint();
                    }
                }
                
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    double zoomFactor = 0.1;
                    if (e.getWheelRotation() < 0) {
                        scale *= (1 + zoomFactor);
                    } else {
                        scale /= (1 + zoomFactor);
                    }
                    scale = Math.max(0.2, Math.min(scale, 3.0));
                    repaint();
                }
            };
            
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
            addMouseWheelListener(mouseAdapter);
        }
        
        public void changeZoom(double factor) {
            scale = Math.max(0.2, Math.min(scale * (1 + factor), 3.0));
            repaint();
        }
        
        public void resetView() {
            offsetX = 0;
            offsetY = 0;
            scale = 1.0;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Enable anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Apply transformations
            g2d.translate(offsetX, offsetY);
            g2d.scale(scale, scale);
            
            // Draw connections first so they appear behind nodes
            drawConnections(g2d);
            
            // Draw nodes
            for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
                String personName = entry.getKey();
                Point position = entry.getValue();
                
                // Draw node
                if (selectedPerson != null && personName.equals(selectedPerson.getName())) {
                    g2d.setColor(new Color(173, 216, 230)); // Light blue for selected
                } else {
                    g2d.setColor(new Color(240, 240, 240)); // Light gray
                }
                g2d.fillRoundRect(position.x, position.y, nodeWidth, nodeHeight, 10, 10);
                
                g2d.setColor(Color.BLACK);
                g2d.drawRoundRect(position.x, position.y, nodeWidth, nodeHeight, 10, 10);
                
                // Draw person name
                g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(personName);
                g2d.drawString(personName, position.x + (nodeWidth - textWidth) / 2, position.y + nodeHeight / 2);
                
                // Draw details if available
                Node<Person> node = lineageService.getNode(personName);
                if (node != null) {
                    Person person = node.val;
                    String[] details = person.getDetails();
                    if (details != null && details.length > 0) {
                        String detail = details[0];
                        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
                        fm = g2d.getFontMetrics();
                        textWidth = fm.stringWidth(detail);
                        if (textWidth > nodeWidth - 10) {
                            detail = detail.substring(0, 15) + "...";
                            textWidth = fm.stringWidth(detail);
                        }
                        g2d.drawString(detail, position.x + (nodeWidth - textWidth) / 2, 
                            position.y + nodeHeight / 2 + 15);
                    }
                }
            }
        }
        
        private void drawConnections(Graphics2D g2d) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            
            // Draw connections between parents and children
            for (Map.Entry<String, List<String>> entry : parentChildConnections.entrySet()) {
                String parentName = entry.getKey();
                Point parentPos = nodePositions.get(parentName);
                
                if (parentPos == null) continue;
                
                for (String childName : entry.getValue()) {
                    Point childPos = nodePositions.get(childName);
                    
                    if (childPos != null) {
                        // Draw line from bottom center of parent to top center of child
                        int parentBottomX = parentPos.x + nodeWidth / 2;
                        int parentBottomY = parentPos.y + nodeHeight;
                        int childTopX = childPos.x + nodeWidth / 2;
                        int childTopY = childPos.y;
                        
                        g2d.drawLine(parentBottomX, parentBottomY, childTopX, childTopY);
                    }
                }
            }
        }
    }
    
    private void updateInfoPanel(Person person) {
        if (person == null) {
            infoPanel.setText("");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(person.getName()).append("\n\n");
        
        String[] details = person.getDetails();
        if (details != null && details.length > 0) {
            sb.append("Details:\n");
            for (String line : details) {
                sb.append("• ").append(line).append("\n");
            }
        }
        
        // Add parent information
        Node<Person> node = lineageService.getNode(person.getName());
        if (node != null) {
            if (!node.prev.isEmpty()) {
                sb.append("\nParents:\n");
                for (Node<Person> parent : node.prev) {
                    sb.append("• ").append(parent.val.getName()).append("\n");
                }
            }
            
            // Add children information
            if (!node.next.isEmpty()) {
                sb.append("\nChildren:\n");
                for (Node<Person> child : node.next) {
                    sb.append("• ").append(child.val.getName()).append("\n");
                }
            }
        }
        
        infoPanel.setText(sb.toString());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LineagePopulate populator = new LineagePopulate();
            LineageService service = populator.getLineageService();
            new AncestryTreeGUI(service);
        });
    }
}