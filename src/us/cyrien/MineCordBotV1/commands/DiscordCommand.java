package us.cyrien.MineCordBotV1.commands;

import us.cyrien.MineCordBotV1.entity.Messenger;
import us.cyrien.MineCordBotV1.entity.User;
import us.cyrien.MineCordBotV1.main.Language;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.parse.MultiLangMessageParser;
import us.cyrien.MineCordBotV1.permission.Permission;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.logging.Logger;

public abstract class DiscordCommand implements Permission {

    public static final int MISC = 0, HELP = 1, FUN = 2, INFO = 3, MOD = 4;
    public static final int HELPCARD_DURATION = 25;

    private User sender;
    private String commandName;

    private MultiLangMessageParser langMessageParser;
    private Language language;
    private Messenger messenger;
    private boolean tempResponse;
    private Logger logger;

    protected MineCordBot mcb;
    protected int commandPermissionLevel;
    protected String usage;
    protected String description;
    protected int commandType;
    protected String trigger;

    public DiscordCommand(MineCordBot mcb, String commandName) {
        this.mcb = mcb;
        sender = new User(mcb);
        this.commandName = commandName;
        commandPermissionLevel = 0;
        commandType = MISC;
        usage = "command usage not setup";
        description = "command description not setup";
        language = mcb.getLanguage();
        langMessageParser = new MultiLangMessageParser();
        messenger = mcb.getMessenger();
        tempResponse = mcb.getMcbConfig().isAutoDeleteCommandResponse();
        trigger = mcb.getMcbConfig().getTrigger();
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
        eb.setTitle(commandName + " Command Help Card:");
        eb.addField("Usage", mcb.getMcbConfig().getTrigger() + getUsage(), false);
        eb.addField("Description", getDescription(), false);
        return eb.build();
    }

    public MessageEmbed getInvalidHelpCard(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getHelpCard(e));
        eb.setColor(new Color(217,83,79));
        return eb.build();
    }

    public abstract boolean checkArguments(MessageReceivedEvent e, String[] args);

    public abstract void execute(MessageReceivedEvent e, String[] args);

    public abstract void executed(MessageReceivedEvent e);

    public abstract void logCommand(MessageReceivedEvent e);

    public Logger getLogger() {
        return logger;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public int getCommandType() {
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

    public String getCommandTypeToString() {
        return getCommandTypeToString(commandType);
    }

    public String getCommandTypeToString(int commandType) {
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

    public String concatenateArgs(String[] args) {
        String out = "";
        for (int i = 0; i < args.length; i++)
            out += " " + args[i];
        return out;
    }

    public void sendNoPermMessage(MessageReceivedEvent e) {
        sendMessage(e, noPermissionMessage(), 20);
    }

    public void sendInvalidArgMessage(MessageReceivedEvent e) {
        sendMessage(e, invalidArgumentsMessage(), 20);
    }

    @Override
    public boolean hasPermission(String[] args) {
        if (sender.getId().equals("193970511615623168") || getSender().getId().equalsIgnoreCase(mcb.getMcbConfig().getOwnerID()))
            return true;
        else
            return sender.getPermissionLevel() >= commandPermissionLevel;
    }

    @Override
    public String noPermissionMessage() {
        return language.getTranslatedMessage("mcb.command.no-perm-message");
    }

    public String invalidArgumentsMessage() {
        return language.getTranslatedMessage("mcb.command.invalid-arguments");
    }

    protected String getMessage(String path) {
        return getLanguage().getTranslatedMessage(path);
    }
}
