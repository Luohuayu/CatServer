package luohuayu.CatServer.remapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.repo.RuntimeRepo;

public class Transformer {
    public static JarMapping jarMapping;
    public static JarRemapper remapper;

    public static void init(JarMapping mapping, JarRemapper remapper) {
        if (Transformer.jarMapping == null) Transformer.jarMapping = mapping;
        if (Transformer.remapper == null) Transformer.remapper = remapper;
    }

    /**
     * Remap code using the provided jarMapping and
     * convert code from using Class.X methods to our remapped versions
     */
    public static byte[] transformSS(JarRemapper remapper, byte[] code) {
        return remapper.remapClassFile(
                transform(code),
                RuntimeRepo.getInstance());

    }

    /**
     * Convert code from using Class.X methods to our remapped versions
     */
    public static byte[] transform(byte[] code) {
        ClassReader reader = new ClassReader(code); // Turn from bytes into visitor
        ClassNode node = new ClassNode();
        reader.accept(node, 0); // Visit using ClassNode

        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode insn = insnIterator.next();
                switch (insn.getOpcode()) {
                    case Opcodes.INVOKEVIRTUAL:
                        remapVirtual(insn);
                        break;
                    case Opcodes.INVOKESTATIC:
                        remapForName(insn);
                        break;
                }
            }
        }

        ClassWriter writer = new ClassWriter(0/*ClassWriter.COMPUTE_FRAMES*/);
        node.accept(writer); // Convert back into bytes
        return writer.toByteArray();
    }

    public static void remapForName(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        if (!method.owner.equals("java/lang/Class") || !method.name.equals("forName")) return;
        method.owner = "luohuayu/CatServer/remapper/RemappedMethods";
    }

    public static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;

        if (!method.owner.equals("java/lang/Class") ||
                !(method.name.equals("getField") || method.name.equals("getDeclaredField") ||
                method.name.equals("getMethod") || method.name.equals("getDeclaredMethod")))
            return;

        Type returnType = Type.getReturnType(method.desc);

        ArrayList<Type> args = new ArrayList<Type>();
        args.add(Type.getObjectType(method.owner));
        args.addAll(Arrays.asList(Type.getArgumentTypes(method.desc)));

        method.setOpcode(Opcodes.INVOKESTATIC);
        method.owner = "luohuayu/CatServer/remapper/RemappedMethods";
        method.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[args.size()]));
    }
}
