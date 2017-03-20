package us.cyrien.MineCordBotV1.commands.discordCommands;

import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PingCommand extends DiscordCommand {

    public PingCommand(MineCordBot mcb) {
        super(mcb, "Ping", CommandType.INFO, PermissionLevel.LEVEL_0);
        this.mcb = mcb;
        usage = getLanguage().getTranslatedMessage("mcb.commands.ping.usage");
        description = getLanguage().getTranslatedMessage("mcb.commands.ping.description");
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        return true;
    }

    @Override
    public void execute(MessageReceivedEvent e, String args[]) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        boolean isAutoDelete = MCBConfig.get("auto_delete_command_response");
        if (isAutoDelete)
            sendLatency(e, msg -> scheduler.schedule(() -> msg.delete().queue(), 20, TimeUnit.SECONDS));
        else
            sendLatency(e, null);
    }

    private void sendLatency(MessageReceivedEvent e, Consumer<Message> consumer) {
        getMessenger().sendMessage(e, "Pong...", msg ->
                msg.editMessage("Pong: `" + e.getMessage().getCreationTime().until(msg.getCreationTime(), ChronoUnit.MILLIS) + " ms`").queue(consumer));
    }

}
