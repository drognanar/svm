package com.lexicalscope.svm.partition.trace;

import static com.lexicalscope.svm.j.instruction.instrumentation.InstructionCounting.countIs;
import static com.lexicalscope.svm.vm.j.JavaConstants.*;
import static com.lexicalscope.svm.vm.j.KlassInternalName.*;
import static java.lang.Math.max;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.lexicalscope.svm.classloading.AsmSClassLoader;
import com.lexicalscope.svm.classloading.SClassLoader;
import com.lexicalscope.svm.j.instruction.instrumentation.InstructionCounting;
import com.lexicalscope.svm.j.instruction.instrumentation.finders.FindConstructorCall;
import com.lexicalscope.svm.vm.j.klass.SMethod;


public class TestCanFindConstructorCall {
   private final SClassLoader classLoader = new AsmSClassLoader();

   public static class ClassUnderConstruction { }
   public static class AnotherClassUnderConstruction { }

   public static class ClassCallingConstructor {
      public void method() {
         new ClassUnderConstruction();
      }
   }

   public static class ClassCallingConstructorInBranch {
      public void method() {
         if (max(7, 8) == 0) {
            new ClassUnderConstruction();
         } else {
            new AnotherClassUnderConstruction();
         }
      }
   }

   public static class ClassCallingNestedConstructors {
      public ClassCallingNestedConstructors(ClassCallingNestedConstructors c) {
      }

      public static void method() {
         new ClassCallingNestedConstructors(new ClassCallingNestedConstructors(null));
      }
   }

   public static class ClassCallingSuperConstructor extends ClassUnderConstruction {
      public ClassCallingSuperConstructor() {
         super();
      }
   }

   final InstructionCounting counting = new InstructionCounting();

   @Test public void constructorCallCanBeFound() {
      final SMethod method = classLoader.virtualMethod(ClassCallingConstructor.class, "method", NOARGS_VOID_DESC);

      new FindConstructorCall(matchingKlass(ClassUnderConstruction.class)).findInstruction(method.entry(), counting);

      assertThat("found constructor", counting, countIs(1));
   }

   @Test public void constructorCallsInBranchCanBeFound() {
      final SMethod method = classLoader.virtualMethod(ClassCallingConstructorInBranch.class, "method", NOARGS_VOID_DESC);

      new FindConstructorCall(anyKlass()).findInstruction(method.entry(), counting);

      assertThat("found constructor", counting, countIs(2));
   }

   @Test public void superConstructorIsNotFound() {
      final SMethod method = classLoader.declaredMethod(ClassCallingSuperConstructor.class, INIT, NOARGS_VOID_DESC);

      new FindConstructorCall(anyKlass()).findInstruction(method.entry(), counting);

      assertThat("found constructor", counting, countIs(0));
   }

   @Test public void findNestedConstructors() {
      final SMethod method = classLoader.virtualMethod(ClassCallingNestedConstructors.class, "method", NOARGS_VOID_DESC);

      new FindConstructorCall(anyKlass()).findInstruction(method.entry(), counting);

      assertThat("found constructor", counting, countIs(2));
   }
}
