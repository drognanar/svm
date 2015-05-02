package com.lexicalscope.svm.classloading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.lexicalscope.svm.vm.j.KlassInternalName;

public class JarClassRepository implements ClassSource {
   private final URL baseLocation;
   private final String prefix;

   public JarClassRepository(final Class<?> loadFromWhereverThisWasLoaded, final String prefix) {
      this(urlOfJarFile(loadFromWhereverThisWasLoaded), prefix);
   }

   public JarClassRepository(final URL baseLocation, final String prefix) {
      this.baseLocation = baseLocation;
      this.prefix = prefix;
   }

   @Override public URL loadFromRepository(final KlassInternalName name) {
      final String relativeLocation = prefix + name + ".class";
      try {
         if(baseLocation.getProtocol().equals("file")) {
            final File baseFile = new File(baseLocation.toURI());
            if(baseFile.isFile() && baseFile.getName().endsWith("jar")) {
               return new URL("jar:" + baseLocation + "!/" + relativeLocation);
            }
         }
         return new URL(baseLocation, relativeLocation);
      } catch (final MalformedURLException e) {
         throw new SClassLoadingFailException(name, e);
      } catch (final URISyntaxException e) {
         throw new SClassLoadingFailException(name, e);
      }
   }

   @Override public String toString() {
      return "loading from " + baseLocation + prefix;
   }

   public static JarClassRepository loadFromLibDirectoryInSameJarFileAs(final Class<?> loadFromWhereverThisWasLoaded) {
      return new JarClassRepository(loadFromWhereverThisWasLoaded, "lib/");
   }

   public static URL urlOfJarFile(final Class<?> loadFromWhereverThisWasLoaded) {
      return loadFromWhereverThisWasLoaded.getProtectionDomain().getCodeSource().getLocation();
   }
}
