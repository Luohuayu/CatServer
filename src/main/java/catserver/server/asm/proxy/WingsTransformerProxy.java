package catserver.server.asm.proxy;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Optional;

public class WingsTransformerProxy implements IClassTransformer {
    private final IClassTransformer originTransformer;

    public WingsTransformerProxy(IClassTransformer originTransformer) {
        this.originTransformer = originTransformer;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;
        byte[] patchedClass = originTransformer.transform(name, transformedName, basicClass);
        if ("net.minecraft.entity.player.EntityPlayer".equals(transformedName)) {
            return patchEntityPlayer(basicClass, patchedClass);
        }
        return patchedClass;
    }

    public byte[] patchEntityPlayer(byte[] originClass, byte[] patchedClass) {
        ClassReader patchedReader = new ClassReader(patchedClass);
        ClassNode patchedNode = new ClassNode();
        patchedReader.accept(patchedNode, 0);

        ClassReader originReader = new ClassReader(originClass);
        ClassNode originNode = new ClassNode();
        originReader.accept(originNode, 0);

        Optional<MethodNode> optional_func_184808_cD = patchedNode.methods.stream().filter(methodNode -> "func_184808_cD".equals(methodNode.name) && "()V".equals(methodNode.desc)).findFirst();
        if (optional_func_184808_cD.isPresent()) {
            originNode.methods.removeIf(methodNode -> "func_184808_cD".equals(methodNode.name) && "()V".equals(methodNode.desc));
            originNode.methods.add(optional_func_184808_cD.get());
        }

        Optional<MethodNode> optional_func_71000_j = patchedNode.methods.stream().filter(methodNode -> "func_71000_j".equals(methodNode.name) && "(DDD)V".equals(methodNode.desc)).findFirst();
        if (optional_func_71000_j.isPresent()) {
            originNode.methods.removeIf(methodNode -> "func_71000_j".equals(methodNode.name) && "(DDD)V".equals(methodNode.desc));
            originNode.methods.add(optional_func_71000_j.get());
        }

        Optional<MethodNode> optional_func_174820_d = patchedNode.methods.stream().filter(methodNode -> "func_174820_d".equals(methodNode.name) && "(ILnet/minecraft/item/ItemStack;)Z".equals(methodNode.desc)).findFirst();
        if (optional_func_174820_d.isPresent()) {
            originNode.methods.removeIf(methodNode -> "func_174820_d".equals(methodNode.name) && "(ILnet/minecraft/item/ItemStack;)Z".equals(methodNode.desc));
            originNode.methods.add(optional_func_174820_d.get());
        }

        ClassWriter writer = new ClassWriter(0);
        originNode.accept(writer);
        return writer.toByteArray();
    }
}
