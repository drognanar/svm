package com.lexicalscope.svm.vm.conc;

import com.lexicalscope.svm.classloading.ClassSource;
import com.lexicalscope.svm.j.natives.NativeMethodDef;
import com.lexicalscope.svm.vm.SearchLimits;
import com.lexicalscope.svm.vm.StateCountSearchLimit;
import com.lexicalscope.svm.vm.StateSearch;
import com.lexicalscope.svm.vm.Vm;
import com.lexicalscope.svm.vm.VmImpl;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.StateTag;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

import java.util.Set;

public final class JvmBuilder {
   private StateSearchFactory searchFactory = new DepthFirstStateSearchFactory();
   private InitialStateBuilder initialStateBuilder = new InitialStateBuilder();

   public static JvmBuilder jvm() { return new JvmBuilder(); }

    public Vm<JState> build(
            final StateTag[] tags,
            final ClassSource[] classSources,
            final SMethodDescriptor entryPointName,
            final SearchLimits searchLimits,
            final Object... args) {
        final StateSearch<JState> search = searchFactory.search();
        for (int i = 0; i < classSources.length; i++) {
            final ClassSource classSource = classSources[i];
            final StateTag tag = tags[i];
            search.consider(initialStateBuilder.createInitialState(tag, search, classSource, entryPointName, args));
        }
        return new VmImpl<JState>(search, searchLimits);
    }

   public Vm<JState> build(
         final StateTag[] tags,
         final ClassSource[] classSources,
         final SMethodDescriptor entryPointName,
         final Object... args) {
       return build(tags, classSources, entryPointName, new StateCountSearchLimit(), args);
   }

   public <T> JvmBuilder initialState(final InitialStateBuilder initialStateBuilder) {
      this.initialStateBuilder = initialStateBuilder;
      return this;
   }

   public JvmBuilder searchWith(final StateSearchFactory searchFactory) {
      this.searchFactory = searchFactory;
      return this;
   }

   public void addNative(NativeMethodDef nativeMethod) {
       this.initialState().natives();
   }

   public InitialStateBuilder initialState() {
      return this.initialStateBuilder;
   }
}
