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
        HashMap originalMap = (HashMap) passedParameters;
        Map resultMap = (Map) originalMap.clone();
        if (!resultMap.containsKey(receiver)) {
            resultMap.put(receiver, new HashMap());
        }
        Map passedObjectParameters = (Map) resultMap.get(receiver);
        if (!passedObjectParameters.containsKey(passedClass)) {
            passedObjectParameters.put(passedClass, new ArrayList());
        }

        ArrayList possibleValues = (ArrayList) passedObjectParameters.get(passedClass);
        possibleValues.add(passedObject);
        return resultMap;
    }

    public static int countObjects(Map passedParameters, Object receiver, Object passedClass) {
        return getObject(passedParameters, receiver, passedClass).size();
    }

    public static List<Object> getObject(Map passedParameters, Object receiver, Object passedClass) {
        assert passedParameters.containsKey(receiver): "No parameters were ever passed to the receiver.";
        Map passedObjectParameters = (Map) passedParameters.get(receiver);
        assert passedObjectParameters.containsKey(passedClass): "No instance of a given class was ever passed to the object.";
        List possibleValues = (List) passedObjectParameters.get(passedClass);
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
