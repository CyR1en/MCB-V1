package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import org.json.JSONArray;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.MineCordBotV1.utils.JsonUtils;

public class TextChannelCommand extends DiscordCommand {

    public TextChannelCommand(MineCordBot mcb) {
        super(mcb, "TextChannel", CommandType.MOD, PermissionLevel.LEVEL_0);
        usage = getLanguage().getTranslatedMessage("mcb.commands.textchannel.usage");
        description = getLanguage().getTranslatedMessage("mcb.commands.textchannel.description");
    }

    @Override
    public boolean hasPermission(String[] args) {
        if (args.length != 0) {
            if (getSender().getId().equalsIgnoreCase(MCBConfig.get("owner_id")))
                return true;
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))
                return getSender().getPermissionLevel().ordinal() >= PermissionLevel.LEVEL_1.ordinal();
        }
        return true;
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("list")) {
                sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
                return false;
            }
        }
        if (args.length == 0 || args.length > 2) {
            sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
            return false;
        }
        if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
            return true;
        } else {
            sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
            return false;
        }
    }

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        if (args[0].equalsIgnoreCase("list"))
            sendMessageEmbed(e, generateListEmbed(e), 6 * args.length);
        if (args[0].equalsIgnoreCase("add"))
            addTextChannel(args[1], e);
        if (args[0].equalsIgnoreCase("remove"))
            removeTextChannel(args[1], e);
    }

    private MessageEmbed generateListEmbed(MessageReceivedEvent e) {
        String path = "mcb.commands.textchannel.list.";
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("- " + getLanguage().getTranslatedMessage(path + "header") + " -", null);
        eb.setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        int i = 1;
        JSONArray tcArray = MCBConfig.get("text_channels");
        for (Object tc : tcArray) {
            TextChannel tc1 = mcb.getJda().getTextChannelById(tc.toString());
            if (tc1 != null) {
                String gName = tc1.getGuild().getName();
                String tcName = tc1.getName();
                String str = getLanguage().getTranslatedMessage(path + "guild_name") + ": " + gName + "\n";
                str += getLanguage().getTranslatedMessage(path + "channel_name") + ": " + tcName;
                eb.addField( i++ + ". " + "[" + tc + "]" + ": ", str, false);
            }
        }
        return eb.build();
    }

    private void addTextChannel(String textChannelID, MessageReceivedEvent e) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        net.dv8tion.jda.core.entities.TextChannel c = mcb.getJda().getTextChannelById(textChannelID);
        if (c != null) {
            tcArray.put(textChannelID);
            MCBConfig.set("text_channels", tcArray);
        } else {
            sendMessage(e, "Text channel `" + textChannelID + "` is not a valid text channel.", 10);
            return;
        }
        sendMessage(e, "Added text channel `" + textChannelID + "` to the bound channels.", 10);
        mcb.getMcbLogger().info("Added text channel " + textChannelID);
    }

    private void removeTextChannel(String textChannelID, MessageReceivedEvent e) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        if (!containsID(textChannelID)) {
            sendMessage(e, "Text channel `" + textChannelID + "` is not bound to Minecraft", 20);
            return;
        }
        if(tcArray.length() == 1) {
            sendMessage(e, "Cannot remove textchannel `" + textChannelID + "` because it's the last textchannel on the list", 20);
            return;
        }
        tcArray.remove(JsonUtils.indexOf(textChannelID, tcArray));
        MCBConfig.set("text_channels", tcArray);
        sendMessage(e, "Removed text channel `" + textChannelID + "` from the bound channels.", 10);
        mcb.getMcbLogger().info("Removed text channel " + textChannelID);
    }

    private boolean containsID(String textChannelID) {
        JSONArray ids = MCBConfig.get("text_channels");
        assert ids != null;
        for (Object s : ids)
            if (s.toString().equalsIgnoreCase(textChannelID))
                return true;
        return false;
    }

}
