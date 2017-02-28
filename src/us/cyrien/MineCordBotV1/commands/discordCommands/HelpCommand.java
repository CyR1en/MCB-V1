package us.cyrien.MineCordBotV1.commands.discordCommands;

import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HelpCommand extends DiscordCommand {

    private HashMap<String, DiscordCommand> discordCommands;

    public HelpCommand(MineCordBot mcb) {
        super(mcb, "Help");
        usage = getLanguage().getTranslatedMessage("mcb.commands.help.usage");
        description = getLanguage().getTranslatedMessage("mcb.commands.help.description");
        commandType = CommandType.HELP;
        discordCommands = mcb.getDiscordCommands();
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if(args.length == 0)
            return true;
        if(args.length > 2) {
            String msg = "```\n" + invalidArgumentsMessage();
            msg += getLanguage().getTranslatedMessage("mcb.commands.help.to-much-args");
            msg += usage + "```";
            getMessenger().sendMessage(e, msg);
            return false;
        } if(args.length == 1 && discordCommands.containsKey(args[0]))
            return true;
        else {
            String msg = "```\n" + invalidArgumentsMessage();
            msg += "\n" +getLanguage().getTranslatedMessage("mcb.commands.help.no-such-command");
            msg += usage + "```";
            getMessenger().sendMessage(e, msg);
            return false;
        }
    }

    @Override
    public void execute(MessageReceivedEvent e, String args[]) {
        EmbedBuilder eb = new EmbedBuilder().setColor(e.getGuild().getMember(e.getJDA().getSelfUser()).getColor());
        if(args.length == 0)
            sendMessageEmbed(e, listCommands(eb), 55);
        else {
            DiscordCommand cmd = discordCommands.get(args[0]);
            sendMessageEmbed(e, cmd.getHelpCard(e), HELPCARD_DURATION);
        }
    }

    private MessageEmbed listCommands(EmbedBuilder ebi) {
        ebi.setTitle("-- " + getLanguage().getTranslatedMessage("mcb.commands.help.list.header") + " --");
        for(int i = 0; i <= 4; i++) {
            StringBuilder str = new StringBuilder();
            for (Map.Entry<String, DiscordCommand> entry : getAllCommandsOf(CommandType.values()[i]).entrySet()) {
                str.append(trigger).append(entry.getKey()).append(" - ").append(entry.getValue().getDescription()).append("\n");
            }
            ebi.addField(getCommandTypeToString(CommandType.values()[i]), str.toString(), false);
        }
        return ebi.build();
    }

    private Map<String, DiscordCommand> getAllCommandsOf(CommandType commandType) {
        Map<String, DiscordCommand> temp = new HashMap<>();
        for(HashMap.Entry<String, DiscordCommand> entry : discordCommands.entrySet())
            if (entry.getValue().getCommandType() == commandType)
                temp.put(entry.getKey(), entry.getValue());
        temp = new TreeMap<>(temp);
        return temp;
    }

}