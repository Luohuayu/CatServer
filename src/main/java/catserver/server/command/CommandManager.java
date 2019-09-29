package catserver.server.command;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.io.FileUtils;
import org.bukkit.command.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CommandManager {
    public static void logToFile() {
        StringBuilder sb = new StringBuilder();
        CraftSimpleCommandMap commandMap = MinecraftServer.getServerInst().server.getCraftCommandMap();
        for (Command command : commandMap.getCommands()) {
            if (command instanceof ModCustomCommand) {
                if (!((ModCustomCommand) command).vanillaCommand.getClass().getName().startsWith("net.minecraft.command.")) {
                    sb.append(String.format("注册指令 %s 权限为 minecraft.command.%s\n", command.getName(), command.getName()));
                }
            }
        }
        try {
            FileUtils.writeByteArrayToFile(new File("forgePermission.log"), sb.toString().getBytes(StandardCharsets.UTF_8));
            FMLLog.info("已将注册的Forge指令权限输出到forgePermission.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
