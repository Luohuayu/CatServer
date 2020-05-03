package catserver.server.asm;

import java.util.function.Predicate;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class SideTransformer implements IClassTransformer {
    private final Predicate<? super MethodNode> filter = (method) ->
    method.desc.contains("Lnet/minecraft/client/") && (
        method.desc.contains("Lnet/minecraft/client/util/ITooltipFlag") ||
        (method.desc.contains("Lnet/minecraft/client/renderer/") && !method.desc.contains("Lnet/minecraft/client/renderer/block/model/ModelResourceLocation"))
    );

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        boolean removed = node.methods.removeIf(filter);

        if (removed) {
            ClassWriter writer = new ClassWriter(0);
            node.accept(writer);
            return writer.toByteArray();
        }

        return basicClass;
    }
}
