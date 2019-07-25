package catserver.server;

import catserver.server.delta.GDiffPatcher;
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
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class CatServerLaunch {
    private static String[] resFiles = new String[] {"forge_logo.png", "forge_at.cfg", "forge.srg", "forge.exc", "deobfuscation_data-1.12.2.lzma", "mcpmod.info", "mcplogo.png", "url.png", "mcp/MethodsReturnNonnullByDefault.class", "assets/forge/lang/en_US.lang"};

    private static final byte[] EMPTY_DATA = new byte[0];
    private static final GDiffPatcher PATCHER = new GDiffPatcher();

    public static void main(String[] args) throws Throwable {
        System.out.println("\n" +
                "   _____      _    _____                          \n" +
                "  / ____|    | |  / ____|                         \n" +
                " | |     __ _| |_| (___   ___ _ ____   _____ _ __ \n" +
                " | |    / _` | __|\\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|\n" +
                " | |___| (_| | |_ ____) |  __/ |   \\ V /  __/ |   \n" +
                "  \\_____\\__,_|\\__|_____/ \\___|_|    \\_/ \\___|_| \n");
        checkEULA();


        InputStream in = ClassLoader.getSystemResourceAsStream("patchFiles.txt");
        if (in == null) {
            System.out.println("patchFiles.txt不存在,请勿直接运行服务端缓存文件!");
            Runtime.getRuntime().exit(0);
        }
        if (!new File("minecraft_server.1.12.2.jar").exists()) {
            System.out.println("minecraft_server.1.12.2.jar不存在,请将官服放到服务端目录!");
            Runtime.getRuntime().exit(0);
        }

        String version = CatServerLaunch.class.getPackage().getImplementationVersion() != null ? CatServerLaunch.class.getPackage().getImplementationVersion() : "null";
        File patchedLib = new File("cache-patched-" + version + ".jar");
        if (!patchedLib.exists()) {
            System.out.println("正在生成服务端缓存,请耐心等待..");
            try {
                List<String> patchFiles = IOUtils.readLines(in, StandardCharsets.UTF_8);
                generatePatchCache(patchedLib, patchFiles);
            } catch (Exception e) {
                System.out.println("生成服务端缓存发生错误!");
                e.printStackTrace();
                Runtime.getRuntime().exit(0);
            }
        }
        loadLibrary(patchedLib);

        Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[] { args });
    }

    private static void generatePatchCache(File patchedLib, List<String> patchFiles) throws IOException {
        JarFile bukkiJarFile = new JarFile(new File("libraries", "spigot-1.12.2.jar"));
        JarFile forgeJarFile = new JarFile(new File("libraries", "forge-1.12.2-14.23.5.2838-universal.jar"));

        ByteArrayOutputStream bytesOutput = new ByteArrayOutputStream();
        JarOutputStream jarOutput = new JarOutputStream(bytesOutput);
        for (String patchFile : patchFiles) {
            byte[] bytes = patch(patchFile.startsWith("net/minecraftforge/") ? getCleanClass(forgeJarFile, patchFile) : getCleanClass(bukkiJarFile, patchFile), ByteStreams.toByteArray(ClassLoader.getSystemResourceAsStream(patchFile + ".patch")));
            jarOutput.putNextEntry(new JarEntry(patchFile));
            jarOutput.write(bytes);
            jarOutput.closeEntry();
        }

        for (String resFile : resFiles) {
            byte[] bytes = getCleanClass(forgeJarFile, resFile);
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
        return patchData.length == 0 ? EMPTY_DATA : PATCHER.patch(cleanData, patchData);
    }

    private static void checkEULA() {
        File eulaFile = new File(".eula");
        if (!eulaFile.exists()) {
            try {
                System.out.println("你需要同意Mojang和CatServer的EULA才能使用,输入accept以同意EULA!");
                Scanner sc = new Scanner(System.in);
                while (!"accept".equalsIgnoreCase(sc.next()));
                eulaFile.createNewFile();
                sc.close();
            } catch (Exception e) {}
        }
    }
}
