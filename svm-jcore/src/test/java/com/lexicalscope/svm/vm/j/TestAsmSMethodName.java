package com.lexicalscope.svm.vm.j;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import com.lexicalscope.svm.vm.j.code.AsmSMethodName;

public class TestAsmSMethodName {
   final AsmSMethodName objectToStringMethodName = new AsmSMethodName("java/lang/Object", "toString", "()Ljava/lang/String;");

   @Test public void testToString() {
      assertThat(objectToStringMethodName,
            hasToString("java/lang/Object.toString()Ljava/lang/String;"));
   }

   @Test public void testQualifiedMethodName() {
      assertThat(objectToStringMethodName.qualifiedName(),
            equalTo("java/lang/Object.toString()Ljava/lang/String;"));
   }
}
