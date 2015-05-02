package com.lexicalscope.svm.vm.j.klass;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lexicalscope.svm.vm.j.KlassInternalName;

public final class Fields {
   private final List<SField> fields;
   private final Map<SFieldName, Integer> map;

   public Fields() {
      this(new ArrayList<SField>(), new LinkedHashMap<SFieldName, Integer>());
   }

   public Fields(final ArrayList<SField> fields, final LinkedHashMap<SFieldName, Integer> fieldMap) {
      this.fields = fields;
      this.map = fieldMap;
   }

   public int indexOf(final SFieldName name) {
      assert map.containsKey(name) : "cannot find " + name + " in " + map;
      return map.get(name);
   }

   public int indexOf(final String name) {
      for (final Entry<SFieldName, Integer> field : map.entrySet()) {
         if(field.getKey().getName().equals(name)) {
            return field.getValue();
         }
      }
      throw new IndexOutOfBoundsException();
   }

   public SField get(final int index) {
      return fields.get(index);
   }

   public boolean contains(final SFieldName name) {
      return map.containsKey(name);
   }

   public final Fields copy() {
      return new Fields(new ArrayList<>(fields), new LinkedHashMap<>(map));
   }

   public Fields extend(final KlassInternalName subclassName, final DeclaredFields declaredFields) {
      final Fields result = copy();
      for (final Entry<SFieldName, Integer> superField : map.entrySet()) {
         // if a field is not shadowed, looking it up in this class should resolve the superclass field
         result.map.put(new SFieldName(subclassName, superField.getKey().getName()), superField.getValue());
      }
      for (final SField field : declaredFields.dynamicFields()) {
         result.fields.add(field);
         result.map.put(field.name(), result.fields.size() - 1);
      }
      return result;
   }

   @Override public String toString() {
      return map.toString();
   }

   public int count() {
      return fields.size();
   }
}
