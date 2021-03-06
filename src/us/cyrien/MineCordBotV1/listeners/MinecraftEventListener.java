package us.cyrien.MineCordBotV1.listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.ChatColor;
import org.bukkit.event.EventPriority;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.entity.Messenger;
import us.cyrien.MineCordBotV1.handle.MinecraftMentionHandler;
import us.cyrien.MineCordBotV1.main.Language;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.parse.MultiLangMessageParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;

public class MinecraftEventListener implements Listener {

    private MineCordBot mcb;
    private Messenger messenger;
    private Language language;
    private MultiLangMessageParser langMessageParser;
    private MinecraftMentionHandler mentionHandler;

    public MinecraftEventListener(MineCordBot mcb) {
        this.mcb = mcb;
        messenger = mcb.getMessenger();
        language = mcb.getLanguage();
        langMessageParser = new MultiLangMessageParser();
        mentionHandler = new MinecraftMentionHandler(mcb);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if(!e.isCancelled()) {
            String msg = mentionHandler.handleMention(e.getMessage());
            String prefix = MCBConfig.get("message_prefix_discord");
            String author = ChatColor.stripColor(e.getPlayer().getName());
            prefix = prefix.replaceAll("\\{sender}", author);
            prefix = prefix.replaceAll("\\{world}", e.getPlayer().getWorld().getName());
            messenger.sendMessageToAllBoundChannel("**" + prefix + ":** " + msg);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        boolean isJoinBroadCast = MCBConfig.getJSONObject("broadcasts").getBoolean("join_event");
        if (!e.getPlayer().hasPermission("minecordbot.incognito") || isJoinBroadCast) {
            String msg = language.getTranslatedMessage("mc.message.login");
            messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(new Color(92, 184, 92)).setTitle(langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), null).build());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        boolean isLeaveBroadCast = MCBConfig.getJSONObject("broadcasts").getBoolean("leave_event");
        if (!e.getPlayer().hasPermission("minecordbot.incognito") || isLeaveBroadCast) {
            String msg = language.getTranslatedMessage("mc.message.logout");
            messenger.sendMessageEmbedToAllBoundChannel(new EmbedBuilder().setColor(new Color(243, 119, 54)).setTitle(langMessageParser.parsePlayer(msg, ChatColor.stripColor(e.getPlayer().getName())), null).build());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean isDeathBroadCast = MCBConfig.getJSONObject("broadcasts").getBoolean("death_event");
        if(isDeathBroadCast) {
            Player player = event.getEntity();
            EntityDamageEvent.DamageCause deathCause = player.getLastDamageCause().getCause();
            if (player.getKiller() != null) {
                String msg = language.getTranslatedMessage("mc.deaths.pvp");
                msg = langMessageParser.parse(msg, player.getDisplayName(), deathCause.name());
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("wolf")) {
                String msg = language.getTranslatedMessage("mc.mobs.wolf");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("ocelot")) {
                String msg = language.getTranslatedMessage("mc.mobs.ocelot");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("pigman")) {
                String msg = language.getTranslatedMessage("mc.mobs.pigman");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("zombie")) {
                String msg = language.getTranslatedMessage("mc.mobs.zombie");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("skeleton")) {
                String msg = language.getTranslatedMessage("mc.mobs.skeleton");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("cave spider")) {
                String msg = language.getTranslatedMessage("mc.mobs.cavespider");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("spider")) {
                String msg = language.getTranslatedMessage("mc.mobs.spider");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("silverfish")) {
                String msg = language.getTranslatedMessage("mc.mobs.silverfish");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("slime")) {
                String msg = language.getTranslatedMessage("mc.mobs.slime");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("blew up")) {
                String msg = language.getTranslatedMessage("mc.mobs.creeper");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("enderman")) {
                String msg = language.getTranslatedMessage("mc.mobs.enderman");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("ghast")) {
                String msg = language.getTranslatedMessage("mc.mobs.ghast");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("blaze")) {
                String msg = language.getTranslatedMessage("mc.mobs.blaze");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("ender dragon")) {
                String msg = language.getTranslatedMessage("mc.mobs.enderdragon");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("wither skeleton")) {
                String msg = language.getTranslatedMessage("mc.mobs.witherskeleton");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("wither")) {
                String msg = language.getTranslatedMessage("mc.mobs.wither");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (event.getDeathMessage().toLowerCase().contains("anvil")) {
                String msg = language.getTranslatedMessage("mc.deaths.anvil");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.DROWNING) {
                String msg = language.getTranslatedMessage("mc.deaths.drowning");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.SUFFOCATION) {
                String msg = language.getTranslatedMessage("mc.deaths.suffocation");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.SUICIDE) {
                String msg = language.getTranslatedMessage("mc.deaths.suicide");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.FALL) {
                String msg = language.getTranslatedMessage("mc.deaths.fall");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.VOID) {
                String msg = language.getTranslatedMessage("mc.deaths.vod");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.LAVA) {
                String msg = language.getTranslatedMessage("mc.deaths.lava");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.FIRE) {
                String msg = language.getTranslatedMessage("mc.deaths.fire");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.CONTACT) {
                String msg = language.getTranslatedMessage("mc.deaths.cactus");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else if (deathCause == EntityDamageEvent.DamageCause.WITHER) {
                String msg = language.getTranslatedMessage("mc.deaths.withers");
                msg = langMessageParser.parsePlayer(msg, ChatColor.stripColor(player.getName()));
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            } else {
                String msg = ChatColor.stripColor(event.getDeathMessage());
                messenger.sendMessageToAllBoundChannel("```css" + "\n[" + msg + "]\n```");
            }
        }
    }
}

