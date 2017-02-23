package us.cyrien.MineCordBotV1.listeners;

import us.cyrien.MineCordBotV1.entity.Messenger;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.ChatColor;

public class DiscordMessageListener extends ListenerAdapter {

    private MineCordBot mcb;
    private Messenger messenger;

    public DiscordMessageListener(MineCordBot mcb) {
        this.mcb = mcb;
        messenger = mcb.getMessenger();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        boolean execHead = !e.getMessage().getContent().startsWith(mcb.getMcbConfig().getTrigger());
        boolean notCommand = !e.getMessage().getContent().startsWith(mcb.getMcbConfig().getTrigger());
        boolean bound = mcb.getMcbConfig().containsChannel(e.getTextChannel().getId());
        boolean notSelf = !e.getMember().getUser().getId().equalsIgnoreCase(mcb.getJda().getSelfUser().getId());
        if (execHead & notCommand && bound && notSelf) {
            String msg = e.getMessage().getContent();
            String author = e.getMember().getEffectiveName();
            String prefix = mcb.getMcbConfig().getMinecraftPrefix();
            prefix = prefix.replaceAll("\\{sender}", author);
            messenger.sendGlobalMessageToMC(ChatColor.translateAlternateColorCodes('&', prefix + ": " + ChatColor.WHITE + msg));
            mcb.getLogger().info(author + " -> mc: " + msg);
        }
    }


}
