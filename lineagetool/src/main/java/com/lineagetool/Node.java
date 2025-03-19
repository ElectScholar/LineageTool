package com.lineagetool;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in a lineage tree.
 * Each node contains a reference to a person, as well as lists of child and parent nodes.
 *
 * @param <T> The type of the person stored in the node. Must extend {@code Person}.
 * @author ElectScholar
 */
public class Node<T extends Person> {
    /**
     * The person represented by this node.
     */
    public T val;

    /**
     * A list of child nodes connected to this node.
     */
    public List<Node<T>> next;

    /**
     * A list of parent nodes connected to this node.
     */
    public List<Node<T>> prev;

    /**
     * Constructs a new node with the specified person and initializes empty child and parent lists.
     *
     * @param person The person represented by this node. Must not be {@code null}.
     */
    public Node(T person) {
        this.val = person;
        this.next = new ArrayList<>();
        this.prev = new ArrayList<>();
    }

    /**
     * Constructs a new node with the specified person and parent nodes.
     * The child list is initialized as empty.
     *
     * @param val  The person represented by this node. Must not be {@code null}.
     * @param prev A list of parent nodes. Can be empty but must not be {@code null}.
     */
    public Node(T val, List<Node<T>> prev) {
        this.val = val;
        this.next = new ArrayList<>();
        this.prev = prev;
    }

    /**
     * Constructs a new node with the specified person, child nodes, and parent nodes.
     *
     * @param val  The person represented by this node. Must not be {@code null}.
     * @param next A list of child nodes. Can be empty but must not be {@code null}.
     * @param prev A list of parent nodes. Can be empty but must not be {@code null}.
     */
    public Node(T val, List<Node<T>> next, List<Node<T>> prev) {
        this.val = val;
        this.next = next;
        this.prev = prev;
    }
}