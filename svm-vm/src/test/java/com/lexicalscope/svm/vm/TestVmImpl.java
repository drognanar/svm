package com.lexicalscope.svm.vm;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class TestVmImpl {
   @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

   @Auto private Sequence limitsOrder;

   @Mock private StateSearch<VmState> search;
   @Mock private SearchLimits limits;

   @Mock protected VmState state0;
   @Mock protected VmState stateR;

   private final Action callTermination = new Action() {
      @Override
      public Object invoke(Invocation invocation) throws Throwable {
         search.reachedLeaf();
         return null;
      }

      @Override
      public void describeTo(Description description) {

      }
   };

   @Test public void pendingStatesAreEvaluatedWhileAvailable() {
      context.checking(new Expectations(){{
         oneOf(limits).reset(); inSequence(limitsOrder);
         oneOf(search).pendingState(); will(returnValue(state0));
         oneOf(limits).withinLimits(); will(returnValue(true)); inSequence(limitsOrder);
         oneOf(state0).eval();
         oneOf(limits).searchedState(); inSequence(limitsOrder);
         oneOf(search).pendingState(); will(returnValue(null));
         oneOf(limits).done(); inSequence(limitsOrder);
         oneOf(search).firstResult(); will(returnValue(stateR));
      }});

      new VmImpl<VmState>(search, limits).execute();
   }

   @Test public void terminationAtLeafButSearchContinues() {
      context.checking(new Expectations(){{
         oneOf(limits).reset(); inSequence(limitsOrder);
         oneOf(search).pendingState(); will(returnValue(state0));
         oneOf(limits).withinLimits(); will(returnValue(true)); inSequence(limitsOrder);
         oneOf(state0).eval(); will(callTermination);
         oneOf(search).reachedLeaf();
         oneOf(limits).searchedState(); inSequence(limitsOrder);
         oneOf(search).pendingState(); will(returnValue(null));
         oneOf(limits).done(); inSequence(limitsOrder);
         oneOf(search).firstResult(); will(returnValue(stateR));
      }});

      new VmImpl<VmState>(search, limits).execute();
   }

   @Test public void limitsStopSearch() {
      context.checking(new Expectations(){{
         oneOf(limits).reset(); inSequence(limitsOrder);
         oneOf(search).pendingState(); will(returnValue(state0));
         oneOf(limits).withinLimits(); will(returnValue(false)); inSequence(limitsOrder);
         oneOf(limits).done(); inSequence(limitsOrder);
         oneOf(search).firstResult(); will(returnValue(stateR));
      }});

      new VmImpl<VmState>(search, limits).execute();
   }
}
