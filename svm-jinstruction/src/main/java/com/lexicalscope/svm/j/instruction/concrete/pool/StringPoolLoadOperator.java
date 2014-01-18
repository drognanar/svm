package com.lexicalscope.svm.j.instruction.concrete.pool;

import static com.lexicalscope.symb.vm.JavaConstants.STRING_CLASS;
import static org.objectweb.asm.Type.getInternalName;

import com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp;
import com.lexicalscope.svm.j.instruction.concrete.array.NewConcArray;
import com.lexicalscope.svm.j.instruction.concrete.object.NewObjectOp;
import com.lexicalscope.symb.klass.SClass;
import com.lexicalscope.symb.klass.SFieldName;
import com.lexicalscope.symb.vm.State;
import com.lexicalscope.symb.vm.Vop;

public final class StringPoolLoadOperator implements Vop {
   private final String val;

   public StringPoolLoadOperator(final String val) {
      this.val = val;
   }

   @Override public void eval(final State ctx) {
      final SClass stringClass = ctx.load(STRING_CLASS);

      // create new string
      new NewObjectOp(getInternalName(String.class)).eval(ctx);
      final Object stringAddress = ctx.pop();

      // create char array
      final char[] chars = val.toCharArray();

      ctx.push(chars.length);
      new NewArrayOp(new NewConcArray()).eval(ctx);
      final Object valueAddress = ctx.pop();

      for (int i = 0; i < chars.length; i++) {
         ctx.put(valueAddress, NewArrayOp.ARRAY_PREAMBLE + i, chars[i]);
      }

      ctx.put(stringAddress, stringClass.fieldIndex(new SFieldName(STRING_CLASS, "value")), valueAddress);
      ctx.push(stringAddress);
   }

   @Override
   public String toString() {
      return "LDC string: " + new String(val);
   }
}