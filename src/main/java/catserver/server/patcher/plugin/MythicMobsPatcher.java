package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class MythicMobsPatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("io.lumine.xikage.mythicmobs.volatilecode.v1_12_R1.VolatileWorldHandler_v1_12_R1")) {
            return patchVolatileWorldHandler_v1_12_R1(basicClass);
        }
        return basicClass;
    }

    private byte[] patchVolatileWorldHandler_v1_12_R1(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            boolean flag = false;
            ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode insnNode = iterator.next();
                if (insnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                    if (methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && "net/minecraft/world/chunk/Chunk".equals(methodInsnNode.owner) && "getEntitySlices".equals(methodInsnNode.name) && "()[Ljava/util/List;".equals(methodInsnNode.desc)) {
                        methodInsnNode.name = "func_177429_s";
                        methodInsnNode.desc = "()[Lnet/minecraft/util/ClassInheritanceMultiMap;";
                    }
                    if (methodInsnNode.getOpcode() == Opcodes.INVOKEINTERFACE && "java/util/List".equals(methodInsnNode.owner) &&
                            ("size".equals(methodInsnNode.name) || "iterator".equals(methodInsnNode.name) || "add".equals(methodInsnNode.name) || "remove".equals(methodInsnNode.name))) {
                        methodInsnNode.owner = "java/util/Collection";
                        flag = true;
                    }
                }
            }
            if (flag) method.localVariables.clear();
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
