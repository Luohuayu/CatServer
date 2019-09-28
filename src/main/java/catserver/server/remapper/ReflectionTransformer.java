package catserver.server.remapper;

import java.util.*;

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
    private static final String DESC_ReflectionMethods = Type.getInternalName(ReflectionMethods.class);
    private static final String DESC_RemapMethodHandle = Type.getInternalName(CatHandleLookup.class);
    private static final String DESC_CatURLClassLoader = Type.getInternalName(CatURLClassLoader.class);
    private static final String DESC_CatClassLoader = Type.getInternalName(CatClassLoader.class);

    private static Map<String, String> remapStaticMethod = Maps.newHashMap();
    private static Map<String, String> remapVirtualMethod = Maps.newHashMap();
    private static Map<String, String> remapVirtualMethodToStatic = Maps.newHashMap();
    private static Map<String, String> remapSuperClass = Maps.newHashMap();

    public static JarMapping jarMapping;
    public static CatServerRemapper remapper;

    public static final HashMap<String, String> classDeMapping = Maps.newHashMap();
    public static final Multimap<String, String> methodDeMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> fieldDeMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> methodFastMapping = ArrayListMultimap.create();

    static {
        remapStaticMethod.put("java/lang/Class;forName", DESC_ReflectionMethods);
        remapStaticMethod.put("java/lang/invoke/MethodType;fromMethodDescriptorString", DESC_RemapMethodHandle);

        remapVirtualMethodToStatic.put("java/lang/Class;getField", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredField", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/Class;getMethod", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredMethod", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/Class;getSimpleName", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredMethods", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/reflect/Field;getName", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/reflect/Method;getName", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/ClassLoader;loadClass", DESC_ReflectionMethods);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findVirtual", DESC_RemapMethodHandle);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findStatic", DESC_RemapMethodHandle);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findSpecial", DESC_RemapMethodHandle);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;unreflect", DESC_RemapMethodHandle);

        remapSuperClass.put("java/net/URLClassLoader", DESC_CatURLClassLoader);
        remapSuperClass.put("java/lang/ClassLoader", DESC_CatClassLoader);
    }

    public static void init() {
        jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add(new ClassInheritanceProvider());
        jarMapping.setFallbackInheritanceProvider(provider);
        remapper = new CatServerRemapper(jarMapping);

        jarMapping.classes.forEach((k, v) -> classDeMapping.put(v, k));
        jarMapping.methods.forEach((k, v) -> methodDeMapping.put(v, k));
        jarMapping.fields.forEach((k, v) -> fieldDeMapping.put(v, k));
        jarMapping.methods.forEach((k, v) -> methodFastMapping.put(k.split("\\s+")[0], k));

        try {
            Class.forName("catserver.server.remapper.CatHandleLookup");
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
        String remappedSuperClass = remapSuperClass.get(node.superName);
        if (remappedSuperClass != null) {
            if (remappedSuperClass.equals(DESC_CatClassLoader)) remapVirtualMethod.put(node.name + ";defineClass", DESC_CatClassLoader);
            node.superName = remappedSuperClass;
            remapCL = true;
        }

        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();

                if (next instanceof TypeInsnNode && next.getOpcode() == Opcodes.NEW) { // remap new URLClassLoader
                    TypeInsnNode insn = (TypeInsnNode) next;
                    String remappedClass = remapSuperClass.get(insn.desc);
                    if (remappedClass != null) {
                        insn.desc = remappedClass;
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
                            break;
                    }

                    if (insn.owner.equals("javax/script/ScriptEngineManager") && insn.desc.equals("()V") && insn.name.equals("<init>")) {
                        insn.desc = "(Ljava/lang/ClassLoader;)V";
                        method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;"));
                        method.maxStack++;
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0/* ClassWriter.COMPUTE_FRAMES */);
        node.accept(writer); // Convert back into bytes
        return writer.toByteArray();
    }

    public static void remapStatic(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        String owner = remapStaticMethod.get((method.owner + ";" + method.name));
        if (owner != null) {
            method.owner = owner;
        }
    }

    public static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        String owner = remapVirtualMethodToStatic.get((method.owner + ";" + method.name));
        if (owner != null) {
            Type returnType = Type.getReturnType(method.desc);
            ArrayList<Type> args = new ArrayList<>();
            args.add(Type.getObjectType(method.owner));
            args.addAll(Arrays.asList(Type.getArgumentTypes(method.desc)));

            method.setOpcode(Opcodes.INVOKESTATIC);
            method.owner = owner;
            method.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
        } else {
            owner = remapVirtualMethod.get((method.owner + ";" + method.name));
            if (owner != null) {
                method.name += "Remap";
                method.owner = owner;
            }
        }
    }

    private static void remapSuperClass(MethodInsnNode method) {
        String remappedClass = remapSuperClass.get(method.owner);
        if (remappedClass != null && method.name.equals("<init>")) {
            method.owner = remappedClass;
        }
    }
}
