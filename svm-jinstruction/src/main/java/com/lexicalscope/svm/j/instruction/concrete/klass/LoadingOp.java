package com.lexicalscope.svm.j.instruction.concrete.klass;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;
import static com.lexicalscope.svm.vm.j.code.AsmSMethodName.staticInitialiser;
import static java.util.Arrays.asList;

import java.util.List;

import com.google.common.collect.Lists;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.statementBuilder.StatementBuilder;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Op;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

/*
 * Only instructions new, getstatic, putstatic, or invokestatic can cause class loading.
 */
public class LoadingOp implements Vop {
   private final Op<List<SClass>> loader;
   private final InstructionSource instructions;

   private LoadingOp(
         final List<KlassInternalName> klassNames,
         final InstructionSource instructions) {
      this(new DefineClassOp(klassNames), instructions);
   }

   public LoadingOp(
         final Op<List<SClass>> loader,
         final InstructionSource instructions) {
      this.loader = loader;
      this.instructions = instructions;
   }

   public LoadingOp(
         final KlassInternalName klassName,
         final InstructionSource instructions) {
      this(asList(klassName), instructions);
   }

   @Override public void eval(final JState ctx) {
      final List<SClass> definedClasses = loader.eval(ctx);
      if(definedClasses.isEmpty()){
         ctx.advanceToNextInstruction();
      } else {
         final StatementBuilder replacementInstruction = statements(instructions).before(ctx.instructionNext());
         for (final SClass klass : Lists.reverse(definedClasses)) {
            if(klass.hasStaticInitialiser())
            {
               replacementInstruction
                  .createInvokeStatic(staticInitialiser(klass.name()))
                  .invokeConstructorOfClassObjects(klass.name());
            }
         }
         ctx.advanceTo(replacementInstruction.buildInstruction());
      }
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.synthetic();
   }

   @Override public String toString() {
      return "(loading " + loader + ") ";
   }
}
