package com.lexicalscope.svm.partition.trace.ops;

import static org.hamcrest.Matchers.any;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.InstructionInternal;
import com.lexicalscope.svm.j.instruction.LinearOp;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.instrumentation.CompositeMethodInstrumentor;
import com.lexicalscope.svm.j.instruction.instrumentation.InstructionInstrumentor;
import com.lexicalscope.svm.j.instruction.instrumentation.MethodInstrumentor;
import com.lexicalscope.svm.j.instruction.instrumentation.finders.FindConstructorCall;
import com.lexicalscope.svm.partition.spec.CallContext;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.InstructionCode;
import com.lexicalscope.svm.vm.j.InstructionQueryAdapter;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class TrackPartitionAtNew implements MethodInstrumentor {
   private final static Object aPart = new Object(){ @Override public String toString() { return "aPart"; }};
   private final static Object uPart = new Object(){ @Override public String toString() { return "uPart"; }};

   private final MatcherPartition partition;

   public TrackPartitionAtNew(final MatcherPartition partition) {
      this.partition = partition;
   }

   public static MethodInstrumentor instanciationOf(
         final Matcher<? super CallContext> aPartNewInstanceMatcher,
         final Matcher<? super CallContext> uPartNewInstanceMatcher) {
      return new TrackPartitionAtNew(matcherPartition(aPartNewInstanceMatcher, uPartNewInstanceMatcher));
   }

   private static MatcherPartition matcherPartition(final Matcher<? super CallContext> aPartNewInstanceMatcher, final Matcher<? super CallContext> uPartNewInstanceMatcher) {
      return new MatcherPartition(
            aPartNewInstanceMatcher,
            uPartNewInstanceMatcher,
            aPart,
            uPart);
   }

   public static MethodInstrumentor constructionOf(
         final Matcher<? super CallContext> aPartNewInstanceMatcher,
         final Matcher<? super CallContext> uPartNewInstanceMatcher) {
      final MatcherPartition partition = matcherPartition(aPartNewInstanceMatcher, uPartNewInstanceMatcher);
      return new CompositeMethodInstrumentor(
               new FindConstructorCall(any(KlassInternalName.class)),
               new InstructionInstrumentor(){
                  @Override public void candidate(final Instruction instruction) {
                     final Vop tagger = instruction.query(new InstructionQueryAdapter<Vop>(){
                        @Override public Vop invokespecial(final SMethodDescriptor methodName, final MethodArguments methodArguments) {
                           return new TagConstructorRecieverWithPartition(methodName, methodArguments, partition);
                        }
                     });
                     // TODO[tim]:make it easier to manufacture instructions
                     assert tagger != null;
                     instruction.insertHere(new InstructionInternal(new LinearOp(tagger), InstructionCode.synthetic));
                  }
               });
   }

   @Override public Instruction instrument(
         final InstructionSource instructions,
         final SMethodDescriptor method,
         final Instruction methodEntry) {
      for (final Instruction instruction : methodEntry) {
         instruction.query(new InstructionQueryAdapter<Void>() {
            @Override public Void newobject(final KlassInternalName klassDesc) {
               final Vop op = new TagNewInstanceWithPartitionOp(
                     klassDesc,
                     instruction.op(),
                     partition);

               instruction.replaceOp(op);
               return null;
            }
         });
      }

      return methodEntry;
   }
}
