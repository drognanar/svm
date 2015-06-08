package com.lexicalscope.svm.partition.trace;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;

import static java.util.Arrays.copyOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TraceExtender {
   private final Object[] normalisedArgs;
   private int newNextAlias;
   private Map<Object, Alias> newMap;
   private final Map<Object, Alias> map;

   public TraceExtender(final Object[] args, final Map<Object, Alias> map, final int nextAlias) {
      this.normalisedArgs = copyOf(args, args.length);
      this.newNextAlias = nextAlias;
      this.map = map;
      this.newMap = map;
   }

   public void aliasesForZerothArguments() {
      normalisedArgs[0] = aliasForArg(normalisedArgs[0]);
   }

   public void aliasesForCallArguments(final int[] objectArgIndexes) {
      for (final int i : objectArgIndexes) {
         normalisedArgs[i + 1] = aliasForArg(normalisedArgs[i + 1]);
      }
   }

   public void normaliseArrayArguments(JState ctx, final AsmSMethodName.ArrayArgItem[] arrayArgIndexes) {
      for (final AsmSMethodName.ArrayArgItem i : arrayArgIndexes) {
         normalisedArgs[i.index + 1] = normaliseArray(ctx, (ObjectRef) normalisedArgs[i.index + 1], i.objectElementType);
      }
   }

   private Object normaliseArray(JState ctx, ObjectRef address, boolean objectElementType) {
      if (address == ctx.nullPointer()) {
         return address;
      }
      int length = (int) ctx.get(address, NewArrayOp.ARRAY_LENGTH_OFFSET);
      ArrayList<Object> arr = new ArrayList<>(length);
      for (int i = 0; i < length; i++) {
         if (objectElementType) {
            arr.add(i, aliasForArg(ctx.get(address, NewArrayOp.ARRAY_PREAMBLE + i)));
         } else {
            arr.add(i, ctx.get(address, NewArrayOp.ARRAY_PREAMBLE + i));
         }
      }

      return arr;
   }

   private Alias aliasForArg(final Object arg) {
      Alias alias;
      if(null == (alias = newMap.get(arg))) {
         alias = newAlias(arg);
      }
      return alias;
   }

   private Alias newAlias(final Object arg) {
      // TODO[tim] do proper COW here
      if(map == newMap) {
         newMap = new HashMap<>(map);
      }
      final Alias alias = new Alias(newNextAlias++);
      newMap.put(arg, alias);
      return alias;
   }

   public Map<Object, Alias> newMap() {
      return newMap;
   }

   public int newNextAlias() {
      return newNextAlias;
   }

   public Object[] normalisedArgs() {
      return normalisedArgs;
   }
}
