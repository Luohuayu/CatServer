package catserver.server.remapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import catserver.server.CatServer;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MavenShade;

public class MappingLoader {
    private static final String org_bukkit_craftbukkit = new String(new char[] {'o','r','g','/','b','u','k','k','i','t','/','c','r','a','f','t','b','u','k','k','i','t'});
    private static final JarMapping globalJarMapping = new JarMapping();

    private static Field fieldPackages;
    private static Field fieldClasses;
    private static Field fieldFields;
    private static Field fieldMethods;

    static {
        initGlobalMapping();
    }

    private static void initGlobalMapping() {
        try {
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/com/google/gson", "com/google/gson");
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/it/unimi/dsi/fastutil", "it/unimi/dsi/fastutil");
            globalJarMapping.packages.put(org_bukkit_craftbukkit + "/libs/joptsimple", "joptsimple");
            globalJarMapping.classes.put(org_bukkit_craftbukkit + "/Main", org_bukkit_craftbukkit + "/" + CatServer.getNativeVersion() + "/Main");
            globalJarMapping.classes.put(org_bukkit_craftbukkit + "/Main$1", org_bukkit_craftbukkit + "/" + CatServer.getNativeVersion() + "/Main$1");
            globalJarMapping.methods.put("org/bukkit/Bukkit/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "getOnlinePlayers_Array");
            globalJarMapping.methods.put("org/bukkit/Server/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "getOnlinePlayers_Array");

            loadNmsMappings(globalJarMapping, CatServer.getNativeVersion());

            fieldPackages = JarMapping.class.getDeclaredField("packages");
            fieldPackages.setAccessible(true);
            ReflectionUtils.modifyFiledFinal(fieldPackages);

            fieldClasses = JarMapping.class.getDeclaredField("classes");
            fieldClasses.setAccessible(true);
            ReflectionUtils.modifyFiledFinal(fieldClasses);

            fieldFields = JarMapping.class.getDeclaredField("fields");
            fieldFields.setAccessible(true);
            ReflectionUtils.modifyFiledFinal(fieldFields);

            fieldMethods = JarMapping.class.getDeclaredField("methods");
            fieldMethods.setAccessible(true);
            ReflectionUtils.modifyFiledFinal(fieldMethods);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadNmsMappings(JarMapping jarMapping, String obfVersion) throws IOException {
        Map<String, String> relocations = new HashMap<String, String>();
        relocations.put("net.minecraft.server", "net.minecraft.server." + obfVersion);

        jarMapping.loadMappings(
                new BufferedReader(new InputStreamReader(MappingLoader.class.getClassLoader().getResourceAsStream("mappings/"+obfVersion+"/cb2srg.srg"))),
                new MavenShade(relocations),
                null, false);
    }

    public static JarMapping loadMapping() {
        JarMapping jarMapping = new JarMapping();
        try {
            if (catserver.server.launch.Java11Support.enable) {
                catserver.server.launch.Java11Support.FieldHelper.set(jarMapping, fieldPackages, fieldPackages.get(globalJarMapping));
                catserver.server.launch.Java11Support.FieldHelper.set(jarMapping, fieldClasses, fieldClasses.get(globalJarMapping));
                catserver.server.launch.Java11Support.FieldHelper.set(jarMapping, fieldFields, fieldFields.get(globalJarMapping));
                catserver.server.launch.Java11Support.FieldHelper.set(jarMapping, fieldMethods, fieldMethods.get(globalJarMapping));
            } else {
                fieldPackages.set(jarMapping, fieldPackages.get(globalJarMapping));
                fieldClasses.set(jarMapping, fieldClasses.get(globalJarMapping));
                fieldFields.set(jarMapping, fieldFields.get(globalJarMapping));
                fieldMethods.set(jarMapping, fieldMethods.get(globalJarMapping));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jarMapping;
    }


}
