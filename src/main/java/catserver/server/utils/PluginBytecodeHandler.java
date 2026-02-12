package catserver.server.utils;

import catserver.server.CatServer;
import net.minecraftforge.eventbus.EventSubclassTransformer;
import net.minecraftforge.fml.ModWorkManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.objectweb.asm.*;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public enum PluginBytecodeHandler {
    INSTANCE;

    /**
     * See {@link ModWorkManager#parallelExecutor()}.
     * Some plugins (TrChat...) load libraries in CompletableFuture, whose default thread class loader context is AppClassLoader, and this will cause exception in {@link EventSubclassTransformer#getClassLoader()}.
     */
    private static final ForkJoinPool EXECUTOR = new ForkJoinPool(CatServer.getConfig().pluginExecutorMaxThreads <= 0 ? Runtime.getRuntime().availableProcessors() : CatServer.getConfig().pluginExecutorMaxThreads, PluginBytecodeHandler::newForkJoinWorkerThread, PluginBytecodeHandler::onPluginThreadException, false);

    private static final boolean isLowerThanJava21;
    static {
        boolean isLowerThanJava21Temp;
        try {
            isLowerThanJava21Temp = Runtime.version().version().get(0) < 21;
        } catch (Throwable e) {
            isLowerThanJava21Temp = true;
        }
        isLowerThanJava21 = isLowerThanJava21Temp;
    }

    private static void onPluginThreadException(Thread thread, Throwable e) {
        CatServer.LOGGER.error(String.format(Locale.ROOT, "Caught exception in thread %s", thread), e);
    }

    /** See {@link ModWorkManager#newForkJoinWorkerThread(ForkJoinPool)} and {@link net.minecraft.Util#makeExecutor(String)} */
    private static ForkJoinWorkerThread newForkJoinWorkerThread(ForkJoinPool pool) {
        ForkJoinWorkerThread thread = new ForkJoinWorkerThread(pool) {
            protected void onTermination(Throwable p_211561_) {
                if (p_211561_ != null) {
                    CatServer.LOGGER.warn("{} died", this.getName(), p_211561_);
                } else {
                    CatServer.LOGGER.debug("{} shutdown", (Object)this.getName());
                }

                super.onTermination(p_211561_);
            }
        };
        thread.setName("catserver-plugin-worker-" + thread.getPoolIndex());
        thread.setContextClassLoader(Thread.currentThread().getContextClassLoader()); // This class loader should be TransformingClassLoader
        return thread;
    }

    /**
     * Called by asm code in {@link #convertPluginClassBytecode(byte[])}.
     */
    public static CompletableFuture<Void> catserver$CompletableFuture$runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, EXECUTOR);
    }

    /**
     * Called by asm code in {@link #convertPluginClassBytecode(byte[])}.
     */
    public static Object catserver$List$getFirst(List<?> list) {
        return list.get(0);
    }

    /**
     * Called by asm code in {@link #convertPluginClassBytecode(byte[])}.
     */
    public static Object catserver$List$getLast(List<?> list) {
        return list.get(list.size() - 1);
    }

    /**
     * See {@link org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers#processClass(PluginDescriptionFile, String, byte[])}
     */
    public static byte[] processPluginClass(String path, byte[] clazz) {
        try {
            clazz = convertPluginClassBytecode(clazz);
        } catch (Exception ex) {
            CatServer.LOGGER.error("Fatal error trying to convert " + path, ex);
        }

        return clazz;
    }

    private static byte[] convertPluginClassBytecode(byte[] b) {
        ClassReader cr = new ClassReader(b);
        ClassWriter cw = new ClassWriter(cr, 0);

        cr.accept(new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (opcode == Opcodes.INVOKESTATIC && owner.equals("java/util/concurrent/CompletableFuture") && name.equals("runAsync") && descriptor.equals("(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;")) {
                            // CompletableFuture#runAsync(Runnable)
                            super.visitMethodInsn(opcode, "catserver/server/utils/PluginBytecodeHandler", "catserver$CompletableFuture$runAsync", descriptor, false);
                        } else if (PluginBytecodeHandler.isLowerThanJava21 && opcode == Opcodes.INVOKEINTERFACE && owner.equals("java/util/List") && name.equals("getFirst") && descriptor.equals("()Ljava/lang/Object;")) {
                            // [compat:plugin:QuickShop-Hikari:6.2.0.10] Java21 List#getFirst()
                            super.visitMethodInsn(Opcodes.INVOKESTATIC, "catserver/server/utils/PluginBytecodeHandler", "catserver$List$getFirst", "(Ljava/util/List;)Ljava/lang/Object;", false);
                        } else if (PluginBytecodeHandler.isLowerThanJava21 && opcode == Opcodes.INVOKEINTERFACE && owner.equals("java/util/List") && name.equals("getLast") && descriptor.equals("()Ljava/lang/Object;")) {
                            // Java21 List#getLast()
                            super.visitMethodInsn(Opcodes.INVOKESTATIC, "catserver/server/utils/PluginBytecodeHandler", "catserver$List$getLast", "(Ljava/util/List;)Ljava/lang/Object;", false);
                        } else {
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        }
                    }
                };
            }
        }, 0);

        return cw.toByteArray();
    }
}
