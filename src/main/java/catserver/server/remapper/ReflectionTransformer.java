package catserver.server.remapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;

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
    public static final String DESC_ReflectionMethods = Type.getInternalName(ReflectionMethods.class);
    public static final String DESC_RemapMethodHandle = Type.getInternalName(CatHandleLookup.class);
    public static JarMapping jarMapping;
    public static CatServerRemapper remapper;

    public static final HashMap<String, String> classDeMapping = Maps.newHashMap();
    public static final Multimap<String, String> methodDeMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> fieldDeMapping = ArrayListMultimap.create();
    public static final Multimap<String, String> methodFastMapping = ArrayListMultimap.create();

    private static boolean disable = false;

    public static void init() {
        try {
            ReflectionUtils.getCallerClassloader();
        } catch (Throwable e) {
            new RuntimeException("Unsupported Java version, disabled reflection remap!", e).printStackTrace();
            disable = true;
        }
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
        if (node.superName.equals("java/net/URLClassLoader")) {
            node.superName = "catserver/server/remapper/CatURLClassLoader";
            remapCL = true;
        }

        for (MethodNode method : node.methods) { // Taken from SpecialSource
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
            while (insnIterator.hasNext()) {
                AbstractInsnNode next = insnIterator.next();

                if (next instanceof TypeInsnNode) {
                    TypeInsnNode insn = (TypeInsnNode) next;
                    if (insn.getOpcode() == Opcodes.NEW && insn.desc.equals("java/net/URLClassLoader")) { // remap new URLClassLoader
                        insn.desc = "catserver/server/remapper/CatURLClassLoader";
                        remapCL = true;
                    }
                }

                if (!(next instanceof MethodInsnNode)) continue;
                MethodInsnNode insn = (MethodInsnNode) next;
                switch (insn.getOpcode()) {
                    case Opcodes.INVOKEVIRTUAL:
                        remapVirtual(insn);
                        break;
                    case Opcodes.INVOKESTATIC:
                        remapForName(insn);
                        break;
                    case Opcodes.INVOKESPECIAL:
                        if (remapCL) remapURLClassLoader(insn);
                        break;
                }

                if(insn.owner.equals("javax/script/ScriptEngineManager") && insn.desc.equals("()V") && insn.name.equals("<init>")){
                    insn.desc="(Ljava/lang/ClassLoader;)V";
                    method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;"));
                    method.maxStack++;
                }
            }
        }

        ClassWriter writer = new ClassWriter(0/* ClassWriter.COMPUTE_FRAMES */);
        node.accept(writer); // Convert back into bytes
        return writer.toByteArray();
    }

    public static void remapForName(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        if (method.owner.equals("java/lang/invoke/MethodType") && method.name.equals("fromMethodDescriptorString")) {
            method.owner = DESC_RemapMethodHandle;
        }

        if (disable || !method.owner.equals("java/lang/Class") || !method.name.equals("forName")) return;
        method.owner = DESC_ReflectionMethods;
    }

    public static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
        boolean remapFlag = false;
        if (method.owner.equals("java/lang/Class")) {
            switch (method.name) {
                case "getField":
                case "getDeclaredField":
                case "getMethod":
                case "getDeclaredMethod":
                case "getSimpleName":
                    remapFlag = true;
            }
        } else if (method.name.equals("getName")) {
            switch (method.owner) {
                case "java/lang/reflect/Field":
                case "java/lang/reflect/Method":
                    remapFlag = true;
            }
        } else if (method.owner.equals("java/lang/ClassLoader") && method.name.equals("loadClass")) {
            remapFlag = true;
        } else if (method.owner.equals("java/lang/invoke/MethodHandles$Lookup")) {
            switch (method.name) {
                case "findVirtual":
                case "findStatic":
                case "findSpecial":
                case "unreflect":
                    virtualToStatic(method, DESC_RemapMethodHandle); // remapMethodLookup
            }
        }

        if (remapFlag) virtualToStatic(method, DESC_ReflectionMethods);
    }

    private static void remapURLClassLoader(MethodInsnNode method) {
        if (!(method.owner.equals("java/net/URLClassLoader") && method.name.equals("<init>"))) return;
        method.owner = "catserver/server/remapper/CatURLClassLoader";
    }

    private static void virtualToStatic(MethodInsnNode method, String desc) {
        Type returnType = Type.getReturnType(method.desc);
        ArrayList<Type> args = new ArrayList<>();
        args.add(Type.getObjectType(method.owner));
        args.addAll(Arrays.asList(Type.getArgumentTypes(method.desc)));

        method.setOpcode(Opcodes.INVOKESTATIC);
        method.owner = desc;
        method.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[0]));
    }
}
