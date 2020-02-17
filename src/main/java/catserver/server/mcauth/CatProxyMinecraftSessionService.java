package catserver.server.mcauth;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

public class CatProxyMinecraftSessionService extends YggdrasilMinecraftSessionService {
    protected CatProxyMinecraftSessionService(CatProxyAuthenticationService authenticationService) {
        super(authenticationService);
    }

    @Override
    protected GameProfile fillGameProfile(GameProfile profile, boolean requireSecure) {
        return profile;
    }

}
