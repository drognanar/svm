package com.lexicalscope.svm.j.instruction.symbolic.ops.natives;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.symbolic.ops.array.NewSymbArray;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.natives.AbstractNativeMethodDef;
import com.lexicalscope.svm.j.natives.NativeMethodDef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

public class Symbolic_Java_lang_system_arraycopy extends AbstractNativeMethodDef implements NativeMethodDef {
    public Symbolic_Java_lang_system_arraycopy() {
        super("java/lang/System", "arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V");
    }

    @Override public MethodBody instructions(final InstructionSource instructions) {
        return statements(instructions).maxLocals(5).linearOp(new SymbolicArrayCopyOp()).returnVoid(name()).build();
    }

    private class SymbolicArrayCopyOp implements Vop {
        @Override
        public void eval(JState ctx) {
            final Object lengthObject = ctx.pop();
            final int destPos = (int) ctx.pop();
            final ObjectRef dest = (ObjectRef) ctx.pop();
            final int  srcPos = (int) ctx.pop();
            final ObjectRef src = (ObjectRef) ctx.pop();

            if (lengthObject instanceof ISymbol) {
                symbolicArrayCopy(ctx, lengthObject, destPos, dest, srcPos, src);
            } else {
                concreteArrayCopy(ctx, (int) lengthObject, destPos, dest, srcPos, src);
            }
        }

        public void symbolicArrayCopy(JState ctx, Object lengthObject, int destPos, ObjectRef dest, int srcPos, ObjectRef src) {
            if (destPos != 0 || srcPos != 0) {
                throw new RuntimeException("Cannot copy a symbolic array with an offset.");
            }

            ctx.put(dest, NewArrayOp.ARRAY_LENGTH_OFFSET, lengthObject);
            ctx.put(dest, NewSymbArray.ARRAY_SYMBOL_OFFSET, ctx.get(src, NewSymbArray.ARRAY_SYMBOL_OFFSET));
        }

        public void concreteArrayCopy(JState ctx, int lengthObject, int destPos, ObjectRef dest, int srcPos, ObjectRef src) {
            int length = lengthObject;
            for (int i = 0; i < length; i++) {
                // TODO[tim]: check bounds
                final Object val = ctx.get(src, NewArrayOp.ARRAY_PREAMBLE + srcPos + i);
                ctx.put(dest, NewArrayOp.ARRAY_PREAMBLE + destPos + i, val);
            }
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.arraycopy();
        }
    }
}
