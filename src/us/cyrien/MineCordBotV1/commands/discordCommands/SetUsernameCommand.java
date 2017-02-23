package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;

public class SetUsernameCommand extends DiscordCommand {

    public SetUsernameCommand(MineCordBot mcb) {
        super(mcb, "Set Username");
        commandPermissionLevel = LEVEL_2;
        description = getLanguage().getTranslatedMessage("mcb.commands.setusername.description");
        usage = getLanguage().getTranslatedMessage("mcb.commands.setusername.usage");
        commandType = MISC;
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
        e.getJDA().getSelfUser().getManager().setName(concatenateArgs(args)).queue();
    }

    @Override
    public void executed(MessageReceivedEvent e) {
        logCommand(e);
    }

    @Override
    public void logCommand(MessageReceivedEvent e) {
        getLogger().info(e.getAuthor().getName() + " Issued set username command");
    }
}
