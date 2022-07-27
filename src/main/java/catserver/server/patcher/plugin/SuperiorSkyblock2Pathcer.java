package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.chunk.Chunk;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class SuperiorSkyblock2Pathcer implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("com.bgsoftware.superiorskyblock.nms.v1_12_R1.NMSChunksImpl")) {
            return patchNMSChunksImpl(basicClass);
        } else if (className.equals("com.bgsoftware.superiorskyblock.island.SIsland")) {
            return patchSIsland(basicClass);
        }
        return basicClass;
    }

    private byte[] patchNMSChunksImpl(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("removeEntities") && method.desc.equals("(Lnet/minecraft/world/chunk/Chunk;)V")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(SuperiorSkyblock2Pathcer.class), "removeEntities", "(Lnet/minecraft/world/chunk/Chunk;)V", false));
                insnList.add(new InsnNode(Opcodes.RETURN));
                method.instructions = insnList;
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchSIsland(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("updateOldUpgradeValues") && method.desc.equals("()V")) {
                InsnList insnList = new InsnList();
                insnList.add(new InsnNode(Opcodes.RETURN));
                method.instructions = insnList;
                method.localVariables.clear();
                method.tryCatchBlocks.clear();
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static void removeEntities(Chunk chunk) {
        for (int i = 0; i < chunk.entityLists.length; i++) {
            for (Entity entity : chunk.entityLists[i]) {
                if (!(entity instanceof EntityPlayer)) {
                    entity.isDead = true;
                }
            }
            chunk.entityLists[i] = new ClassInheritanceMultiMap(Entity.class);
        }
    }
}
