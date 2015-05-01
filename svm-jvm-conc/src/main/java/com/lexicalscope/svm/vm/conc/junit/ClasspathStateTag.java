package com.lexicalscope.svm.vm.conc.junit;

import com.lexicalscope.svm.vm.j.StateTag;

public class ClasspathStateTag implements StateTag {
    private String classPath;

    public ClasspathStateTag(String classPath) {
        this.classPath = classPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClasspathStateTag that = (ClasspathStateTag) o;

        return !(classPath != null ? !classPath.equals(that.classPath) : that.classPath != null);

    }

    @Override
    public int hashCode() {
        return classPath != null ? classPath.hashCode() : 0;
    }
}
