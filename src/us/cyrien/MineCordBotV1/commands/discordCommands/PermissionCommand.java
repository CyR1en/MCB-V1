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
        commandType = MOD;
        commandPermissionLevel = LEVEL_2;
        description = getLanguage().getTranslatedMessage("mcb.commands.permission.description");
        usage = getLanguage().getTranslatedMessage("mcb.commands.permission.usage");
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        System.out.println(args.length + " args length");
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

        List<String> pl = (List<String>) mcb.getPluginFile().getConfig().getList("Permissons.level_" + permLevel);
            /*
        if(pl == null)
            mcb.getPluginFile().getConfig().createSection("Permissons.level_" + permLevel);
            */
        Member member = e.getGuild().getMember(e.getJDA().getUserById(id));
        if (member != null) {
            pl.add(member.getUser().getId());
            mcb.getPluginFile().getConfig().set("Permissions.level_" + permLevel, pl);
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
        sendMessage(e, "User `" + id + "` now have Level_" + permLevel + " Permission.", 10);
        mcb.getMcbLogger().info("Added User " + id + " to permissionLevel_" + permLevel);
    }

    private void removePerm(String id, MessageReceivedEvent e) {
        User user = e.getJDA().getUserById(id);
        if (getPermissionLevel(user.getId(), e) == LEVEL_0) {
            sendMessage(e, "No permission to be removed from `" + user.getId() + "`", 20);
            return;
        }
        int permLevel = getPermissionLevel(user.getId(), e);
        List<String> pl = (List<String>) mcb.getPluginFile().getConfig().getList("Permissons.level_" + permLevel);

        mcb.getPluginFile().getConfig().set("Permissions.level_" + permLevel, pl);
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
        sendMessage(e, "Removed Level_" + permLevel + " permission from `" + user.getId() + "`", 10);
        mcb.getMcbLogger().info("Added User " + id + " to permission Level_" + permLevel);
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
        int level = getPermissionLevel(id, e);
        return "User `" + id + "` have a `Level_" + level + "` permission.";
    }

    private int getPermissionLevel(String id, MessageReceivedEvent e) {
        MineCordBotConfig cfg = mcb.getMcbConfig();
        if (cfg.getPermLvl3().contains(id))
            return 3;
        else if (cfg.getPermLvl2().contains(id))
            return 2;
        else if (cfg.getPermLvl1().contains(id))
            return 1;
        else
            return 0;
    }

    @Override
    public void executed(MessageReceivedEvent e) {

    }

    @Override
    public void logCommand(MessageReceivedEvent e) {

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
