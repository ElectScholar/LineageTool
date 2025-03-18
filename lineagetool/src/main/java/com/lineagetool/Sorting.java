package com.lineagetool;

import java.util.ArrayList;
import java.util.List;

public class Sorting {

    public static List<Node<Person>> sortChildren(List<Node<Person>> children) {
        if (children == null || children.size() <= 1) return children; // Base case
    
        return mergeSort(children);
    }
    
    private static List<Node<Person>> mergeSort(List<Node<Person>> children) {
        int n = children.size();
        if (n <= 1) return children;
    
        int mid = n / 2;
        List<Node<Person>> left = new ArrayList<>(children.subList(0, mid));
        List<Node<Person>> right = new ArrayList<>(children.subList(mid, n));
    
        return merge(mergeSort(left), mergeSort(right));
    }
    
    private static List<Node<Person>> merge(List<Node<Person>> left, List<Node<Person>> right) {
        List<Node<Person>> sorted = new ArrayList<>();
        int i = 0, j = 0;
    
        while (i < left.size() && j < right.size()) {
            if (left.get(i).val.getName().compareTo(right.get(j).val.getName()) <= 0) {
                sorted.add(left.get(i++));
            } else {
                sorted.add(right.get(j++));
            }
        }
    
        while (i < left.size()) sorted.add(left.get(i++));
        while (j < right.size()) sorted.add(right.get(j++));
    
        return sorted;
    }
    
}
