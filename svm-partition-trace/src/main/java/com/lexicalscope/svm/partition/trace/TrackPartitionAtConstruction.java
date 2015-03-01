package com.lexicalscope.svm.partition.trace;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.instrumentation.Instrumentor;
import com.lexicalscope.svm.partition.spec.CallContext;
import com.lexicalscope.svm.partition.trace.ops.NewInstanceVariablePartitionOp;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class TrackPartitionAtConstruction implements Instrumentor {
   private final Object aPart = new Object(){ @Override public String toString() { return "aPart"; }};
   private final Object uPart = new Object(){ @Override public String toString() { return "uPart"; }};

   private final Matcher<? super CallContext> aPartNewInstanceMatcher;
   private final Matcher<? super CallContext> uPartNewInstanceMatcher;

   public TrackPartitionAtConstruction(
         final Matcher<? super CallContext> aPartNewInstanceMatcher,
         final Matcher<? super CallContext> uPartNewInstanceMatcher) {
      this.aPartNewInstanceMatcher = aPartNewInstanceMatcher;
      this.uPartNewInstanceMatcher = uPartNewInstanceMatcher;
   }

   public static Instrumentor constructionOf(final PartitionBuilder aPart, final PartitionBuilder uPart) {
      return constructionOf(
            aPart.newInstanceMatcher(),
            uPart.newInstanceMatcher());
   }

   public static Instrumentor constructionOf(
         final Matcher<? super CallContext> aPartNewInstanceMatcher,
         final Matcher<? super CallContext> uPartNewInstanceMatcher) {
      return new TrackPartitionAtConstruction(aPartNewInstanceMatcher, uPartNewInstanceMatcher);
   }

   @Override public Instruction instrument(
         final InstructionSource instructions,
         final SMethodDescriptor method,
         final Instruction methodEntry) {
      for (final Instruction instruction : methodEntry) {
         instruction.query(new InstructionQueryAdapter<Void>() {
            @Override public Void newobject(final String klassDesc) {
               final Vop op = new NewInstanceVariablePartitionOp(
                     klassDesc,
                     instruction.op(),
                     aPartNewInstanceMatcher,
                     uPartNewInstanceMatcher,
                     aPart,
                     uPart);

               instruction.replaceOp(op);
               return null;
            }
         });
      }

      return methodEntry;
   }
}
