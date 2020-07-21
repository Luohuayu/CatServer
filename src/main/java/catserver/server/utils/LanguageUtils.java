package catserver.server.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LanguageUtils {
    private static Map<String, String> CN = new HashMap<>();
    private static Map<String, String> EN = new HashMap<>();
    private static Map<String, String> current;

    static {
        CN.put("launch.java_wrong", "服务端不支持当前Java版本(%s), 请更换为Java8");
        CN.put("launch.lib_missing", "需要下载缺少的库文件, 请耐心等待..");
        CN.put("launch.lib_failure_check", "文件 %s 校验失败, 你也可以手动下载: %s");
        CN.put("launch.lib_failure_download", "下载文件失败(HTTP状态: %s), 你也可以手动下载: %s");
        CN.put("launch.lib_downloading", "正在下载文件 %s 大小: %s");
        CN.put("versioncheck.new_version", "检查到CatServer有新版本(%s), 请到官网(https://catserver.moe)下载更新或在catserver.yml中关闭版本检查");
        CN.put("versioncheck.failure", "CatServer更新检查失败, 请检查网络: %s");

        EN.put("launch.java_wrong", "Current Java version (%s) is not supported, please replace to Java8");
        EN.put("launch.lib_missing", "Missing libraries file, downloading..");
        EN.put("launch.lib_failure_check", "File %s verification failed, URL: %s");
        EN.put("launch.lib_failure_download", "File download failed(HTTP Status: %s), URL: %s");
        EN.put("launch.lib_downloading", "Downloading %s Size: %s");
        EN.put("versioncheck.new_version", "Check CatServer has a new version(%s), you can download the update from https://catserver.moe or turn off the version check in catserver.yml");
        EN.put("versioncheck.failure", "CatServer version check failed, please check network: %s");

        current = ("zh".equals(Locale.getDefault().getLanguage()) ? CN : EN);
    }

    public static String I18nToString(String text) {
        return current.getOrDefault(text, "null");
    }
}
