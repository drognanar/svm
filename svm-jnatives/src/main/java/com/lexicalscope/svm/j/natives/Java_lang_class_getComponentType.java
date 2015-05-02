package com.lexicalscope.svm.j.natives;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.j.instruction.concrete.object.GetComponentClassOp;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.MethodBody;

public class Java_lang_class_getComponentType extends AbstractNativeMethodDef {
   public Java_lang_class_getComponentType() {
      super(internalName("java/lang/Class"), "getComponentType", "()Ljava/lang/Class;");
   }
   @Override public MethodBody instructions(final InstructionSource instructions) {
      return statements(instructions)
               .maxStack(1)
               .maxLocals(1)
               .aload(0)
               .linearOp(new GetComponentClassOp())
               .return1(name()).build();
   }
}
