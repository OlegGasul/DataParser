package com.testtask.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortedList<E> extends AbstractList<E> {
    private Comparable comparable = null;
    private ArrayList<E> internalList = new ArrayList<E>();

    @Override
    public void add(int position, E e) {
        internalList.add(e);
        Collections.sort(internalList, (Comparator<? super E>) comparable);
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

    public SortedList(Comparable comparable) {
        this.comparable = comparable;
    }
}