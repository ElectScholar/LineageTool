package com.lineagetool;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for sorting nodes in a lineage tree.
 * This class provides methods to sort a list of child nodes based on their names
 * using the merge sort algorithm.
 */
public class Sorting {

    /**
     * Sorts a list of child nodes alphabetically by their names.
     *
     * @param children A list of {@code Node<Person>} objects representing the children to be sorted.
     *                 Each node contains a {@code Person} object with a name.
     * @return A sorted list of {@code Node<Person>} objects in alphabetical order by name.
     *         If the input list is null or contains one or fewer elements, it is returned as is.
     */
    public static List<Node<Person>> sortChildren(List<Node<Person>> children) {
        if (children == null || children.size() <= 1) return children; // Base case
    
        return mergeSort(children);
    }

    /**
     * Recursively sorts a list of nodes using the merge sort algorithm.
     *
     * @param children A list of {@code Node<Person>} objects to be sorted.
     * @return A sorted list of {@code Node<Person>} objects.
     */
    private static List<Node<Person>> mergeSort(List<Node<Person>> children) {
        int n = children.size();
        if (n <= 1) return children;
    
        int mid = n / 2;
        List<Node<Person>> left = new ArrayList<>(children.subList(0, mid));
        List<Node<Person>> right = new ArrayList<>(children.subList(mid, n));
    
        return merge(mergeSort(left), mergeSort(right));
    }

    /**
     * Merges two sorted lists of nodes into a single sorted list.
     *
     * @param left  A sorted list of {@code Node<Person>} objects.
     * @param right A sorted list of {@code Node<Person>} objects.
     * @return A merged and sorted list of {@code Node<Person>} objects.
     */
    private static List<Node<Person>> merge(List<Node<Person>> left, List<Node<Person>> right) {
        List<Node<Person>> sorted = new ArrayList<>();
        int i = 0, j = 0;
    
        // Compare elements from both lists and add the smaller one to the sorted list
        while (i < left.size() && j < right.size()) {
            if (left.get(i).val.getName().compareTo(right.get(j).val.getName()) <= 0) {
                sorted.add(left.get(i++));
            } else {
                sorted.add(right.get(j++));
            }
        }
    
        // Add any remaining elements from the left list
        while (i < left.size()) sorted.add(left.get(i++));
        // Add any remaining elements from the right list
        while (j < right.size()) sorted.add(right.get(j++));
    
        return sorted;
    }
}