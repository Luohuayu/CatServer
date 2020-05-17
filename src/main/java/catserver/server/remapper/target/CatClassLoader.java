package catserver.server.remapper.target;

import catserver.server.remapper.CatServerRemapper;
import catserver.server.remapper.ClassInheritanceProvider;
import catserver.server.remapper.MappingLoader;
import catserver.server.remapper.ReflectionTransformer;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.server.MinecraftServer;

import java.security.ProtectionDomain;

public class CatClassLoader extends ClassLoader {
    private JarMapping jarMapping;
    private CatServerRemapper remapper;

    {
        this.jarMapping = MappingLoader.loadMapping();
        final JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        provider.add(new ClassLoaderProvider(this));
        this.jarMapping.setFallbackInheritanceProvider(provider);
        this.remapper = new CatServerRemapper(this.jarMapping);
    }

    protected CatClassLoader() {
        super();
    }

    protected CatClassLoader(ClassLoader parent) {
        super(parent);
    }

    public final Class<?> defineClassRemap(byte[] b, int off, int len) throws ClassFormatError {
        return defineClassRemap(null, b, off, len, null);
    }

    public final Class<?> defineClassRemap(String name, byte[] b, int off, int len) throws ClassFormatError {
        return defineClassRemap(name, b, off, len, null);
    }

    public final Class<?> defineClassRemap(String name, java.nio.ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (!b.isDirect() && b.hasArray()) {
            return remappedFindClass(name, b.array(), protectionDomain);
        }
        return defineClass(name, b, protectionDomain);
    }

    public final Class<?> defineClassRemap(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (off == 0) {
            return remappedFindClass(name, b, protectionDomain);
        }

        return defineClass(name, b, off, len, protectionDomain);
    }

    private Class<?> remappedFindClass(String name, byte[] stream, ProtectionDomain protectionDomain) throws ClassFormatError {
        Class<?> result = null;

        try {
            byte[] bytecode = remapper.remapClassFile(stream, RuntimeRepo.getInstance());
            bytecode = ReflectionTransformer.transform(bytecode);

            result = this.defineClass(name, bytecode, 0, bytecode.length, protectionDomain);
        } catch (Throwable t) {
            throw new ClassFormatError("Failed to remap class " + name);
        }

        return result;
    }
}
