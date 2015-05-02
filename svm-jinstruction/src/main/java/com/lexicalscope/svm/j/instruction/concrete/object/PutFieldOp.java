package com.lexicalscope.svm.j.instruction.concrete.object;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.klass.GetStaticOp;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SFieldName;

public final class PutFieldOp implements Vop {
   private final FieldInsnNode fieldInsnNode;
   private final SFieldName name;
   private final FieldConversion conversion;

   public PutFieldOp(final FieldConversionFactory fieldConversionFactory, final FieldInsnNode fieldInsnNode) {
      this.fieldInsnNode = fieldInsnNode;
      this.name = new SFieldName(internalName(fieldInsnNode.owner), fieldInsnNode.name);
      this.conversion = conversionForGetField(fieldConversionFactory, fieldInsnNode);
   }

   private FieldConversion conversionForGetField(final FieldConversionFactory fieldConversionFactory, final FieldInsnNode fieldInsnNode) {
      final Type fieldType = Type.getType(fieldInsnNode.desc);
      FieldConversion conversion;
      switch (fieldType.getSort()) {
         case Type.CHAR:
            conversion = fieldConversionFactory.intToChar();
            break;

         case Type.INT:
            conversion = fieldConversionFactory.intToInt();
            break;

         default:
            conversion = fieldConversionFactory.noConversion();
            break;
      }
      return conversion;
   }

   @Override public void eval(final JState ctx) {
      // TODO[tim]: link should remove this
      final int offset = ctx.loadKlassFor(internalName(fieldInsnNode.owner)).fieldIndex(name);

      final Object val = ctx.pop();
      final ObjectRef obj = (ObjectRef) ctx.pop();

      ctx.put(obj, offset, conversion.convert(val));
   }

   @Override
   public String toString() {
      return "PUTFIELD " + GetStaticOp.fieldKey(fieldInsnNode);
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.putfield(name);
   }
}