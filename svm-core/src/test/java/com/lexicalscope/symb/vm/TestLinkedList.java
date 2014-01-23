package com.lexicalscope.symb.vm;

import static com.lexicalscope.symb.vm.StateMatchers.normalTerminiationWithResult;
import static com.lexicalscope.symb.vm.conc.VmFactory.concreteVm;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.lexicalscope.symb.vm.conc.MethodInfo;

public class TestLinkedList {
   private final MethodInfo linkedListAddRemove = new MethodInfo(StaticLinkedList.class, "addRemove", "(I)I");

   @Test public void linkedListAddThenGet() {
      final Vm<State> vm = concreteVm(linkedListAddRemove, 4);
      assertThat(vm.execute(), normalTerminiationWithResult(4));
   }
}
