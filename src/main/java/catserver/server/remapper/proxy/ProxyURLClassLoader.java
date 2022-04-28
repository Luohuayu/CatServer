package catserver.server.remapper.proxy;

import catserver.server.remapper.ClassInheritanceProvider;
import catserver.server.remapper.LoliServerRemapper;
import catserver.server.remapper.MappingLoader;
import catserver.server.remapper.ReflectionTransformer;
import catserver.server.remapper.RemapRules;
import cpw.mods.modlauncher.TransformingClassLoader;
import io.netty.util.internal.ConcurrentSet;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.server.MinecraftServer;

import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

public class ProxyURLClassLoader extends URLClassLoader
{
    private JarMapping jarMapping;
    private LoliServerRemapper remapper;
    private final Map<String, Class<?>> classes = new HashMap<>();
    private TransformingClassLoader launchClassLoader;

    private ConcurrentSet<Package> fixedPackages = new ConcurrentSet<Package>();

    {
        this.launchClassLoader = (TransformingClassLoader) MinecraftServer.getServer().getClass().getClassLoader();
        this.jarMapping = MappingLoader.loadMapping();
        final JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        provider.add(new ClassLoaderProvider(this));
        this.jarMapping.setFallbackInheritanceProvider(provider);
        this.remapper = new LoliServerRemapper(this.jarMapping);
    }

    public ProxyURLClassLoader(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
    }

    public ProxyURLClassLoader(final URL[] urls) {
        super(urls);
    }

    public ProxyURLClassLoader(final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (RemapRules.isNMSPackage(name)) {
            final String remappedClass = this.jarMapping.classes.get(name.replaceAll("\\.", "\\/"));
            return launchClassLoader.loadClass(remappedClass);
        }

        Class<?> result = this.classes.get(name);
        synchronized (name.intern()) {
            if (result == null) {
                result = this.remappedFindClass(name);

                if (result == null) {
                    try {
                        result = super.findClass(name);
                    } catch (ClassNotFoundException ignored) { }
                }

                if (result == null) {
                    try {
                        result = launchClassLoader.loadClass(name);
                    } catch (ClassNotFoundException ignored) { }
                }

                if (result == null) {
                    try {
                        result = launchClassLoader.getClass().getClassLoader().loadClass(name);
                    } catch (Throwable throwable) {
                        throw new ClassNotFoundException(name, throwable);
                    }
                }

                if (result == null) throw new ClassNotFoundException(name);
                this.classes.put(name, result);
            }
        }
        return result;
    }

    private Class<?> remappedFindClass(final String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            final String path = name.replace('.', '/').concat(".class");
            final URL url = this.findResource(path);
            if (url != null) {
                final InputStream stream = url.openStream();
                if (stream != null) {
                    final JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                    final URL jarURL = jarURLConnection.getJarFileURL();
                    final Manifest manifest = jarURLConnection.getManifest();

                    // Remap the classes
                    byte[] bytecode = this.remapper.remapClassFile(stream, RuntimeRepo.getInstance());
                    bytecode = ReflectionTransformer.transform(bytecode);

                    // Fix the package
                    int dot = name.lastIndexOf('.');
                    if (dot != -1) {
                        String pkgName = name.substring(0, dot);
                        if (getPackage(pkgName) == null) {
                            try {
                                if (manifest != null) {
                                    definePackage(pkgName, manifest, url);
                                } else {
                                    definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (IllegalArgumentException ignored) {}
                        }
                    }

                    // Define the classes
                    final CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);
                    result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                    if (result != null) {
                        // Resolve it - sets the class loader of the class
                        this.resolveClass(result);
                    }
                }
            }
        }
        catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }
        return result;
    }
}
