package com.lexicalscope.svm.j.instruction.concrete.klass;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.State;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SFieldName;

public final class PutStaticOp implements Vop {
   private final FieldInsnNode fieldInsnNode;
   private final SFieldName name;
   private final boolean doubleWord;

   public PutStaticOp(final FieldInsnNode fieldInsnNode) {
      this.fieldInsnNode = fieldInsnNode;
      this.name = new SFieldName(fieldInsnNode.owner, fieldInsnNode.name);
      final int sort = Type.getType(fieldInsnNode.desc).getSort();
      doubleWord = sort == Type.DOUBLE || sort == Type.LONG;
   }

   @Override public void eval(final State ctx) {
      final SClass klass = ctx.load(fieldInsnNode.owner);
      final Object staticsAddress = ctx.whereMyStaticsAt(klass);
      final int offset = klass.staticFieldIndex(name);

      Object value;
      if(doubleWord) {
         value = ctx.popDoubleWord();
      } else {
         value = ctx.pop();
      }
      ctx.put(staticsAddress, offset, value);
   }

   @Override
   public String toString() {
      return "PUTSTATIC " + GetStaticOp.fieldKey(fieldInsnNode);
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.putstatic();
   }
}