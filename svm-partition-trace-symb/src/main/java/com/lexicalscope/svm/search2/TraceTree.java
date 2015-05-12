package com.lexicalscope.svm.search2;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.*;
import static com.lexicalscope.svm.partition.trace.TraceBuilder.trace;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.vm.j.JState;

public class TraceTree {
   private final Trace nodeTrace;
   private final List<JState> pStates = new ArrayList<>();
   private final List<JState> qStates = new ArrayList<>();
   private final LinkedHashMap<Trace, TraceTree> children = new LinkedHashMap<>();
   private TraceTreeObserver ttObserver;
   private BoolSymbol pPc = falsity();
   private BoolSymbol qPc = falsity();

   public TraceTree() {
      this(trace().build());
   }

   public TraceTree(final Trace nodeTrace) {
      this.nodeTrace = nodeTrace;
      ttObserver = new NullTraceTreeObserver();
   }

   public Trace nodeTrace() {
      return nodeTrace;
   }

   protected List<JState> pStates() {
      return pStates;
   }

   public void pState(final JState state) {
      pStates.add(state);
      if(pStates.size() == 1) {
         ttObserver.pstateAvailable(this);
      }
   }

   public JState removePState(final int i) {
      final JState result = pStates.remove(i);
      if(pStates.isEmpty()) {
         ttObserver.pstateUnavailable(this);
      }
      return result;
   }

   public void qState(final JState state) {
      qStates.add(state);
      if(qStates.size() == 1) {
         ttObserver.qstateAvailable(this);
      }
   }

   public JState removeQState(final int i) {
      final JState result = qStates.remove(i);
      if(qStates.isEmpty()) {
         ttObserver.qstateUnavailable(this);
      }
      return result;
   }

   public List<JState> qStates() {
      return qStates;
   }

   public TraceTree child(final Trace trace) {
      if(!children.containsKey(trace)) {
         final TraceTree child = new TraceTree(trace);
         child.listener(ttObserver);
         children.put(trace, child);

         //System.out.println("adding child " + this);
      }
      return children.get(trace);
   }

   public Collection<TraceTree> children() {
      return children.values();
   }

   public void listener(final TraceTreeObserver ttObserver) {
      this.ttObserver = ttObserver;
   }

   private static FeatureMatcher<TraceTree, Collection<JState>> pStates(final Matcher<? super Collection<JState>> contains) {
      return new FeatureMatcher<TraceTree, Collection<JState>>(contains, "p state collection", "pStates") {
         @Override protected Collection<JState> featureValueOf(final TraceTree actual) {
            return actual.pStates();
         }
      };
   }

   private static FeatureMatcher<TraceTree, Collection<JState>> qStates(final Matcher<? super Collection<JState>> contains) {
      return new FeatureMatcher<TraceTree, Collection<JState>>(contains,
            "q state collection",
            "qStates") {
         @Override protected Collection<JState> featureValueOf(final TraceTree actual) {
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
      return qStates(emptyCollectionOf(JState.class));
   }

   public static Matcher<? super TraceTree> noPStates() {
      return pStates(emptyCollectionOf(JState.class));
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