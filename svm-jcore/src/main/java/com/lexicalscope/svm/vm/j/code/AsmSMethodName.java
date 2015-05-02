package com.lexicalscope.svm.vm.j.code;

import static com.lexicalscope.svm.vm.j.JavaConstants.*;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;
import static org.objectweb.asm.Type.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.objectweb.asm.Type;

import com.google.common.primitives.Ints;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.SVirtualMethodName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public final class AsmSMethodName implements Comparable<AsmSMethodName>, SMethodDescriptor {
   private final KlassInternalName klassName;
   private final SVirtualMethodName virtualName;
   private final int hashCode;
   private final int[] objectArgIndexes;
   private final boolean returnTypeIsObject;

   public AsmSMethodName(
         final String klassName,
         final String name,
         final String desc
         ) {
      this(internalName(klassName), name, desc);
   }

   public AsmSMethodName(
         final KlassInternalName klassName,
         final String name,
         final String desc
         ) {
      this.klassName = klassName;
      this.virtualName = new AsmSVirtualMethodName(name, desc);
      this.hashCode = klassName.hashCode() ^ virtualName.hashCode();
      this.objectArgIndexes = indexesOfObjectArgs(desc);
      this.returnTypeIsObject = getReturnType(desc).getSort() == Type.OBJECT;
   }

   private static int[] indexesOfObjectArgs(final String desc) {
      final List<Integer> result = new ArrayList<>();
      final Type[] argumentTypes = Type.getArgumentTypes(desc);
      for (int j = 0; j < argumentTypes.length; j++) {
         if(argumentTypes[j].getSort() == Type.OBJECT) {
            result.add(j);
         }
      }
      return Ints.toArray(result);
   }

   @Override public boolean returnIsObject() {
      return returnTypeIsObject;
   }

   public AsmSMethodName(final Class<?> klass, final String name, final String desc) {
      this(internalName(klass), name, desc);
   }

   @Override
   public boolean isVoidMethod() {
      return Type.getReturnType(virtualName.desc()).equals(Type.VOID_TYPE);
   }

   @Override
   public int compareTo(final AsmSMethodName o) {
      final int firstCompare = this.klassName.compareTo(o.klassName);
      if(firstCompare != 0) {
         return firstCompare;
      }
      return this.virtualName.compareTo(o.virtualName);
   }

   @Override
   public boolean equals(final Object obj) {
      if (obj == this) {
         return true;
      }

      if(obj != null && obj.getClass().equals(this.getClass())) {
         final AsmSMethodName that = (AsmSMethodName) obj;
         return that.klassName.equals(klassName) && that.virtualName.equals(virtualName);
      }
      return false;
   }

   @Override
   public int hashCode() {
      return hashCode;
   }

   public String qualifiedName() {
      return klassName + "." + virtualName;
   }

   @Override
   public String toString() {
      return qualifiedName();
   }

   @Override
   public KlassInternalName klassName() {
      return klassName;
   }

   @Override
   public String name() {
      return virtualName.name();
   }

   @Override
   public String desc() {
      return virtualName.desc();
   }

   @Override
   public int argSize() {
      return getArgumentsAndReturnSizes(desc()) >> 2;
   }

   @Override public int[] objectArgIndexes() {
      return objectArgIndexes;
   }

   @Override
   public int returnCount() {
      return getArgumentsAndReturnSizes(desc()) & 0x03;
   }

   @Override
   public SVirtualMethodName virtualName() {
      return virtualName;
   }

   public static SMethodDescriptor staticInitialiser(final KlassInternalName klassName) {
      return new AsmSMethodName(klassName, CLINIT, NOARGS_VOID_DESC);
   }

   public static SMethodDescriptor defaultConstructor(final KlassInternalName klassName) {
      return new AsmSMethodName(klassName, INIT, NOARGS_VOID_DESC);
   }

   public static SMethodDescriptor defaultConstructor(final Class<?> klass) {
      return defaultConstructor(internalName(klass));
   }

   public static SMethodDescriptor method(
         final Class<?> klass,
         final String name,
         final String desc) {
      return new AsmSMethodName(klass, name, desc);
   }

   @Override public boolean isConstructor() {
      return name().equals(INIT);
   }

   @Override public boolean declaredOn(final KlassInternalName klassInternalName) {
      return klassName().equals(klassInternalName);
   }

   @Override public boolean declaredOn(final Matcher<KlassInternalName> klassInternalNameMatcher) {
      return klassInternalNameMatcher.matches(klassName());
   }
}
