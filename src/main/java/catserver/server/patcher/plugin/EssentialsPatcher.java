package catserver.server.patcher.plugin;

import catserver.server.patcher.IPatcher;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class EssentialsPatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("com.earth2me.essentials.commands.Commandhat")) { // for 2.18.0.0/2.18.1.0
            return patchCommandhat(basicClass);
        } else if (className.equals("com.earth2me.essentials.perm.PermissionsDefaults")) { // for 2.18.2.0+
            return patchPermissionsDefaults(basicClass);
        }
        return basicClass;
    }

    private byte[] patchCommandhat(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("registerPermissionsIfNecessary") && method.desc.equals("(Lorg/bukkit/plugin/PluginManager;)V")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EssentialsPatcher.class), "registerPermissionsIfNecessaryHook", "(Lorg/bukkit/plugin/PluginManager;)V", false));
                insnList.add(new InsnNode(Opcodes.RETURN));
                method.instructions = insnList;
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchPermissionsDefaults(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("registerAllHatDefaults") && method.desc.equals("()V")) {
                InsnList insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EssentialsPatcher.class), "registerAllHatDefaultsHook", "()V", false));
                insnList.add(new InsnNode(Opcodes.RETURN));
                method.instructions = insnList;
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static void registerPermissionsIfNecessaryHook(final PluginManager toRegister) {
        final Permission hatPerm = toRegister.getPermission("essentials.hat.prevent-type.*");
        if (hatPerm != null) {
            return;
        }
        final ImmutableMap.Builder<String, Boolean> children = ImmutableMap.builder();
        for (final Material mat : Material.values()) {
            if (mat.getMaterialType() == Material.MaterialType.VANILLA || mat.getMaterialType() == Material.MaterialType.MOD_ITEM) {
                final String matPerm = "essentials.hat.prevent-type." + mat.name().toLowerCase();
                if (toRegister.getPermission(matPerm) == null) {
                    children.put(matPerm, true);
                    toRegister.addPermission(new Permission(matPerm, "Prevent using " + mat + " as a type of hat.", PermissionDefault.FALSE));
                }
            }
        }
        toRegister.addPermission(new Permission("essentials.hat.prevent-type.*", "Prevent all types of hats", PermissionDefault.FALSE, children.build()));
    }

    public static void registerAllHatDefaultsHook() {
        final PluginManager pluginManager = org.bukkit.Bukkit.getPluginManager();
        final Permission hatPerm = pluginManager.getPermission("essentials.hat.prevent-type.*");
        if (hatPerm != null) {
            return;
        }
        final ImmutableMap.Builder<String, Boolean> children = ImmutableMap.builder();
        for (final Material mat : Material.values()) {
            if (mat.getMaterialType() == Material.MaterialType.VANILLA || mat.getMaterialType() == Material.MaterialType.MOD_ITEM) {
                final String matPerm = "essentials.hat.prevent-type." + mat.name().toLowerCase();
                if (pluginManager.getPermission(matPerm) == null) {
                    children.put(matPerm, true);
                    pluginManager.addPermission(new Permission(matPerm, "Prevent using " + mat + " as a type of hat.", PermissionDefault.FALSE));
                }
            }
        }
        pluginManager.addPermission(new Permission("essentials.hat.prevent-type.*", "Prevent all types of hats", PermissionDefault.FALSE, children.build()));
    }
}
