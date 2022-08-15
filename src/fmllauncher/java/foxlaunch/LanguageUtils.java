package foxlaunch;

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
        CN.put("launch.lib_failure_download_source_error", "下载源不可用(地址: %s | HTTP状态: %s), 已切换其他下载源");
        CN.put("launch.lib_downloading", "正在下载文件 %s 大小: %s");
        CN.put("launch.lib_download_completed", "库文件下载完成, 如果出现错误只需要重新运行服务端");

        CN.put("versioncheck.new_version", "检查到CatServer有新版本: %s, 你可以从构建站(https://catserver.moe/download/catserver_1_16_5)下载更新或设置启动参数关闭版本检查");
        CN.put("versioncheck.failure", "更新检查异常: %s");

        EN.put("launch.java_wrong", "Current Java version (%s) is not supported, please replace to Java8");
        EN.put("launch.lib_missing", "Missing libraries file, downloading.. (Add -Dcatserver.skipCheckLibraries=true to the jvm flag to skip it)");
        EN.put("launch.lib_failure_check", "File %s verification failed, URL: %s");
        EN.put("launch.lib_failure_download", "File download failed(HTTP Status: %s), URL: %s");
        EN.put("launch.lib_failure_download_source_error", "Download source not available(URL: %s | HTTP Status: %s), switched to another download source");
        EN.put("launch.lib_downloading", "Downloading %s Size: %s");
        EN.put("launch.lib_download_completed", "The libraries file download completed, if an error occurs only need re-run the server");

        EN.put("versioncheck.new_version", "Check CatServer has a new version: %s, you can download the update from https://catserver.moe/download/catserver_1_16_5 or set jvm param to disable the version check (-Dloliserver.disableVersionCheck=false)");
        EN.put("versioncheck.failure", "VersionCheck exception: %s");

        current = ("zh".equals(Locale.getDefault().getLanguage()) ? CN : EN);
    }

    public static String I18nToString(String text) {
        return current.getOrDefault(text, "null");
    }
}
