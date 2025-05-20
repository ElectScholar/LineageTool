package com.lineagetool;// In CustomFrame.java

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * CustomFrame provides a JFrame with a custom title bar and styling.
 * It removes the standard window decoration and replaces it with a
 * customizable title bar that includes a title and close button.
 */
public class CustomFrame extends JFrame {
    private Point initialClick;
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton closeButton;
    private JButton minimizeButton;
    private Color titleBarColor = new Color(50, 50, 50);  // Dark gray
    private Color titleTextColor = Color.WHITE;
    private Color closeButtonColor = new Color(232, 17, 35);  // Red
    private Color minimizeButtonColor = new Color(80, 80, 80);  // Dark gray

    /**
     * Creates a new CustomFrame with default size and title.
     */
    public CustomFrame() {
        this("Custom Window", 800, 600);
    }

    /**
     * Creates a new CustomFrame with the specified title and default size.
     *
     * @param title the title of the window
     */
    public CustomFrame(String title) {
        this(title, 800, 600);
    }

    /**
     * Creates a new CustomFrame with the specified title, width, and height.
     *
     * @param title the title of the window
     * @param width the width of the window
     * @param height the height of the window
     */
    public CustomFrame(String title, int width, int height) {
        super();
        setTitle(title);
        setSize(width, height);
        setUndecorated(true);  // Remove standard window decoration
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create custom window chrome
        initializeTitleBar();

        // Set up the main content area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to the frame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(titleBar, BorderLayout.NORTH);
        contentPane.add(contentPanel, BorderLayout.CENTER);

        // Add a drop shadow border to the window
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(30, 30, 30), 1));
    }

    /**
     * Sets up the custom title bar with title and buttons.
     */
    private void initializeTitleBar() {
        titleBar = new JPanel();
        titleBar.setBackground(titleBarColor);
        titleBar.setPreferredSize(new Dimension(getWidth(), 40));
        titleBar.setLayout(new BorderLayout());
        // --- DEBUGGING: Add a bright border to the title bar ---
        //titleBar.setBorder(BorderFactory.createLineBorder(Color., 3)); // Blue border, 3 pixels thick

        // Add title label
        titleLabel = new JLabel(" " + getTitle());
        titleLabel.setForeground(titleTextColor);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Create a panel for the buttons (for layout purposes)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        // Create minimize button
        minimizeButton = createButton("-", minimizeButtonColor); // Changed '−' to '-'
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        // Create close button
        closeButton = createButton("X", closeButtonColor); // Changed '×' to 'X'
        closeButton.addActionListener(e -> dispose());

        // Add components to panels
        buttonPanel.add(minimizeButton);
        buttonPanel.add(closeButton);

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(buttonPanel, BorderLayout.EAST);

        // Add window dragging functionality
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Get current location of the frame
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // Determine how much the mouse moved
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // Move frame to new location
                setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
    }

    /**
     * Creates a styled button for the title bar.
     *
     * @param text the text to display on the button
     * @param bgColor the background color for the button
     * @return the styled JButton
     */
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(45, 30));
        // --- DEBUGGING: Add a bright border to the button ---
        button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Yellow border, 2 pixels thick

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(brighten(bgColor, 0.2f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * Makes a color brighter by the specified amount.
     *
     * @param color the color to brighten
     * @param amount amount to brighten by (0.0 to 1.0)
     * @return the brightened color
     */
    private Color brighten(Color color, float amount) {
        int r = Math.min(255, (int)(color.getRed() * (1 + amount)));
        int g = Math.min(255, (int)(color.getGreen() * (1 + amount)));
        int b = Math.min(255, (int)(color.getBlue() * (1 + amount)));
        return new Color(r, g, b);
    }

    /**
     * Sets the color of the title bar.
     *
     * @param color the new color for the title bar
     */
    public void setTitleBarColor(Color color) {
        this.titleBarColor = color;
        if (titleBar != null) {
            titleBar.setBackground(color);
        }
    }

    /**
     * Sets the color of the title text.
     *
     * @param color the new color for the title text
     */
    public void setTitleTextColor(Color color) {
        this.titleTextColor = color;
        if (titleLabel != null) {
            titleLabel.setForeground(color);
        }
    }

    /**
     * Gets the content panel where you can add your UI components.
     *
     * @return the content panel
     */
    public Container getContentPanel() {
        Container panel = (Container) ((BorderLayout) getContentPane().getLayout())
        .getLayoutComponent(BorderLayout.CENTER);
        return panel;
    }
}