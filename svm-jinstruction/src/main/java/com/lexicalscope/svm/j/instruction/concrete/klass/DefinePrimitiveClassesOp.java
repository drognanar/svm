package com.lexicalscope.svm.j.instruction.concrete.klass;

import static com.lexicalscope.svm.j.instruction.concrete.klass.DefineClassOp.allocate;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Op;
import com.lexicalscope.svm.vm.j.klass.SClass;

public final class DefinePrimitiveClassesOp implements Op<List<SClass>> {
   public static final Map<KlassInternalName, KlassInternalName> primitivesMap = new LinkedHashMap<KlassInternalName, KlassInternalName>(){{
         put(internalName(boolean.class), internalName(boolean[].class));
         put(internalName(char.class), internalName(char[].class));
         put(internalName(byte.class), internalName(byte[].class));
         put(internalName(short.class), internalName(short[].class));
         put(internalName(int.class), internalName(int[].class));
         put(internalName(long.class), internalName(long[].class));
         put(internalName(float.class), internalName(float[].class));
         put(internalName(double.class), internalName(double[].class));
   }};

   public static final List<KlassInternalName> primitives = new ArrayList<KlassInternalName>(){{
         addAll(primitivesMap.keySet());
         addAll(primitivesMap.values());
         add(internalName(Object[].class));
   }};

   private final DefineClassOp op;

   public DefinePrimitiveClassesOp(final DefineClassOp op) {
      this.op = op;
   }

   @Override public List<SClass> eval(final JState ctx) {
      final List<SClass> result = op.eval(ctx);
      for (final Entry<KlassInternalName, KlassInternalName> primitive : primitivesMap.entrySet()) {
         final KlassInternalName primitiveName = primitive.getKey();
         final KlassInternalName primitiveArrayName = primitive.getValue();

         assert !ctx.isDefined(primitiveName) : primitiveName;
         assert !ctx.isDefined(primitiveArrayName) : primitiveArrayName;

         // primitive classes do not need initialisation
         allocate(ctx, ctx.definePrimitiveClass(primitiveName));
         allocate(ctx, ctx.definePrimitiveClass(primitiveArrayName));
      }
      allocate(ctx, ctx.definePrimitiveClass(internalName(Object[].class)));
      return result;
   }

   @Override public String toString() {
      return "Define Primitives";
   }

   @Override public <S> S query(final InstructionQuery<S> instructionQuery) {
      return instructionQuery.synthetic();
   }

   public static boolean primitivesContains(final KlassInternalName klassName) {
      return primitives.contains(klassName);
   }
}