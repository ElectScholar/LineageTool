package com.lineagetool;

public class Node<T extends Person> {

    public T val = null;
    public Node next = null;
    public Node prev = null;

    public Node(T person){
        this.val = person;
    }
    public Node(T val, Node prev){
        this.val = val;
        this.prev = prev;
    }

    public Node(T val, Node next, Node prev){
        this.val = val;
        this.next = next;
        this.prev = prev;
    }

}
