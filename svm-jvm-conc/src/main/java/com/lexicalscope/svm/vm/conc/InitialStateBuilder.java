package com.lexicalscope.svm.vm.conc;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;
import static com.lexicalscope.svm.stack.MethodScope.STATIC;
import static com.lexicalscope.svm.vm.j.InstructionCode.synthetic;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.classloading.AsmSClassLoader;
import com.lexicalscope.svm.classloading.ClassSource;
import com.lexicalscope.svm.classloading.SClassLoader;
import com.lexicalscope.svm.classloading.SelectingClassLoader;
import com.lexicalscope.svm.classloading.StaticsImpl;
import com.lexicalscope.svm.heap.HeapFactory;
import com.lexicalscope.svm.j.instruction.concrete.klass.DefineClassOp;
import com.lexicalscope.svm.j.instruction.concrete.klass.DefinePrimitiveClassesOp;
import com.lexicalscope.svm.j.instruction.concrete.klass.LoadingOp;
import com.lexicalscope.svm.j.instruction.concrete.nativ3.InitThreadOp;
import com.lexicalscope.svm.j.instruction.factory.BaseInstructionSourceFactory;
import com.lexicalscope.svm.j.instruction.factory.ConcInstructionFactory;
import com.lexicalscope.svm.j.instruction.factory.InstructionFactory;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource.InstructionSink;
import com.lexicalscope.svm.j.instruction.factory.InstructionSourceFactory;
import com.lexicalscope.svm.j.instruction.instrumentation.InstrumentationBuilder;
import com.lexicalscope.svm.j.instruction.instrumentation.MethodInstrumentor;
import com.lexicalscope.svm.j.natives.DefaultNativeMethods;
import com.lexicalscope.svm.j.natives.NativeMethods;
import com.lexicalscope.svm.j.statementBuilder.StatementBuilder;
import com.lexicalscope.svm.metastate.HashMetaState;
import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.metastate.MetaState;
import com.lexicalscope.svm.stack.DequeStack;
import com.lexicalscope.svm.stack.SnapshotableStackFrame;
import com.lexicalscope.svm.vm.StateSearch;
import com.lexicalscope.svm.vm.conc.checkingheap.CheckingHeapFactory;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.JStateImpl;
import com.lexicalscope.svm.vm.j.JavaConstants;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.StateTag;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class InitialStateBuilder {
   private InstructionFactory instructionFactory = new ConcInstructionFactory();
   private InstructionSourceFactory instructionSourceFactory = new BaseInstructionSourceFactory();
   private HeapFactory heapFactory = new CheckingHeapFactory();
   private final NativeMethods natives = DefaultNativeMethods.natives();
   private final InstrumentationBuilder instrumentationBuilder = new InstrumentationBuilder();
   private final MetaState metaState = new HashMetaState();

   public JStateImpl createInitialState(
         final StateTag stateTag,
         final StateSearch<JState> search,
         final ClassSource classSource,
         final SMethodDescriptor entryPointName,
         final Object... args) {
      final InstructionSource instructions = instructionSourceFactory.instructionSource(instructionFactory);
      final SClassLoader classLoader = new AsmSClassLoader(
            instructions,
            instrumentationBuilder.instrumentation(instructions),
            natives(),
            classSource);
      final SClassLoader mainLoader = classLoader;

      final StatementBuilder statements = statements(instructions);
      defineBootstrapClassesInstruction(statements.sink(), instructions);
      InitThreadOp.initThreadInstruction(statements);
      loadArgsInstruction(statements, args);

      final Instruction initialInstruction = statements.createInvokeStatic(entryPointName).buildInstruction();

      final DequeStack stack = new DequeStack();
      stack.push(new SnapshotableStackFrame(JavaConstants.INITIAL_FRAME_NAME, STATIC, initialInstruction, 0, entryPointName.argSize()));
      return new JStateImpl(stateTag, search, new StaticsImpl(mainLoader), stack, heapFactory().heap(), metaState.snapshot());
   }

   private void loadArgsInstruction(final StatementBuilder statements, final Object[] args) {
      for (final Object object : args) {
         statements.loadArg(object);
      }
   }

   private void defineBootstrapClassesInstruction(final InstructionSink sink, final InstructionSource instructions) {
      final List<KlassInternalName> bootstrapClasses = new ArrayList<>();
      bootstrapClasses.add(internalName(Class.class));
      bootstrapClasses.add(internalName(String.class));
      bootstrapClasses.add(internalName(Thread.class));
      sink.nextOp(new LoadingOp(new DefinePrimitiveClassesOp(new DefineClassOp(bootstrapClasses)), instructions), synthetic);
   }

   public InitialStateBuilder() {
      if(getClass().desiredAssertionStatus()) {
         heapFactory = new CheckingHeapFactory();
      } else {
         heapFactory = new FastHeapFactory();
      }
   }

   public static JvmBuilder jvm() { return new JvmBuilder(); }

   public InitialStateBuilder instructionFactory(final InstructionFactory instructionFactory) {
      this.instructionFactory = instructionFactory;
      return this;
   }

   public InitialStateBuilder instructionSourceFactory(final InstructionSourceFactory instructionSourceFactory) {
      this.instructionSourceFactory = instructionSourceFactory;
      return this;
   }

   public InitialStateBuilder instrument(final Matcher<? super SMethodDescriptor> methodMatcher, final MethodInstrumentor instrumentation) {
      instrumentationBuilder.instrument(methodMatcher, instrumentation);
      return this;
   }

   public NativeMethods natives() {
      return natives;
   }

   public HeapFactory heapFactory() {
      return heapFactory;
   }

   public InitialStateBuilder heapFactory(final HeapFactory heapFactory) {
      this.heapFactory = heapFactory;
      return this;
   }

   public <T> InitialStateBuilder meta(final MetaKey<T> key, final T initialMeta) {
      metaState.set(key, initialMeta);
      return this;
   }

   public static InitialStateBuilder initialState() {
      return new InitialStateBuilder();
   }
}
