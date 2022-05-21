package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import catserver.server.remapper.RemapRules;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.ListIterator;

public class DynmapPatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("org.dynmap.bukkit.helper.BukkitVersionHelperGeneric")) {
            return patchBukkitVersionHelperGeneric(basicClass);
        }
        if (className.equals("org.dynmap.bukkit.helper.v116_4.BukkitVersionHelperSpigot116_4")) {
            return patchBukkitVersionHelperSpigot116_4(basicClass);
        }
        return basicClass;
    }

    private byte[] patchBukkitVersionHelperSpigot116_4(byte[] basicClass) {
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

    public static byte[] patchBukkitVersionHelperGeneric(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode insnNode = iterator.next();
                if (insnNode instanceof LdcInsnNode) {
                    LdcInsnNode insnNode_ = (LdcInsnNode) insnNode;
                    if (insnNode_.cst instanceof String) {
                        String cst = (String) insnNode_.cst;
                        if ("[Lnet.minecraft.server.BiomeBase;".equals(cst)) {
                            insnNode_.cst = "[Lnet.minecraft.world.biome.Biome;";
                        }
                        if ("[Lnet.minecraft.server.BiomeStorage;".equals(cst)) {
                            insnNode_.cst = "[Lnet.minecraft.world.biome.BiomeContainer;";
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}

