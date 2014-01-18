package com.lexicalscope.svm.j.instruction.concrete.klass;

import org.objectweb.asm.tree.FieldInsnNode;

import com.lexicalscope.svm.j.instruction.factory.BaseInstructions;
import com.lexicalscope.symb.klass.SClass;
import com.lexicalscope.symb.klass.SFieldName;
import com.lexicalscope.symb.vm.State;
import com.lexicalscope.symb.vm.Vop;

public final class GetStaticOp implements Vop {
   private final FieldInsnNode fieldInsnNode;
   private final SFieldName name;

   public GetStaticOp(final FieldInsnNode fieldInsnNode) {
      this.fieldInsnNode = fieldInsnNode;
      this.name = new SFieldName(fieldInsnNode.owner, fieldInsnNode.name);
   }

   @Override public void eval(final State ctx) {
      final SClass klass = ctx.load(fieldInsnNode.owner);
      final Object staticsAddress = ctx.whereMyStaticsAt(klass);
      final int offset = klass.staticFieldIndex(name);

      ctx.push(ctx.get(staticsAddress, offset));
   }

   @Override
   public String toString() {
      return "GETSTATIC " + BaseInstructions.fieldKey(fieldInsnNode);
   }
}