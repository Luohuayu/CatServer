package catserver.server.asm.proxy;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Optional;

public class LibrarianLibTransformerProxy implements IClassTransformer {
    private final IClassTransformer originTransformer;

    public LibrarianLibTransformerProxy(IClassTransformer originTransformer) {
        this.originTransformer = originTransformer;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;
        byte[] patchedClass = originTransformer.transform(name, transformedName, basicClass);
        if ("net.minecraft.network.NetHandlerPlayServer".equals(transformedName)) {
            return patchNetHandlerPlayServer(basicClass, patchedClass);
        }
        return patchedClass;
    }

    public byte[] patchNetHandlerPlayServer(byte[] originClass, byte[] patchedClass) {
        ClassReader patchedReader = new ClassReader(patchedClass);
        ClassNode patchedNode = new ClassNode();
        patchedReader.accept(patchedNode, 0);

        ClassReader originReader = new ClassReader(originClass);
        ClassNode originNode = new ClassNode();
        originReader.accept(originNode, 0);

        Optional<MethodNode> optional_func_73660_a = patchedNode.methods.stream().filter(methodNode -> "func_73660_a".equals(methodNode.name) && "()V".equals(methodNode.desc)).findFirst();
        if (optional_func_73660_a.isPresent()) {
            originNode.methods.removeIf(methodNode -> "func_73660_a".equals(methodNode.name) && "()V".equals(methodNode.desc));
            originNode.methods.add(optional_func_73660_a.get());
        }

        ClassWriter writer = new ClassWriter(0);
        originNode.accept(writer);
        return writer.toByteArray();
    }
}
