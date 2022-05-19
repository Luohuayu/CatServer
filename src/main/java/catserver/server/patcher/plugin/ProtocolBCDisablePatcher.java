package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import java.util.Objects;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ProtocolBCDisablePatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("com.comphenix.protocol.ProtocolConfig")) {
            return patchProtocolConfig(basicClass);
        }
        return basicClass;
    }

    private byte[] patchProtocolConfig(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (Objects.equals(method.name, "isBackgroundCompilerEnabled") && Objects.equals(method.desc, "()Z")) {
                method.instructions.clear();
                method.instructions.add(new InsnNode(Opcodes.ICONST_0));
                method.instructions.add(new InsnNode(Opcodes.IRETURN));
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
