package com.lexicalscope.symb.vm.symb;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.OSymbol;
import com.lexicalscope.svm.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestWithSymbolicObject {
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRule();
   private @Fresh(type = SimpleExpression.class) OSymbol symbol1;

   public interface SimpleExpression {
      int type();

      int left();
      int right();
   }

   @TestEntryPoint public static int symbolicObject(final SimpleExpression expression) {
      switch (expression.type()) {
         case 0:
            return expression.left() + expression.right();
         case 1:
            return expression.left() - expression.right();
         case 2:
            return expression.left() * expression.right();
         case 3:
            return expression.left() / expression.right();
         default:
            return 0;
      }
   }

   @Test @Ignore public void createSymbolicObject() throws Exception {
      vm.execute(symbol1);
   }
}
