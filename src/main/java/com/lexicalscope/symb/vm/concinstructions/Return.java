package com.lexicalscope.symb.vm.concinstructions;

import com.lexicalscope.symb.vm.Instruction;
import com.lexicalscope.symb.vm.Stack;
import com.lexicalscope.symb.vm.State;
import com.lexicalscope.symb.vm.classloader.SClassLoader;
import com.lexicalscope.symb.vm.instructions.ops.StackOp;

public class Return implements Instruction {
   private final int returnCount;

   public Return(final int returnCount) {
      this.returnCount = returnCount;
   }

   @Override
   public void eval(final SClassLoader cl, final State state) {
      state.op(new StackOp<Void>() {
		@Override
		public Void eval(Stack stack) {
			stack.popFrame(returnCount);
			
			return null;
		}
	  });
   }
   
   @Override
	public String toString() {
		return String.format("RETURN %s", returnCount);
	}
}
