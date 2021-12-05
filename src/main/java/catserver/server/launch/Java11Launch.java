package catserver.server.launch;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.LogWrapper;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.*;

public class Java11Launch {
    public static void main(String[] args) {
        new Java11Launch().launch(args);
    }

    private Java11Launch() {
        try {
            AccessController.doPrivileged((PrivilegedExceptionAction<Void>) () -> {
                Field ucpField = null;
                try {
                    ucpField = Java11Launch.class.getClassLoader().getClass().getDeclaredField("ucp");
                } catch (NoSuchFieldException e) {
                    ucpField = Java11Launch.class.getClassLoader().getClass().getSuperclass().getDeclaredField("ucp");
                }
                Object ucp = Java11Support.FieldHelper.get(Java11Launch.class.getClassLoader(), ucpField);
                Field pathField = ucp.getClass().getDeclaredField("path");
                URL[] sources = ((List<URL>) Java11Support.FieldHelper.get(ucp, pathField)).toArray(new URL[0]); // Luohuayu: May cause thread safety issues, find a better way to invoke getURLs

                Launch.classLoader = new LaunchClassLoader(sources);
                Launch.blackboard = new HashMap<>();
                Thread.currentThread().setContextClassLoader(Launch.classLoader);

                Launch.classLoader.addClassLoaderExclusion("javax.");

                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void launch(String[] args) {
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        OptionSpec<String> profileOption = parser.accepts("version", "The version we launched with").withRequiredArg();
        OptionSpec<File> gameDirOption = parser.accepts("gameDir", "Alternative game directory").withRequiredArg().ofType(File.class);
        OptionSpec<File> assetsDirOption = parser.accepts("assetsDir", "Assets directory").withRequiredArg().ofType(File.class);
        OptionSpec<String> tweakClassOption = parser.accepts("tweakClass", "Tweak class(es) to load").withRequiredArg().defaultsTo("net.minecraft.launchwrapper.VanillaTweaker");
        OptionSpec<String> nonOption = parser.nonOptions();
        OptionSet options = parser.parse(args);
        Launch.minecraftHome = options.valueOf(gameDirOption);
        Launch.assetsDir = options.valueOf(assetsDirOption);
        String profileName = options.valueOf(profileOption);
        List<String> tweakClassNames = new ArrayList<>(options.valuesOf(tweakClassOption));
        List<String> argumentList = new ArrayList<>();
        Launch.blackboard.put("TweakClasses", tweakClassNames);
        Launch.blackboard.put("ArgumentList", argumentList);
        Set<String> allTweakerNames = new HashSet<>();
        List<ITweaker> allTweakers = new ArrayList<>();
        try {
            List<ITweaker> tweakers = new ArrayList<>(tweakClassNames.size() + 1);
            Launch.blackboard.put("Tweaks", tweakers);
            ITweaker primaryTweaker = null;
            do {
                Iterator<String> it = tweakClassNames.iterator();
                while (it.hasNext()) {
                    String tweakName = it.next();
                    if (allTweakerNames.contains(tweakName)) {
                        LogWrapper.log(Level.WARN, "Tweak class name %s has already been visited -- skipping", tweakName);
                        it.remove();
                    }
                    else {
                        allTweakerNames.add(tweakName);
                        LogWrapper.log(Level.INFO, "Loading tweak class name %s", tweakName);
                        Launch.classLoader.addClassLoaderExclusion(tweakName.substring(0, tweakName.lastIndexOf(46)));
                        ITweaker tweaker = (ITweaker)Class.forName(tweakName, true, Launch.classLoader).getConstructor(new Class[0]).newInstance(new Object[0]);
                        tweakers.add(tweaker);
                        it.remove();
                        if (primaryTweaker != null) {
                            continue;
                        }
                        LogWrapper.log(Level.INFO, "Using primary tweak class name %s", tweakName);
                        primaryTweaker = tweaker;
                    }
                }
                Iterator<ITweaker> it2 = tweakers.iterator();
                while (it2.hasNext()) {
                    ITweaker tweaker2 = it2.next();
                    LogWrapper.log(Level.INFO, "Calling tweak class %s", tweaker2.getClass().getName());
                    tweaker2.acceptOptions(options.valuesOf(nonOption), Launch.minecraftHome, Launch.assetsDir, profileName);
                    tweaker2.injectIntoClassLoader(Launch.classLoader);
                    allTweakers.add(tweaker2);
                    it2.remove();
                }
            } while (!tweakClassNames.isEmpty());
            for (ITweaker tweaker3 : allTweakers) {
                argumentList.addAll(Arrays.asList(tweaker3.getLaunchArguments()));
            }
            String launchTarget = primaryTweaker.getLaunchTarget();
            Class<?> clazz = Class.forName(launchTarget, false, Launch.classLoader);
            Method mainMethod = clazz.getMethod("main", String[].class);
            LogWrapper.info("Launching wrapped minecraft {%s}", launchTarget);
            mainMethod.invoke(null, new Object[] { argumentList.toArray(new String[0]) });
        } catch (Exception e) {
            LogWrapper.log(Level.ERROR, e, "Unable to launch");
            System.exit(1);
        }
    }
}
