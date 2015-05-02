package com.lexicalscope.svm.partition.trace.ops;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;
import static com.lexicalscope.svm.partition.trace.Trace.CallReturn.*;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.instrumentation.MethodInstrumentor;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.InstructionQueryAdapter;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class CheckPartitionAtMethodEntryExit implements MethodInstrumentor {
   public static MethodInstrumentor checkPartitionAtMethodEntryExit() {
      return new CheckPartitionAtMethodEntryExit();
   }

   @Override public Instruction instrument(
         final InstructionSource instructions,
         final SMethodDescriptor method,
         final Instruction methodEntry) {
      for (final Instruction instruction : methodEntry) {
         instruction.query(new InstructionQueryAdapter<Void>() {
            @Override public Void methodentry(final SMethodDescriptor methodName) {
               instruction.insertNext(statements(instructions).
                     linearOp(new TraceMethodEntryExitOp(methodName, CALL)).
                     buildInstruction());
               return null;
            }

            @Override public Void r3turn(final SMethodDescriptor methodName, final int returnCount) {
               instruction.insertHere(statements(instructions).
                     linearOp(new TraceMethodEntryExitOp(methodName, RETURN)).
                     buildInstruction());
               return null;
            }
         });
      }

      return methodEntry;
   }
}
