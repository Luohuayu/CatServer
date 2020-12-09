package catserver.server.asm.proxy;

import com.google.common.collect.Maps;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.Map;

public class TransformerProxyRules {
    private static Map<String, Class<?>> transformerProxyRules = Maps.newHashMap();

    static {
        transformerProxyRules.put("com.teamwizardry.librarianlib.asm.LibLibTransformer", LibrarianLibTransformerProxy.class);
        transformerProxyRules.put("me.paulf.wings.server.asm.WingsRuntimePatcher", WingsTransformerProxy.class);
        transformerProxyRules.put("com.teamwizardry.wizardry.asm.WizardryTransformer", WizardryTransformerProxy.class);
    }

    public static IClassTransformer getTarget(IClassTransformer originTransformer) {
        Class<?> target = transformerProxyRules.get(originTransformer.getClass().getName());
        if (target != null) {
            try {
                return (IClassTransformer)target.getConstructor(IClassTransformer.class).newInstance(originTransformer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return originTransformer;
    }
}
