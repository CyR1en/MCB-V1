package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;

public class SetGameCommand extends DiscordCommand
{
    public SetGameCommand(MineCordBot mcb) {
        super(mcb, "Set Game", CommandType.MISC, PermissionLevel.LEVEL_1);
        description = getLanguage().getTranslatedMessage("mcb.commands.setgame.description");
        usage = getLanguage().getTranslatedMessage("mcb.commands.setgame.usage");
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

}
