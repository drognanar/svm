package com.lexicalscope.svm.classloading;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import java.util.*;

import com.lexicalscope.svm.classloading.asm.AsmSClass;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.JavaConstants;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Statics;
import com.lexicalscope.svm.vm.j.StaticsMarker;
import com.lexicalscope.svm.vm.j.klass.DeclaredFields;
import com.lexicalscope.svm.vm.j.klass.DeclaredMethods;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class StaticsImpl implements Statics {
   // TODO[tim]: need fast-clone version
   private static final KlassInternalName klassKlassName = internalName(Class.class);
   private final Map<KlassInternalName, SClass> defined;

   // TODO[tim]: combine these maps for efficiency
   private final Map<SClass, ObjectRef> staticsAddresses;
   private final Map<SClass, ObjectRef> classAddresses;

   private final SClassLoader classLoader;

   public StaticsImpl(final SClassLoader classLoader) {
      this(classLoader,
            new HashMap<KlassInternalName, SClass>(),
            new HashMap<SClass, ObjectRef>(),
            new HashMap<SClass, ObjectRef>());
   }

   private StaticsImpl(
         final SClassLoader classLoader,
         final Map<KlassInternalName, SClass> defined,
         final Map<SClass, ObjectRef> staticsAddresses,
         final Map<SClass, ObjectRef> classAddresses) {
       this.defined = defined;
       this.classLoader = classLoader;
       this.staticsAddresses = staticsAddresses;
       this.classAddresses = classAddresses;
   }

   @Override public Statics snapshot() {
      return new StaticsImpl(
            classLoader,
            new HashMap<>(defined),
            new HashMap<>(staticsAddresses),
            new HashMap<>(classAddresses));
   }

   @Override public List<SClass> defineClass(final KlassInternalName klassName) {
      if(isDefined(klassName)) {
         throw new DuplicateClassDefinitionException(defined.get(klassName));
      }

      final List<SClass> result = new ArrayList<>();
      final SClass loaded = classLoader.load(klassName);
      cache(loaded);
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

   @Override public SClass definePrimitiveClass(final KlassInternalName klassName) {
      if(isDefined(klassName)) {
         throw new DuplicateClassDefinitionException(defined.get(klassName));
      }

      // TODO[tim]: sort out this use of nulls
      final SClass componentType = klassName.isArrayClass() ? load(internalName(klassName.componentType())) : null;

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


   private SClass cache(final SClass result) {
      return cache(result.name(), result);
   }

   private SClass cache(final KlassInternalName klassName, final SClass result) {
      defined.put(klassName, result);
      return result;
   }

   @Override public boolean isDefined(final KlassInternalName klass) {
      return defined.containsKey(klass);
   }

   @Override public SClass load(final KlassInternalName klassName) {
      if(!isDefined(klassName)) {
         throw new MissingClassDefinitionException(klassName, defined);
      }
      return defined.get(klassName);
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

   @Override public ObjectRef whereMyClassAt(final KlassInternalName internalName) {
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
