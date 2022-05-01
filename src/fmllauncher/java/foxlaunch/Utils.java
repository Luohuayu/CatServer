package foxlaunch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Utils {
    public static String getFileMD5(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");

            final byte[] buffer = new byte[4096];
            int read = in.read(buffer, 0, 4096);

            while (read > -1) {
                md.update(buffer, 0, read);
                read = in.read(buffer, 0, 4096);
            }

            byte[] digest = md.digest();
            return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest)).toUpperCase();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public static String getFileSHA256(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            final byte[] buffer = new byte[4096];
            int read = in.read(buffer, 0, 4096);

            while (read > -1) {
                md.update(buffer, 0, read);
                read = in.read(buffer, 0, 4096);
            }

            byte[] digest = md.digest();
            return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest)).toUpperCase();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public static URL pathToURL(String path) throws Exception {
        return new File(path).toURI().toURL();
    }

    public static void relaunch(String mainClass, URL[] classPath, String[] args, boolean closeClassLoader) throws Exception {
        URLClassLoader ucl = new URLClassLoader(classPath, null);
        Class.forName(mainClass, true, ucl).getMethod("main", String[].class).invoke(null, new Object[] { args });
        if (closeClassLoader) {
            ucl.close();
        }
    }

    public static boolean isJarCorrupted(File jarFile) {
        try {
            if (jarFile.exists()) {
                new JarFile(jarFile).close();
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static File unpackSingleFileZip(File file) throws IOException {
        try (ZipFile zipFile = new ZipFile(file)) {
            if (zipFile.size() > 1) {
                throw new IOException("Not single file zip!");
            }

            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            if (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                File outFile = new File(file.getParentFile(), zipEntry.getName());
                try (FileOutputStream out = new FileOutputStream(outFile)) {
                    try (InputStream in = zipFile.getInputStream(zipEntry)) {
                        byte[] bytes = new byte[4096];
                        int readSize;
                        while ((readSize = in.read(bytes)) > 0) {
                            out.write(bytes, 0, readSize);
                        }
                    }
                    out.flush();
                    return outFile;
                }
            } else {
                throw new IOException("Empty zip!");
            }
        } finally {
            try { file.delete(); } catch (Exception ignored) {}
        }
    }

    public static File findServerJar() throws IOException {
        try {
            URL jarUrl = Utils.class.getProtectionDomain().getCodeSource().getLocation();
            File jarFile = new File(URLDecoder.decode(jarUrl.getPath(), "UTF-8"));
            if (jarFile.isFile()) {
                return jarFile;
            } else {
                throw new IOException(jarFile.getName() + " is not a file!");
            }
        } catch (IOException e) {
            String s = System.getProperty("java.class.path");
            if (s != null) {
                if (s.replace(":", ";").split(";").length == 1) {
                    return new File(s);
                }
            }
            throw e;
        }
    }
}
