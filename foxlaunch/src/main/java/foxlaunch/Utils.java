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

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
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

    public static String getMissingSHA256(String filename) {
        switch (filename) {
            case "minecraft_server.1.20.1.jar":
                return "3AF73A9DC5A102E38147946360DD27D4D70BAE7055BF91CF2151CD5D121B79E0";
            case "server-1.20.1-mappings.txt":
                return "DCA153D20DEFB32CFAC3F069C3BF77B3E13C30AE63847637479D3137E099BB72";
            case "mcp_config-1.20.1-20230612.114412.zip": // 重要: Update it when updating MCP version
                return "F46D1050D8BED9046886B90A0D2FB80B4C6B5120FC35D32E5D37498DBE0A6D2A";
            case "authlib-4.0.43.jar":
                return "697043D19E0B84B04F011DBEC1BEC3D80C04D8C468E7E4C6B221C0183BC1C0EF";
            case "brigadier-1.1.8.jar":
                return "CF65AFA612FFFBE4DC976115DAC0312F48B9C84B3DDAF58BEC8018A9454440D7";
            case "commons-io-2.11.0.jar":
                return "961B2F6D87DBACC5D54ABF45AB7A6E2495F89B75598962D8C723CEA9BC210908";
            case "datafixerupper-6.0.8.jar":
                return "6E38E9FC5404BE7E45189AA14CA6058BC85DF20D6760A212B6D48E666ABF0FEF";
            case "failureaccess-1.0.1.jar":
                return "A171EE4C734DD2DA837E4B16BE9DF4661AFAB72A41ADAF31EB84DFDAF936CA26";
            case "fastutil-8.5.9.jar":
                return "9578BF2A1700CF20D21746A2EE89E57BA1ABBD37FA9FEDA68FF5E9A28473A7F9";
            case "gson-2.10.1.jar":
                return "4241C14A7727C34FEEA6507EC801318A3D4A90F070E4525681079FB94EE4C593";
            case "guava-31.1-jre.jar":
                return "A42EDC9CAB792E39FE39BB94F3FCA655ED157FF87A8AF78E1D6BA5B07C4A00AB";
            case "javabridge-1.2.24.jar":
                return "B5F8871A1799B36E27A5F2AD8A4B47DB39210031C967B794707B92E9E3F8598E";
            case "jna-5.12.1.jar":
                return "91A814AC4F40D60DEE91D842E1A8AD874C62197984403D0E3C30D39E55CF53B3";
            case "jna-platform-5.12.1.jar":
                return "8CE969116CAC95BD61B07A8D5E07174B352E63301473CAAC72C395E3C08488D2";
            case "jopt-simple-5.0.4.jar":
                return "DF26CC58F235F477DB07F753BA5A3AB243EBE5789D9F89ECF68DD62EA9A66C28";
            case "log4j-api-2.19.0.jar":
                return "5CCB24AD9F92E768D0BC456D3061A737951262DF803E004D2CAD096B75A88D60";
            case "log4j-core-2.19.0.jar":
                return "B4A1796FAB7BFC36DF015C1B4052459147997E8D215A7199D71D05F9E747E4F4";
            case "log4j-slf4j2-impl-2.19.0.jar":
                return "825605EACB2D5605B105C53D4108C18125E0F82F62960D0BE583278B9C524F3C";
            case "logging-1.1.1.jar":
                return "C1756EAF0685DA94142BACC84309BB7CCDDF65E004638C3299AB645710B5938C";
            case "netty-buffer-4.1.82.Final.jar":
                return "A7CE7F6D8C1E82B044CD56765DAB4338251596416DE03B1F9A05D7402429C29D";
            case "netty-codec-4.1.82.Final.jar":
                return "170C0CE091C2E1CC1D952D87A6A30DEB773922507BBC609B962B539F03F53407";
            case "netty-common-4.1.82.Final.jar":
                return "D5923DB1CC68B26BAF5368F1F76EC2C6100D12DB8406C6DE00EB6DBC08052550";
            case "netty-handler-4.1.82.Final.jar":
                return "31015505983DC57EEC491AD4873411BA51F0F3BEB24D22D509336C72511019CB";
            case "netty-resolver-4.1.82.Final.jar":
                return "4CFEBA7EC535964A35722178AEECC87EC186DBA0203D04389A850E8E62575383";
            case "netty-transport-4.1.82.Final.jar":
                return "C210069BE4AB4120F5AC7CB5E4A126D3A007E78A64FD44C9971220E013DBAEF1";
            case "netty-transport-classes-epoll-4.1.82.Final.jar":
                return "73F827FCA18870959B9581C6875130576E5DE684CFB4E6F346F8C1C266840BA3";
            case "netty-transport-native-epoll-4.1.82.Final-linux-aarch_64.jar":
                return "C9551F4622B4C44C9E7DC8C6DC17874B4DDB953A943F402740E64126180EE85E";
            case "netty-transport-native-epoll-4.1.82.Final-linux-x86_64.jar":
                return "D4651F6082FB60F4218F100322DE33E350184F5287900D163698298CF72DDBE7";
            case "netty-transport-native-unix-common-4.1.82.Final.jar":
                return "1096F16527A51DD5F44CED90636110A429CC7359A9A8A91DB25000B747793429";
            case "oshi-core-6.2.2.jar":
                return "EFBCA93DF18F3EB16964A92D8342F0674DD5D90DA575EF8E04EF2E4EABEC6910";
            case "slf4j-api-2.0.1.jar":
                return "B36B99B8D99EA7857554D9B25DFDBD1FB25378C926F7B9E0249983C99335B2C4";
            case "bootstraplauncher-1.1.2.jar":
                return "853B61FD165CAF4103B5B969AFBC5B2B037ABC310ED644518CD967B50D5189A4";
            default:
                return null;
        }
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

    public static File unpackZipEntry(File file, File target, String entry) throws IOException {
        try (ZipFile zipFile = new ZipFile(file)) {
            ZipEntry zipEntry = zipFile.getEntry(entry);
            if (zipEntry != null) {
                try (FileOutputStream out = new FileOutputStream(target)) {
                    try (InputStream in = zipFile.getInputStream(zipEntry)) {
                        byte[] bytes = new byte[4096];
                        int readSize;
                        while ((readSize = in.read(bytes)) > 0) {
                            out.write(bytes, 0, readSize);
                        }
                    }
                    out.flush();
                    return target;
                }
            } else {
                throw new IOException("Zip entry not found: " + entry);
            }
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
