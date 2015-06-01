package com.lexicalscope.svm.metastate;

import java.util.HashMap;
import java.util.Map;

public class HashMetaState implements SnapshotableMetaState {
   // TODO[tim]: use a map that has better sharing characteristics
   private final Map<MetaKey<?>, Object> meta;

   public HashMetaState() {
      this(new HashMap<MetaKey<?>, Object>());
   }

   private HashMetaState(final HashMap<MetaKey<?>, Object> meta) {
      this.meta = meta;
   }

   @Override public HashMetaState snapshot() {
      return new HashMetaState(new HashMap<>(meta));
   }

   @Override
   public <T> T get(final MetaKey<T> key) {
      return key.valueType().cast(meta.get(key));
   }

   @Override public <T> void set(final MetaKey<T> key, final T value) {
      meta.put(key, value);
   }

   @Override public boolean contains(final MetaKey<?> key) {
      return meta.containsKey(key);
   }

   @Override public void remove(final MetaKey<?> key) {
      meta.remove(key);
   }

   @Override public String toString() {
      return meta.toString();
   }
}
