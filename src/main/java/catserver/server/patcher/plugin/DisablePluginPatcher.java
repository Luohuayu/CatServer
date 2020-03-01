package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;

public class DisablePluginPatcher implements IPatcher {
    private final String reason;

    public DisablePluginPatcher(String reason) {
        this.reason = reason;
    }

    @Override
    public byte[] transform(String className, byte[] basicClass) {
        throw new RuntimeException("The plugin has been disabled! Reason: " + this.reason);
    }
}
