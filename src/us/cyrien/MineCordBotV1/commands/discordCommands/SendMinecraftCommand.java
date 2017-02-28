package us.cyrien.MineCordBotV1.commands.discordCommands;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.entity.DiscordCommandSender;
import us.cyrien.MineCordBotV1.entity.DiscordConsoleCommandSender;
import us.cyrien.MineCordBotV1.main.MineCordBot;

public class SendMinecraftCommand extends DiscordCommand {

    public SendMinecraftCommand(MineCordBot mcb) {
        super(mcb, "Minecraft Command Sender");
        usage = getLanguage().getTranslatedMessage("mcb.commands.mcmd.usage");
        description = getLanguage().getTranslatedMessage("mcb.commands.mcmd.description");
        commandPermissionLevel = PermissionLevel.LEVEL_2;
        commandType = CommandType.MOD;
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if (args.length == 0) {
            sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
            return false;
        }
        return true;
    }

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        System.out.println(concatenateArgs(args));
        if (args[0].equalsIgnoreCase("help")) {
            Bukkit.getServer().dispatchCommand(new DiscordCommandSender(e), concatenateArgs(args));
        } else
            Bukkit.getServer().dispatchCommand(new DiscordConsoleCommandSender(e), concatenateArgs(args));
    }

}
