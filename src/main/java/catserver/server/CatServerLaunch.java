package catserver.server;

public class CatServerLaunch {
    public static void main(String[] args) throws Throwable {
        LibrariesManager.checkLibraries();
        Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[] { args });
    }
}
