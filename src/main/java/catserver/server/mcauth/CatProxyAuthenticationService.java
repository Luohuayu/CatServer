package catserver.server.mcauth;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import java.net.Proxy;

public class CatProxyAuthenticationService extends YggdrasilAuthenticationService {
    public CatProxyAuthenticationService(Proxy proxy, String clientToken) {
        super(proxy, clientToken);
    }

    @Override
    public MinecraftSessionService createMinecraftSessionService() {
        return new CatProxyMinecraftSessionService(this);
    }
}
