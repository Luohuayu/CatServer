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
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

import catserver.server.CatServer;
import catserver.server.remapper.CatServerRemapper;
import catserver.server.remapper.ClassInheritanceProvider;
import catserver.server.remapper.MappingLoader;
import catserver.server.remapper.ReflectionTransformer;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.server.MinecraftServer;

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

    private CatServerRemapper remapper;
    private JarMapping jarMapping;

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

        jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        provider.add(new ClassLoaderProvider(this));
        jarMapping.setFallbackInheritanceProvider(provider);
        remapper = new CatServerRemapper(jarMapping);

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
        if (remapper.isNeedRemap(name)) {
            String remappedClass = jarMapping.classes.get(name.replaceAll("\\.", "\\/"));
            Class<?> clazz = ((net.minecraft.launchwrapper.LaunchClassLoader)MinecraftServer.getServerInst().getClass().getClassLoader()).findClass(remappedClass);
            return clazz;
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
                    LaunchClassLoader lw = ((LaunchClassLoader) MinecraftServer.getServerInst().getClass().getClassLoader());
                    try {
                        result = lw.findClass(name);
                    }catch (Throwable throwable) {
                        try {
                            lw.addTransformerExclusion(name);
                            result = lw.findClass(name);
                        }catch (Throwable throwable1) {
                            result = null;
                        }finally {
                            Set<String> set = ReflectionHelper.getPrivateValue(LaunchClassLoader.class, lw, "transformerExceptions");
                            set.remove(name);
                        }
                    }
                    if (result == null) {
                        throw new ClassNotFoundException(name);
                    }
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
                    byte[] bytecode = null;

                    // Remap the classes
                    bytecode = remapper.remapClassFile(stream, RuntimeRepo.getInstance());
                    bytecode = ReflectionTransformer.transform(bytecode);

                    // Define (create) the class using the modified byte code
                    // The top-child class loader is used for this to prevent access violations
                    // Set the codesource to the jar, not within the jar, for compatibility with
                    // plugins that do new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()))
                    // instead of using getResourceAsStream - see https://github.com/MinecraftPortCentral/Cauldron-Plus/issues/75
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection(); // parses only
                    URL jarURL = jarURLConnection.getJarFileURL();
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

    // CatServer - can't get the package because no class below the package, remap it
    protected Package getPackage(String name) {
        if ("org.bukkit.craftbukkit".equals(name))
            name = "org.bukkit.craftbukkit." + CatServer.getNativeVersion();
        return super.getPackage(name);
    }
}
