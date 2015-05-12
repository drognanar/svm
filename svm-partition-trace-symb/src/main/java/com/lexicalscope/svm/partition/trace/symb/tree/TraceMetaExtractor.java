package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.lexicalscope.svm.j.instruction.concrete.object.PartitionTagMetaKey.PARTITION_TAG;
import static com.lexicalscope.svm.j.instruction.symbolic.PcMetaKey.PC;
import static com.lexicalscope.svm.partition.trace.TraceMetaKey.TRACE;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.HashTrace;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.search.SearchMetaExtractor;
import com.lexicalscope.svm.vm.j.JState;

public class TraceMetaExtractor implements SearchMetaExtractor {
   private final Object initialTag = new Object(){ @Override public String toString() { return "InitialTag";}};

   @Override public Trace goal(final JState state) {
      return state.getMeta(TRACE);
   }

   @Override public BoolSymbol pc(final JState state) {
      return state.getMeta(PC);
   }

   @Override public void configureInitial(final JState state) {
      state.setFrameMeta(PARTITION_TAG, initialTag);
      state.setMeta(TRACE, new HashTrace());
   }
}
