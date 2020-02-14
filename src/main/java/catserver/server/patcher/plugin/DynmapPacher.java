package catserver.server.patcher.plugin;

import catserver.server.CatServer;
import catserver.server.patcher.IPatcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class DynmapPacher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("org.dynmap.bukkit.helper.BukkitVersionHelperCB")) {
            return patchArrayRemap(patchBukkitVersionHelperCB(basicClass));
        }
        if (className.equals("org.dynmap.bukkit.helper.BukkitVersionHelperGeneric")) {
            return patchArrayRemap(basicClass);
        }
        return basicClass;
    }

    private byte[] patchArrayRemap(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();
                if (next instanceof LdcInsnNode) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) next;
                    if (ldcInsnNode.cst instanceof String) {
                        String str = (String) ldcInsnNode.cst;
                        if ("[Lnet.minecraft.server.Block;".equals(str)) {
                            ldcInsnNode.cst = "[Lnet.minecraft.block.Block;";
                        } else if ("[Lnet.minecraft.server.BiomeBase;".equals(str)) {
                            ldcInsnNode.cst = "[Lnet.minecraft.world.biome.Biome;";
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchBukkitVersionHelperCB(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("getNMSPackage")) {
                InsnList insnList = new InsnList();
                insnList.add(new LdcInsnNode("net.minecraft.server." + CatServer.getNativeVersion()));
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
