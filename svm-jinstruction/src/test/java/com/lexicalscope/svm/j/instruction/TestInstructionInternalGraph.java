package com.lexicalscope.svm.j.instruction;

import static com.lexicalscope.MatchersAdditional.has;
import static com.lexicalscope.svm.vm.j.InstructionCode.synthetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import com.lexicalscope.svm.vm.j.Instruction;

public class TestInstructionInternalGraph {
   final Instruction node1 = new InstructionInternal(new NoOp("1"), synthetic, -1);
   final Instruction node2 = new InstructionInternal(new NoOp("2"), synthetic, -1);
   final Instruction node3 = new InstructionInternal(new NoOp("3"), synthetic, -1);
   final Instruction node4 = new InstructionInternal(new NoOp("4"), synthetic, -1);
   final Instruction node5 = new InstructionInternal(new NoOp("5"), synthetic, -1);

   final Instruction nodeE = new InstructionInternal(new NoOp("E"), synthetic, -1);
   final Instruction nodeF = new InstructionInternal(new NoOp("F"), synthetic, -1);

   @Test public void instructionsAreAppendedAtTheEndOfTheChain() {
      node1.append(node2);
      node1.append(node3);

      assertThat(node1.next(), sameInstance(node2));
      assertThat(node2.next(), sameInstance(node3));
      assertThat(node2.prev(), sameInstance(node1));
      assertThat(node3.prev(), sameInstance(node2));
   }

   @Test public void instructionsAreInsertedDirectlyAfter() {
      node1.append(node2).append(node3).append(node4).append(node5);

      node3.insertNext(nodeE);

      assertThat(node3.next(), sameInstance(nodeE));
      assertThat(nodeE.next(), sameInstance(node4));
      assertThat(nodeE.prev(), sameInstance(node3));
      assertThat(node4.prev(), sameInstance(nodeE));
   }

   @Test public void instructionListIsInsertedDirectlyAfter() {
      node1.append(node2).append(node3).append(node4).append(node5);

      nodeE.append(nodeF);

      node3.insertNext(nodeE);

      assertThat(node3.next(), sameInstance(nodeE));
      assertThat(nodeE.next(), sameInstance(nodeF));
      assertThat(nodeF.next(), sameInstance(node4));
      assertThat(nodeE.prev(), sameInstance(node3));
      assertThat(node4.prev(), sameInstance(nodeF));
   }

   @Test public void instructionsAreInsertedDirectlyBefore() {
      node1.append(node2).append(node3).append(node4).append(node5);

      node3.insertHere(nodeE);

      assertThat(node2.next(), sameInstance(nodeE));
      assertThat(nodeE.next(), sameInstance(node3));
      assertThat(nodeE.prev(), sameInstance(node2));
      assertThat(node3.prev(), sameInstance(nodeE));
   }

   @Test public void instructionListIsInsertedDirectlyBefore() {
      node1.append(node2).append(node3).append(node4).append(node5);

      nodeE.append(nodeF);

      node3.insertHere(nodeE);

      assertThat(node2.next(), sameInstance(nodeE));
      assertThat(nodeE.next(), sameInstance(nodeF));
      assertThat(nodeF.next(), sameInstance(node3));
      assertThat(nodeE.prev(), sameInstance(node2));
      assertThat(node3.prev(), sameInstance(nodeF));
   }

   @Test public void whenInsertingBeforeBranchTargetsAreFixedUp() {
      node1.append(node2).append(node3).append(node4).append(node5);

      node2.jmpTarget(node5);
      node3.jmpTarget(node5);

      node5.insertHere(nodeE);

      assertThat(node2.jmpTarget(), sameInstance(nodeE));
      assertThat(node3.jmpTarget(), sameInstance(nodeE));
      assertThat(node5.targetOf(), empty());
      assertThat(nodeE.targetOf(), has(sameInstance(node2), sameInstance(node3)).only().inOrder());
   }

   @Test public void instructionListInsertedAsReplacement() {
      node1.append(node2).append(node3).append(node4).append(node5);

      nodeE.append(nodeF);

      node3.replaceWith(nodeE);

      assertThat(node3.next(), nullValue());
      assertThat(node3.prev(), nullValue());

      assertThat(node2.next(), sameInstance(nodeE));
      assertThat(nodeE.next(), sameInstance(nodeF));
      assertThat(nodeF.next(), sameInstance(node4));
      assertThat(nodeE.prev(), sameInstance(node2));
      assertThat(node4.prev(), sameInstance(nodeF));
   }
}

