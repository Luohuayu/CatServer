package org.bukkit.plugin.java;

import catserver.server.patcher.IPatcher;
import catserver.server.patcher.PatcherManager;
import catserver.server.remapper.ClassInheritanceProvider;
import catserver.server.remapper.CatServerRemapper;
import catserver.server.remapper.MappingLoader;
import catserver.server.remapper.ReflectionTransformer;
import catserver.server.remapper.RemapRules;
import cpw.mods.modlauncher.TransformingClassLoader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public final class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    final JavaPlugin plugin;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;
    private final Set<String> seenIllegalAccess = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private TransformingClassLoader launchClassLoader;
    private CatServerRemapper remapper;
    private JarMapping jarMapping;

    // For 1.18+, Fucking the obf
    private JarMapping preJarMapping;
    private CatServerRemapper preRemapper;

    private IPatcher patcher; // CatServer

    static {
        ClassLoader.registerAsParallelCapable();
    }

    PluginClassLoader(@NotNull final JavaPluginLoader loader, @Nullable final ClassLoader parent, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file) throws IOException, InvalidPluginException, MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();

        this.launchClassLoader = parent instanceof TransformingClassLoader ? (TransformingClassLoader) parent : (TransformingClassLoader) MinecraftServer.getServer().getClass().getClassLoader();
        this.jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        provider.add(new ClassLoaderProvider(this));
        this.jarMapping.setFallbackInheritanceProvider(provider);
        this.remapper = new CatServerRemapper(jarMapping);

        this.preJarMapping = MappingLoader.loadPreMapping();
        this.preJarMapping.setFallbackInheritanceProvider(provider);
        this.preRemapper = new CatServerRemapper(preJarMapping);

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
    public URL getResource(String name) {
        return findResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return findResources(name);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(@NotNull String name, boolean checkGlobal) throws ClassNotFoundException {
        if (RemapRules.isNMSPackage(name)) {
            String remappedClass = jarMapping.classes.getOrDefault(name.replace(".", "/"), name).replace('/', '.');
            return launchClassLoader.loadClass(remappedClass);
        }

        if (name.startsWith("org.bukkit.")) {
            throw new ClassNotFoundException(name);
        }

        if (name.startsWith("org.apache.commons.lang.")) {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        }

        if (name.startsWith("org.apache.commons.codec.")) {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        }

        Class<?> result = classes.get(name);
        synchronized (name.intern()) {
            if (result == null) {
                if (checkGlobal) {
                    // This ignores the libraries of other plugins, unless they are transitive dependencies.
                    result = loader.getClassByName(name);
                    // If the class was loaded from a library instead of a PluginClassLoader, we can assume that its associated plugin is a transitive dependency and can therefore skip this check.
                    if (result != null && result.getClassLoader() instanceof PluginClassLoader) {
                        PluginDescriptionFile provider = ((PluginClassLoader) result.getClassLoader()).description;

                        if (provider != description
                                && !seenIllegalAccess.contains(provider.getName())
                                && !((SimplePluginManager) loader.server.getPluginManager()).isTransitiveDepend(description, provider)) {

                            seenIllegalAccess.add(provider.getName());
                            if (plugin != null) {
                                plugin.getLogger().log(Level.WARNING, "Loaded class {0} from {1} which is not a depend, softdepend or loadbefore of this plugin.", new Object[]{name, provider.getFullName()});
                            } else {
                                // In case the bad access occurs on construction
                                loader.server.getLogger().log(Level.WARNING, "[{0}] Loaded class {1} from {2} which is not a depend, softdepend or loadbefore of this plugin.", new Object[]{description.getName(), name, provider.getFullName()});
                            }
                        }
                    }
                }

                if (result == null) {
                    result = remappedFindClass(name);
                }

                if (result != null) {
                    loader.setClass(name, result);
                }

                if (result == null) {
                    if (checkGlobal) {
                        try {
                            result = launchClassLoader.loadClass(name);
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

    @NotNull
    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(@NotNull JavaPlugin javaPlugin) {
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
                    byte[] bytecode = remapper.remapClassFile(preRemapper.remapClassFile(stream, RuntimeRepo.getInstance()), RuntimeRepo.getInstance());
                    if (this.patcher != null) bytecode = this.patcher.transform(name.replace("/", "."), bytecode);
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
                            } catch (IllegalArgumentException ignored) {
                            }
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
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }

        return result;
    }
}
