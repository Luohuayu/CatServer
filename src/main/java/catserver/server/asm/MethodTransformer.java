package catserver.server.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.MethodNode;

public class MethodTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if ("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher$1".equals(transformedName)) {
            return patchNetworkDispatcher(basicClass);
        } else if ("net.minecraft.entity.Entity".equals(transformedName)) {
            return patchEntity(basicClass);
        } else if ("net.minecraft.entity.player.EntityPlayer".equals(transformedName)) {
            return patchEntityPlayer(basicClass);
        } else if ("com.typesafe.config.impl.SimpleConfigOrigin".equals(transformedName)) {
            return patchSimpleConfigOrigin(basicClass);
        } else if ("com.google.gson.internal.bind.TypeAdapters$EnumTypeAdapter".equals(transformedName)) {
            return patchTypeAdapters(basicClass);
        }

        return basicClass;
    }

    private byte[] patchNetworkDispatcher(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        /*
         * public void sendPacket(Packet<?> packetIn) {
         *      super.func_147359_a(packetIn);
         * }
         */

        MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "sendPacket", "(Lnet/minecraft/network/Packet;)V", "(Lnet/minecraft/network/Packet<*>;)V", null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/network/NetHandlerPlayServer", "func_147359_a", "(Lnet/minecraft/network/Packet;)V", true);
        mv.visitInsn(RETURN);
        mv.visitEnd();

        classNode.access = ACC_SUPER + ACC_PUBLIC;

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private byte[] patchEntity(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        /*
         * public UUID getUniqueID() {
         *      this.func_110124_au();
         * }
         */

        MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "getUniqueID", "()Ljava/util/UUID;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/Entity", "func_110124_au", "()Ljava/util/UUID;", false);
        mv.visitInsn(ARETURN);
        mv.visitEnd();

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private byte[] patchEntityPlayer(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        /*
         * public GameProfile getProfile() {
         *      this.func_146103_bH();
         * }
         */

        MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "getProfile", "()Lcom/mojang/authlib/GameProfile;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", "func_146103_bH", "()Lcom/mojang/authlib/GameProfile;", false);
        mv.visitInsn(ARETURN);
        mv.visitEnd();

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private byte[] patchSimpleConfigOrigin(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        MethodVisitor mv1 = classWriter.visitMethod(ACC_PUBLIC, "setComments", "(Ljava/util/List;)Lcom/typesafe/config/impl/SimpleConfigOrigin;", "(Ljava/util/List<Ljava/lang/String;>;)Lcom/typesafe/config/impl/SimpleConfigOrigin;", null);
        mv1.visitCode();
        mv1.visitVarInsn(ALOAD, 0);
        mv1.visitVarInsn(ALOAD, 1);
        mv1.visitMethodInsn(INVOKEVIRTUAL, "com/typesafe/config/impl/SimpleConfigOrigin", "withComments", "(Ljava/util/List;)Lcom/typesafe/config/impl/SimpleConfigOrigin;", false);
        mv1.visitInsn(ARETURN);
        mv1.visitEnd();

        MethodVisitor mv2 = classWriter.visitMethod(ACC_PUBLIC, "setLineNumber", "(I)Lcom/typesafe/config/impl/SimpleConfigOrigin;", null, null);
        mv2.visitCode();
        mv2.visitVarInsn(ALOAD, 0);
        mv2.visitVarInsn(ILOAD, 1);
        mv2.visitMethodInsn(INVOKEVIRTUAL, "com/typesafe/config/impl/SimpleConfigOrigin", "withLineNumber", "(I)Lcom/typesafe/config/impl/SimpleConfigOrigin;", false);
        mv2.visitInsn(ARETURN);
        mv2.visitEnd();

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private byte[] patchTypeAdapters(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        for (MethodNode methodNode : classNode.methods) {
            if ("<init>".equals(methodNode.name) && "(Ljava/lang/Class;)V".equals(methodNode.desc)) {
                methodNode.instructions.clear();
                methodNode.tryCatchBlocks.clear();
                methodNode.localVariables.clear();

                methodNode.visitCode();

                methodNode.visitVarInsn(ALOAD, 0);
                methodNode.visitMethodInsn(INVOKESPECIAL, "com/google/gson/TypeAdapter", "<init>", "()V", false);

                methodNode.visitVarInsn(ALOAD, 0);
                methodNode.visitTypeInsn(NEW, "java/util/HashMap");
                methodNode.visitInsn(DUP);
                methodNode.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
                methodNode.visitFieldInsn(PUTFIELD, "com/google/gson/internal/bind/TypeAdapters$EnumTypeAdapter", "nameToConstant", "Ljava/util/Map;");

                methodNode.visitVarInsn(ALOAD, 0);
                methodNode.visitTypeInsn(NEW, "java/util/HashMap");
                methodNode.visitInsn(DUP);
                methodNode.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
                methodNode.visitFieldInsn(PUTFIELD, "com/google/gson/internal/bind/TypeAdapters$EnumTypeAdapter", "constantToName", "Ljava/util/Map;");

                methodNode.visitVarInsn(ALOAD, 1);
                methodNode.visitVarInsn(ALOAD, 0);
                methodNode.visitFieldInsn(GETFIELD, "com/google/gson/internal/bind/TypeAdapters$EnumTypeAdapter", "nameToConstant", "Ljava/util/Map;");
                methodNode.visitVarInsn(ALOAD, 0);
                methodNode.visitFieldInsn(GETFIELD, "com/google/gson/internal/bind/TypeAdapters$EnumTypeAdapter", "constantToName", "Ljava/util/Map;");
                methodNode.visitMethodInsn(INVOKESTATIC, "catserver/server/asm/MethodHook", "TypeAdapters$EnumTypeAdapter_Init", "(Ljava/lang/Class;Ljava/util/Map;Ljava/util/Map;)V", false);

                methodNode.visitInsn(RETURN);

                methodNode.visitEnd();

                break;
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
