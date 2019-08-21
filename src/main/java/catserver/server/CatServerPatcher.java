package catserver.server;

import catserver.server.delta.GDiffPatcher;
import catserver.server.utils.LanguageUtils;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class CatServerPatcher {
    private static String[] ForgeResources = new String[] {"forge_at.cfg", "forge.srg", "forge.exc", "deobfuscation_data-1.12.2.lzma", "mcpmod.info", "mcp/MethodsReturnNonnullByDefault.class", "assets/forge/lang/en_US.lang", "Log4j-config.xsd", "Log4j-events.dtd", "Log4j-events.xsd", "Log4j-levels.xsd"};
    private static String[] BukkitResources = new String[] {};

    private static final byte[] EMPTY_DATA = new byte[0];
    private static final GDiffPatcher PATCHER = new GDiffPatcher();

    public static void patch() {
        InputStream in = ClassLoader.getSystemResourceAsStream("patchFiles.txt");
        if (in == null) {
            System.out.println(LanguageUtils.I18nToString("launch.patch_does_not_exist"));
            Runtime.getRuntime().exit(0);
            return;
        }

        String version = CatServerLaunch.class.getPackage().getImplementationVersion() != null ? CatServerLaunch.class.getPackage().getImplementationVersion() : "null";
        File patchedLib = new File("cache-patched-" + version + ".jar");
        if (!patchedLib.exists()) {
            System.out.println(LanguageUtils.I18nToString("launch.patch_patching"));
            try {
                List<String> patchFiles = IOUtils.readLines(in, StandardCharsets.UTF_8);
                generatePatchCache(patchedLib, patchFiles);
            } catch (Exception e) {
                System.out.println(LanguageUtils.I18nToString("launch.patch_exception"));
                e.printStackTrace();
                Runtime.getRuntime().exit(0);
            }
        }
        loadLibrary(patchedLib);
    }

    private static void generatePatchCache(File patchedLib, List<String> patchFiles) throws IOException {
        JarFile bukkitJarFile = new JarFile(new File("libraries", "spigot-1.12.2.jar"));
        JarFile forgeJarFile = new JarFile(new File("libraries", "forge-1.12.2-14.23.5.2838-universal.jar"));

        ByteArrayOutputStream bytesOutput = new ByteArrayOutputStream();
        JarOutputStream jarOutput = new JarOutputStream(bytesOutput);
        for (String patchFile : patchFiles) {
            byte[] bytes = patch(patchFile.startsWith("net/minecraftforge/") ? getCleanClass(forgeJarFile, patchFile) : getCleanClass(bukkitJarFile, patchFile), ByteStreams.toByteArray(ClassLoader.getSystemResourceAsStream(patchFile + ".patch")));
            jarOutput.putNextEntry(new JarEntry(patchFile));
            jarOutput.write(bytes);
            jarOutput.closeEntry();
        }

        for (String resFile : ForgeResources) {
            byte[] bytes = getCleanClass(forgeJarFile, resFile);
            jarOutput.putNextEntry(new JarEntry(resFile));
            jarOutput.write(bytes);
            jarOutput.closeEntry();
        }

        for (String resFile : BukkitResources) {
            byte[] bytes = getCleanClass(bukkitJarFile, resFile);
            jarOutput.putNextEntry(new JarEntry(resFile));
            jarOutput.write(bytes);
            jarOutput.closeEntry();
        }

        JarFile serverJar = new JarFile(new File(URLDecoder.decode(CatServerLaunch.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")));
        Enumeration<JarEntry> enumeration = serverJar.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry entry = enumeration.nextElement();
            String name = entry.getName();
            if (entry.isDirectory() || name.endsWith(".patch") || name.equals("patchFiles.txt")) continue;

            byte[] bytes = ByteStreams.toByteArray(serverJar.getInputStream(entry));
            jarOutput.putNextEntry(new JarEntry(name));
            jarOutput.write(bytes);
            jarOutput.closeEntry();
        }

        jarOutput.flush();
        jarOutput.close();
        Files.write(bytesOutput.toByteArray(), patchedLib);
    }

    private static void loadLibrary(File file) {
        try {
            URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(cl, file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] getCleanClass(JarFile jarFile,String name) throws IOException
    {
        ZipEntry zipEntry = jarFile.getEntry(name);
        if (zipEntry == null)
            return EMPTY_DATA;
        return ByteStreams.toByteArray(jarFile.getInputStream(zipEntry));
    }

    public static byte[] patch(byte[] cleanData, byte[] patchData) throws IOException {
        if (patchData.length == 0) return EMPTY_DATA;
        for (int i = 0; i < patchData.length; i++) {
            patchData[i] ^= 233;
        }
        return PATCHER.patch(cleanData, patchData);
    }
}
