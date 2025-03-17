package com.lineagetool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class LineageService implements DoublyLinkedList<Node<Person>> {
    private int size = 0;
    private Node<Person> head;
    private Node<Person> tail;

    public LineageService() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void addFirst(Node<Person> data) {
        if (data == null) return;
        
        if (head != null) {
            data.next.add(head);
            head.prev.add(data);
        }
        
        head = data;
        if (tail == null) tail = head;
        size++;
    }

    @Override
    public void addLast(Node<Person> data) {
        if (data == null) return;
        
        if (tail != null) {
            tail.next.add(data);
            data.prev.add(tail);
        }

        tail = data;
        if (head == null) head = tail;
        size++;
    }

    @Override
    public Node<Person> removeFirst() {
        if (head == null) return null;
        
        Node<Person> removed = head;
        if (!head.next.isEmpty()) {
            head = head.next.get(0); // Move to the first child (if any)
            head.prev.clear();
        } else {
            head = null;
            tail = null;
        }
        
        size--;
        return removed;
    }

    @Override
    public Node<Person> removeLast() {
        if (tail == null) return null;
        
        Node<Person> removed = tail;
        if (!tail.prev.isEmpty()) {
            tail = tail.prev.get(0); // Move to first parent (if any)
            tail.next.clear();
        } else {
            head = null;
            tail = null;
        }
        
        size--;
        return removed;
    }

    @Override
    public Node<Person> getFirst() {
        return head;
    }

    @Override
    public Node<Person> getLast() {
        return tail;
    }

    @Override
    public boolean isEmpty() {

        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

public void addChild(Node<Person> child, String father) {
    Node<Person> curr = head;
    
    while (curr != null) {
        if (curr.val.getName().equals(father)) {
            if (curr.next == null) {
                curr.next = new ArrayList<>(); // Ensure list exists
            }
            curr.next.add(child);

            if (child.prev == null) {
                child.prev = new ArrayList<>(); // Ensure list exists
            }
            child.prev.add(curr);

            System.out.println("Added child: " + child.val.getName() + " to father: " + father);
            return; // Exit once the father is found
        }
        curr = curr.next.get(0); // Move to next node in the list
    }

    System.out.println("addChild() could not find father: " + father + " so unable to add " + child.val.getName());
}


public Node<Person> getNode(String name) {
    if (head == null) return null; // Handle empty tree

    Queue<Node<Person>> queue = new LinkedList<>();
    queue.add(head); // Start from the root node

    while (!queue.isEmpty()) {
        Node<Person> current = queue.poll();
        
        if (current.val.getName().equals(name)) {
            return current; // Found the node
        }
        
        queue.addAll(current.next); // Add all children to queue for further searching
    }

    System.out.println("Cannot find node: " + name);
    return null; // Node not found
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
