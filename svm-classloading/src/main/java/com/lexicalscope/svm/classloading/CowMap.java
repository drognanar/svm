package com.lexicalscope.svm.classloading;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CowMap<K, V> implements Map<K, V> {
    private MapInternal internal;

    public CowMap() {
        this(new HashMap<K, V>());
    }

    public CowMap(Map<K, V> delegate) {
        this.internal = new MapInternal(delegate);
    }

    public CowMap(CowMap<K, V> map) {
        internal = map.internal;
        internal.shared = true;
    }

    @Override
    public int size() {
        return internal.delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internal.delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return internal.delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return internal.delegate.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (internal.shared) {
            copy();
        }
        return internal.delegate.put(key, value);
    }

    @Override
    public V remove(Object key) {
        if (internal.shared) {
            copy();
        }
        return internal.delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (internal.shared) {
            copy();
        }
        internal.delegate.putAll(m);
    }

    @Override
    public void clear() {
        if (internal.shared) {
            copy();
        }
        internal.delegate.clear();
    }

    @Override
    public Set<K> keySet() {
        return internal.delegate.keySet();
    }

    @Override
    public Collection<V> values() {
        return internal.delegate.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return internal.delegate.entrySet();
    }

    private void copy() {
        internal = new MapInternal(new HashMap<>(internal.delegate));
    }

    public class MapInternal {
        public boolean shared = false;
        public Map<K, V> delegate;

        public MapInternal(Map<K, V> delegate) {
            this.delegate = delegate;
        }
    }
}
