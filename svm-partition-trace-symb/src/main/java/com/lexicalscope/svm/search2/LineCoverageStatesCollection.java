package com.lexicalscope.svm.search2;

import static com.lexicalscope.svm.search2.LineCoverageStatesCollection.ExploreCountMetaKey.EXPLORE_COUNT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.vm.j.JState;

public class LineCoverageStatesCollection implements StatesCollection {
   public static class ExploreCountMetaKey implements com.lexicalscope.svm.metastate.MetaKey<Integer> {
      public static final com.lexicalscope.svm.metastate.MetaKey<Integer> EXPLORE_COUNT = new ExploreCountMetaKey();
      
      private ExploreCountMetaKey() { }
      
      public Class<Integer> valueType() {
         return Integer.class;
      }
   }

   private final Map<Integer, List<JState>> states = new TreeMap<Integer, List<JState>>();
   private final TraceTreeSideObserver listener;

   public LineCoverageStatesCollection(final TraceTreeSideObserver listener) {
      this.listener = listener;
   }

   @Override public Iterator<JState> iterator() {
      return new Iterator<JState>() {
         private final Iterator<List<JState>> mapIterator = states.values().iterator();
         private Iterator<JState> listIterator;

         @Override public boolean hasNext() {
            return mapIterator.hasNext() || listIterator.hasNext();
         }

         @Override public JState next() {
            if(listIterator == null || !listIterator.hasNext()) {
               listIterator = mapIterator.next().iterator();
            }
            return listIterator.next();
         }

         @Override public void remove() {
            listIterator.remove();
         }
      };
   }

   @Override public void add(final JState state) {
      final boolean empty = states.isEmpty();

      if(!state.instruction().contains(EXPLORE_COUNT)) {
         state.instruction().set(EXPLORE_COUNT, 0);
      }

      final Integer exploreCount = state.instruction().get(EXPLORE_COUNT);
      if(!states.containsKey(exploreCount)) {
         states.put(exploreCount, new ArrayList<JState>());
      }
      states.get(exploreCount).add(state);

      if(empty) {
         listener.stateAvailable();
      }
   }

   @Override public JState pickState() {
      final Iterator<Entry<Integer, List<JState>>> iterator = states.entrySet().iterator();
      final Entry<Integer, List<JState>> entry = iterator.next();

      final JState result = entry.getValue().remove(0);
      if(entry.getValue().isEmpty()) {
         iterator.remove();
      }

      result.instruction().set(EXPLORE_COUNT, result.instruction().get(EXPLORE_COUNT) + 1);

      if(states.isEmpty()) {
         listener.stateUnavailable();
      }

      return result;
   }
}
