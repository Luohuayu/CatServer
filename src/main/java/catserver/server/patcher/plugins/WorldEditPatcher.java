package catserver.server.patcher.plugins;

import catserver.server.patcher.IPatcher;
import org.bukkit.Material;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

public class WorldEditPatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("com.sk89q.worldedit.bukkit.WorldEditPlugin")) {
            System.setProperty("worldedit.bukkit.adapter", "com.sk89q.worldedit.bukkit.adapter.impl.v1_18_R2.PaperweightAdapter");
        } else if (className.equals("com.sk89q.worldedit.bukkit.BukkitAdapter")) {
            return patchBukkitAdapter(basicClass);
        } else if (className.equals("com.sk89q.worldedit.bukkit.adapter.impl.v1_18_R2.PaperweightAdapter")) {
            return patchPaperweightAdapter(basicClass);
        }
        return basicClass;
    }

    private byte[] patchBukkitAdapter(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode methodNode : node.methods) {
            if (methodNode.name.equals("adapt") && methodNode.desc.equals("(Lcom/sk89q/worldedit/world/block/BlockType;)Lorg/bukkit/Material;")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/sk89q/worldedit/world/block/BlockType", "getId", "()Ljava/lang/String;"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(WorldEditPatcher.class), "adaptHook", "(Ljava/lang/String;)Lorg/bukkit/Material;"));
                insnList.add(new InsnNode(Opcodes.ARETURN));
                methodNode.instructions = insnList;
            }

            if (methodNode.name.equals("adapt") && methodNode.desc.equals("(Lcom/sk89q/worldedit/world/item/ItemType;)Lorg/bukkit/Material;")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/sk89q/worldedit/world/item/ItemType", "getId", "()Ljava/lang/String;"));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(WorldEditPatcher.class), "adaptHook", "(Ljava/lang/String;)Lorg/bukkit/Material;"));
                insnList.add(new InsnNode(Opcodes.ARETURN));
                methodNode.instructions = insnList;
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static Material adaptHook(String type) {
        checkNotNull(type);
        return Material.getMaterial(
                (!type.startsWith("minecraft:") ? type.replace(":", "_") : type.substring(10))
                        .toUpperCase(Locale.ROOT)
        );
    }

    private byte[] patchPaperweightAdapter(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for1:
        for (MethodNode methodNode : node.methods) {
            if (methodNode.name.equals("getProperties") && methodNode.desc.equals("(Lcom/sk89q/worldedit/world/block/BlockType;)Ljava/util/Map;")) {
                ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode insnNode = iterator.next();
                    if (insnNode instanceof InsnNode) {
                        if (insnNode.getOpcode() == Opcodes.ATHROW) {
                            InsnList insnList = new InsnList();
                            insnList.add(new InsnNode(Opcodes.POP));
                            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/util/Collections", "emptyMap", "()Ljava/util/Map;"));
                            insnList.add(new InsnNode(Opcodes.ARETURN));
                            methodNode.instructions.insertBefore(insnNode, insnList);
                            methodNode.instructions.remove(insnNode);
                            break for1;
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
