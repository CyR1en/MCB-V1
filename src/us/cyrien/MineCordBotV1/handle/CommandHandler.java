package us.cyrien.MineCordBotV1.handle;

import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.parse.CommandParser;

import java.util.HashMap;

public class CommandHandler {

    private MineCordBot mcb;

    public CommandHandler(MineCordBot mcb) {
        this.mcb = mcb;
    }

    public void handleCommand(CommandParser.CommandContainer cmd) {
        HashMap<String, DiscordCommand> commands = mcb.getDiscordCommands();
        if (commands.containsKey(cmd.invoke)) {
            commands.get(cmd.invoke).setSender(cmd.sender);
            if (commands.get(cmd.invoke).checkArguments(cmd.event, cmd.args)) {
                if (commands.get(cmd.invoke).hasPermission(cmd.args)) {
                    commands.get(cmd.invoke).execute(cmd.event, cmd.args);
                    commands.get(cmd.invoke).executed(cmd.event);
                } else
                    commands.get(cmd.invoke).sendNoPermMessage(cmd.event);
            }
        }
    }
}
