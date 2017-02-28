package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.command.Command;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SetAvatarCommand extends DiscordCommand {
    private Icon avatar;

    public SetAvatarCommand(MineCordBot mcb) {
        super(mcb, "Set Avatar");
        commandPermissionLevel = PermissionLevel.LEVEL_2;
        description = getLanguage().getTranslatedMessage("mcb.commands.setavatar.description");
        usage = getLanguage().getTranslatedMessage("mcb.commands.setavatar.usage");
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if (args.length == 0 || args.length > 1) {
            sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
            return false;
        }
        try {
            URL url = new URL(args[0]);
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            avatar = Icon.from(in);
        } catch (MalformedURLException ex) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Invalid Link(MalformedURLException)");
            eb.addField("Exception:", ex.getMessage(), false);
            sendMessageEmbed(e, eb.build(), 15);
            return false;
        } catch (IOException ex) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Invalid Link(IOException)");
            eb.addField("Exception:", ex.getMessage(), false);
            sendMessageEmbed(e, eb.build(), 15);
            return false;
        }
        return true;
    }

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        e.getJDA().getSelfUser().getManager().setAvatar(avatar).queue();
    }

}
