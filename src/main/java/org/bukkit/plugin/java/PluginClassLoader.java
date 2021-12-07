package org.bukkit.plugin.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

import catserver.server.CatServer;
import catserver.server.patcher.IPatcher;
import catserver.server.patcher.PatcherManager;
import catserver.server.remapper.*;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
final class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    final JavaPlugin plugin;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;

    private LaunchClassLoader launchClassLoader;
    private CatServerRemapper remapper;
    private JarMapping jarMapping;

    private IPatcher patcher;

    private ConcurrentSet<Package> fixedPackages = new ConcurrentSet<Package>();

    PluginClassLoader(final JavaPluginLoader loader, final ClassLoader parent, final PluginDescriptionFile description, final File dataFolder, final File file) throws IOException, InvalidPluginException, MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();

        this.launchClassLoader = parent instanceof LaunchClassLoader ? (LaunchClassLoader)parent : (LaunchClassLoader) MinecraftServer.getServerInst().getClass().getClassLoader();
        this.jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        provider.add(new ClassLoaderProvider(this));
        this.jarMapping.setFallbackInheritanceProvider(provider);
        this.remapper = new CatServerRemapper(jarMapping);

        this.patcher = PatcherManager.getPluginPatcher(description.getName());

        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
            }

            Class<? extends JavaPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(JavaPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (RemapRules.isNMSPackage(name)) {
            String remappedClass = jarMapping.classes.getOrDefault(name.replace(".", "/"), name);
            return launchClassLoader.findClass(remappedClass);
        }

        if (name.startsWith("org.bukkit.")) {
            throw new ClassNotFoundException(name);
        }

        Class<?> result = classes.get(name);
        synchronized (name.intern()) {
            if (result == null) {
                if (checkGlobal) {
                    result = loader.getClassByName(name);
                }
    
                if (result == null) {
                    result = remappedFindClass(name);
    
                    if (result != null) {
                        loader.setClass(name, result);
                    }
                }

                if (result == null) {
                    if (checkGlobal) {
                        try {
                            result = launchClassLoader.getClass().getClassLoader().loadClass(name);
                        } catch (Throwable throwable) {
                            throw new ClassNotFoundException(name, throwable);
                        }
                    }
                }

                if (result == null) {
                    throw new ClassNotFoundException(name);
                }

                classes.put(name, result);
            }
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            jar.close();
        }
    }

    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;

        javaPlugin.init(loader, loader.server, description, dataFolder, file, this);
    }

    private Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class<?> result = null;

        try {
            // Load the resource to the name
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null) {
                InputStream stream = url.openStream();
                if (stream != null) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection(); // parses only
                    URL jarURL = jarURLConnection.getJarFileURL();

                    // Remap the classes
                    byte[] bytecode = remapper.remapClassFile(stream, RuntimeRepo.getInstance());
                    if (this.patcher != null) bytecode = this.patcher.transform(name.replace("/", "."), bytecode);
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
                            fixPackage(pkg);
                        }
                    }

                    // Define the classes
                    CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);
                    result = this.defineClass(name, bytecode, 0, bytecode.length, codeSource);
                    if (result != null) {
                        // Resolve it - sets the class loader of the class
                        this.resolveClass(result);
                    }
                }
            }
        } catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class "+name, t);
        }

        return result;
    }

    // CatServer - remap package
    protected Package getPackage(String name) {
        if ("org.bukkit.craftbukkit".equals(name))
            name = "org.bukkit.craftbukkit." + CatServer.getNativeVersion();
        return super.getPackage(name);
    }

    private void fixPackage(Package pkg) {
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
