package catserver.server.remapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import catserver.server.CatServer;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.transformer.MavenShade;

public class MappingLoader {
    private static final String org_bukkit_craftbukkit = new String(new char[] {'o','r','g','/','b','u','k','k','i','t','/','c','r','a','f','t','b','u','k','k','i','t'});

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
            jarMapping.packages.put(org_bukkit_craftbukkit + "/libs/com/google/gson", "com/google/gson");
            jarMapping.packages.put(org_bukkit_craftbukkit + "/libs/it/unimi/dsi/fastutil", "it/unimi/dsi/fastutil");
            jarMapping.packages.put(org_bukkit_craftbukkit + "/libs/jline", "jline");
            jarMapping.packages.put(org_bukkit_craftbukkit + "/libs/joptsimple", "joptsimple");
            jarMapping.methods.put("org/bukkit/Bukkit/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "getOnlinePlayers_1710");
            jarMapping.methods.put("org/bukkit/Server/getOnlinePlayers ()[Lorg/bukkit/entity/Player;", "getOnlinePlayers_1710");

            loadNmsMappings(jarMapping, CatServer.getNativeVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jarMapping;
    }
}
