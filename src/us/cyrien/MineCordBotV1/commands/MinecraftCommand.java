package us.cyrien.MineCordBotV1.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.cyrien.MineCordBotV1.main.MineCordBot;

public abstract class MinecraftCommand implements CommandExecutor {

    protected MineCordBot mcb;
    private String commandName;

    public MinecraftCommand(MineCordBot mcb) {
        this.mcb = mcb;
        commandName = this.getClass().getSimpleName();
    }

    public abstract boolean preCommand(CommandSender commandSender, Command command, String[] args);

    public abstract boolean usage(CommandSender cs);

    public boolean noPermMessage(CommandSender cs) {
        cs.sendMessage(ChatColor.RED + "You do not have permission to reload MineCordBot");
        return false;
    }
}
