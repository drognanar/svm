package com.lexicalscope.svm.j.instruction.concrete.object;

import com.lexicalscope.svm.metastate.MetaKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the mapping of parameters passed to abstract objects.
 */
public class SymbolicParametersMetaKey implements MetaKey<Map> {
    public static final SymbolicParametersMetaKey S_PARAMETERS = new SymbolicParametersMetaKey();

    public static Map addMapping(Map passedParameters, Object receiver, Object passedClass, Object passedObject) {
        HashMap classToObjectMapping = copyOrCreateMap(passedParameters);
        HashMap objectToInstancesMapping = copyOrCreateMap(passedParameters.get(receiver));
        ArrayList instancesMapping = copyOrCreateList(objectToInstancesMapping.get(passedClass));

        instancesMapping.add(passedObject);
        objectToInstancesMapping.put(passedClass, instancesMapping);
        classToObjectMapping.put(receiver, objectToInstancesMapping);
        return classToObjectMapping;
    }

    private static HashMap copyOrCreateMap(Object o) {
        Map map = (Map) o;
        return map == null
                ? new HashMap()
                : new HashMap(map);
    }

    private static ArrayList copyOrCreateList(Object o) {
        ArrayList list = (ArrayList) o;
        return list == null
                ? new ArrayList()
                : new ArrayList(list);
    }

    public static int countObjects(Map passedParameters, Object receiver, Object passedClass) {
        List<Object> possibleValues = getPossibleValues(passedParameters, receiver, passedClass);
        if (possibleValues == null) {
            return 0;
        }
        return possibleValues.size();
    }

    public static List<Object> getPossibleValues(Map passedParameters, Object receiver, Object passedClass) {
        if (!passedParameters.containsKey(receiver)) {
            return null;
        }

        Map passedObjectParameters = (Map) passedParameters.get(receiver);
        if (!passedObjectParameters.containsKey(passedClass)) {
            return null;
        }
        List possibleValues = (List) passedObjectParameters.get(passedClass);
        return possibleValues;
    }

    public static List<Object> getObject(Map passedParameters, Object receiver, Object passedClass) {
        List<Object> possibleValues = getPossibleValues(passedParameters, receiver, passedClass);
        assert possibleValues != null: "No parameters were ever passed to the receiver.";
        return possibleValues;
    }

    private SymbolicParametersMetaKey() {}

    @Override public Class<Map> valueType() {
        return Map.class;
    }

    @Override public String toString() {
        return "SymbolicParameters";
    }
}
