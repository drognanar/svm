package com.lexicalscope.svm.heap;

import com.lexicalscope.rcbittrie.BitTrie;


public class FastHeap implements Heap {
   public static FastHeap createFastHeap() {
      final BitTrie trie = new BitTrie();
      return new FastHeap(trie, new ObjectRef(trie.nullPointer()));
   }

   final ObjectRef nullPointer;
   private final BitTrie trie;

   private FastHeap(final BitTrie trie, final ObjectRef nullPointer) {
      this.trie = trie;
      this.nullPointer = nullPointer;
   }

   @Override
   public ObjectRef newObject(final Allocatable klass) {
      final int key = trie.allocate(klass.allocateSize());
      return new ObjectRef(key);
   }

   @Override
   public void put(final ObjectRef obj, final int offset, final Object val) {
      trie.insert(addressFromRef(obj) + offset, val);
   }

   @Override
   public Object get(final ObjectRef obj, final int offset) {
      assert offset >= 0;
      return trie.get(addressFromRef(obj) + offset);
   }

   private int addressFromRef(final ObjectRef obj) {
      return obj.address();
   }

   @Override public ObjectRef nullPointer() {
      return nullPointer;
   }

   @Override
   public Heap snapshot() {
      return new FastHeap(trie.copy(), nullPointer);
   }

   @Override
   public String toString() {
      return trie.toString();
   }

   @Override public int hashCode(final ObjectRef address) {
      return addressFromRef(address);
   }
}
