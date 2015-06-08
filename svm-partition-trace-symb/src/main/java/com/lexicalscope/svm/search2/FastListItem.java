package com.lexicalscope.svm.search2;

public class FastListItem<T> {
    public T item;
    public FastListLocation location;

    public FastListItem(T item, int index) {
        this.item = item;
        this.location = new FastListLocation(index);
    }
}
