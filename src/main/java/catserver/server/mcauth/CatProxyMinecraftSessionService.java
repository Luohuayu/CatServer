package catserver.server.mcauth;

import catserver.server.CatServer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

public class CatProxyMinecraftSessionService extends YggdrasilMinecraftSessionService {
    protected CatProxyMinecraftSessionService(CatProxyAuthenticationService authenticationService) {
        super(authenticationService);
    }
    @Override
    protected GameProfile fillGameProfile(GameProfile profile, boolean requireSecure) {
        if (CatServer.disableUpdateGameProfile) {
            return profile;
        }
        return super.fillGameProfile(profile, requireSecure);
    }

}
