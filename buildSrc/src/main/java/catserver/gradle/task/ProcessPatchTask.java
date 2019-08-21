package catserver.gradle.task;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.nothome.delta.Delta;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.*;
import java.util.zip.ZipEntry;

public class ProcessPatchTask extends DefaultTask {
    @Getter
    @Setter
    @InputFile
    private File inputJar;

    @Getter
    @Setter
    public Set<String> excludePaths;

    @Getter
    @Setter
    public File bukkitJar;

    @Getter
    @Setter
    public File forgeJar;

    private JarFile bukkitJarFile;
    private JarFile forgeJarFile;

    private static final byte[] EMPTY_DATA = new byte[0];
    private static final Delta DELTA = new Delta();

    @TaskAction
    void doTask() throws Exception {
        bukkitJarFile = new JarFile(bukkitJar);
        forgeJarFile = new JarFile(forgeJar);

        File outputJar = new File(getProject().getProjectDir(), "target/" + inputJar.getName());
        outputJar.getParentFile().mkdirs();
        createJar(inputJar, outputJar);
    }

    private void createJar(File inputFile, File outputFile) {
        ByteArrayOutputStream bytesOutput = new ByteArrayOutputStream();
        Collection<String> patchFiles = Lists.newArrayList();
        try (JarOutputStream jarOutput = new JarOutputStream(bytesOutput)) {
            JarFile jarFile = new JarFile(inputFile);
            Enumeration<JarEntry> enumeration = jarFile.entries();
            w:while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();
                String name = entry.getName();
                if (entry.isDirectory()) continue;
                for (String excludePath : excludePaths) {
                    if (name.startsWith(excludePath)) continue w;
                }

                byte[] bytes = ByteStreams.toByteArray(jarFile.getInputStream(entry));

                if (name.startsWith("org/bukkit/") || name.startsWith("org/spigotmc/")) {
                    patchFiles.add(entry.getName());
                    bytes = generatePatch(getCleanClass(bukkitJarFile, name), bytes);
                    entry = new JarEntry(entry.getName() + ".patch");
                } else if (entry.getName().startsWith("net/minecraftforge/")) {
                    patchFiles.add(entry.getName());
                    bytes = generatePatch(getCleanClass(forgeJarFile, name), bytes);
                    entry = new JarEntry(entry.getName() + ".patch");
                }

                jarOutput.putNextEntry(entry);
                jarOutput.write(bytes);
                jarOutput.closeEntry();
            }
            generatePatchList(jarOutput, patchFiles);

            jarOutput.flush();
            jarOutput.close();
            Files.write(bytesOutput.toByteArray(), outputFile);
        } catch (IOException e) {
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

    private void generatePatchList(JarOutputStream jarOutput, Collection<String> patchFiles) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.writeLines(patchFiles, null, out, StandardCharsets.UTF_8);
        jarOutput.putNextEntry(new JarEntry("patchFiles.txt"));
        jarOutput.write(out.toByteArray());
        jarOutput.closeEntry();
    }

    private static byte[] generatePatch(byte[] clean, byte[] dirty) throws IOException {
        if (dirty.length == 0) return EMPTY_DATA;
        byte[] patch = DELTA.compute(clean, dirty);
        for (int i = 0; i < patch.length; i++) {
            patch[i] ^= 233;
        }
        return patch;
    }
}
