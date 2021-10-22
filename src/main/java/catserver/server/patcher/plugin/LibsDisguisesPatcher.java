package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import catserver.server.remapper.ReflectionUtils;
import catserver.server.remapper.RemapRules;
import catserver.server.remapper.target.ReflectionMethods;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.jar.Manifest;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class LibsDisguisesPatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("me.libraryaddict.disguise.utilities.reflection.ReflectionManager")) {
            return patchReflectionManager(basicClass);
        }
        return basicClass;
    }

    private byte[] patchReflectionManager(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("getNmsClass") && method.desc.equals("(Ljava/lang/String;)Ljava/lang/Class;")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(LibsDisguisesPatcher.class), "getNmsClassHook", "(Ljava/lang/String;)Ljava/lang/Class;", false));
                insnList.add(new InsnNode(Opcodes.ARETURN));
                method.instructions = insnList;
                method.tryCatchBlocks.clear();
            }

            if (method.name.equals("getCraftClass") && method.desc.equals("(Ljava/lang/String;)Ljava/lang/Class;")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(LibsDisguisesPatcher.class), "getCraftClassHook", "(Ljava/lang/String;)Ljava/lang/Class;", false));
                insnList.add(new InsnNode(Opcodes.ARETURN));
                method.instructions = insnList;
                method.tryCatchBlocks.clear();
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static Class<?> getNmsClassHook(String className) {
        try {
            return ReflectionMethods.forName(RemapRules.getNMSPackage() + "." + className, true, ReflectionUtils.getCallerClassloader());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraftClassHook(String className) {
        try {
            Map<Package, Manifest> packageManifests = ReflectionHelper.getPrivateValue(LaunchClassLoader.class, (LaunchClassLoader) LibsDisguisesPatcher.class.getClassLoader(), "packageManifests");
            for (Package definedPackage : packageManifests.keySet()) {
                if (definedPackage.getName().startsWith("org.bukkit.craftbukkit.v1_12_R1")) {
                    try {
                        return Class.forName(definedPackage.getName() + "." + className);
                    } catch (Exception ignored) {}
                }
            }
            throw new ClassNotFoundException(className);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
