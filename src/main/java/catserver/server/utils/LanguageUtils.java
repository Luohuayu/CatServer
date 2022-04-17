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
        CN.put("launch.java11_compatibility", "即将尝试使用Java11兼容模式启动服务端(即使服务端能够正常启动, 也无法保证MOD和插件可以正常使用, 如果遇到问题请更换为Java8)");
        CN.put("launch.lib_missing", "需要下载缺少的库文件, 请耐心等待..");
        CN.put("launch.lib_failure_check", "文件 %s 校验失败, 你也可以手动下载: %s");
        CN.put("launch.lib_failure_download", "下载文件失败(HTTP状态: %s), 你也可以手动下载: %s");
        CN.put("launch.lib_downloading", "正在下载文件 %s 大小: %s");
        CN.put("launch.lib_download_completed", "库文件下载完成, 如果出现错误只需要重新运行服务端");
        CN.put("versioncheck.new_version", "检查到CatServer有新版本(%s), 请到官网(https://catmc.org)下载更新或在catserver.yml中关闭版本检查");
        CN.put("versioncheck.failure", "CatServer更新检查失败, 请检查网络: %s");
        CN.put("spark.recommend", "服务端已安装spark插件, 推荐使用spark更好的分析性能使用情况!");

        EN.put("launch.java_wrong", "Current Java version (%s) is not supported, please replace to Java8");
        EN.put("launch.java11_compatibility", "Trying to use Java11 compatibility mode to start (even if the server can start, some mods and plugins may not work, please use Java8 if an error occurs)");
        EN.put("launch.lib_missing", "Missing libraries file, downloading.. (Add -Dcatserver.skipCheckLibraries=true to the jvm flag to skip it)");
        EN.put("launch.lib_failure_check", "File %s verification failed, URL: %s");
        EN.put("launch.lib_failure_download", "File download failed(HTTP Status: %s), URL: %s");
        EN.put("launch.lib_downloading", "Downloading %s Size: %s");
        EN.put("launch.lib_download_completed", "The libraries file download completed, if an error occurs only need re-run the server");
        EN.put("versioncheck.new_version", "Check CatServer has a new version(%s), you can download the update from https://catmc.org or disable the version check in catserver.yml");
        EN.put("versioncheck.failure", "CatServer version check failed, please check network: %s");
        EN.put("spark.recommend", "Spark plugin has been installed on the server, recommended to use spark to better analyze performance usage!");

        current = ("zh".equals(Locale.getDefault().getLanguage()) ? CN : EN);
    }

    public static String I18nToString(String text) {
        return current.getOrDefault(text, "null");
    }
}
