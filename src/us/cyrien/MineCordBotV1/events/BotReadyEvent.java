package us.cyrien.MineCordBotV1.events;

import us.cyrien.MineCordBotV1.entity.Messenger;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotReadyEvent extends ListenerAdapter {

    private MineCordBot mcb;
    private Messenger messenger;
    public BotReadyEvent(MineCordBot mcb) {
        this.mcb = mcb;
        this.messenger = mcb.getMessenger();
    }

    @Override
    public void onReady(ReadyEvent event) {
        mcb.getMcbLogger().info("MineCordBot Enabled");
    }
}
