package com.lexicalscope.svm.partition.trace.symb.search2;

import java.util.ArrayList;
import java.util.Iterator;

import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.search2.StatesCollection;
import com.lexicalscope.svm.search2.TraceTreeSideObserver;
import com.lexicalscope.svm.vm.j.JState;

public class ArrayListStatesCollection implements StatesCollection {
    private final ArrayList<JState> states = new ArrayList<>();
    private final TraceTreeSideObserver listener;
    private final Randomiser randomiser;

    public ArrayListStatesCollection(
            final Randomiser randomiser,
            final TraceTreeSideObserver listener) {
        this.randomiser = randomiser;
        this.listener = listener;
    }

    @Override public Iterator<JState> iterator() {
        return states.iterator();
    }

    @Override public void add(final JState state) {
        final boolean empty = states.isEmpty();
        states.add(state);
        if(empty) {
            listener.stateAvailable();
        }
    }

    private int size() {
        return states.size();
    }

    private JState remove(final int i) {
        final JState result = states.remove(i);
        if(isEmpty()) {
            listener.stateUnavailable();
        }
        return result;
    }

    private boolean isEmpty() {
        return states.isEmpty();
    }

    @Override public JState pickState() {
        return remove(randomiser.random(size()));
    }
}
