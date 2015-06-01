package com.lexicalscope.svm.vm.j;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.heap.Heap;
import com.lexicalscope.svm.metastate.SnapshotableMetaState;
import com.lexicalscope.svm.stack.Stack;
import com.lexicalscope.svm.vm.StateSearch;

public class TestStateSnapshot {
   @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

   @Test
   public void snapshotOfStateCopiesEverythingStateful() {
      @SuppressWarnings("unchecked")
      final StateSearch<JState> stateSearch = context.mock(StateSearch.class, "StateSearch");

      final StateTag tag = new StateTag() {};

      final Statics statics = context.mock(Statics.class, "Statics");
      final Statics staticsCopy = context.mock(Statics.class, "Statics copy");

      final Stack stack = context.mock(Stack.class, "Stack");
      final Stack stackCopy = context.mock(Stack.class, "Stack copy");

      final Heap heap = context.mock(Heap.class, "Heap");
      final Heap heapCopy = context.mock(Heap.class, "Heap copy");

      final SnapshotableMetaState meta = context.mock(SnapshotableMetaState.class, "Meta");
      final SnapshotableMetaState metaCopy = context.mock(SnapshotableMetaState.class, "Meta copy");

      context.checking(new Expectations(){{
         oneOf(statics).snapshot(); will(returnValue(staticsCopy));
         oneOf(heap).snapshot(); will(returnValue(heapCopy));
         oneOf(stack).snapshot(); will(returnValue(stackCopy));
         oneOf(meta).snapshot(); will(returnValue(metaCopy));
      }});

      assertThat(new JStateImpl(tag, stateSearch, statics, stack, heap, meta).snapshot(), equalTo(new JStateImpl(tag, stateSearch, staticsCopy, stackCopy, heapCopy, metaCopy)));
   }
}
