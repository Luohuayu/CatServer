package catserver.server.remapper.target;

import catserver.server.remapper.*;
import io.netty.util.internal.ConcurrentSet;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class CatURLClassLoader extends URLClassLoader
{
    private JarMapping jarMapping;
    private CatServerRemapper remapper;
    private final Map<String, Class<?>> classes = new HashMap<>();
    private LaunchClassLoader launchClassLoader;

    private ConcurrentSet<Package> fixedPackages = new ConcurrentSet<Package>();

    {
        this.launchClassLoader = (LaunchClassLoader) MinecraftServer.getServerInst().getClass().getClassLoader();
        this.jarMapping = MappingLoader.loadMapping();
        final JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        provider.add(new ClassLoaderProvider(this));
        this.jarMapping.setFallbackInheritanceProvider(provider);
        this.remapper = new CatServerRemapper(this.jarMapping);
    }

    public CatURLClassLoader(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
    }

    public CatURLClassLoader(final URL[] urls) {
        super(urls);
    }

    public CatURLClassLoader(final URL[] urls, final ClassLoader parent, final URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (RemapRules.isNMSPackage(name)) {
            final String remappedClass = this.jarMapping.classes.getOrDefault(name.replace(".", "/"), name);
            return launchClassLoader.findClass(remappedClass);
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
                        result = launchClassLoader.findClass(name);
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
                        Package pkg = getPackage(pkgName);
                        if (pkg == null) {
                            try {
                                if (manifest != null) {
                                    pkg = definePackage(pkgName, manifest, url);
                                } else {
                                    pkg = definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (Exception e) {
                                // do nothing
                            }
                        }
                        if (pkg != null && manifest != null) {
                            fixPackage(pkg, manifest);
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

    private void fixPackage(Package pkg, Manifest manifest) {
        if (!fixedPackages.contains(pkg)) {
            Attributes attr = manifest.getMainAttributes();
            if (attr != null) {
                try {
                    if (catserver.server.launch.Java11Support.enable) {
                        try {
                            Object versionInfo = catserver.server.launch.Java11Support.FieldHelper.get(pkg, Package.class.getDeclaredField("versionInfo"));
                            if (versionInfo != null) {
                                Class<?> classVersionInfo = Class.forName("java.lang.Package$VersionInfo");
                                catserver.server.launch.Java11Support.FieldHelper.set(versionInfo, classVersionInfo.getDeclaredField("specTitle"), attr.getValue(Attributes.Name.SPECIFICATION_TITLE));
                                catserver.server.launch.Java11Support.FieldHelper.set(versionInfo, classVersionInfo.getDeclaredField("specVersion"), attr.getValue(Attributes.Name.SPECIFICATION_VERSION));
                                catserver.server.launch.Java11Support.FieldHelper.set(versionInfo, classVersionInfo.getDeclaredField("specVendor"), attr.getValue(Attributes.Name.SPECIFICATION_VENDOR));
                                catserver.server.launch.Java11Support.FieldHelper.set(versionInfo, classVersionInfo.getDeclaredField("implTitle"), attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE));
                                catserver.server.launch.Java11Support.FieldHelper.set(versionInfo, classVersionInfo.getDeclaredField("implVersion"), attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION));
                                catserver.server.launch.Java11Support.FieldHelper.set(versionInfo, classVersionInfo.getDeclaredField("implVendor"), attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR));
                            }
                            return;
                        } catch (Exception ignored) {}
                    }

                    try {
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.SPECIFICATION_TITLE), "specTitle");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.SPECIFICATION_VERSION), "specVersion");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.SPECIFICATION_VENDOR), "specVendor");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE), "implTitle");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION), "implVersion");
                        ReflectionHelper.setPrivateValue(Package.class, pkg, attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR), "implVendor");
                    } catch (Exception ignored) {}
                } finally {
                    fixedPackages.add(pkg);
                }
            }
        }
    }
}
