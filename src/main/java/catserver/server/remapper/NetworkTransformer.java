package catserver.server.remapper;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class NetworkTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return basicClass;
        if (transformedName.equals("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher$1"))
            basicClass = transformClass(basicClass);
        return basicClass;

    }

    private byte[] transformClass(byte[] basicClass) {
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
}
