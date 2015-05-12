package com.lexicalscope.svm.search2;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class TraceTreeSearchState {
   private TraceTree currentlySearching;
   private Side side;

   public TraceTreeSearchState(final TraceTree root) {
      this.currentlySearching = root;
      side = Side.QSIDE;
   }

   protected TraceTree currentNode() {
      return currentlySearching;
   }

   protected Side currentSide() {
      return side;
   }

   public void search(final Side side, final TraceTree tt) {
      this.side = side;
      currentlySearching = tt;
   }

   public static Matcher<? super TraceTreeSearchState> currentlySearching(final TraceTree tt) {
      return currentlySearching(equalTo(tt));
   }

   private static Matcher<TraceTreeSearchState> currentlySearching(final Matcher<TraceTree> equalTo) {
      return new FeatureMatcher<TraceTreeSearchState, TraceTree>(equalTo, "current tree node being searched", "current tree node") {
         @Override protected TraceTree featureValueOf(final TraceTreeSearchState actual) {
            return actual.currentNode();
         }
      };
   }

   public static Matcher<? super TraceTreeSearchState> currentSideIs(final Side side) {
      return currentSideIs(equalTo(side));
   }

   public static Matcher<? super TraceTreeSearchState> currentSideIs(final Matcher<Side> equalTo) {
      return new FeatureMatcher<TraceTreeSearchState, Side>(equalTo, "current side", "currentSide") {
         @Override protected Side featureValueOf(final TraceTreeSearchState actual) {
            return actual.currentSide();
         }
      };
   }
}