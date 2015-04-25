package com.lexicalscope.svm.vm;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class TestVmImpl {
   @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
   @Mock private StateSearch<VmState> search;
   @Mock private SearchLimits limits;

   @Mock protected VmState state0;
   @Mock protected VmState stateR;

   @Test public void pendingStatesAreEvaluatedWhileAvailable() {
      context.checking(new Expectations(){{
         oneOf(limits).reset();
         oneOf(search).pendingState(); will(returnValue(state0));
         oneOf(limits).withinLimits(); will(returnValue(true));
         oneOf(state0).eval();
         oneOf(limits).searchedState();
         oneOf(search).pendingState(); will(returnValue(null));
         oneOf(search).firstResult(); will(returnValue(stateR));
      }});

      new VmImpl<VmState>(search, limits).execute();
   }

   @Test public void terminationAtLeafButSearchContinues() {
      context.checking(new Expectations(){{
         oneOf(limits).reset();
         oneOf(search).pendingState(); will(returnValue(state0));
         oneOf(limits).withinLimits(); will(returnValue(true));
         oneOf(state0).eval(); will(throwException(new TerminationException()));
         oneOf(search).reachedLeaf();
         oneOf(limits).searchedState();
         oneOf(search).pendingState(); will(returnValue(null));
         oneOf(search).firstResult(); will(returnValue(stateR));
      }});

      new VmImpl<VmState>(search, limits).execute();
   }

   @Test public void limitsStopSearch() {
      context.checking(new Expectations(){{
         oneOf(limits).reset();
         oneOf(search).pendingState(); will(returnValue(state0));
         oneOf(limits).withinLimits(); will(returnValue(false));
         oneOf(search).firstResult(); will(returnValue(stateR));
      }});

      new VmImpl<VmState>(search, limits).execute();
   }
}
