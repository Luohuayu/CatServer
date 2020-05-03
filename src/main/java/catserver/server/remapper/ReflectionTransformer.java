package catserver.server.remapper;

import java.util.*;

import catserver.server.remapper.target.CatClassLoader;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.JointProvider;
import org.objectweb.asm.tree.TypeInsnNode;

public class ReflectionTransformer {

    public static JarMapping jarMapping;
    public static CatServerRemapper remapper;

    public static final HashMap<String, String> classReverseMapping = Maps.newHashMap();
    public static final Multimap<String, String> methodReverseMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> fieldReverseMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> methodFastMapping = ArrayListMultimap.create();

    public static void init() {
        jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        jarMapping.setFallbackInheritanceProvider(provider);
        remapper = new CatServerRemapper(jarMapping);

        jarMapping.classes.forEach((k, v) -> classReverseMapping.put(v, k));
        jarMapping.methods.forEach((k, v) -> methodReverseMapping.put(v, k));
        jarMapping.fields.forEach((k, v) -> fieldReverseMapping.put(v, k));
        jarMapping.methods.forEach((k, v) -> methodFastMapping.put(k.split("\\s+")[0], k));

        try {
            Class.forName("catserver.server.remapper.target.MethodHandleMethods");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert code from using Class.X methods to our remapped versions
     */
    public static byte[] transform(byte[] code) {
        ClassReader reader = new ClassReader(code); // Turn from bytes into visitor
        ClassNode node = new ClassNode();
        reader.accept(node, 0); // Visit using ClassNode

        boolean remapCL = false;
        Class<?> remappedSuperClass = RemapRules.getSuperClassTarget(node.superName);
        if (remappedSuperClass != null) {
            if (remappedSuperClass == CatClassLoader.class) RemapRules.addVirtualMethodTarget(node.name + ";defineClass", CatClassLoader.class);
            node.superName = Type.getInternalName(remappedSuperClass);
            remapCL = true;
        }

        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();

                if (next instanceof TypeInsnNode && next.getOpcode() == Opcodes.NEW) { // remap new URLClassLoader
                    TypeInsnNode insn = (TypeInsnNode) next;
                    Class<?> remappedClass = RemapRules.getSuperClassTarget(insn.desc);
                    if (remappedClass != null) {
                        insn.desc = Type.getInternalName(remappedClass);
                        remapCL = true;
                    }
                }

                if (next instanceof MethodInsnNode) {
                    MethodInsnNode insn = (MethodInsnNode) next;
                    switch (insn.getOpcode()) {
                        case Opcodes.INVOKEVIRTUAL:
                            remapVirtual(insn);
                            break;
                        case Opcodes.INVOKESTATIC:
                            remapStatic(insn);
                            break;
                        case Opcodes.INVOKESPECIAL:
                            if (remapCL) remapSuperClass(insn);
                            remapScriptEngineManager(insn, method);
                            break;
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0/* ClassWriter.COMPUTE_FRAMES */);
        node.accept(writer); // Convert back into bytes
        return writer.toByteArray();
    }

    public static void remapStatic(MethodInsnNode insn) {
        Class<?> remappedClass = RemapRules.getStaticMethodTarget((insn.owner + ";" + insn.name));
        if (remappedClass != null) {
            insn.owner = Type.getInternalName(remappedClass);
        }
    }

    public static void remapVirtual(MethodInsnNode insn) {
        Class<?> remappedClass = RemapRules.getVirtualMethodToStaticTarget((insn.owner + ";" + insn.name));
        if (remappedClass != null) {
            Type returnType = Type.getReturnType(insn.desc);
            ArrayList<Type> args = new ArrayList<>();
            args.add(Type.getObjectType(insn.owner));
            args.addAll(Arrays.asList(Type.getArgumentTypes(insn.desc)));

            insn.setOpcode(Opcodes.INVOKESTATIC);
            insn.owner = Type.getInternalName(remappedClass);
            insn.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
        } else {
            remappedClass = RemapRules.getVirtualMethodTarget((insn.owner + ";" + insn.name));
            if (remappedClass != null) {
                insn.name += "Remap";
                insn.owner = Type.getInternalName(remappedClass);
            }
        }
    }

    private static void remapSuperClass(MethodInsnNode insn) {
        Class<?> remappedClass = RemapRules.getSuperClassTarget(insn.owner);
        if (remappedClass != null && insn.name.equals("<init>")) {
            insn.owner = Type.getInternalName(remappedClass);
        }
    }

    private static void remapScriptEngineManager(MethodInsnNode insn, MethodNode node) {
        if (insn.owner.equals("javax/script/ScriptEngineManager") && insn.desc.equals("()V") && insn.name.equals("<init>")) {
            insn.desc = "(Ljava/lang/ClassLoader;)V";
            node.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;"));
            node.maxStack++;
        }
    }
}
