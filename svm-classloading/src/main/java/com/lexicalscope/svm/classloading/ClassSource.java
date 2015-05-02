package com.lexicalscope.svm.classloading;

import java.net.URL;

import com.lexicalscope.svm.vm.j.KlassInternalName;

public interface ClassSource {
   URL loadFromRepository(KlassInternalName name);
}
