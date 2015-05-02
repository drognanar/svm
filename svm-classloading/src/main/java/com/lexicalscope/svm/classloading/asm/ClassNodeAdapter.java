package com.lexicalscope.svm.classloading.asm;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.lexicalscope.svm.j.instruction.concrete.klass.DefineClassOp;
import com.lexicalscope.svm.vm.j.JavaConstants;

public class ClassNodeAdapter {
   private final ClassNode classNode;

   public ClassNodeAdapter(final ClassNode classNode) {
      this.classNode = classNode;
   }

   @SuppressWarnings("unchecked")
   public List<FieldNode> fields() {
      if(internalName(classNode.name).equals(JavaConstants.CLASS_CLASS)) {
         final List<FieldNode> result = new ArrayList<>(classNode.fields);
         result.add(new FieldNode(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, DefineClassOp.internalClassPointer.getName(), Type.getDescriptor(Object.class), "Ljava/lang/Object;", null));
         return result;
      }
      return classNode.fields;
   }

   @SuppressWarnings("unchecked")
   public List<MethodNode> methods() {
      return classNode.methods;
   }
}
