package catserver.server;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;

import catserver.server.remapper.NetworkTransformer;
import catserver.server.remapper.SideTransformer;

import java.util.Map;

public class CatCorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return !CatServer.isDev() ? new String[] {
                NetworkTransformer.class.getCanonicalName(),
                SideTransformer.class.getCanonicalName()
        } : null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
