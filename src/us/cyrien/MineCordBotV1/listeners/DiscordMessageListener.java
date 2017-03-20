package us.cyrien.MineCordBotV1.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.ChatColor;
import org.json.JSONArray;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.entity.Messenger;
import us.cyrien.MineCordBotV1.main.MineCordBot;

public class DiscordMessageListener extends ListenerAdapter {

    private MineCordBot mcb;
    private Messenger messenger;

    public DiscordMessageListener(MineCordBot mcb) {
        this.mcb = mcb;
        messenger = mcb.getMessenger();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        boolean execHead = !e.getMessage().getContent().startsWith(MCBConfig.get("trigger"));
        boolean notCommand = !e.getMessage().getContent().startsWith(MCBConfig.get("trigger"));
        boolean bound = containsChannel(e.getTextChannel().getId());
        boolean notSelf = !e.getMember().getUser().getId().equalsIgnoreCase(mcb.getJda().getSelfUser().getId());
        if (execHead & notCommand && bound && notSelf) {
            String msg = e.getMessage().getContent();
            String author = e.getMember().getEffectiveName();
            String prefix = MCBConfig.get("message_prefix_minecraft");
            prefix = prefix.replaceAll("\\{sender}", author);
            prefix = prefix.replaceAll("\\{guild}", e.getGuild().getName());
            prefix = prefix.replaceAll("\\{text channel}", e.getTextChannel().getName());
            messenger.sendGlobalMessageToMC(ChatColor.translateAlternateColorCodes('&', prefix + "&r: " + ChatColor.WHITE + msg));
        }
    }

    public boolean containsChannel(String id) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        for(Object s : tcArray)
            if(s.toString().equalsIgnoreCase(id))
                return true;
        return false;
    }

}
