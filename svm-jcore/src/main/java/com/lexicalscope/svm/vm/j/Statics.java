package com.lexicalscope.svm.vm.j;

import java.util.List;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.state.Snapshotable;
import com.lexicalscope.svm.vm.j.klass.SClass;


public interface Statics extends Snapshotable<Statics> {
   SClass load(KlassInternalName klassName);

   List<SClass> defineClass(KlassInternalName klass);
   SClass definePrimitiveClass(KlassInternalName klassName);

   StaticsMarker staticsMarker(SClass klass);

   void staticsAt(SClass klass, ObjectRef staticsAddress);
   ObjectRef whereMyStaticsAt(SClass klass);

   void classAt(SClass klass, ObjectRef classAddress);
   ObjectRef whereMyClassAt(SClass klass);
   ObjectRef whereMyClassAt(KlassInternalName internalName);

   boolean isDefined(KlassInternalName klass);

   SClass classClass();
}
