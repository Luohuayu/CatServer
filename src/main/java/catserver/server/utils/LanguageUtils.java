package catserver.server.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LanguageUtils {
    private static Map<String, String> CN = new HashMap<>();
    private static Map<String, String> EN = new HashMap<>();
    private static Map<String, String> current;

    static {
        CN.put("launch.lib_exception", "校验库文件时发生错误,请检查网络或手动下载,服务端将尝试继续启动!");
        CN.put("launch.lib_failure_check", "文件 %s 校验失败, 你也可以手动下载: %s");
        CN.put("launch.lib_failure_download", "下载文件失败(HTTP状态: %s), 你也可以手动下载: %s");
        CN.put("launch.lib_downloading", "正在下载文件 %s 大小: %s");

        EN.put("launch.lib_exception", "An error occurred while checking the library file, will try to continue to start!");
        EN.put("launch.lib_failure_check", "File %s verification failed, URL: %s");
        EN.put("launch.lib_failure_download", "File download failed(HTTP Status: %s), URL: %s");
        EN.put("launch.lib_downloading", "Downloading %s Size: %s");

        current = ("zh".equals(Locale.getDefault().getLanguage()) ? CN : EN);
    }

    public static String I18nToString(String text) {
        return current.getOrDefault(text, "null");
    }
}
