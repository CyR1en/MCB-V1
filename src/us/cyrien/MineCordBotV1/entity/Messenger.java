package us.cyrien.MineCordBotV1.entity;

import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.parse.MultiLangMessageParser;
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
    private MultiLangMessageParser langMessageParser;

    private ScheduledExecutorService scheduler;

    public Messenger(MineCordBot mcb) {
        scheduler = Executors.newScheduledThreadPool(1);
        this.mcb = mcb;
        langMessageParser = new MultiLangMessageParser();
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
        e.getTextChannel().sendMessage(message).queue(msg -> scheduler.schedule(() -> msg.deleteMessage().queue(), length, TimeUnit.SECONDS));
    }

    public void sendTempMessageEmbed(MessageReceivedEvent e, MessageEmbed me, int length) {
        e.getTextChannel().sendMessage(me).queue(msg -> scheduler.schedule(() -> msg.deleteMessage().queue(), length, TimeUnit.SECONDS));
    }

    public void sendMessageToAllBoundChannel(String message) {
        for (String s : mcb.getMcbConfig().getTextChannels())
            mcb.getJda().getTextChannelById(s).sendMessage(message).queue();
    }

    public void sendMessageEmbedToAllBoundChannel(MessageEmbed messageEmbed) {
        for(String s : mcb.getMcbConfig().getTextChannels())
            mcb.getJda().getTextChannelById(s).sendMessage(messageEmbed).queue();
    }

}