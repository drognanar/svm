package com.lexicalscope.svm.classloading;

import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

import static org.objectweb.asm.Type.getInternalName;

public class SelectingClassLoader implements SClassLoader {
   private SClassLoader defaultLoader;
   private SClassLoader abstractLoader;

   public SelectingClassLoader(SClassLoader defaultLoader, SClassLoader abstractLoader) {
      this.defaultLoader = defaultLoader;
      this.abstractLoader = abstractLoader;
   }

   @Override
   public SClass load(String name) {
      // Manually marked file.
      if (name.startsWith("@")) {
         String className = name.substring(1);
         return abstractLoader.load(className);
      }
      return getDefaultClassLoader().load(name);
   }

   private SClassLoader getDefaultClassLoader() {
      return defaultLoader;
   }

   @Override
   public SClass load(Class<?> klass) {
      return load(getInternalName(klass));
   }

   @Override
   public MethodBody resolveNative(SMethodDescriptor methodName) {
      return getDefaultClassLoader().resolveNative(methodName);
   }

   @Override
   public Instruction instrument(SMethodDescriptor name, Instruction methodEntry) {
      return getDefaultClassLoader().instrument(name, methodEntry);
   }
}
