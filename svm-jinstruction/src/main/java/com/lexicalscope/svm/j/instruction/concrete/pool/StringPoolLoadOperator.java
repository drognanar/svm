package com.lexicalscope.svm.j.instruction.concrete.pool;

import static com.lexicalscope.svm.vm.j.JavaConstants.STRING_CLASS;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp;
import com.lexicalscope.svm.j.instruction.concrete.array.NewConcArray;
import com.lexicalscope.svm.j.instruction.concrete.object.NewObjectOp;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SFieldName;

public final class StringPoolLoadOperator implements Vop {
   private final String val;

   public StringPoolLoadOperator(final String val) {
      this.val = val;
   }

   @Override public void eval(final JState ctx) {
      final SClass stringClass = ctx.loadKlassFor(STRING_CLASS);

      // create new string
      new NewObjectOp(internalName(String.class)).eval(ctx);
      final ObjectRef stringAddress = (ObjectRef) ctx.pop();

      // create char array
      final char[] chars = val.toCharArray();

      ctx.push(chars.length);
      new NewArrayOp(new NewConcArray()).eval(ctx);
      final ObjectRef valueAddress = (ObjectRef) ctx.pop();

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

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.stringpoolload();
   }
}
