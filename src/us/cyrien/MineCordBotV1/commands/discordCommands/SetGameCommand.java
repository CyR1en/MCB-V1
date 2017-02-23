package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;

public class SetGameCommand extends DiscordCommand
{
    public SetGameCommand(MineCordBot mcb) {
        super(mcb, "Set Game");
        commandPermissionLevel = LEVEL_2;
        description = getLanguage().getTranslatedMessage("mcb.commands.setgame.description");
        usage = getLanguage().getTranslatedMessage("mcb.commands.setgame.usage");
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
        e.getJDA().getPresence().setGame(Game.of(concatenateArgs(args)));
    }

    @Override
    public void executed(MessageReceivedEvent e) {
        logCommand(e);
    }

    @Override
    public void logCommand(MessageReceivedEvent e) {
        getLogger().info(e.getAuthor().getName() + " Issued set game command");
    }
}
