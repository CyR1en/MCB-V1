package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextChannelCommand extends DiscordCommand {

    private ArrayList<String> textChannels;

    public TextChannelCommand(MineCordBot mcb) {
        super(mcb, "TextChannel");
        usage = getLanguage().getTranslatedMessage("mcb.commands.textchannel.usage");
        description = getLanguage().getTranslatedMessage("mcb.commands.textchannel.description");
        commandType = CommandType.MOD;
        textChannels = (ArrayList<String>) mcb.getMcbConfig().getTextChannels();
        Collections.sort(textChannels);
    }

    @Override
    public boolean hasPermission(String[] args) {
        if (args.length != 0) {
            if (getSender().getId().equalsIgnoreCase(mcb.getMcbConfig().getOwnerID()))
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
        eb.setTitle("- " + getLanguage().getTranslatedMessage(path + "header") + " -");
        eb.setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        int i = 1;
        for (String tc : textChannels) {
            TextChannel tc1 = mcb.getJda().getTextChannelById(tc);
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
        List<String> tc = (List<String>) mcb.getPluginFile().getConfig().getList("text_channels");
        net.dv8tion.jda.core.entities.TextChannel c = mcb.getJda().getTextChannelById(textChannelID);
        if (c != null) {
            tc.add(textChannelID);
            mcb.getPluginFile().getConfig().set("chat_channel", tc);
        } else {
            sendMessage(e, "Text channel `" + textChannelID + "` is not a valid text channel.", 10);
            return;
        }
        try {
            mcb.getPluginFile().save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            mcb.getMcbConfig().reload();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        sendMessage(e, "Added text channel `" + textChannelID + "` to the bound channels.", 10);
        mcb.getMcbLogger().info("Added text channel " + textChannelID);
    }

    private void removeTextChannel(String textChannelID, MessageReceivedEvent e) {
        List<String> tc = (List<String>) mcb.getPluginFile().getConfig().getList("text_channels");
        if (!containsID(textChannelID)) {
            sendMessage(e, "Text channel `" + textChannelID + "` is not bound to Minecraft", 20);
            return;
        }
        if(tc.size() == 1) {
            sendMessage(e, "Cannot remove textchannel `" + textChannelID + "` because it's the last textchannel on the list", 20);
            return;
        }
        tc.remove(textChannelID);
        mcb.getPluginFile().getConfig().set("chat_chasuppnnel", tc);
        try {
            mcb.getPluginFile().save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            mcb.getMcbConfig().reload();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        sendMessage(e, "Removed text channel `" + textChannelID + "` from the bound channels.", 10);
        mcb.getMcbLogger().info("Removed text channel " + textChannelID);
    }

    private boolean containsID(String textChannelID) {
        ArrayList<String> ids = (ArrayList<String>) mcb.getMcbConfig().getTextChannels();
        for (String s : ids)
            if (s.equalsIgnoreCase(textChannelID))
                return true;
        return false;
    }

}
