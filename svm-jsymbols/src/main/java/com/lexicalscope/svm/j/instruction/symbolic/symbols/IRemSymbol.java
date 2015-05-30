package com.lexicalscope.svm.j.instruction.symbolic.symbols;

public class IRemSymbol implements ISymbol {
    private final ISymbol left;
    private final ISymbol right;

    public IRemSymbol(ISymbol left, ISymbol right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IRemSymbol that = (IRemSymbol) o;

        if (left != null ? !left.equals(that.left) : that.left != null) return false;
        return !(right != null ? !right.equals(that.right) : that.right != null);

    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("(rem %s %s)", left, right);
    }

    @Override
    public <T, E extends Throwable> T accept(SymbolVisitor<T, E> visitor) throws E {
        return visitor.rem(left, right);
    }
}
