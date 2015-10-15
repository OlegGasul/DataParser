package com.testtask.model;

import java.util.*;

public class SortedList<E> extends AbstractList<E> {
    private Comparator comparator;
    private LinkedList<E> internalList = new LinkedList<E>();

    @Override
    public void add(int position, E e) {
        internalList.add(e);
        Collections.sort(internalList, comparator);
    }

    @Override
    public E get(int i) {
        return internalList.get(i);
    }

    @Override
    public int size() {
        return internalList.size();
    }

    public SortedList() {

    }

    public SortedList(Comparator comparator) {
        this.comparator = comparator;
    }
}