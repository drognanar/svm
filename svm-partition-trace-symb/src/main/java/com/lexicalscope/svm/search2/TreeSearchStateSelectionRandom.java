package com.lexicalscope.svm.search2;

import com.lexicalscope.svm.search.Randomiser;

public class TreeSearchStateSelectionRandom implements TreeSearchStateSelection {
   private final Randomiser randomiser;
   private final StatesCollectionFactory statesCollectionFactory;

   public TreeSearchStateSelectionRandom(
         final Randomiser randomiser,
         final StatesCollectionFactory statesCollectionFactory) {
      this.randomiser = randomiser;
      this.statesCollectionFactory = statesCollectionFactory;
   }

   public TreeSearchStateSelectionRandom(final Randomiser randomiser) {
      this(randomiser, new ListStatesCollectionFactory());
   }

   @Override public TraceTree qnode(final FastRemovalList<TraceTree> qstatesAvailable) {
      return pickNode(qstatesAvailable);
   }

   @Override public TraceTree pnode(final FastRemovalList<TraceTree> pstatesAvailable) {
      return pickNode(pstatesAvailable);
   }

   private TraceTree pickNode(final FastRemovalList<TraceTree> statesAvailable) {
      final int node = randomiser.random(statesAvailable.size());
      return statesAvailable.get(node);
   }

   @Override public StatesCollection statesCollection(final TraceTreeSideObserver listener) {
      return statesCollectionFactory.statesCollection(randomiser, listener);
   }
}
