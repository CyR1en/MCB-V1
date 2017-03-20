package us.cyrien.MineCordBotV1.commands.discordCommands;

import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InfoCommand extends DiscordCommand {

    private MineCordBot mcb;

    public InfoCommand(MineCordBot mcb) {
        super(mcb, "Info", CommandType.INFO, PermissionLevel.LEVEL_0);
        this.mcb = mcb;
        usage = getLanguage().getTranslatedMessage("mcb.commands.info.usage");
        description = getLanguage().getTranslatedMessage("mcb.commands.info.description");
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        return true;
    }

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        sendMessageEmbed(e, generateInfoMessage(e), 90);
    }

    private MessageEmbed generateInfoMessage(MessageReceivedEvent e) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String path = "mcb.commands.info.minfo.";
        int textChannelCount = e.getGuild().getTextChannels().size();
        int voiceChannelCount = e.getGuild().getVoiceChannels().size();
        String clientID = MCBConfig.get("client_id");
        String botName = e.getJDA().getSelfUser().getName();
        Guild guild = e.getGuild();
        Member member = guild.getMember(e.getJDA().getSelfUser());
        String nickName = (getLanguage().getTranslatedMessage(path + "nonick"));
        if (e.getGuild().getMember(e.getJDA().getSelfUser()).getNickname() != null)
            nickName = e.getGuild().getMember(e.getJDA().getSelfUser()).getNickname();
        String mcbInfo = "\n" + getLanguage().getTranslatedMessage(path + "version") + ": " + MineCordBot.CURRENT_VERSION +
                "\n" + getLanguage().getTranslatedMessage(path + "textchannel") + ": " + textChannelCount +
                "\n" + getLanguage().getTranslatedMessage(path + "voicechannel") + ": " + voiceChannelCount +
                "\n" + getLanguage().getTranslatedMessage(path + "uptime") + ": " + mcb.getUpTimer().getCurrentUptime();
        String botInfo = "\n" + getLanguage().getTranslatedMessage(path + "clientid") + ": " + clientID +
                "\n" + getLanguage().getTranslatedMessage(path + "botname") + ": " + botName +
                "\n" + getLanguage().getTranslatedMessage(path + "botnick") + ": " + nickName;
        embedBuilder.setColor(member.getColor());
        embedBuilder.setThumbnail("https://media-elerium.cursecdn.com/attachments/thumbnails/124/611/310/172/minecord.png");
        embedBuilder.setAuthor("MineCordBot", "https://dev.bukkit.org/projects/minecordbot-bukkit", "https://media-elerium.cursecdn.com/attachments/thumbnails/124/611/310/172/minecord.png" );
        //embedBuilder.addBlankField(false);
        embedBuilder.addField(getLanguage().getTranslatedMessage(path + "generalinfo_header"), mcbInfo, false);
        embedBuilder.addField(getLanguage().getTranslatedMessage(path + "botinfo_header"), botInfo, false);
        //embedBuilder.addBlankField(false);
        embedBuilder.setFooter("- C Y R I Ξ N  | A L V A R I Ξ N -", "https://yt3.ggpht.com/-uuXItiIhgcU/AAAAAAAAAAI/AAAAAAAAAAA/3xzbfTTz9oU/s88-c-k-no-mo-rj-c0xffffff/photo.jpg");
        return embedBuilder.build();
    }

}
