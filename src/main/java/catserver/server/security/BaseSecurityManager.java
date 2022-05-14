package catserver.server.security;

public class BaseSecurityManager {
    public static void onTick() {
        OpSecurityManager.tick();
    }
}
