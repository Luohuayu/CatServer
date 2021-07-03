package catserver.server.patcher.plugin;

import catserver.server.inventory.BukkitMaterialHelper;
import catserver.server.patcher.IPatcher;
import org.bukkit.Material;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class WorldEditPatcher implements IPatcher {
    @Override
    public byte[] transform(String className, byte[] basicClass) {
        if (className.equals("com.sk89q.worldedit.bukkit.BukkitServerInterface")) {
            return patchBukkitServerInterface(basicClass);
        } else if (className.equals("com.sk89q.worldedit.bukkit.BukkitWorld")) {
            return patchBukkitWorld(basicClass);
        } else if (className.equals("com.sk89q.worldedit.world.registry.NullItemRegistry")) {
            return patchNullItemRegistry(basicClass);
        }
        return basicClass;
    }

    private byte[] patchBukkitServerInterface(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("resolveItem") && method.desc.equals("(Ljava/lang/String;)I")) {
                ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
                while (insnIterator.hasNext()) {
                    AbstractInsnNode next = insnIterator.next();
                    if (next instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) next;
                        if (methodInsnNode.owner.equals("org/bukkit/Material") && methodInsnNode.name.equals("matchMaterial") && methodInsnNode.desc.equals("(Ljava/lang/String;)Lorg/bukkit/Material;")) {
                            methodInsnNode.owner = Type.getInternalName(WorldEditPatcher.class);
                            methodInsnNode.name = "matchMaterialHook";
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static Material matchMaterialHook(String name) {
        Material material = Material.getMaterial(name.toUpperCase(java.util.Locale.ENGLISH).replaceAll("\\s+", "_").replaceAll("\\W", ""));;
        return material != null ? BukkitMaterialHelper.convertModItemMaterialToBlock(material) : null;
    }

    private byte[] patchBukkitWorld(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("isValidBlockType") && method.desc.equals("(I)Z")) {
                ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator();
                while (insnIterator.hasNext()) {
                    AbstractInsnNode next = insnIterator.next();
                    if (next instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) next;
                        if (methodInsnNode.owner.equals("org/bukkit/Material") && methodInsnNode.name.equals("getMaterial") && methodInsnNode.desc.equals("(I)Lorg/bukkit/Material;")) {
                            methodInsnNode.name = "getBlockMaterial";
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchNullItemRegistry(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("createFromId") && method.desc.equals("(Ljava/lang/String;)Lcom/sk89q/worldedit/blocks/BaseItem;")) {
                InsnList insnList = new InsnList();
                LabelNode label1 = new LabelNode();
                LabelNode label2 = new LabelNode();
                LabelNode label3 = new LabelNode();

                insnList.add(label1);
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/item/Item", "field_150901_e", "Lnet/minecraft/util/registry/RegistryNamespaced;"));
                insnList.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/util/ResourceLocation"));
                insnList.add(new InsnNode(Opcodes.DUP));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/registry/RegistryNamespaced", "func_82594_a", "(Ljava/lang/Object;)Ljava/lang/Object;", false));
                insnList.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/item/Item"));
                insnList.add(new VarInsnNode(Opcodes.ASTORE, 2));

                insnList.add(label2);
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                insnList.add(new JumpInsnNode(Opcodes.IFNULL, label3));
                insnList.add(new TypeInsnNode(Opcodes.NEW, "com/sk89q/worldedit/blocks/BaseItem"));
                insnList.add(new InsnNode(Opcodes.DUP));
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/item/Item", "field_150901_e", "Lnet/minecraft/util/registry/RegistryNamespaced;"));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/registry/RegistryNamespaced", "func_148757_b", "(Ljava/lang/Object;)I", false));
                insnList.add(new InsnNode(Opcodes.ICONST_0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/sk89q/worldedit/blocks/BaseItem", "<init>", "(IS)V", false));
                insnList.add(new InsnNode(Opcodes.ARETURN));

                insnList.add(label3);
                insnList.add(new FrameNode(Opcodes.F_NEW, 3, new Object[]{ "com/sk89q/worldedit/world/registry/NullItemRegistry", "java/lang/String", "net/minecraft/item/Item" }, 0, new Object[]{}));
                insnList.add(new InsnNode(Opcodes.ACONST_NULL));
                insnList.add(new InsnNode(Opcodes.ARETURN));

                method.instructions = insnList;
                method.localVariables.clear();
                method.maxStack = 4;
                method.maxLocals = 3;
            }

            if (method.name.equals("createFromId") && method.desc.equals("(I)Lcom/sk89q/worldedit/blocks/BaseItem;")) {
                InsnList insnList = new InsnList();
                LabelNode label1 = new LabelNode();
                LabelNode label2 = new LabelNode();
                LabelNode label3 = new LabelNode();

                insnList.add(label1);
                insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/item/Item", "field_150901_e", "Lnet/minecraft/util/registry/RegistryNamespaced;"));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/registry/RegistryNamespaced", "func_148754_a", "(I)Ljava/lang/Object;", false));
                insnList.add(new JumpInsnNode(Opcodes.IFNULL, label3));

                insnList.add(label2);
                insnList.add(new TypeInsnNode(Opcodes.NEW, "com/sk89q/worldedit/blocks/BaseItem"));
                insnList.add(new InsnNode(Opcodes.DUP));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
                insnList.add(new InsnNode(Opcodes.ICONST_0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/sk89q/worldedit/blocks/BaseItem", "<init>", "(IS)V", false));
                insnList.add(new InsnNode(Opcodes.ARETURN));

                insnList.add(label3);
                insnList.add(new FrameNode(Opcodes.F_NEW, 2, new Object[]{ "com/sk89q/worldedit/world/registry/NullItemRegistry", 1 /* int */ }, 0, new Object[]{}));
                insnList.add(new InsnNode(Opcodes.ACONST_NULL));
                insnList.add(new InsnNode(Opcodes.ARETURN));

                method.instructions = insnList;
                method.localVariables.clear();
                method.maxStack = 4;
                method.maxLocals = 2;
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
