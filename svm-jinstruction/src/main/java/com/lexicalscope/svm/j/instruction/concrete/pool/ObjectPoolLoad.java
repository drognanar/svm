package com.lexicalscope.svm.j.instruction.concrete.pool;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.j.instruction.concrete.klass.DefineClassOp;
import org.objectweb.asm.Type;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Vop;

public class ObjectPoolLoad implements Vop {
   private final KlassInternalName internalName;

   public ObjectPoolLoad(final Type type) {
      this(internalName(type.getInternalName()));
   }

   public ObjectPoolLoad(final KlassInternalName internalName) {
      this.internalName = internalName;
   }

   @Override public void eval(final JState ctx) {
      new DefineClassOp(internalName).eval(ctx);
      ctx.push(ctx.whereMyClassAt(internalName));
   }

   @Override public String toString() {
      return "LDC object " + internalName;
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.objectpoolload();
   }
}
