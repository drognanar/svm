package com.lexicalscope.svm.j.instruction.concrete.nativ3;

import static com.lexicalscope.svm.vm.j.code.AsmSMethodName.defaultConstructor;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.statementBuilder.StatementBuilder;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.JavaConstants;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class InitThreadOp implements Vop {
   @Override public void eval(final JState ctx) {
      final SClass threadClass = ctx.load(JavaConstants.THREAD_CLASS);
      final ObjectRef address = ctx.newObject(threadClass);
      ctx.put(address, SClass.OBJECT_MARKER_OFFSET, threadClass);
      ctx.currentThreadIs(address);
      ctx.push(address);
   }

   @Override public String toString() {
      return "INIT_THREAD";
   }

   public static void initThreadInstruction(final StatementBuilder statements) {
      statements
         .linearOp(new InitThreadOp())
         .createInvokeSpecial(defaultConstructor("java/lang/Thread"));
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.synthetic();
   }
}
