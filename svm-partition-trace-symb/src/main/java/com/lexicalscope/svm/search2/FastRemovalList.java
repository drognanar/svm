package com.lexicalscope.svm.search2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Array list with an O(1) remove operation.
 * @param <T>
 */
public class FastRemovalList<T> implements Collection<T> {
    /**
     * This is the list with all of the elements.
     */
    private FastListItem<T>[] arrayList = new FastListItem[1];

    private int size;

    /**
     * This map contains indices where the elements are stored in a arrayList.
     */
    private HashMap<T, Integer> indices = new HashMap<>();

    public FastListLocation addItem(T el) {
        ensureCapacity();
        int index = this.size;
        FastListItem<T> newItem = new FastListItem<>(el, index);
        arrayList[index] = newItem;
        size++;
        return newItem.location;
    }


    public boolean add(T el) {
        addItem(el);
        return true;
    }

    private void ensureCapacity() {
        if (size == arrayList.length) {
            arrayList = Arrays.copyOf(arrayList, arrayList.length * 2);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        arrayList = new FastListItem[1];
        size = 0;
        indices.clear();
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("Not implemented");
    }

    public T remove(FastListLocation location) {
        return remove(location.getLocation());
    }

    public T remove(int index) {
        FastListItem<T> removedItem = arrayList[index];
        if (index < size - 1) {
            FastListItem<T> lastItem = arrayList[size - 1];
            arrayList[index] = lastItem;
            arrayList[size - 1] = null;
            lastItem.location.setLocation(index);
        } else {
            arrayList[index] = null;
        }
        size--;
        return removedItem.item;
    }

    public T get(int index) {
        return arrayList[index].item;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indices.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(arrayList[i].item);
        }
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

}
