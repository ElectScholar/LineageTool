package com.lineagetool;

public class LineageService implements DoublyLinkedList<Node> {
    private int size = 0;
    private Node head;
    private Node tail;

    public LineageService() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void addFirst(Node data) {
        if (data == null) return;
        data.next = head;
        if (head != null) head.prev = data;
        head = data;
        if (tail == null) tail = head;
        size++;
    }

    @Override
    public void addLast(Node data) {
        if (data == null) return;
        if (tail == null) {
            head = tail = data;
        } else {
            tail.next = data;
            data.prev = tail;
            tail = data;
        }
        size++;
    }

    @Override
    public Node removeFirst() {
        if (head == null) return null;
        Node removed = head;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null; // If list is empty, tail should also be null
        size--;
        return removed;
    }

    @Override
    public Node removeLast() {
        if (tail == null) return null;
        Node removed = tail;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null; // If list is empty, head should also be null
        size--;
        return removed;
    }

    @Override
    public Node getFirst() {
        return head;
    }

    @Override
    public Node getLast() {
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
}
