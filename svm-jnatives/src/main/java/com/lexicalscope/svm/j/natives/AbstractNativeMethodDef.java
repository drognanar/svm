package com.lexicalscope.svm.j.natives;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public abstract class AbstractNativeMethodDef implements NativeMethodDef {
   private final SMethodDescriptor name;

   public AbstractNativeMethodDef(final String klass, final String name, final String desc) {
      this(internalName(klass), name, desc);
   }

   public AbstractNativeMethodDef(final KlassInternalName klass, final String name, final String desc) {
      this.name = new AsmSMethodName(klass, name, desc);
   }

   @Override public final SMethodDescriptor name() {
      return name;
   }
}
