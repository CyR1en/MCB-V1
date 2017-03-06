package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.configuration.MineCordBotConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;

import java.io.IOException;
import java.util.List;

public class PermissionCommand extends DiscordCommand {

    public PermissionCommand(MineCordBot mcb) {
        super(mcb, "Permission Command");
        commandType = CommandType.MOD;
        commandPermissionLevel = PermissionLevel.LEVEL_2;
        description = getLanguage().getTranslatedMessage("mcb.commands.permission.description");
        usage = getLanguage().getTranslatedMessage("mcb.commands.permission.usage");
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if (args.length == 0 || args.length > 3 || args.length == 1) {
            sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
            return false;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("remove")) {
                if (e.getGuild().getMemberById(args[1]) == null) {
                    if (args[0].equalsIgnoreCase("get"))
                        sendMessageEmbed(e, getHelp(e), HELPCARD_DURATION);
                    else
                        sendMessageEmbed(e, removeHelp(e), HELPCARD_DURATION);
                    return false;
                }
            } else {
                sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
                return false;
            }
            if (e.getJDA().getUserById(args[1]) == null)
                return false;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {
                int permLvl = Integer.parseInt(args[1]);
                if (permLvl < 0 || permLvl > 3) {
                    sendMessageEmbed(e, addHelp(e), HELPCARD_DURATION);
                    return false;
                }
            } else {
                sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
                return false;
            }
            if (e.getJDA().getUserById(args[2]) == null)
                return false;
        }
        return true;
    }

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        switch (args[0]) {
            case "add":
                addPerm(Integer.parseInt(args[1]), args[2], e);
                break;
            case "remove":
                removePerm(args[1], e);
                break;
            case "get":
                sendMessage(e, getPerm(args[1], e), 15);
                break;
        }
    }

    private void addPerm(int permLevel, String id, MessageReceivedEvent e) {

        List<String> pl = (List<String>) mcb.getPluginFile().getConfig().getList(("Permissions.level_" + permLevel));
        Member member = e.getGuild().getMember(e.getJDA().getUserById(id));
        if (member != null) {
            pl.add(member.getUser().getId());
            mcb.getPluginFile().getConfig().set(("Permissions.level_" + permLevel), pl);
        } else {
            sendMessage(e, "User with `" + id + "` cannot be found.", 20);
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
        sendMessage(e, "User `" + id + "` now has a level_" + permLevel + " Permission.", 20);
        mcb.getMcbLogger().info("Added User " + id + " to permission level_" + permLevel);
    }

    private void removePerm(String id, MessageReceivedEvent e) {
        User user = e.getJDA().getUserById(id);
        if (getPermissionLevel(user.getId(), e) == PermissionLevel.LEVEL_0) {
            sendMessage(e, "No permission to be removed from `" + user.getId() + "`", 20);
            return;
        }
        PermissionLevel permLevel = getPermissionLevel(user.getId(), e);
        List<String> pl = (List<String>) mcb.getPluginFile().getConfig().getList(("Permissions.level_" + permLevel.ordinal()));
        System.out.println(("Permission.level_" + permLevel.ordinal()));
        if (pl.size() == 1) {
            sendMessage(e, "Cannot remove user `" + id + "` because it's the last user on the permission list " + permLevel.toString().toLowerCase(), 20);
            return;
        }
        mcb.getPluginFile().getConfig().set(("Permissions.level_" + permLevel.ordinal()), pl);
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
        sendMessage(e, "Removed " + permLevel.toString().toLowerCase() + " permission from `" + user.getId() + "`", 10);
        mcb.getMcbLogger().info("Added User " + id + " to permission " + permLevel);
    }

    private String getPerm(String id, MessageReceivedEvent e) {
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
        PermissionLevel level = getPermissionLevel(id, e);
        return "User `" + id + "` has a `" + level + "` permission.";
    }

    private PermissionLevel getPermissionLevel(String id, MessageReceivedEvent e) {
        MineCordBotConfig cfg = mcb.getMcbConfig();
        if (containsID(cfg.getPermLvl3(), id))
            return PermissionLevel.LEVEL_3;
        else if (containsID(cfg.getPermLvl2(), id))
            return PermissionLevel.LEVEL_2;
        else if (containsID(cfg.getPermLvl1(), id))
            return PermissionLevel.LEVEL_1;
        else
            return PermissionLevel.LEVEL_0;
    }

    private boolean containsID(List<String> str, String id) {
        for (String s : str) {
            if (s.equals(id))
                return true;
        }
        return false;
    }

    private MessageEmbed getHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", mcb.getMcbConfig().getTrigger() + " <get> <userID>", false);
        return eb.build();
    }

    private MessageEmbed addHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", mcb.getMcbConfig().getTrigger() + " <add> <permLevel> <userID>", false);
        return eb.build();
    }

    private MessageEmbed removeHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", mcb.getMcbConfig().getTrigger() + " <remove> <userID>", false);
        return eb.build();
    }

}
