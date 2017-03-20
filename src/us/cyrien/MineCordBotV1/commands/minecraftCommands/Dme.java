package us.cyrien.MineCordBotV1.commands.minecraftCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.cyrien.MineCordBotV1.commands.MinecraftCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.utils.ArrayUtils;

public class Dme extends MinecraftCommand {

    public Dme (MineCordBot mcb) {
        super(mcb);
    }

    @Override
    public boolean preCommand(CommandSender commandSender, Command command, String[] args) {
        if(!command.getName().equalsIgnoreCase("dme"))
            return usage(commandSender);
        if(!commandSender.hasPermission("minecordbot.dme")) {
            commandSender.sendMessage(ChatColor.RED + "You don't have permission to use /dme");
            return false;
        }
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can do discord me or /dme");
            return false;
        }
        return true;
    }

    private void command(CommandSender commandSender, String[] args) {
        Player p = (Player) commandSender;
        String msg = ArrayUtils.concatenateArgs(args);
        for(Player pl : mcb.getServer().getOnlinePlayers())
            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5* " + "&r" + p.getDisplayName() + " &5" + msg));
        getMessenger().sendMessageToAllBoundChannel("`* " + p.getName() + msg + "`");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!preCommand(commandSender, command, args))
            return true;
        command(commandSender, args);
        return true;
    }

    @Override
    public boolean usage(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l=== dme usage ==="));
        return false;
    }
}