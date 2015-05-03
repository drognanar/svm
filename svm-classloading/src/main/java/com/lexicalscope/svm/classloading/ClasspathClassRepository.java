package com.lexicalscope.svm.classloading;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.lexicalscope.svm.vm.j.KlassInternalName;

public final class ClasspathClassRepository implements ClassSource {
   private final ClassLoader classLoader;

   public ClasspathClassRepository() {
      this(ClasspathClassRepository.class.getClassLoader());
   }

   public ClasspathClassRepository(final ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public URL loadFromRepository(final String name) {
      return loadFromRepository(internalName(name));
   }

   @Override public URL loadFromRepository(final KlassInternalName name) {
      assert !name.string().contains(".");
      return classLoader
            .getResource(name + ".class");
   }

   public static ClasspathClassRepository classpathClassRepostory(final Class<?> loadFromWhereverThisWasLoaded) {
      return classpathClassRepository(new Class[]{loadFromWhereverThisWasLoaded});
   }

   public static ClasspathClassRepository classpathClassRepository(final Class<?>[] loadFromWhereverTheseWereLoaded) {
       return classpathClassRepository(getClasspathURLs(loadFromWhereverTheseWereLoaded));
   }

    public static ClasspathClassRepository classpathClassRepository(String classPath) {
        return classpathClassRepository(getClasspathURLs(classPath));
    }

    private static URL[] getClasspathURLs(String classPath) {
        String[] classPaths = classPath.split(":");
        URL[] urls = new URL[classPaths.length];
        for (int i = 0; i < classPaths.length; i++) {
            try {
                urls[i] = new File(classPaths[i]).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private static URL[] getClasspathURLs(Class<?>[] loadFromWhereverTheseWereLoaded) {
        URL[] urls = new URL[loadFromWhereverTheseWereLoaded.length];
        for (int i = 0; i < loadFromWhereverTheseWereLoaded.length; i++) {
            urls[i] = JarClassRepository.urlOfJarFile(loadFromWhereverTheseWereLoaded[i]);
        }
        return urls;
    }

    public static ClasspathClassRepository classpathClassRepository(final URL[] loadFromWhereverTheseWereLoaded) {
      return new ClasspathClassRepository(new URLClassLoader(loadFromWhereverTheseWereLoaded, null));
   }
}
