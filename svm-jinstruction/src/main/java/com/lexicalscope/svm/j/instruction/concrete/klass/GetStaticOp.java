package com.lexicalscope.svm.j.instruction.concrete.klass;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import org.objectweb.asm.tree.FieldInsnNode;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SFieldName;

public final class GetStaticOp implements Vop {
   private final FieldInsnNode fieldInsnNode;
   private final SFieldName name;

   public GetStaticOp(final FieldInsnNode fieldInsnNode) {
      this.fieldInsnNode = fieldInsnNode;
      this.name = new SFieldName(internalName(fieldInsnNode.owner), fieldInsnNode.name);
   }

   @Override public void eval(final JState ctx) {
      final SClass klass = ctx.loadKlassFor(internalName(fieldInsnNode.owner));
      final ObjectRef staticsAddress = ctx.whereMyStaticsAt(klass);
      final int offset = klass.staticFieldIndex(name);

      ctx.push(ctx.get(staticsAddress, offset));
   }

   @Override
   public String toString() {
      return "GETSTATIC " + GetStaticOp.fieldKey(fieldInsnNode);
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.getstatic();
   }

   public static String fieldKey(final FieldInsnNode fieldInsnNode) {
      return String.format("%s.%s:%s", fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc);
   }
}