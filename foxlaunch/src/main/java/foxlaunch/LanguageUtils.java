package foxlaunch;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LanguageUtils {
    private static Map<String, String> CN = new HashMap<>();
    private static Map<String, String> EN = new HashMap<>();
    private static Map<String, String> current;

    static {
        CN.put("launch.java_wrong", "服务端不支持当前Java版本(%s), 请更换为Java17");
        CN.put("launch.lib_missing", "需要下载缺少的库文件, 请耐心等待..");
        CN.put("launch.lib_failure_check", "文件 %s 校验失败, 你也可以手动下载: %s");
        CN.put("launch.lib_failure_download", "下载文件失败(HTTP状态: %s), 你也可以手动下载: %s");
        CN.put("launch.lib_downloading", "正在下载文件 %s 大小: %s");
        CN.put("launch.lib_download_completed", "库文件下载完成, 如果出现错误只需要重新运行服务端");

        EN.put("launch.java_wrong", "Current Java version (%s) is not supported, please replace to Java17");
        EN.put("launch.lib_missing", "Missing libraries file, downloading.. (Add -Dcatserver.skipCheckLibraries=true to the jvm flag to skip it)");
        EN.put("launch.lib_failure_check", "File %s verification failed, URL: %s");
        EN.put("launch.lib_failure_download", "File download failed(HTTP Status: %s), URL: %s");
        EN.put("launch.lib_downloading", "Downloading %s Size: %s");
        EN.put("launch.lib_download_completed", "The libraries file download completed, if an error occurs only need re-run the server");

        current = ("zh".equals(Locale.getDefault().getLanguage()) ? CN : EN);
    }

    public static String I18nToString(String text) {
        return current.getOrDefault(text, "null");
    }
}
