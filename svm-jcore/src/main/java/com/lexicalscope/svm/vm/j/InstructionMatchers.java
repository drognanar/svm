package com.lexicalscope.svm.vm.j;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.CombinableMatcher.both;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import com.lexicalscope.MatchersAdditional;

public final class InstructionMatchers {
   public static Matcher<Instruction> instructionSequence(final InstructionCode ... codes) {
      // TODO[tim]: need a list version of MatchersAdditional has
      return MatchersAdditional.has(instructionCodes(codes).toArray(new Matcher[]{})).only().inOrder();
   }

   @SafeVarargs
   public static Matcher<Iterable<Instruction>> instructionSequence(final Matcher<Instruction> ... matchers) {
      return MatchersAdditional.has(matchers).only().inOrder();
   }

   public static List<Matcher<Instruction>> instructionCodes(final InstructionCode ... codes) {
      final List<Matcher<Instruction>> result = new ArrayList<Matcher<Instruction>>(codes.length);
      for (final InstructionCode instructionCode : codes) {
         result.add(instructionCode(equalTo(instructionCode)));
      }
      return result;
   }

   public static Matcher<Instruction> instructionCode(final Matcher<InstructionCode> subMatcher) {
      return new FeatureMatcher<Instruction, InstructionCode>(subMatcher, "instruction code", "instructionCode") {
         @Override protected InstructionCode featureValueOf(final Instruction actual) {
            return actual.code();
         }
      };
   }

   public static Matcher<Instruction> instructionLine(final Matcher<Integer> subMatcher) {
      return new FeatureMatcher<Instruction, Integer>(subMatcher, "instruction line", "instructionLine") {
         @Override protected Integer featureValueOf(final Instruction actual) {
            return actual.line();
         }
      };
   }

   public static Matcher<Instruction> instructionAt(final InstructionCode code, final int line) {
      return both(instructionCode(equalTo(code))).and(instructionLine(equalTo(line)));
   }
}