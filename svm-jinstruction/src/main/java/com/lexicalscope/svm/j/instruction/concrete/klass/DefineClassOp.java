package com.lexicalscope.svm.j.instruction.concrete.klass;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.object.NewObjectOp;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.JavaConstants;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Op;
import com.lexicalscope.svm.vm.j.StaticsMarker;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SFieldName;

public final class DefineClassOp implements Op<List<SClass>> {
   private final List<KlassInternalName> klassNames;

   public DefineClassOp(final KlassInternalName klassName) {
      this(asList(klassName));
   }

   public DefineClassOp(final List<KlassInternalName> klassNames) {
      this.klassNames = klassNames;
   }

   @Override public List<SClass> eval(final JState ctx) {
         final List<SClass> results = new ArrayList<>();
         for (final KlassInternalName klassName : klassNames) {
            if (!ctx.isDefined(klassName)) {
               final List<SClass> klasses = ctx.defineClass(klassName);
               for (final SClass klass : klasses) {
                  allocate(ctx, klass);
               }

               results.addAll(klasses);
            }
         }
         return results;
   }

   public static final SFieldName internalClassPointer = new SFieldName(JavaConstants.CLASS_CLASS, "*internalClassPointer");
   static void allocateClass(final JState ctx, final SClass klass) {
      final SClass classClass = ctx.classClass();
      final ObjectRef classAddress = NewObjectOp.allocateObject(ctx, classClass);
      ctx.put(classAddress,  classClass.fieldIndex(internalClassPointer), klass);
      ctx.classAt(klass, classAddress);
   }

   static void allocateStatics(final JState ctx, final StaticsMarker staticsMarker, final SClass klass) {
      if(klass.statics().allocateSize() > 0) {
         final ObjectRef staticsAddress = ctx.newObject(klass.statics());
         ctx.put(staticsAddress, SClass.OBJECT_TYPE_MARKER_OFFSET, staticsMarker);
         ctx.staticsAt(klass, staticsAddress);
      }
   }

   static void allocate(final JState ctx, final SClass klass) {
      DefineClassOp.allocateClass(ctx, klass);
      DefineClassOp.allocateStatics(ctx, ctx.staticsMarker(klass), klass);
   }

   @Override public <S> S query(final InstructionQuery<S> instructionQuery) {
      return instructionQuery.synthetic();
   }

   @Override public String toString() {
      return "Define Classes " + klassNames;
   }
}