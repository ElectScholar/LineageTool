package com.lineagetool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LineageService implements DoublyLinkedList<Node<Person>> {
    private ArrayList<Node<Person>> roots;  // Store multiple root nodes
    private int size = 0;

    public LineageService() {
        roots = new ArrayList<>();
        size = 0;
    }

    @Override
    public void addFirst(Node<Person> data) {
        if (data == null) return;
        
        // Add as a new root node
        roots.add(data);
        size++;
    }

    @Override
    public void addLast(Node<Person> data) {
        if (data == null) return;
        
        if (roots.isEmpty()) {
            roots.add(data);
        } else {
            Node<Person> lastRoot = roots.get(roots.size() - 1);
            lastRoot.next.add(data);
            data.prev.add(lastRoot);
        }
        size++;
    }

    @Override
    public Node<Person> removeFirst() {
        if (roots.isEmpty()) return null;
        
        Node<Person> removed = roots.remove(0);
        size--;
        return removed;
    }

    @Override
    public Node<Person> removeLast() {
        if (roots.isEmpty()) return null;
        
        Node<Person> removed = roots.remove(roots.size() - 1);
        size--;
        return removed;
    }

    @Override
    public Node<Person> getFirst() {
        return roots.isEmpty() ? null : roots.get(0);
    }

    @Override
    public Node<Person> getLast() {
        return roots.isEmpty() ? null : roots.get(roots.size() - 1);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    public ArrayList<Node<Person>> getRoots() {
        return roots;
    }

    public void addChild(Node<Person> child, String parentName) {
        if (child == null || parentName == null) {
            return;
        }

        // Search through all root trees for the parent
        for (Node<Person> root : roots) {
            Queue<Node<Person>> queue = new LinkedList<>();
            queue.add(root);

            while (!queue.isEmpty()) {
                Node<Person> current = queue.poll();
                
                if (current.val.getName().equals(parentName)) {
                    // Initialize lists if null
                    if (current.next == null) {
                        current.next = new ArrayList<>();
                    }
                    if (child.prev == null) {
                        child.prev = new ArrayList<>();
                    }

                    // Add the parent-child relationship
                    current.next.add(child);
                    child.prev.add(current);
                    size++;
                    return;
                }
                
                if (current.next != null) {
                    queue.addAll(current.next);
                }
            }
        }
    }

    public Node<Person> getNode(String name) {
        // Search through all root trees
        for (Node<Person> root : roots) {
            Queue<Node<Person>> queue = new LinkedList<>();
            queue.add(root);

            while (!queue.isEmpty()) {
                Node<Person> current = queue.poll();
                
                if (current.val.getName().equals(name)) {
                    return current;
                }
                
                if (current.next != null) {
                    queue.addAll(current.next);
                }
            }
        }
        return null;
    }

    public void printLineage(Node<Person> node) {
        if (node == null) return;
    
        System.out.print(node.val.getName());
    
        // Print parents
        if (node.prev != null && !node.prev.isEmpty()) {
            System.out.print(" (Child of: ");
            for (Node<Person> parent : node.prev) {
                System.out.print(parent.val.getName() + ", ");
            }
            System.out.print(")");
        }
    
        // Print children
        if (node.next != null && !node.next.isEmpty()) {
            System.out.print(" (Parent of: ");
            for (Node<Person> child : node.next) {
                System.out.print(child.val.getName() + ", ");
            }
            System.out.print(")");
        }
    
        System.out.println();
    }
    
}
