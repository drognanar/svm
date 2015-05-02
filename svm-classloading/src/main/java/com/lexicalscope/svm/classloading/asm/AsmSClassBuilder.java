package com.lexicalscope.svm.classloading.asm;

import java.util.List;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.lexicalscope.svm.classloading.AsmSMethod;
import com.lexicalscope.svm.classloading.SClassLoader;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;
import com.lexicalscope.svm.vm.j.klass.DeclaredFields;
import com.lexicalscope.svm.vm.j.klass.DeclaredMethods;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SField;
import com.lexicalscope.svm.vm.j.klass.SFieldName;

public class AsmSClassBuilder {
   private KlassInternalName klassName;
   private final SClassLoader classLoader;
   private final DeclaredFields declaredFields = new DeclaredFields();
   private final DeclaredMethods declaredMethods = new DeclaredMethods();
   private final InstructionSource instructions;

   public AsmSClassBuilder(
         final SClassLoader classLoader,
         final InstructionSource instructions,
         final SClass superclass) {
      this.classLoader = classLoader;
      this.instructions = instructions;
   }

   AsmSClassBuilder withFields(final List<FieldNode> fields) {
      for (final FieldNode fieldNode : fields) {
         withField(
               new SField(
                     new SFieldName(klassName, fieldNode.name),
                     new AsmFieldDesc(fieldNode),
                     instructions.initialFieldValue(fieldNode.desc)));
      }
      return this;
   }

   private void withField(final SField field) {
      if (field.isStatic()) {
         declaredFields.addStatic(field);
      } else {
         declaredFields.addDynamic(field);
      }
   }

   public AsmSClassBuilder withMethods(final List<MethodNode> methods) {
      for (final MethodNode method : methods) {
         declaredMethods.add(
               new AsmSMethod(
                     classLoader,
                     new AsmSMethodName(klassName, method.name, method.desc),
                     instructions,
                     method));
      }
      return this;
   }

   public AsmSClassBuilder withName(final KlassInternalName name) {
      this.klassName = name;
      return this;
   }

   public DeclaredFields declaredFields() {
      return declaredFields;
   }

   public DeclaredMethods declaredMethods() {
      return declaredMethods;
   }
}