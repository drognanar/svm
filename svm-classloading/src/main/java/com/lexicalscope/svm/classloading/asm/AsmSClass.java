package com.lexicalscope.svm.classloading.asm;

import static com.lexicalscope.svm.vm.j.code.AsmSMethodName.staticInitialiser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.stack.trace.SMethodName;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.klass.DeclaredFields;
import com.lexicalscope.svm.vm.j.klass.DeclaredMethods;
import com.lexicalscope.svm.vm.j.klass.Fields;
import com.lexicalscope.svm.vm.j.klass.Methods;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SField;
import com.lexicalscope.svm.vm.j.klass.SFieldName;
import com.lexicalscope.svm.vm.j.klass.SMethod;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class AsmSClass implements SClass {
   private final KlassInternalName klassName;
   private final Set<SClass> superTypes = new LinkedHashSet<>();

   private final DeclaredFields declaredFields;
   private final DeclaredMethods declaredMethods;
   private final Fields fields;
   private final Methods methods;

   private final URL loadedFromUrl;
   private final SClass superclass;
   private final SClass componentType;

   public AsmSClass(
         final URL loadedFromUrl,
         final KlassInternalName klassName,
         final SClass superclass,
         final List<SClass> interfaces,
         final DeclaredFields declaredFields,
         final DeclaredMethods declaredMethods,
         final SClass componentType) {
      this.loadedFromUrl = loadedFromUrl;
      this.superclass = superclass;
      this.klassName = klassName;

      this.declaredFields = declaredFields;
      this.declaredMethods = declaredMethods;
      this.componentType = componentType;
      this.fields = (superclass == null ? new Fields() : superclass.fields()).extend(klassName, declaredFields);
      this.methods = (superclass == null ? new Methods(klassName) : superclass.methods()).extend(klassName, declaredMethods);

      if (superclass != null) {
         superTypes.addAll(superclass.superTypes());
      }
      for (final SClass interfac3 : interfaces) {
         superTypes.addAll(interfac3.superTypes());
      }
      superTypes.add(this);
   }

   @Override
   public SMethod virtualMethod(final SMethodDescriptor sMethodName) {
      return methods.resolve(sMethodName);
   }

   @Override
   public SMethod declaredMethod(final SMethodName sMethodName) {
      return methods.findDefined(sMethodName);
   }

   @Override
   public boolean hasStaticInitialiser() {
      return methods.hasStatic(staticInitialiser(klassName));
   }

   @Override public int allocateSize() {
      return fields.count() + OBJECT_PREAMBLE;
   }

   @Override
   public int fieldIndex(final SFieldName name) {
      return fields.indexOf(name) + OBJECT_PREAMBLE;
   }

   @Override public int fieldIndex(final String name) {
      return fields.indexOf(name) + OBJECT_PREAMBLE;
   }

   @Override public SField fieldAtIndex(final int index) {
      return fields.get(index - OBJECT_PREAMBLE);
   }

   @Override
   public boolean hasField(final SFieldName name) {
      return fields.contains(name);
   }

   private ArrayList<Object> fieldInit;
   @Override
   public List<Object> fieldInit() {
      if(fieldInit == null) {
         fieldInit = new ArrayList<>();
         if(superclass != null) {
            fieldInit.addAll(superclass.fieldInit());
         }
         fieldInit.addAll(declaredFields.fieldInit());
      }
      return fieldInit;
   }

   @Override public int staticFieldCount() {
      return declaredFields.staticCount();
   }

   @Override
   public int staticFieldIndex(final SFieldName name) {
      return declaredFields.staticFieldIndex(name) + STATICS_PREAMBLE;
   }

   @Override
   public boolean hasStaticField(final SFieldName name) {
      return declaredFields.containsStaticField(name);
   }

   @Override
   public KlassInternalName name() {
      return klassName;
   }

   @Override
   public SClass superclass() {
      return superclass;
   }

   @Override
   public Allocatable statics() {
      return new Allocatable() {
         @Override public int allocateSize() {
            return staticFieldCount() + STATICS_PREAMBLE;
         }

         @Override public String toString() {
            return "Statics of " + klassName;
         }
      };
   }

   @Override
   public boolean instanceOf(final SClass other) {
      return other == this || superTypes.contains(other);
   }

   @Override
   public URL loadedFrom() {
      return loadedFromUrl;
   }

   @Override public String toString() {
      return String.format("{%s {staticfields %s} {fields %s}}", name(), declaredFields.staticFieldsToString(), fields);
   }

   @Override public boolean isArray() {
      return componentType != null;
   }

   @Override public boolean isKlassKlass() {
      return name().equals(Type.getInternalName(Class.class));
   }

   @Override public Fields fields() {
      return fields;
   }

   @Override public Collection<SClass> superTypes() {
      return superTypes;
   }

   @Override public Methods methods() {
      return methods;
   }

   @Override public SClass componentType() {
      return componentType;
   }
}
