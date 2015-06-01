package com.lexicalscope.svm.j.natives;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lexicalscope.svm.j.instruction.concrete.klass.GetPrimitiveClass;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class DefaultNativeMethods implements NativeMethods {
   private final AsmSMethodName desiredAssertionStatus0 = new AsmSMethodName("java/lang/Class", "desiredAssertionStatus0", "(Ljava/lang/Class;)Z");
   private final Map<SMethodDescriptor, NativeMethodDef> natives;

   public DefaultNativeMethods(final Map<SMethodDescriptor, NativeMethodDef> natives) {
      this.natives = natives;
   }

   @Override public MethodBody resolveNative(final InstructionSource instructions, final SMethodDescriptor methodName) {
      final NativeMethodDef methodDef = natives.get(methodName);
      if(methodDef != null) {
         return methodDef.instructions(instructions);
      }
      if (methodName.equals(desiredAssertionStatus0)) {
         return statements(instructions).maxStack(1).iconst_0().return1(methodName).build();
      } else if (methodName.equals(new AsmSMethodName("java/lang/Class", "getPrimitiveClass", "(Ljava/lang/String;)Ljava/lang/Class;"))) {
         return statements(instructions)
               .maxLocals(1)
               .maxStack(1)
               .linearOp(new GetPrimitiveClass())
               .return1(methodName)
               .build();
      }

      if (!methodName.isVoidMethod()) { throw new UnsupportedOperationException("only void native methods are supported - " + methodName); }
      return statements(instructions).returnVoid(methodName).build();
   }
   public static NativeMethods natives() {
      return natives(nativeMethodList());
   }

    public static List<NativeMethodDef> nativeMethodList() {
        return Arrays.<NativeMethodDef>asList(
                new Java_lang_object_getClass(),
                new Java_lang_class_getComponentType(),
                new Java_lang_class_getClassLoader0(),
                new Java_lang_system_identityHashCode(),
                new Java_lang_system_nanoTime(),
                new Java_lang_system_currentTimeMillis(),
                new Java_lang_thread_currentThread(),
                new Java_lang_runtime_freeMemory(),
                new Java_lang_system_arraycopy(),
                new Java_lang_float_floatToRawIntBits(),
                new Java_lang_double_doubleToRawLongBits(),
                new Java_lang_object_hashCode(),
                new Java_lang_reflect_array_newArray(),
                new Java_lang_integer_valueOf(),
                new Sun_misc_unsafe_arrayBaseOffset(),
                new Sun_misc_unsafe_arrayIndexScale(),
                new Sun_misc_unsafe_addressSize(),
                new Sun_reflect_reflection_getCallerClass(),
                new Java_security_accessController_doPrivileged(),
                new Symbolic_newSymbol("newIntSymbol", "()I"),
                new Symbolic_newSymbol("newBooleanSymbol", "()Z"),
                new Symbolic_getPassedParameter(),
                new Symbolic_countParameters(),
                new Symbolic_passParameter(),
                new Symbolic_selectState(),
                new Symbolic_randomChoice(),
                new Symbolic_getArgument()
        );
    }

    public void addNative(NativeMethodDef nativeMethodDef) {
       natives.put(nativeMethodDef.name(), nativeMethodDef);
   }

   public static NativeMethods natives(final List<NativeMethodDef> natives) {
      final Map<SMethodDescriptor, NativeMethodDef> nativesMap = new HashMap<>();
      DefaultNativeMethods defaultNativeMethods = new DefaultNativeMethods(nativesMap);
      for (final NativeMethodDef nativeMethodDef : natives) {
          defaultNativeMethods.addNative(nativeMethodDef);
      }
       return defaultNativeMethods;
   }
}
