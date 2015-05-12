package com.lexicalscope.svm.partition.trace.symb.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.partition.trace.symb.search.FakeVmState;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.symb.junit.SolverRule;

public class TestOpenNodes {
   @Rule public final SolverRule solver = new SolverRule();
   final OpenNodes openNodes = new OpenNodes(solver.checker());

   private final JState node1 = new FakeVmState();

   @Test public void singlePushedNodeIsPopped() throws Exception {
      openNodes.push(node1);
      assertThat(openNodes.pop(), equalTo(node1));
   }
}
