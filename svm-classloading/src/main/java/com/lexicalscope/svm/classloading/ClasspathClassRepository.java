package com.lexicalscope.svm.classloading;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public final class ClasspathClassRepository implements ClassSource {
   private final ClassLoader classLoader;

   public ClasspathClassRepository() {
      this(ClasspathClassRepository.class.getClassLoader());
   }

   public ClasspathClassRepository(final ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   @Override public URL loadFromRepository(final String name) {
      return classLoader
            .getResource(name.replace(".", File.separator) + ".class");
   }

   public static ClasspathClassRepository classpathClassRepostory(final Class<?> loadFromWhereverThisWasLoaded) {
      return classpathClassRepository(new Class[]{loadFromWhereverThisWasLoaded});
   }

   public static ClasspathClassRepository classpathClassRepository(final Class<?>[] loadFromWhereverTheseWereLoaded) {
       return classpathClassRepository(getClasspathURLs(loadFromWhereverTheseWereLoaded));
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
