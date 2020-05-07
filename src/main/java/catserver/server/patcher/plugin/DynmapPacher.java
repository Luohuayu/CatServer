package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import catserver.server.remapper.RemapRules;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class DynmapPacher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("org.dynmap.bukkit.helper.BukkitVersionHelperCB") || className.equals("org.dynmap.bukkit.BukkitVersionHelperCB")) {
            return patchBukkitVersionHelperCB(basicClass);
        }
        return basicClass;
    }

    private byte[] patchBukkitVersionHelperCB(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("getNMSPackage")) {
                InsnList insnList = new InsnList();
                insnList.add(new LdcInsnNode(RemapRules.getNMSPackage()));
                insnList.add(new InsnNode(Opcodes.ARETURN));
                method.instructions = insnList;
                method.tryCatchBlocks.clear();
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
