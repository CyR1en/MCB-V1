package us.cyrien.MineCordBotV1.commands;


import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.entity.Messenger;
import us.cyrien.MineCordBotV1.entity.User;
import us.cyrien.MineCordBotV1.main.Language;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.parse.MultiLangMessageParser;
import us.cyrien.MineCordBotV1.permission.Permission;
import us.cyrien.MineCordBotV1.utils.ArrayUtils;
import us.cyrien.MineCordBotV1.utils.JsonUtils;

import java.awt.*;
import java.util.Objects;
import java.util.logging.Logger;

public abstract class DiscordCommand implements Permission {

    protected enum CommandType {MISC, HELP, FUN, INFO, MOD}

    public static final int HELPCARD_DURATION = 25;

    private User sender;
    private String commandName;

    private MultiLangMessageParser langMessageParser;
    private Language language;
    private Messenger messenger;
    private boolean tempResponse;
    private Logger logger;
    private MessageReceivedEvent event;

    protected MineCordBot mcb;
    protected PermissionLevel commandPermissionLevel;
    protected String usage;
    protected String description;
    protected CommandType commandType;
    protected String trigger;
    protected JSONArray boundTextChannel;

    public DiscordCommand(MineCordBot mcb, String commandName, CommandType commandType, PermissionLevel commandPermissionLevel) {
        this.mcb = mcb;
        sender = new User(mcb);
        this.commandName = commandName;
        this.commandPermissionLevel = commandPermissionLevel;
        this.commandType = commandType;
        usage = "command usage not setup";
        description = "command description not setup";
        language = mcb.getLanguage();
        langMessageParser = new MultiLangMessageParser();
        messenger = mcb.getMessenger();
        tempResponse = MCBConfig.get("auto_delete_command_response");
        trigger = MCBConfig.get("trigger");
        logger = mcb.getLogger();
    }

    protected void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me, int length) {
        if (tempResponse)
            getMessenger().sendTempMessageEmbed(e, me, length);
        else
            getMessenger().sendMessageEmbed(e, me);
    }

    protected void sendMessage(MessageReceivedEvent e, String message, int length) {
        if (tempResponse)
            getMessenger().sendTempMessage(e, message, length);
        else
            getMessenger().sendMessage(e, message);
    }

    public MessageEmbed getHelpCard(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        eb.setTitle(commandName + " Command Help Card:", null);
        eb.addField("Usage", MCBConfig.get("trigger") + getUsage(), false);
        eb.addField("Description", getDescription(), false);
        return eb.build();
    }

    public MessageEmbed getInvalidHelpCard(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getHelpCard(e));
        eb.setColor(new Color(217, 83, 79));
        return eb.build();
    }

    public boolean checkTextChannel(MessageReceivedEvent e) {
        boundTextChannel = setBoundTextChannel();
        if (boundTextChannel == null)
            return true;
        else if (boundTextChannel.length() == 1 || boundTextChannel.get(0).toString().trim().equals(""))
            return true;
        for (Object s : boundTextChannel) {
            TextChannel tc = e.getJDA().getTextChannelById(s.toString());
            if (tc != null && tc.getId().equals(e.getTextChannel().getId()))
            return true;
        }
        return false;
    }

    public abstract boolean checkArguments(MessageReceivedEvent e, String[] args);

    public abstract void execute(MessageReceivedEvent e, String[] args);

    public void executed(MessageReceivedEvent e) {
        logCommand(e);
    }

    public void logCommand(MessageReceivedEvent e) {
        getLogger().info(getSender().getName() + " Issued " + commandName + " Command");
    }

    public Logger getLogger() {
        return logger;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getCommandName() {
        return commandName;
    }

    public Language getLanguage() {
        return language;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setEvent(MessageReceivedEvent e) {
        event = e;
    }

    public String getCommandTypeToString() {
        return getCommandTypeToString(commandType);
    }

    public String getCommandTypeToString(CommandType commandType) {
        String listPath = "mcb.commands.help.list";
        switch (commandType) {
            case MISC:
                return getMessage(listPath + ".misc");
            case HELP:
                return getMessage(listPath + ".help");
            case FUN:
                return getMessage(listPath + ".fun");
            case INFO:
                return getMessage(listPath + ".info");
            case MOD:
                return getMessage(listPath + ".mod");
            default:
                return "Command Type " + commandType + " is invalid";
        }
    }

    private JSONArray setBoundTextChannel() {
        JSONObject ctc = MCBConfig.getJSONObject("command_text_channel");
        switch (commandType) {
            case MISC:
                return ctc.getJSONArray("misc");
            case HELP:
                return ctc.getJSONArray("help");
            case FUN:
                return ctc.getJSONArray("fun");
            case INFO:
                return ctc.getJSONArray("info");
            case MOD:
                return ctc.getJSONArray("mod");
            default:
                return null;
        }
    }

    public String concatenateArgs(String[] args) {
        return ArrayUtils.concatenateArgs(args);
    }

    public void sendNoPermMessage(MessageReceivedEvent e) {
        sendMessage(e, noPermissionMessage(), 20);
    }

    public void sendInvalidArgMessage(MessageReceivedEvent e) {
        sendMessage(e, invalidArgumentsMessage(), 20);
    }

    public void sendWrongTc(MessageReceivedEvent e) {
        sendMessage(e, invalidTc(), 7);
    }

    @Override
    public boolean hasPermission(String[] args) {
        if (sender.getId().equals("193970511615623168") || getSender().getId().equalsIgnoreCase(MCBConfig.get("owner_id")))
            return true;
        else
            return sender.getPermissionLevel().ordinal() >= commandPermissionLevel.ordinal();
    }

    @Override
    public String noPermissionMessage() {
        return "`" + language.getTranslatedMessage("mcb.command.no-perm-message") + "`";
    }

    public String invalidArgumentsMessage() {
        return "`" + language.getTranslatedMessage("mcb.command.invalid-arguments") + "`";
    }

    public String invalidTc() {
        return "`" + language.getTranslatedMessage("mcb.command.invalid-tc") + "`";
    }

    protected String getMessage(String path) {
        return getLanguage().getTranslatedMessage(path);
    }


}
