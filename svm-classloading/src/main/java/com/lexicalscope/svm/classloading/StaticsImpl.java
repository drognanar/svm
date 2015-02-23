package com.lexicalscope.svm.classloading;

import static org.objectweb.asm.Type.getInternalName;

import java.util.*;

import com.lexicalscope.svm.classloading.asm.AsmSClass;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.JavaConstants;
import com.lexicalscope.svm.vm.j.Statics;
import com.lexicalscope.svm.vm.j.StaticsMarker;
import com.lexicalscope.svm.vm.j.klass.DeclaredFields;
import com.lexicalscope.svm.vm.j.klass.DeclaredMethods;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class StaticsImpl implements Statics {
   // TODO[tim]: need fast-clone version
   private static final String klassKlassName = getInternalName(Class.class);
   private final Map<String, SClass> defined;
   private final Map<String, SClass> abstractions;

   // TODO[tim]: combine these maps for efficiency
   private final Map<SClass, ObjectRef> staticsAddresses;
   private final Map<SClass, ObjectRef> classAddresses;
   private final Set<String> classAbstractions;

   private final SClassLoader classLoader;

   public StaticsImpl(final SClassLoader classLoader) {
      this(classLoader,
            new HashMap<String, SClass>(),
            new HashMap<SClass, ObjectRef>(),
            new HashMap<SClass, ObjectRef>());
   }

   public StaticsImpl(final SClassLoader classLoader, Set<String> classAbstractions) {
      this(classLoader,
              classAbstractions,
              new HashMap<String, SClass>(),
              new HashMap<String, SClass>(),
              new HashMap<SClass, ObjectRef>(),
              new HashMap<SClass, ObjectRef>());
   }

   private StaticsImpl(
         final SClassLoader classLoader,
         final Map<String, SClass> defined,
         final Map<SClass, ObjectRef> staticsAddresses,
         final Map<SClass, ObjectRef> classAddresses) {
      this(classLoader, new HashSet<String>(), new HashMap<String, SClass>(), defined, staticsAddresses, classAddresses);
   }

   private StaticsImpl(
           final SClassLoader classLoader,
           final Set<String> classAbstractions,
           final Map<String, SClass> abstractions,
           final Map<String, SClass> defined,
           final Map<SClass, ObjectRef> staticsAddresses,
           final Map<SClass, ObjectRef> classAddresses) {
      this.defined = defined;
      this.classLoader = classLoader;
      this.abstractions = abstractions;
      this.classAbstractions = classAbstractions;
      this.staticsAddresses = staticsAddresses;
      this.classAddresses = classAddresses;
   }

   @Override public Statics snapshot() {
      return new StaticsImpl(
            classLoader,
            classAbstractions,
            abstractions,
            new HashMap<>(defined),
            new HashMap<>(staticsAddresses),
            new HashMap<>(classAddresses));
   }

   @Override public List<SClass> defineClass(final String klassName) {
      if(isDefined(klassName)) {
         throw new DuplicateClassDefinitionException(defined.get(klassName));
      }

      final List<SClass> result = new ArrayList<>();
      final SClass loaded = classLoader.load(klassName);
      cache(loaded);
      if (classAbstractions.contains(klassName)) {
         String abstractKlassName = String.format("@%s", klassName);
         final SClass a = classLoader.load(abstractKlassName);
         abstractions.put(a.name(), a);
      }
      // load all supertypes
      for (final SClass klass : loaded.superTypes()) {
         if(!defined.containsKey(klass.name())){
            result.add(cache(klass));
         }
      }
      result.add(loaded);
//      , new ClassLoaded(){
//         @Override public void loaded(final SClass klass) {
//            if(!defined.containsKey(klass.name())) {
//               result.add(cache(klass.name(), klass));
//               fix this
//            }
//         }});

      return result;
   }

   @Override public SClass definePrimitiveClass(final String klassName) {
      if(isDefined(klassName)) {
         throw new DuplicateClassDefinitionException(defined.get(klassName));
      }

      final SClass componentType = klassName.startsWith("[") ? load(toClassName(klassName.substring(1))) : null;

      final AsmSClass result =
            new AsmSClass(
                  null,
                  klassName,
                  load(JavaConstants.OBJECT_CLASS),
                  Collections.<SClass>emptyList(),
                  new DeclaredFields(),
                  new DeclaredMethods(),
                  componentType);
      cache(klassName, result);
      return result;
   }

   private String toClassName(final String substring) {
      switch (substring) {
         case "Z":
            return "boolean";
         case "C":
            return "char";
         case "B":
            return "byte";
         case "S":
            return "short";
         case "I":
            return "int";
         case "J":
            return "long";
         case "F":
            return "float";
         case "D":
            return "double";
         case "Ljava/lang/Object;":
            return "java/lang/Object";
      }
      return substring;
   }

   private SClass cache(final SClass result) {
      return cache(result.name(), result);
   }

   private SClass cache(final String klassName, final SClass result) {
      defined.put(klassName, result);
      return result;
   }

   @Override public boolean isDefined(final String klass) {
      return defined.containsKey(klass);
   }

   @Override public SClass load(final String klassName) {
      if(!isDefined(klassName)) {
         throw new MissingClassDefinitionException(klassName, defined);
      }
      if (classAbstractions.contains(klassName)) {
         return abstractions.get(klassName);
      } else {
         return defined.get(klassName);
      }
   }

   @Override public void staticsAt(final SClass klass, final ObjectRef staticsAddress) {
      assert !staticsAddresses.containsKey(klass);
      staticsAddresses.put(klass, staticsAddress);
   }

   @Override public ObjectRef whereMyStaticsAt(final SClass klass) {
      final ObjectRef address = staticsAddresses.get(klass);
      if(address == null) {
         throw new IllegalStateException("no statics for " + klass);
      }
      return address;
   }

   @Override public void classAt(final SClass klass, final ObjectRef classAddress) {
      assert !classAddresses.containsKey(klass);
      classAddresses.put(klass, classAddress);
   }

   @Override public ObjectRef whereMyClassAt(final SClass klass) {
      final ObjectRef address = classAddresses.get(klass);
      if(address == null) {
         throw new IllegalStateException("no class for " + klass);
      }
      return address;
   }

   @Override public ObjectRef whereMyClassAt(final String internalName) {
      final SClass klass = load(internalName);
      return whereMyClassAt(klass);
   }

   @Override public SClass classClass() {
      return load(klassKlassName);
   }

   @Override public StaticsMarker staticsMarker(final SClass klass) {
      return new StaticsMarker(classClass(), klass);
   }
}
