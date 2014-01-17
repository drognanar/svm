package com.lexicalscope.symb.vm.instructions.ops;

import com.lexicalscope.symb.vm.Context;
import com.lexicalscope.symb.vm.Vop;

public class NullaryOp implements Vop {
	private final NullaryOperator operator;

	public NullaryOp(final NullaryOperator operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return operator.toString();
	}

   @Override public void eval(final Context ctx) {
      ctx.push(operator.eval());
   }
}
