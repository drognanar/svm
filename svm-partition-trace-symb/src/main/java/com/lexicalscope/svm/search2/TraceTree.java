package com.lexicalscope.svm.search2;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.*;
import static com.lexicalscope.svm.partition.trace.TraceBuilder.trace;
import static org.hamcrest.Matchers.*;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.search.ConstantRandomiser;
import com.lexicalscope.svm.vm.j.JState;

public class TraceTree {
   private final TreeSearchStateSelection stateSelection;
   private final Trace nodeTrace;
   private final StatesCollection pStates;
   private final StatesCollection qStates;
   private final LinkedHashMap<Object, TraceTree> children = new LinkedHashMap<>();
   private final TraceTreeObserver ttObserver;
   private FastListLocation pLocation;
   private FastListLocation qLocation;
   private BoolSymbol pPc = falsity();
   private BoolSymbol qPc = falsity();

   public TraceTree(
         final TreeSearchStateSelection stateSelection,
         final TraceTreeObserver ttObserver) {
      this(trace().build(), stateSelection, ttObserver);
   }

   public TraceTree(
         final Trace nodeTrace,
         final TreeSearchStateSelection stateSelection,
         final TraceTreeObserver ttObserver) {
      this.nodeTrace = nodeTrace;
      this.stateSelection = stateSelection;
      this.ttObserver = ttObserver;
      pStates = stateSelection.statesCollection(new TraceTreeSideObserver(){
         @Override public void stateAvailable() { pLocation = ttObserver.pstateAvailable(TraceTree.this); }
         @Override public void stateUnavailable() { ttObserver.pstateUnavailable(TraceTree.this); }});

      qStates = stateSelection.statesCollection(new TraceTreeSideObserver(){
         @Override public void stateAvailable() { qLocation = ttObserver.qstateAvailable(TraceTree.this); }
         @Override public void stateUnavailable() { ttObserver.qstateUnavailable(TraceTree.this); }});
   }

   public TraceTree() {
      this(new NullTraceTreeObserver());
   }

   public TraceTree(final TraceTreeObserver ttObserver) {
      this(new TreeSearchStateSelectionRandom(new ConstantRandomiser(0)), ttObserver);
   }

   public Trace nodeTrace() {
      return nodeTrace;
   }

   public StatesCollection pStates() {
      return pStates;
   }

   public void pState(final JState state) {
      pStates.add(state);
   }

   public void qState(final JState state) {
      qStates.add(state);
   }

   public StatesCollection qStates() {
      return qStates;
   }

   public TraceTree child(final Trace trace) {
      return child(trace, trace);
   }

   public TraceTree child(final Trace trace, Object key) {
      if(!children.containsKey(key)) {
         final TraceTree child = new TraceTree(trace, stateSelection, ttObserver);
         children.put(key, child);
      }
      return children.get(key);
   }

   public Collection<TraceTree> children() {
      return children.values();
   }

   private static FeatureMatcher<TraceTree, Iterable<JState>> pStates(final Matcher<? super Iterable<JState>> contains) {
      return new FeatureMatcher<TraceTree, Iterable<JState>>(contains, "p state collection", "pStates") {
         @Override protected Iterable<JState> featureValueOf(final TraceTree actual) {
            return actual.pStates();
         }
      };
   }

   private static FeatureMatcher<TraceTree, Iterable<JState>> qStates(final Matcher<? super Iterable<JState>> contains) {
      return new FeatureMatcher<TraceTree, Iterable<JState>>(contains,
            "q state collection",
            "qStates") {
         @Override protected Iterable<JState> featureValueOf(final TraceTree actual) {
            return actual.qStates();
         }
      };
   }

   public static Matcher<TraceTree> containsPState(final JState state) {
      return pStates(contains(state));
   }

   public static Matcher<TraceTree> containsQState(final JState state) {
      return qStates(contains(state));
   }

   public static Matcher<? super TraceTree> noQStates() {
      return qStates(emptyIterableOf(JState.class));
   }

   public static Matcher<? super TraceTree> noPStates() {
      return pStates(emptyIterableOf(JState.class));
   }

   public static Matcher<? super TraceTree> nodeTrace(final Trace trace) {
      return nodeTrace(equalTo(trace));
   }

   private static Matcher<TraceTree> nodeTrace(final Matcher<Trace> equalTo) {
      return new FeatureMatcher<TraceTree, Trace>(equalTo, "trace of node", "nodeTrace") {
         @Override protected Trace featureValueOf(final TraceTree actual) {
            return actual.nodeTrace();
         }
      };
   }

   public static Matcher<? super TraceTree> containsChild(final TraceTree child) {
      return children(contains(child));
   }

   public static Matcher<TraceTree> children(final Matcher<? super Collection<TraceTree>> contains) {
      return new FeatureMatcher<TraceTree, Collection<TraceTree>>(contains, "tree children", "treeChildren") {
         @Override protected Collection<TraceTree> featureValueOf(final TraceTree actual) {
            return actual.children();
         }
      };
   }

   public FastListLocation pLocation() {
      return pLocation;
   }

   public FastListLocation qLocation() {
      return qLocation;
   }

   public BoolSymbol pPc() {
      return pPc;
   }

   public BoolSymbol qPc() {
      return qPc;
   }

   public void disjoinP(final BoolSymbol pc) {
      pPc = or(pPc, pc);
   }

   public void disjoinQ(final BoolSymbol pc) {
      qPc = or(qPc, pc);
   }

   @Override public String toString() {
      return String.format("(traceTree %s %s)", nodeTrace, children());
   }
}