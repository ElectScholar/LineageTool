package com.lineagetool.ui;

import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import com.lineagetool.LineageViewer;
import com.lineagetool.Person;
import com.lineagetool.graph.LineageGraphManager;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;

public class LineageEventHandler {
    private final LineageGraphManager graphManager;
    private final LineageViewer viewer;
    private Point dragStart;
    private boolean isDragging;

    public LineageEventHandler(LineageGraphManager graphManager, LineageViewer viewer) {
        this.graphManager = graphManager;
        this.viewer = viewer;
        this.isDragging = false;
    }

    public void setupHandlers() {
        setupMouseHandlers();
        setupKeyboardHandlers();
        setupComponentHandlers();
    }

    private void setupMouseHandlers() {
        mxGraphComponent graphComponent = graphManager.getGraphComponent();
        
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragStart = e.getPoint();
                    isDragging = false;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    isDragging = true;
                    handleDrag(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isDragging) {
                    handleClick(e);
                }
                dragStart = null;
                isDragging = false;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleDoubleClick(e);
                }
            }
        };

        graphComponent.getGraphControl().addMouseListener(mouseHandler);
        graphComponent.getGraphControl().addMouseMotionListener(mouseHandler);
        graphComponent.getGraphControl().addMouseWheelListener(this::handleMouseWheel);
    }

    private void setupKeyboardHandlers() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_PLUS:
                            if (e.isControlDown()) {
                                graphManager.getGraphComponent().zoomIn();
                                return true;
                            }
                            break;
                        case KeyEvent.VK_MINUS:
                            if (e.isControlDown()) {
                                graphManager.getGraphComponent().zoomOut();
                                return true;
                            }
                            break;
                        case KeyEvent.VK_ESCAPE:
                            viewer.getInfoPanel().setVisible(false);
                            return true;
                    }
                }
                return false;
            });
    }

    private void setupComponentHandlers() {
        graphManager.getGraphComponent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (graphManager.isAutoLayout()) {
                    graphManager.executeLayoutWithAnimation();
                }
            }
        });
    }

    private void handleClick(MouseEvent e) {
        Object cell = graphManager.getGraphComponent().getCellAt(e.getX(), e.getY());
        if (cell instanceof mxCell) {
            mxCell mxCell = (mxCell) cell;
            if (mxCell.isVertex()) {
                showNodeInfo(mxCell);
            }
        } else {
            viewer.getInfoPanel().setVisible(false);
        }
    }

    private void handleDoubleClick(MouseEvent e) {
        Object cell = graphManager.getGraphComponent().getCellAt(e.getX(), e.getY());
        if (cell instanceof mxCell) {
            mxCell mxCell = (mxCell) cell;
            if (mxCell.isVertex()) {
                boolean isCollapsed = graphManager.getGraph().isCellCollapsed(mxCell);
                graphManager.setCollapsed(mxCell, !isCollapsed);
            }
        }
    }

    private void handleDrag(MouseEvent e) {
        if (dragStart != null) {
            Point dragEnd = e.getPoint();
            int dx = dragEnd.x - dragStart.x;
            int dy = dragEnd.y - dragStart.y;
            
            if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                graphManager.setAutoLayout(false);
                dragStart = dragEnd;
            }
        }
    }

    private void handleMouseWheel(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
                graphManager.getGraphComponent().zoomIn();
            } else {
                graphManager.getGraphComponent().zoomOut();
            }
            e.consume();
        }
    }

    private void showNodeInfo(mxCell cell) {
        String nodeName = graphManager.extractPersonName(cell);
        Person person = viewer.getLineageService().getNode(nodeName).val;
        
        if (person != null) {
            LineageInfoPanel infoPanel = viewer.getInfoPanel();
            infoPanel.updateInfo(person);
            
            // Position info panel next to the node
            mxRectangle cellBounds = graphManager.getGraphComponent().getGraph().getCellBounds(cell);
            Rectangle bounds = cellBounds != null ? cellBounds.getRectangle() : null;
            if (bounds != null) {
                Point location = graphManager.getGraphComponent().getLocationOnScreen();
                infoPanel.setLocation(
                    location.x + bounds.x + bounds.width + 10,
                    location.y + bounds.y
                );
                infoPanel.setVisible(true);
            }
        }
    }
}