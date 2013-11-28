package com.lexicalscope.symb.vm.concinstructions.ops;

import com.lexicalscope.symb.vm.instructions.NullaryOperator;

public class IConstOperator implements NullaryOperator {
	private final int val;

   public IConstOperator(final int val) {
      this.val = val;
   }

   @Override
	public Object eval() {
		return val;
	}

	@Override
	public String toString() {
		return "ICONST_" + val;
	}
}
