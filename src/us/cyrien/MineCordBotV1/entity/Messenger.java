package us.cyrien.MineCordBotV1.entity;

import org.json.JSONArray;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Messenger {

    private MineCordBot mcb;

    private ScheduledExecutorService scheduler;

    public Messenger(MineCordBot mcb) {
        scheduler = Executors.newScheduledThreadPool(1);
        this.mcb = mcb;
    }

    //To Minecraft
    public void sendGlobalMessageToMC(String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers())
            p.sendMessage(message);
    }

    //To Discord
    public void sendMessage(MessageReceivedEvent e, String message, Consumer<Message> consumer) {
        e.getTextChannel().sendMessage(message).queue(consumer);
    }

    public void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me, Consumer<Message> consumer) {
        e.getTextChannel().sendMessage(me).queue(consumer);
    }

    public void sendMessageEmbed(MessageReceivedEvent e, MessageEmbed me) {
        sendMessageEmbed(e, me, null);
    }

    public void sendMessage(MessageReceivedEvent e, String message) {
        sendMessage(e, message, null);
    }

    public void sendTempMessage(MessageReceivedEvent e, String message, int length) {
        e.getTextChannel().sendMessage(message).queue(msg -> scheduler.schedule(() -> msg.delete().queue(), length, TimeUnit.SECONDS));
    }

    public void sendTempMessageEmbed(MessageReceivedEvent e, MessageEmbed me, int length) {
        e.getTextChannel().sendMessage(me).queue(msg -> scheduler.schedule(() -> msg.delete().queue(), length, TimeUnit.SECONDS));
    }

    public void sendMessageToAllBoundChannel(String message) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        assert tcArray != null;
        for (Object s : tcArray) {
            if (mcb.getJda().getTextChannelById(s.toString()) != null)
                mcb.getJda().getTextChannelById(s.toString()).sendMessage(message).queue();
        }
    }

    public void sendMessageEmbedToAllBoundChannel(MessageEmbed messageEmbed) {
        JSONArray tcArray = MCBConfig.get("text_channels");
        assert tcArray != null;
        for (Object s : tcArray) {
            if (mcb.getJda().getTextChannelById(s.toString()) != null)
                mcb.getJda().getTextChannelById(s.toString()).sendMessage(messageEmbed).queue();
        }
    }

}