package com.lineagetool;
/**
 * A generic interface representing a doubly linked list.
 * This interface defines the core operations that a doubly linked list should support.
 *
 * @param <T> the type of elements stored in the list
 * @author ElectScholar
 */
public interface DoublyLinkedList<T> {

    /**
     * Adds an element to the beginning of the list.
     *
     * @param data the element to add
     */
    void addFirst(T data);

    /**
     * Adds an element to the end of the list.
     *
     * @param data the element to add
     */
    void addLast(T data);

    /**
     * Removes and returns the first element of the list.
     *
     * @return the removed element, or {@code null} if the list is empty
     */
    T removeFirst();

    /**
     * Removes and returns the last element of the list.
     *
     * @return the removed element, or {@code null} if the list is empty
     */
    T removeLast();

    /**
     * Retrieves the first element of the list without removing it.
     *
     * @return the first element, or {@code null} if the list is empty
     */
    T getFirst();

    /**
     * Retrieves the last element of the list without removing it.
     *
     * @return the last element, or {@code null} if the list is empty
     */
    T getLast();

    /**
     * Checks whether the list is empty.
     *
     * @return {@code true} if the list is empty, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Returns the number of elements in the list.
     *
     * @return the size of the list
     */
    int size();
}

