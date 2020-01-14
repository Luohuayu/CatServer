package catserver.server;

import catserver.server.remapper.MethodTransformer;
import catserver.server.remapper.SideTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

public class CatCorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return !FMLLaunchHandler.isDeobfuscatedEnvironment() ? new String[] {
                MethodTransformer.class.getCanonicalName(),
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
