package com.lexicalscope.svm.classloading;

import static com.lexicalscope.svm.vm.j.InstructionCode.*;
import static com.lexicalscope.svm.vm.j.InstructionMatchers.instructionSequence;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;
import com.lexicalscope.svm.vm.j.klass.SMethod;

public class TestMethodLinking {
   private final SClassLoader sClassLoader = new AsmSClassLoader();
   private final SMethod withTwoReturns = sClassLoader.
         load(MethodWithTwoReturns.class).
         declaredMethod(new AsmSMethodName(MethodWithTwoReturns.class, "twoReturns", "(I)I"));

   public static class MethodWithTwoReturns {
      public int twoReturns(final int x) {
         if(3 == x) {
            return 1;
         }
         return 2;
      }
   }

   @Test public void linkedMethodIsTree() throws Exception {
      final Instruction entry = withTwoReturns.entry();
      assertThat(entry, instructionSequence(
            methodentry,
            iconst_3,
            iload,
            ificmpne,
            iconst_1,
            return1,
            iconst_2,
            return1,
            methodexit));
   }
}
