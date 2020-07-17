package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class CoreProtectPatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("net.coreprotect.CoreProtectAPI") || className.equals("net.coreprotect.Functions")) {
            return remapMaterial(basicClass);
        }
        return basicClass;
    }

    private byte[] remapMaterial(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();
                if (next instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) next;
                    if (methodInsnNode.owner.equals("org/bukkit/Material") && methodInsnNode.name.equals("getMaterial") && methodInsnNode.desc.equals("(Ljava/lang/String;)Lorg/bukkit/Material;")) {
                        methodInsnNode.name = "getItemOrBlockMaterial";
                    } else if (methodInsnNode.owner.equals("org/bukkit/Material") && methodInsnNode.name.equals("getMaterial") && methodInsnNode.desc.equals("(I)Lorg/bukkit/Material;")) {
                        methodInsnNode.name = "getBlockMaterial";
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
