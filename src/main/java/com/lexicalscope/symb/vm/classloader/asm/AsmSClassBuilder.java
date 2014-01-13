package com.lexicalscope.symb.vm.classloader.asm;

import java.util.List;
import java.util.TreeMap;

import org.objectweb.asm.tree.FieldNode;

import com.lexicalscope.symb.vm.classloader.SClassLoader;
import com.lexicalscope.symb.vm.classloader.SField;
import com.lexicalscope.symb.vm.classloader.SFieldName;

public class AsmSClassBuilder {
   private final SClassLoader classLoader;
   private final DeclaredFields declaredFields;
   final TreeMap<SFieldName, Integer> declaredStaticFieldMap = new TreeMap<>();


   public AsmSClassBuilder(final SClassLoader classLoader, final AsmSClass superclass) {
      this.classLoader = classLoader;
      declaredFields = new DeclaredFields();
   }

   void initialiseFieldMaps(final List<FieldNode> fields) {
      for (final FieldNode fieldNode : fields) {
         final SField field = new SField(new SFieldName(name, fieldNode.name), fieldNode, classLoader.init(fieldNode.desc));

         withField(field);
      }
   }

   int staticOffset = 0;
   private void withField(final SField field) {
      if (field.isStatic()) {
         declaredStaticFieldMap.put(field.name(), staticOffset);
         staticOffset++;
      } else {
         declaredFields.addField(field);
      }
   }

   private String name;
   public AsmSClassBuilder withName(final String name) {
      this.name = name;
      return this;
   }

   public DeclaredFields declaredFields() {
      return declaredFields;
   }
}