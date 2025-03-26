package com.lineagetool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class LineageService implements DoublyLinkedList<Node<Person>> {
    private final List<Node<Person>> heads;
    private final Set<String> rootNodes;
    private int size = 0;

    public LineageService() {
        this.heads = new ArrayList<>();
        this.rootNodes = new HashSet<>();
    }

    @Override
    public void addFirst(Node<Person> node) {
        if (node == null) return;
        
        heads.add(node);
        rootNodes.add(node.val.getName());
        size++;
    }

    @Override
    public void addLast(Node<Person> data) {
        if (data == null) return;
        
        if (heads.isEmpty()) {
            heads.add(data);
        } else {
            Node<Person> lastRoot = heads.get(heads.size() - 1);
            lastRoot.next.add(data);
            data.prev.add(lastRoot);
        }
        size++;
    }

    @Override
    public Node<Person> removeFirst() {
        if (heads.isEmpty()) return null;
        
        Node<Person> removed = heads.remove(0);
        rootNodes.remove(removed.val.getName());
        size--;
        return removed;
    }

    @Override
    public Node<Person> removeLast() {
        if (heads.isEmpty()) return null;
        
        Node<Person> removed = heads.remove(heads.size() - 1);
        rootNodes.remove(removed.val.getName());
        size--;
        return removed;
    }

    @Override
    public Node<Person> getFirst() {
        return heads.isEmpty() ? null : heads.get(0);
    }

    @Override
    public Node<Person> getLast() {
        return heads.isEmpty() ? null : heads.get(heads.size() - 1);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    public List<Node<Person>> getHeads() {
        return heads;
    }

    public Set<String> getRootNodes() {
        Set<String> roots = new HashSet<>();
        for (Node<Person> head : heads) {
            roots.add(head.val.getName());
        }
        return Collections.unmodifiableSet(roots);
    }

    public void addChild(Node<Person> child, String parentName) {
        if (child == null || parentName == null) {
            return;
        }

        // Search through all root trees for the parent
        for (Node<Person> root : heads) {
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
        for (Node<Person> root : heads) {
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
