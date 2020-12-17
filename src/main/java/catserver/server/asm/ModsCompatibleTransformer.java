package catserver.server.asm;

import catserver.server.utils.ModFixUtils;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class ModsCompatibleTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if ("ichttt.mods.firstaid.common.DataManagerWrapper".equals(transformedName)) {
            return patchFirstAidDataManagerWrapper(basicClass);
        }

        return basicClass;
    }

    private byte[] patchFirstAidDataManagerWrapper(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        f1:
        for (MethodNode methodNode : node.methods) {
            if ("set_impl".equals(methodNode.name) && "(Lnet/minecraft/network/datasync/DataParameter;Ljava/lang/Object;)V".equals(methodNode.desc)) {
                // bypass patched class
                ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode insnNode = iterator.next();
                    if (insnNode instanceof MethodInsnNode) {
                        if ("ichttt/mods/firstaid/common/DataManagerWrapperHook".equals(((MethodInsnNode) insnNode).owner)) break f1;
                    }
                }

                // patch hook
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "ichttt/mods/firstaid/common/DataManagerWrapper", "player", "Lnet/minecraft/entity/player/EntityPlayer;"));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ModFixUtils.class), "hookFirstAidHealthUpdate", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/network/datasync/DataParameter;Ljava/lang/Object;)V", false));
                methodNode.instructions.insert(insnList);
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
