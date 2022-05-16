package com.lan.town;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pool<E> {

    protected Object[] array;

    public Pool(int size) {

        array = new Object[size];

    }

    public boolean addAll(int index, Collection<? extends E> c) {

        Object[] a = c.toArray();

        for (int i = 0; i < a.length; i++, index++) {

            array[index] = a[i];

        }

        return true;

    }

    public List<E> subList(int fromIndex, int toIndex) {

        List<E> temp = new ArrayList<>(toIndex - fromIndex);

        for (; fromIndex < toIndex && fromIndex < array.length; fromIndex++) {

            temp.add((E) array[fromIndex]);

        }

        return temp;

    }

    public E get(int index) {

        Object temp = array[index];

        return temp != null ? (E) temp : null;

    }

    public E getOrDefault(int index, E def) {

        Object temp = array[index];

        return temp != null ? (E) temp : def;

    }

    public void set(int index, E value) {

        array[index] = value;

    }
}
