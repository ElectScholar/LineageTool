package com.lineagetool;

import java.util.ArrayList;
import java.util.List;

public class Node<T extends Person> {
    public T val;
    public List<Node<T>> next; // Children
    public List<Node<T>> prev; // Parents

    public Node(T person) {
        this.val = person;
        this.next = new ArrayList<>();
        this.prev = new ArrayList<>();
    }

    public Node(T val, List<Node<T>> prev) {
        this.val = val;
        this.next = new ArrayList<>();
        this.prev = prev;
    }

    public Node(T val, List<Node<T>> next, List<Node<T>> prev) {
        this.val = val;
        this.next = next;
        this.prev = prev;
    }
}
