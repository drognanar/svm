package com.lexicalscope.svm.classloading;

import static com.lexicalscope.svm.vm.j.HeapMatchers.*;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.lexicalscope.svm.heap.FastHeap;
import com.lexicalscope.svm.heap.Heap;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.object.NewObjectOp;
import com.lexicalscope.svm.stack.DequeStack;
import com.lexicalscope.svm.stack.SnapshotableStackFrame;
import com.lexicalscope.svm.vm.j.JStateImpl;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class TestFieldInit {
   private final SClassLoader classLoader = new AsmSClassLoader();
   private final StaticsImpl statics = new StaticsImpl(classLoader);
   private final KlassInternalName klassName = internalName(ClassWithAllTypesOfFields.class);

   private final Heap heap = FastHeap.createFastHeap();
   private SClass sClass;
   private ObjectRef newObject;

   @Before public void before() {
      final List<SClass> klasses = statics.defineClass(klassName);
      assertThat(klasses, Matchers.hasSize(2));
      sClass = klasses.get(1);

      newObject = new NewObjectOp(klassName).eval(new JStateImpl(null, null, statics, new DequeStack(new SnapshotableStackFrame(null, null, null, 0, 1)), heap, null));
   }

   @Test public void intFieldInit() {
      checkFieldInit("intField", 0);
   }

   @Test public void charFieldInit() {
      checkFieldInit("charField", (char) 0);
   }

   @Test public void byteFieldInit() {
      checkFieldInit("byteField", (byte) 0);
   }

   @Test public void shortFieldInit() {
      checkFieldInit("shortField", (short) 0);
   }

   @Test public void longFieldInit() {
      checkFieldInit("longField", 0L);
   }

   @Test public void floatFieldInit() {
      checkFieldInit("floatField", (float) 0);
   }

   @Test public void doubleFieldInit() {
      checkFieldInit("doubleField", (double) 0);
   }

   @Test public void objectFieldInit() {
      checkFieldInit("objectField", heap.nullPointer());
   }

   private void checkFieldInit(final String fieldName, final Object value) {
      assertThat(heap, contains(heapLocation(newObject, sClass, fieldName), value));
   }
}
