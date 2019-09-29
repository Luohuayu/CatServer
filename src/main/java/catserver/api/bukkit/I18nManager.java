package catserver.api.bukkit;

import com.google.common.collect.Maps;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class I18nManager {
    private static Map<String, String> i18n = Maps.newHashMap();
    private static boolean initialized = false;

    public static void loadI18n() {
        if (initialized) return;

        LanguageMap.parseLangFile(I18nManager.class.getResourceAsStream("/assets/minecraft/lang/zh_cn.lang")).forEach((k, v) -> i18n.put(k, v));

        for (ModContainer modContainer : Loader.instance().getActiveModList()) {
            try {
                String langFile = "assets/" + modContainer.getModId().toLowerCase() + "/lang/zh_cn.lang";
                String langFile2 = "assets/" + modContainer.getModId().toLowerCase() + "/lang/zh_CN.lang";
                File source = modContainer.getSource();

                if (source.exists()) {
                    ZipFile zip = new ZipFile(source);
                    ZipEntry entry = zip.getEntry(langFile);
                    if (entry == null) entry = zip.getEntry(langFile2);
                    if (entry == null) throw new FileNotFoundException();
                    InputStream stream = zip.getInputStream(entry);
                    if (stream != null)
                        LanguageMap.parseLangFile(stream).forEach((k, v) -> i18n.put(k, v));
                }
            } catch (Exception e) {}
        }

        initialized = true;
    }

    public static String getI18nLocal(String i18n) {
        String text = I18nManager.i18n.get(i18n);
        if (text == null)
            text = I18n.translateToLocal(i18n);
        return text;
    }
}
