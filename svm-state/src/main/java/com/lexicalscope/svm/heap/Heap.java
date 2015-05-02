package com.lexicalscope.svm.heap;

import com.lexicalscope.svm.state.Snapshotable;


public interface Heap extends Snapshotable<Heap> {
   ObjectRef newObject(Allocatable klass);

   void put(ObjectRef address, int offset, Object val);
   Object get(ObjectRef address, int offset);

   ObjectRef nullPointer();

   /**
    * Get a hashCode for the given address
    */
   int hashCode(ObjectRef address);
}