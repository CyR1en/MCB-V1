package us.cyrien.MineCordBotV1.commands.minecraftCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import us.cyrien.MineCordBotV1.commands.MinecraftCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.utils.ArrayUtils;

public class Dcmd extends MinecraftCommand {

    public Dcmd(MineCordBot mcb) {
        super(mcb);
    }

    public boolean preCommand(CommandSender commandSender, Command command, String[] args) {
        if (!command.getName().equalsIgnoreCase("dcmd"))
            return usage(commandSender);
        if (!commandSender.hasPermission("minecordbot.dcmd")) {
            commandSender.sendMessage(ChatColor.RED + "You have no permission to do Discord command from Minecraft");
            return false;
        }
        return !(args.length == 0) || usage(commandSender);
    }

    private void command(CommandSender commandSender, String[] args) {
        for (String s : mcb.getMcbConfig().getTextChannels())
            mcb.getJda().getTextChannelById(s).sendMessage(ArrayUtils.concatenateArgs(args)).queue(consumer ->
                    mcb.getLogger().info(commandSender.getName() + " Executed a discord command : " + args[0])
            );
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!preCommand(commandSender, command, args))
            return true;
        command(commandSender, args);
        commandSender.sendMessage(ChatColor.GOLD + "Command sent to Discord!");
        return true;
    }

    public boolean usage(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l=== dcmd usage ==="));
        commandSender.sendMessage("/dcmd <discord command> {other arguments if applies)");
        commandSender.sendMessage("include command executor for discord command.");
        commandSender.sendMessage("i.e : /dcmd !play sample_song");
        return false;
    }
}
