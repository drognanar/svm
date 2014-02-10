package com.lexicalscope.svm.partition.trace;

import static com.lexicalscope.svm.partition.trace.CrossingCallMetaKey.CROSSINGCALL;
import static com.lexicalscope.svm.partition.trace.Trace.CallReturn.RETURN;
import static com.lexicalscope.svm.partition.trace.TraceMetaKey.TRACE;

import com.lexicalscope.svm.metastate.MetaFactory;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.State;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class TraceCallbackReturnOp implements Vop {
   private final SMethodDescriptor methodName;

   public TraceCallbackReturnOp(final SMethodDescriptor methodName) {
      this.methodName = methodName;
   }

   @Override public void eval(final State ctx) {
      if(ctx.currentFrame().containsMeta(CROSSINGCALL)) {
         ctx.currentFrame().removeMeta(CROSSINGCALL);
//         ctx.setMeta(TRACE, ctx.getMeta(TRACE).extend(methodName, RETURN, ctx.peek(methodName.returnCount())));
         ctx.replaceMeta(TRACE, new MetaFactory<Trace>(){
            @Override public Trace replacement(final Trace original) {
               return original.extend(methodName, RETURN, ctx.peek(methodName.returnCount()));
            }
         });
      }
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.synthetic();
   }
}