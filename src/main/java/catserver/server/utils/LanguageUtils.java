package catserver.server.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LanguageUtils {
    private static Map<String, String> CN = new HashMap<>();
    private static Map<String, String> EN = new HashMap<>();
    private static Map<String, String> current;

    static {
        CN.put("launch.outdated", "服务端版本过旧请更新!");
        CN.put("launch.lib_need_download", "首次运行服务端需要下载库文件才能运行,请耐心等待..");
        CN.put("launch.lib_failure_download_list", "库文件列表下载失败,请检查网络!");
        CN.put("launch.lib_exception", "校验库文件时发生错误,请检查网络或手动下载,服务端将尝试继续启动!");
        CN.put("launch.lib_failure_check", "文件 %s 校验失败, 你也可以手动下载: %s");
        CN.put("launch.lib_failure_download", "下载文件失败(HTTP状态: %s), 你也可以手动下载: %s");
        CN.put("launch.lib_downloading", "正在下载文件 %s 大小: %s");
        CN.put("launch.eula_need_accept", "你需要同意Mojang和CatServer的EULA才能使用,输入accept以同意EULA!");
        CN.put("launch.patch_patching", "正在生成服务端缓存,请耐心等待..");
        CN.put("launch.patch_exception", "生成服务端缓存发生错误!");
        CN.put("launch.patch_does_not_exist", "patchFiles.txt不存在,请勿直接运行服务端缓存文件!");

        EN.put("launch.outdated", "The server version is outdated, please update!");
        EN.put("launch.lib_need_download", "Downloading libraries file, please wait..");
        EN.put("launch.lib_failure_download_list", "Unable to download libraries list, please check the network!");
        EN.put("launch.lib_exception", "An error occurred while checking the library file, will try to continue to start!");
        EN.put("launch.lib_failure_check", "File %s verification failed, URL: %s");
        EN.put("launch.lib_failure_download", "File download failed(HTTP Status: %s), URL: %s");
        EN.put("launch.lib_downloading", "Downloading %s Size: %s");
        EN.put("launch.eula_need_accept", "You need to agree to the EULA in order to run the server, please enter \"accept\"");
        EN.put("launch.patch_patching", "Patching server jar, please wait..");
        EN.put("launch.patch_exception", "An error occurred while patch!");
        EN.put("launch.patch_does_not_exist", "patchFiles.txt does not exist, do not run the patched jar!");

        current = (Locale.SIMPLIFIED_CHINESE.equals(Locale.getDefault()) || Locale.TRADITIONAL_CHINESE.equals(Locale.getDefault())) ? CN : EN;
    }

    public static String I18nToString(String text) {
        return current.getOrDefault(text, "null");
    }
}
