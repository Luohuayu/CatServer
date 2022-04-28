package catserver.server.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class CommandNodeTransformer implements ITransformer<ClassNode> {
    @Nonnull
    @Override
    public ClassNode transform(ClassNode input, ITransformerVotingContext context) {
        // hook canUse
        MethodNode canUseMethod = input.methods.stream().filter(methodNode -> Objects.equals(methodNode.name, "canUse") && Objects.equals(methodNode.desc, "(Ljava/lang/Object;)Z")).findFirst().orElseThrow(() -> new RuntimeException("Can't found canUse method!"));
        canUseMethod.localVariables.clear();
        canUseMethod.instructions.clear();
        canUseMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        canUseMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        canUseMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        canUseMethod.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "com/mojang/brigadier/tree/CommandNode", "requirement", "Ljava/util/function/Predicate;"));
        canUseMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "catserver/server/hook/CommandNodeHook", "canUse", "(Lcom/mojang/brigadier/tree/CommandNode;Ljava/lang/Object;Ljava/util/function/Predicate;)Z", false));
        canUseMethod.instructions.add(new InsnNode(Opcodes.IRETURN));
        canUseMethod.maxStack = 3;
        canUseMethod.maxLocals = 2;

        // hook addChild
        MethodNode addChildMethod = input.methods.stream().filter(methodNode -> Objects.equals(methodNode.name, "addChild") && Objects.equals(methodNode.desc, "(Lcom/mojang/brigadier/tree/CommandNode;)V")).findFirst().orElseThrow(() -> new RuntimeException("Can't found addChild method!"));
        InsnList dispatcherHook = new InsnList();
        dispatcherHook.add(new VarInsnNode(Opcodes.ALOAD, 0));
        dispatcherHook.add(new VarInsnNode(Opcodes.ALOAD, 1));
        dispatcherHook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "catserver/server/hook/CommandNodeHook", "dispatcher", "(Lcom/mojang/brigadier/tree/CommandNode;Lcom/mojang/brigadier/tree/CommandNode;)V", false));
        List<AbstractInsnNode> returnInsnNodes = Arrays.stream(addChildMethod.instructions.toArray()).filter(insnNode -> insnNode instanceof InsnNode && insnNode.getOpcode() == Opcodes.RETURN).collect(Collectors.toList());
        if (returnInsnNodes.size() == 0) {
            throw new RuntimeException("Can't found addChild return!");
        }
        AbstractInsnNode lastReturnInsnNode = returnInsnNodes.get(returnInsnNodes.size() -1);
        addChildMethod.instructions.insertBefore(lastReturnInsnNode, dispatcherHook);

        // add removeCommand
        MethodNode removeCommandMethod = new MethodNode(Opcodes.ACC_PUBLIC, "removeCommand", "(Ljava/lang/String;)V", null, new String[0]);
        for (String fieldName : new String[]{ "children", "literals", "arguments" }) {
            removeCommandMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            removeCommandMethod.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "com/mojang/brigadier/tree/CommandNode", fieldName, "Ljava/util/Map;"));
            removeCommandMethod.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            removeCommandMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Map", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
            removeCommandMethod.instructions.add(new InsnNode(Opcodes.POP));
        }
        removeCommandMethod.maxStack = removeCommandMethod.maxLocals = 2;
        removeCommandMethod.instructions.add(new InsnNode(Opcodes.RETURN));
        input.methods.add(removeCommandMethod);

        return input;
    }

    @Nonnull
    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    @Nonnull
    @Override
    public Set<Target> targets() {
        return Collections.singleton(Target.targetPreClass("com.mojang.brigadier.tree.CommandNode"));
    }
}
