package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;

public class ReloadCommand extends DiscordCommand{

    public ReloadCommand(MineCordBot mcb) {
        super(mcb, "Reload", CommandType.MOD, PermissionLevel.LEVEL_3);
        usage = getLanguage().getTranslatedMessage("mcb.commands.reload.usage");
        description = getLanguage().getTranslatedMessage("mcb.commands.reload.description");
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        return true;
    }

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        MCBConfig.reload();
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(getLanguage().getTranslatedMessage("mcb.commands.reload.reloaded"), null);
        sendMessageEmbed(e, eb.build(), HELPCARD_DURATION);
    }
}
