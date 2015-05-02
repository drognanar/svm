package com.lexicalscope.svm.vm.j.klass;

import java.net.URL;
import java.util.Collection;
import java.util.List;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.MethodResolver;

public interface SClass extends Allocatable, MethodResolver {
   public static final int STATICS_PREAMBLE = 1;
   public static final int OBJECT_PREAMBLE = 2;
   public static final int OBJECT_TYPE_MARKER_OFFSET = 0;
   public static final int OBJECT_TAG_OFFSET = 1;

   KlassInternalName name();
   SClass superclass();

   boolean hasStaticInitialiser();
   Allocatable statics();

   int fieldIndex(SFieldName name);
   int fieldIndex(String field);
   SField fieldAtIndex(int index);
   boolean hasField(SFieldName name);
   Fields fields();

   List<Object> fieldInit();

   int staticFieldIndex(SFieldName name);
   boolean hasStaticField(SFieldName name);
   int staticFieldCount();

   boolean instanceOf(SClass other);
   boolean isArray();
   boolean isKlassKlass();

   URL loadedFrom();

   Collection<SClass> superTypes();
   Methods methods();

   SClass componentType();
}