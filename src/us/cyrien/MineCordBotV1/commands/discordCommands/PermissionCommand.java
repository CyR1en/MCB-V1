package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;


public class PermissionCommand extends DiscordCommand {

    public PermissionCommand(MineCordBot mcb) {
        super(mcb, "Permission Command", CommandType.MOD, PermissionLevel.LEVEL_2);
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
                sendMessage(e, getPerm(args[1]), 15);
                break;
        }
    }

    private void addPerm(int permLevel, String id, MessageReceivedEvent e) {
        JSONArray pl = MCBConfig.getJSONObject("permissions").getJSONArray("level_" + permLevel);
        Member member = e.getGuild().getMember(e.getJDA().getUserById(id));
        if (member != null) {
            assert pl != null;
            pl.put(member.getUser().getId());
            MCBConfig.getJSONObject("permissions").remove("level_" + permLevel);
            MCBConfig.getJSONObject("permissions").put("level_" + permLevel, pl);
            MCBConfig.save();
        } else {
            sendMessage(e, "User with `" + id + "` cannot be found.", 20);
            return;
        }
        sendMessage(e, "User `" + id + "` now has a level_" + permLevel + " Permission.", 20);
        mcb.getMcbLogger().info("Added User " + id + " to permission level_" + permLevel);
    }

    private void removePerm(String id, MessageReceivedEvent e) {
        User user = e.getJDA().getUserById(id);
        if (getPermissionLevel(user.getId()) == PermissionLevel.LEVEL_0) {
            sendMessage(e, "No permission to be removed from `" + user.getId() + "`", 20);
            return;
        }
        PermissionLevel permLevel = getPermissionLevel(user.getId());
        JSONArray pl = MCBConfig.getJSONObject("permissions").getJSONArray("level_" + permLevel.ordinal());
        assert pl != null;
        if (pl.length() == 1) {
            sendMessage(e, "Cannot remove user `" + id + "` because it's the last user on the permission list " + permLevel.toString().toLowerCase(), 20);
            return;
        }
        MCBConfig.getJSONObject("permissions").remove("level_" + permLevel.ordinal());
        MCBConfig.getJSONObject("permissions").put("level_" + permLevel.ordinal(), pl);
        MCBConfig.save();
        sendMessage(e, "Removed " + permLevel.toString().toLowerCase() + " permission from `" + user.getId() + "`", 10);
        mcb.getMcbLogger().info("Added User " + id + " to permission " + permLevel);
    }

    private String getPerm(String id) {
        PermissionLevel level = getPermissionLevel(id);
        return "User `" + id + "` has a `" + level + "` permission.";
    }

    private PermissionLevel getPermissionLevel(String id) {
        JSONObject perm = MCBConfig.getJSONObject("permissions");
        if (containsID(perm.getJSONArray("level_3"), id))
            return PermissionLevel.LEVEL_3;
        else if (containsID(perm.getJSONArray("level_2"), id))
            return PermissionLevel.LEVEL_2;
        else if (containsID(perm.getJSONArray("level_1"), id))
            return PermissionLevel.LEVEL_1;
        else
            return PermissionLevel.LEVEL_0;
    }

    private boolean containsID(JSONArray str, String id) {
        for (Object s : str) {
            if (s.equals(id))
                return true;
        }
        return false;
    }

    private MessageEmbed getHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", MCBConfig.get("trigger") + " <get> <userID>", false);
        return eb.build();
    }

    private MessageEmbed addHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", MCBConfig.get("trigger") + " <add> <permLevel> <userID>", false);
        return eb.build();
    }

    private MessageEmbed removeHelp(MessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder(getInvalidHelpCard(e));
        eb.clearFields();
        eb.addField("Usage:", MCBConfig.get("trigger") + " <remove> <userID>", false);
        return eb.build();
    }

}
