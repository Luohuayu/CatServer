package foxlaunch.legacy;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LegacyLauncher {
    public static boolean setup() {
        if (!JVMHack.init) return false;

        try {
            JVMHack.addModuleOptionDynamic("addExportsToAllUnnamed", "java.base", "sun.security.util", null);
            JVMHack.addModuleOptionDynamic("addOpensToAllUnnamed", "java.base", "java.util.jar", null);
            JVMHack.addModuleOptionDynamic("addOpensToAllUnnamed", "java.base", "java.lang", null);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static void loadJars() throws Exception {
        JVMHack.addModuleDynamic("libraries/org/ow2/asm/asm/9.5/asm-9.5.jar");
        JVMHack.addModuleDynamic("libraries/org/ow2/asm/asm-tree/9.5/asm-tree-9.5.jar");
        JVMHack.addModuleDynamic("libraries/org/ow2/asm/asm-analysis/9.5/asm-analysis-9.5.jar");
        JVMHack.addModuleDynamic("libraries/org/ow2/asm/asm-util/9.5/asm-util-9.5.jar");
        JVMHack.addModuleDynamic("libraries/org/ow2/asm/asm-commons/9.5/asm-commons-9.5.jar");
        JVMHack.addModuleDynamic("libraries/cpw/mods/securejarhandler/1.0.8/securejarhandler-1.0.8.jar");
        JVMHack.addModuleDynamic("libraries/cpw/mods/bootstraplauncher/1.0.0/bootstraplauncher-1.0.0.jar");
        JVMHack.addModuleDynamic("libraries/net/minecraftforge/JarJarFileSystems/0.3.19/JarJarFileSystems-0.3.19.jar");

        JVMHack.addModuleOptionDynamic("addExportsToAllUnnamed", "cpw.mods.bootstraplauncher", "cpw.mods.bootstraplauncher", null);
        JVMHack.addModuleOptionDynamic("addOpens", "java.base", "java.util.jar", "cpw.mods.securejarhandler");
        JVMHack.addModuleOptionDynamic("addOpens", "java.base", "java.lang.invoke", "cpw.mods.securejarhandler");
        JVMHack.addModuleOptionDynamic("addExports", "java.base", "sun.security.util", "cpw.mods.securejarhandler");

        JarLoader.loadJar(new File("libraries/commons-lang/commons-lang/2.6/commons-lang-2.6.jar"));
    }

    static class JVMHack {
        protected static boolean init = false;

        static {
            try {
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);

                Class<?> internalModulesClass = Class.forName("jdk.internal.module.Modules");
                Object jdkInternalModule = Class.class.getMethod("getModule").invoke(internalModulesClass);

                Field moduleField = Class.class.getDeclaredField("module");
                unsafe.getAndSetObject(JVMHack.class, unsafe.objectFieldOffset(moduleField), jdkInternalModule);

                init = true;
            } catch (Throwable throwable) {
                System.out.println("Failed to initialize LegacyLauncher, will generate script for startup: " + throwable.toString());
            }
        }

        static void addModuleOptionDynamic(String option, String module, String pkg, String targetModule) throws Exception {
            if (!init) throw new RuntimeException();

            Class<?> internalModulesClass = Class.forName("jdk.internal.module.Modules");
            Class<?> moduleClass = Class.forName("java.lang.Module");

            Optional<Object> foundModuleOptional = (Optional<Object>) internalModulesClass.getMethod("findLoadedModule", String.class).invoke(null, module);
            Object foundModule = foundModuleOptional.orElseThrow(IllegalArgumentException::new);

            if (targetModule != null) {
                Optional<Object> foundTargetModuleOptional = (Optional<Object>) internalModulesClass.getMethod("findLoadedModule", String.class).invoke(null, targetModule);
                Object foundTargetModule = foundTargetModuleOptional.orElseThrow(IllegalArgumentException::new);

                internalModulesClass.getMethod(option, moduleClass, String.class, moduleClass).invoke(null, foundModule, pkg, foundTargetModule);
            } else {
                internalModulesClass.getMethod(option, moduleClass, String.class).invoke(null, foundModule, pkg);
            }
        }

        static void addModuleDynamic(String modulePath) throws Exception {
            Class<?> moduleClass = Class.forName("java.lang.Module");
            Class<?> moduleLayerClass = Class.forName("java.lang.ModuleLayer");
            Class<?> moduleFinderClass = Class.forName("java.lang.module.ModuleFinder");
            Class<?> moduleReferenceClass = Class.forName("java.lang.module.ModuleReference");
            Class<?> moduleDescriptorClass = Class.forName("java.lang.module.ModuleDescriptor");
            Class<?> configurationClass = Class.forName("java.lang.module.Configuration");
            Class<?> resolvedModuleClass = Class.forName("java.lang.module.ResolvedModule");
            Class<?> builtinClassLoaderClass = Class.forName("jdk.internal.loader.BuiltinClassLoader");

            Object finder = moduleFinderClass.getMethod("of", Path[].class).invoke(null, new Object[] { new Path[] { Paths.get(modulePath) } } );
            Set<?> findAll = (Set<?>) moduleFinderClass.getMethod("findAll").invoke(finder);
            Method resolveAndBind = configurationClass.getDeclaredMethod("resolveAndBind", moduleFinderClass, List.class, moduleFinderClass, Collection.class);
            resolveAndBind.setAccessible(true);

            Object config = resolveAndBind.invoke(null, finder, Collections.singletonList(moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null))), finder, findAll.stream().peek(ref -> {
                try {
                    builtinClassLoaderClass.getMethod("loadModule", moduleReferenceClass).invoke(ClassLoader.getSystemClassLoader(), ref);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).map(ref -> {
                try {
                    return moduleReferenceClass.getMethod("descriptor").invoke(ref);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).map(desc -> {
                try {
                    return moduleDescriptorClass.getMethod("name").invoke(desc);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList()));

            Field graphField = configurationClass.getDeclaredField("graph");
            graphField.setAccessible(true);
            Field cfField = resolvedModuleClass.getDeclaredField("cf");
            cfField.setAccessible(true);
            Map<Object, Set<Object>> graph = (Map<Object, Set<Object>>) graphField.get(config);
            graph.forEach((k, v) -> {
                try {
                    cfField.set(k, moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null)));
                    for (Object m : v) {
                        cfField.set(m, moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null)));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            graph.putAll((Map<Object, Set<Object>>) graphField.get(moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null))));
            graphField.set(moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null)), new HashMap<>(graph));

            Set<?> oldBootModules = (Set<?>) configurationClass.getMethod("modules").invoke(moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null)));

            Field modulesField = configurationClass.getDeclaredField("modules");
            modulesField.setAccessible(true);
            Set<Object> modules = new HashSet<>((Set<Object>) configurationClass.getMethod("modules").invoke(config));
            modulesField.set(moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null)), new HashSet<>(modules));

            Field nameToModuleField = configurationClass.getDeclaredField("nameToModule");
            nameToModuleField.setAccessible(true);
            Map<Object, Object> nameToModule = new HashMap<>((Map<Object, Object>) nameToModuleField.get(moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null))));
            nameToModule.putAll((Map<Object, Object>) nameToModuleField.get(config));
            nameToModuleField.set(moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null)), new HashMap<>(nameToModule));

            Field nameToModuleField2 = moduleLayerClass.getDeclaredField("nameToModule");
            nameToModuleField2.setAccessible(true);
            Map<Object, Object> nameToModule2 = (Map<Object, Object>) nameToModuleField2.get(moduleLayerClass.getMethod("boot").invoke(null));
            Method defineModulesMethod = moduleClass.getDeclaredMethod("defineModules", configurationClass, Function.class, moduleLayerClass);
            defineModulesMethod.setAccessible(true);
            nameToModule2.putAll((Map<Object, Object>) defineModulesMethod.invoke(null, moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null)), (Function<String, ClassLoader>) name -> ClassLoader.getSystemClassLoader(), moduleLayerClass.getMethod("boot").invoke(null)));

            modules.addAll(oldBootModules);
            modulesField.set(moduleLayerClass.getMethod("configuration").invoke(moduleLayerClass.getMethod("boot").invoke(null)), new HashSet<>(modules));

            Field modulesField2 = moduleLayerClass.getDeclaredField("modules");
            modulesField2.setAccessible(true);
            modulesField2.set(moduleLayerClass.getMethod("boot").invoke(null), null);
            Field servicesCatalogField = moduleLayerClass.getDeclaredField("servicesCatalog");
            servicesCatalogField.setAccessible(true);
            servicesCatalogField.set(moduleLayerClass.getMethod("boot").invoke(null), null);

            Method implAddReadsMethod = moduleClass.getDeclaredMethod("implAddReads", moduleClass);
            implAddReadsMethod.setAccessible(true);
            Method findModuleMethod = moduleLayerClass.getMethod("findModule", String.class);
            Object bootModuleLayer = moduleLayerClass.getMethod("boot").invoke(null);
            Method nameMethod = resolvedModuleClass.getMethod("name");
            for (Object module : ((Set<Object>) configurationClass.getMethod("modules").invoke(config))) {
                Optional<Object> optional = (Optional<Object>) findModuleMethod.invoke(bootModuleLayer, nameMethod.invoke(module));
                optional.ifPresent(progynova -> {
                    try {
                        for (Object obm : oldBootModules) {
                            Optional<Object> optional2 = (Optional<Object>) findModuleMethod.invoke(bootModuleLayer, nameMethod.invoke(obm));
                            optional2.ifPresent(progynova2 -> {
                                try {
                                    implAddReadsMethod.invoke(progynova, progynova2);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
