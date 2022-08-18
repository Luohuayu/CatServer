package catserver.server.utils;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;

public class CommandNodeHook {
    private static final Field CHILDREN;
    private static final Field LITERALS;
    private static final Field ARGUMENTS;

    static {
        try {
            CHILDREN = CommandNode.class.getDeclaredField("children");
            LITERALS = CommandNode.class.getDeclaredField("literals");
            ARGUMENTS = CommandNode.class.getDeclaredField("arguments");
            CHILDREN.setAccessible(true);
            LITERALS.setAccessible(true);
            ARGUMENTS.setAccessible(true);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void removeCommand(RootCommandNode node, String command) {
        try {
            ((Map<?, ?>) CHILDREN.get(node)).remove(command);
            ((Map<?, ?>) LITERALS.get(node)).remove(command);
            ((Map<?, ?>) ARGUMENTS.get(node)).remove(command);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
