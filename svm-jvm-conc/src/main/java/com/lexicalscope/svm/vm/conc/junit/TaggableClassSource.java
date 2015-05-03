package com.lexicalscope.svm.vm.conc.junit;

import com.lexicalscope.svm.classloading.ClassSource;
import com.lexicalscope.svm.classloading.ClasspathClassRepository;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.StateTag;

import java.net.URL;

import static com.lexicalscope.svm.classloading.ClasspathClassRepository.classpathClassRepository;

public class TaggableClassSource implements StateTag, ClassSource {
    private final StateTag tag;
    private final ClassSource source;

    public TaggableClassSource() {
        this(new StateTag() {}, new ClasspathClassRepository());
    }

    public TaggableClassSource(StateTag tag, ClassSource source) {
        this.tag = tag;
        this.source = source;
    }

    @Override
    public URL loadFromRepository(KlassInternalName name) {
        return source.loadFromRepository(name);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TaggableClassSource)) {
            return false;
        }

        TaggableClassSource other = (TaggableClassSource) obj;
        return tag.equals(other.tag);
    }

    public static TaggableClassSource loadFromTheseClasses(Class<?>... classes) {
        StateTag classStateTag = new ClassStateTag(classes[0]);
        ClassSource classpathClassRepository = classpathClassRepository(classes);
        return new TaggableClassSource(classStateTag, classpathClassRepository);
    }

    public static TaggableClassSource loadFromClassPath(String classPath) {
        StateTag classStateTag = new ClasspathStateTag(classPath);
        ClassSource classPathSource = classpathClassRepository(classPath);
        return new TaggableClassSource(classStateTag, classPathSource);
    }
}
