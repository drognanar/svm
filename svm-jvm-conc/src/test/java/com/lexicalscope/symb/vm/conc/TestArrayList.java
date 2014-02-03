package com.lexicalscope.symb.vm.conc;

import static com.lexicalscope.symb.vm.j.StateMatchers.normalTerminiationWithResult;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.LinkedList;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.symb.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.symb.vm.conc.junit.VmRule;

public class TestArrayList {
   @TestEntryPoint public static int arrayListAddRemove(final int x) {
      final LinkedList<Integer> list = new LinkedList<>();
      list.add(x);
      return list.removeFirst();
   }

   @Rule public final VmRule vm = new VmRule();

   @Test public void linkedListAddThenGet() {
      assertThat(vm.execute(4), normalTerminiationWithResult(4));
   }
}
