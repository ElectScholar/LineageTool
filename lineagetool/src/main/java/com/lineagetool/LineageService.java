package com.lineagetool;

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
        data.next = head;
        if (head != null) head.prev = data;
        head = data;
        if (tail == null) tail = head;
        size++;
    }

    @Override
    public void addLast(Node<Person> data) {
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
    public Node<Person> removeFirst() {
        if (head == null) return null;
        Node<Person> removed = head;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null; // If list is empty, tail should also be null
        size--;
        return removed;
    }

    @Override
    public Node<Person> removeLast() {
        if (tail == null) return null;
        Node<Person> removed = tail;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null; // If list is empty, head should also be null
        size--;
        return removed;
    }

    @Override
    public Node<Person> getFirst() {
        return head;
    }

    public Node<Person> getNode(String name){
        while (head != null){
            if (head.val.getName() == name) return head;
            head = head.next;
        }
        System.out.println("Cant find node: " + name);
        return null;
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

    public void toString(Node<Person> node){
        String person = node.val.getName();
        System.out.println(person + " son of " + node.prev.val.getName());
        System.out.print(person + " father of " + node.next.val.getName());
    }
}
