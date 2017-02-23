package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;

public class SetNicknameCommand extends DiscordCommand {

    public SetNicknameCommand(MineCordBot mcb) {
        super(mcb, "Set Nickname");
        commandPermissionLevel = LEVEL_2;
        description = getLanguage().getTranslatedMessage("mcb.commands.setnickname.description");
        usage = getLanguage().getTranslatedMessage("mcb.commands.setnickname.usage");
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
        GuildController gc = new GuildController(e.getGuild());
        gc.setNickname(gc.getGuild().getMember(e.getJDA().getSelfUser()), concatenateArgs(args)).queue();
    }

    @Override
    public void executed(MessageReceivedEvent e) {
        logCommand(e);
    }

    @Override
    public void logCommand(MessageReceivedEvent e) {
        getLogger().info(e.getAuthor().getName() + " Issued set nickname command");
    }
}
