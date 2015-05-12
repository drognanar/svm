package com.lexicalscope.svm.vm.j;

import com.lexicalscope.svm.vm.j.code.AsmSMethodName;
import com.lexicalscope.svm.vm.j.klass.SFieldName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class JavaConstants {
   public static final String CLINIT = "<clinit>";
   public static final String INIT = "<init>";
   public static final String NOARGS_VOID_DESC = "()V";
   public static final KlassInternalName STRING_CLASS = KlassInternalName.internalName("java/lang/String");
   public static final KlassInternalName THREAD_CLASS = KlassInternalName.internalName("java/lang/Thread");
   public static final KlassInternalName CLASS_CLASS = KlassInternalName.internalName("java/lang/Class");
   public static final KlassInternalName OBJECT_CLASS = KlassInternalName.internalName("java/lang/Object");
   public static final KlassInternalName INTEGER_CLASS = KlassInternalName.internalName("java/lang/Integer");
   public static final SMethodDescriptor CLASS_CLASS_DEFAULT_CONSTRUCTOR = new AsmSMethodName(CLASS_CLASS, INIT, NOARGS_VOID_DESC);
   public static final SMethodDescriptor GET_CLASS = new AsmSMethodName(OBJECT_CLASS, "getClass", "()Ljava/lang/Class;");
   public static final SMethodDescriptor TO_STRING = new AsmSMethodName(OBJECT_CLASS, "toString", "()Ljava/lang/String;");
   public static final SFieldName STRING_VALUE_FIELD = new SFieldName(STRING_CLASS, "value");

   public static SMethodDescriptor INITIAL_FRAME_NAME = new AsmSMethodName("_svm_internal", "<entry>", NOARGS_VOID_DESC);
}
