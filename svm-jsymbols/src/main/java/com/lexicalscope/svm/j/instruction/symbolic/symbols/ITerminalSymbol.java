package com.lexicalscope.svm.j.instruction.symbolic.symbols;


public class ITerminalSymbol implements ISymbol {
   private final String name;

   public ITerminalSymbol(final int name) {
      this(("i" + name).intern());
   }

   public ITerminalSymbol(final String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return String.format("#%s", name);
   }

   @Override
   public int hashCode() {
      return name.hashCode();
   }

   @Override
   public boolean equals(final Object obj) {
      if (obj != null && obj.getClass().equals(this.getClass())) {
         return name.equals(((ITerminalSymbol) obj).name);
      }
      return false;
   }

   @Override
   public <T, E extends Throwable> T accept(final SymbolVisitor<T, E> visitor) throws E {
      return visitor.intSymbol(name);
   }

    public Object getValue() { return this; }
}
