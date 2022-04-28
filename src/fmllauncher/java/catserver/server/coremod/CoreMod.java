package catserver.server.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import java.util.Arrays;
import java.util.List;
import org.objectweb.asm.tree.ClassNode;

public class CoreMod {
    public static List<ITransformer<ClassNode>> getTransformers() {
        return Arrays.asList(
                new CommandNodeTransformer()
        );
    }
}
