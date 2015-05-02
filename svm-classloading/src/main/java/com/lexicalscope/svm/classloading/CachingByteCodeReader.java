package com.lexicalscope.svm.classloading;

import java.util.HashMap;
import java.util.Map;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class CachingByteCodeReader implements ByteCodeReader {
   // TODO[tim]: should probably allow class definitions to be garbage collected using weak references
   private final Map<KlassInternalName, SClass> classCache = new HashMap<>();
   private final ResourceByteCodeReader byteCodeReader;

   public CachingByteCodeReader(
         final InstructionSource instructions,
         final ClassSource classSource) {
      byteCodeReader = new ResourceByteCodeReader(instructions, classSource);
   }

   @Override
   public SClass load(final SClassLoader classLoader, final KlassInternalName name) {
      assert name != null;
      SClass result = classCache.get(name);
      if(result == null) {
         result = byteCodeReader.load(classLoader, name);
         classCache.put(name, result);
      }
      return result;
   }
}
