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
    protected static Map<String, Class> remapStaticMethod = Maps.newHashMap();
    protected static Map<String, Class> remapVirtualMethod = Maps.newHashMap();
    protected static Map<String, Class> remapVirtualMethodToStatic = Maps.newHashMap();
    protected static Map<String, Class> remapSuperClass = Maps.newHashMap();

    public static JarMapping jarMapping;
    public static CatServerRemapper remapper;

    public static final HashMap<String, String> classDeMapping = Maps.newHashMap();
    public static final Multimap<String, String> methodDeMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> fieldDeMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> methodFastMapping = ArrayListMultimap.create();

    static {
        remapStaticMethod.put("java/lang/Class;forName", ReflectionMethods.class);
        remapStaticMethod.put("java/lang/invoke/MethodType;fromMethodDescriptorString", CatHandleLookup.class);

        remapVirtualMethodToStatic.put("java/lang/Class;getField", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredField", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getMethod", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredMethod", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getSimpleName", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/Class;getDeclaredMethods", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/reflect/Field;getName", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/reflect/Method;getName", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/ClassLoader;loadClass", ReflectionMethods.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findVirtual", CatHandleLookup.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findStatic", CatHandleLookup.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;findSpecial", CatHandleLookup.class);
        remapVirtualMethodToStatic.put("java/lang/invoke/MethodHandles$Lookup;unreflect", CatHandleLookup.class);

        remapSuperClass.put("java/net/URLClassLoader", CatURLClassLoader.class);
        remapSuperClass.put("java/lang/ClassLoader", CatClassLoader.class);
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
        Class<?> remappedSuperClass = remapSuperClass.get(node.superName);
        if (remappedSuperClass != null) {
            if (remappedSuperClass == CatClassLoader.class) remapVirtualMethod.put(node.name + ";defineClass", CatClassLoader.class);
            node.superName = Type.getInternalName(remappedSuperClass);
            remapCL = true;
        }

        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();

                if (next instanceof TypeInsnNode && next.getOpcode() == Opcodes.NEW) { // remap new URLClassLoader
                    TypeInsnNode insn = (TypeInsnNode) next;
                    Class<?> remappedClass = remapSuperClass.get(insn.desc);
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
        Class<?> remappedClass = remapStaticMethod.get((method.owner + ";" + method.name));
        if (remappedClass != null) {
            method.owner = Type.getInternalName(remappedClass);
        }
    }

    public static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        Class<?> remappedClass = remapVirtualMethodToStatic.get((method.owner + ";" + method.name));
        if (remappedClass != null) {
            Type returnType = Type.getReturnType(method.desc);
            ArrayList<Type> args = new ArrayList<>();
            args.add(Type.getObjectType(method.owner));
            args.addAll(Arrays.asList(Type.getArgumentTypes(method.desc)));

            method.setOpcode(Opcodes.INVOKESTATIC);
            method.owner = Type.getInternalName(remappedClass);
            method.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
        } else {
            remappedClass = remapVirtualMethod.get((method.owner + ";" + method.name));
            if (remappedClass != null) {
                method.name += "Remap";
                method.owner = Type.getInternalName(remappedClass);
            }
        }
    }

    private static void remapSuperClass(MethodInsnNode method) {
        Class<?> remappedClass = remapSuperClass.get(method.owner);
        if (remappedClass != null && method.name.equals("<init>")) {
            method.owner = Type.getInternalName(remappedClass);
        }
    }
}
