package com.lineagetool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Service class for managing a lineage tree using a doubly linked list structure.
 * This class provides methods to add, remove, and retrieve nodes, as well as
 * to print the lineage of a specific node.
 */
public class LineageService implements DoublyLinkedList<Node<Person>> {
    private int size = 0;
    private Node<Person> head;
    private Node<Person> tail;

    /**
     * Constructs an empty lineage service with no nodes.
     */
    public LineageService() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds a node as the first element in the lineage.
     *
     * @param data The {@code Node<Person>} to add as the first element.
     */
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

    /**
     * Adds a node as the last element in the lineage.
     *
     * @param data The {@code Node<Person>} to add as the last element.
     */
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

    /**
     * Removes and returns the first node in the lineage.
     *
     * @return The removed {@code Node<Person>} or {@code null} if the lineage is empty.
     */
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

    /**
     * Removes and returns the last node in the lineage.
     *
     * @return The removed {@code Node<Person>} or {@code null} if the lineage is empty.
     */
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

    /**
     * Retrieves the first node in the lineage.
     *
     * @return The first {@code Node<Person>} or {@code null} if the lineage is empty.
     */
    @Override
    public Node<Person> getFirst() {
        return head;
    }

    /**
     * Retrieves the last node in the lineage.
     *
     * @return The last {@code Node<Person>} or {@code null} if the lineage is empty.
     */
    @Override
    public Node<Person> getLast() {
        return tail;
    }

    /**
     * Checks if the lineage is empty.
     *
     * @return {@code true} if the lineage is empty, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the size of the lineage.
     *
     * @return The number of nodes in the lineage.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Adds a child node to a specified parent node in the lineage.
     *
     * @param child  The {@code Node<Person>} to add as a child.
     * @param father The name of the parent node to which the child will be added.
     */
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

    /**
     * Retrieves a node by its name using a breadth-first search.
     *
     * @param name The name of the node to retrieve.
     * @return The {@code Node<Person>} with the specified name, or {@code null} if not found.
     */
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

    /**
     * Prints the lineage of a specified node, including its parents and children.
     *
     * @param node The {@code Node<Person>} whose lineage will be printed.
     */
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