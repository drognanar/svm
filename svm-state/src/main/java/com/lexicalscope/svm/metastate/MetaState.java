package com.lexicalscope.svm.metastate;


public interface MetaState {
   <T> T get(MetaKey<T> key);
   <T> void set(MetaKey<T> key, T value);
   boolean contains(MetaKey<?> key);
   void remove(MetaKey<?> key);
}