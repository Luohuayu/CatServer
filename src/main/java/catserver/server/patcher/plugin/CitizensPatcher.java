package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import java.util.ListIterator;
import java.util.Objects;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CitizensPatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("net.citizensnpcs.nms.v1_12_R1.util.PlayerPathfinder")) {
            return remapPlayerPathfinder(basicClass);
        }
        return basicClass;
    }

    private byte[] remapPlayerPathfinder(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode insnNode = insnIterator.next();
                if (insnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                    if (Objects.equals(methodInsnNode.name, "func_186334_a") || Objects.equals(methodInsnNode.name,"func_75853_a") || Objects.equals(methodInsnNode.name,"func_186335_a")) {
                        methodInsnNode.name = "a"; // Don't remap to private func
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
