package us.cyrien.MineCordBotV1.listeners;

import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.handle.CommandHandler;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.parse.CommandParser;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter{

    private MineCordBot mcb;
    private CommandHandler cmdHandler;
    private CommandParser cmdParser;

    public CommandListener(MineCordBot mcb) {
        this.mcb = mcb;
        cmdParser = new CommandParser(mcb);
        cmdHandler = new CommandHandler(mcb);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        boolean execHead = event.getMessage().getContent().startsWith(MCBConfig.get("trigger"));
        boolean notSelf = !event.getMember().getUser().getId().equalsIgnoreCase(mcb.getJda().getSelfUser().getId());
        if(execHead && notSelf)
            cmdHandler.handleCommand(cmdParser.parse(event.getMessage().getContent(), event));
    }
}
