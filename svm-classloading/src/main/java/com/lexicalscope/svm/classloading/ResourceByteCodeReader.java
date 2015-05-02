package com.lexicalscope.svm.classloading;

import static com.lexicalscope.svm.classloading.asm.AsmSClassFactory.newSClass;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import com.lexicalscope.svm.classloading.asm.AsmSClass;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class ResourceByteCodeReader implements ByteCodeReader {
	private final InstructionSource instructions;
   private final ClassSource classRepository;

	public ResourceByteCodeReader(
	      final InstructionSource instructions,
	      final ClassSource classSource) {
		this.instructions = instructions;
      this.classRepository = classSource;
	}

	@Override
	public AsmSClass load(final SClassLoader classLoader, final KlassInternalName name) {
	   assert !name.isArrayClass();
		if (name == null) {
			return null;
		}

		final URL classUrl;
		if (name.equals(internalName(Thread.class))
				|| name.equals(internalName("java/lang/Integer$IntegerCache"))) {
			classUrl = loadLocalVersion(name);
		} else {
			classUrl = classRepository.loadFromRepository(name);
		}

		try {
			if (classUrl == null) {
				throw new SClassNotFoundException(name);
			}

			final ClassNode classNode = loadClassBytecodeFromUrl(classUrl);
			final SClass superclass = classNode.superName != null ? classLoader
					.load(internalName(classNode.superName)) : null;

			@SuppressWarnings("unchecked")
			final List<KlassInternalName> interfaceNames = internalName(classNode.interfaces);
			final List<SClass> interfaces = new ArrayList<>();
			for (final KlassInternalName interfaceName : interfaceNames) {
				interfaces.add(classLoader.load(interfaceName));
			}

			return newSClass(
			      classLoader,
					instructions,
					classUrl,
					classNode,
					superclass,
					interfaces,
					null);
		} catch (final IOException e) {
			throw new SClassLoadingFailException(name + " from " + classUrl, e);
		}
	}

	private URL loadLocalVersion(final KlassInternalName name) {
	   final String relativeLocation = "lib/" + name + ".class";
		try {
		   final URL baseLocation = ResourceByteCodeReader.class.getProtectionDomain()
               .getCodeSource().getLocation();
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

	private ClassNode loadClassBytecodeFromUrl(final URL classUrl)
			throws IOException {
		final ClassNode classNode = new ClassNode();
		final InputStream in = classUrl.openStream();
		try {
			new ClassReader(in).accept(classNode, 0);
		} finally {
			in.close();
		}
		return classNode;
	}
}
