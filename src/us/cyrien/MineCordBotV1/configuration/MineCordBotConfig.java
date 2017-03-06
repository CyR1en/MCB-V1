package us.cyrien.MineCordBotV1.configuration;

import us.cyrien.MineCordBotV1.handle.ConfigUpdateHandler;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MineCordBotConfig {
    public final String CURRENT_CONFIG_VERSION = "0.0.4";
    private MineCordBot mcb;

    private String configVersion;
    private String botToken;
    private String clientID;
    private String ownerID;
    private String trigger;
    private String language;
    private boolean autoDeleteCommandResponse;
    private boolean autoUpdate;
    private List<String> textChannels;
    private List<String> permLvl1;
    private List<String> permLvl2;
    private List<String> permLvl3;
    private String discordPrefix;
    private String minecraftPrefix;
    private boolean deathBroadCast;
    private boolean leaveBroadCast;
    private boolean joinBroadCast;
    private boolean hidePlayersWithPerms;

    private YamlConfiguration configFile;

    private ConfigUpdateHandler cfgUpdateHandler;

    public MineCordBotConfig(MineCordBot mcb) {
        this.mcb = mcb;
        configFile = mcb.getPluginFile().getConfig();
        init();
        cfgUpdateHandler = new ConfigUpdateHandler(this, mcb);
        if (!configFile.getKeys(false).contains("CONFIG_VERSION") || !configFile.getString("CONFIG_VERSION").equals(CURRENT_CONFIG_VERSION)) {
            mcb.getMcbLogger().warning("Outdated Config file. Updating BotConfig.yml....");
            cfgUpdateHandler.updateConfig();
            try {
                reload();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        configVersion = configFile.getString("CONFIG_VERSION");
        botToken = configFile.getString("bot_token");
        ownerID = configFile.getString("owner_id");
        clientID = configFile.getString("client_id");
        trigger = configFile.getString("trigger");
        language = configFile.getString("language");
        autoDeleteCommandResponse = configFile.getBoolean("auto_delete_command_response");
        autoUpdate = configFile.getBoolean("auto_update");
        textChannels = (ArrayList<String>) configFile.getList("text_channels");
        permLvl1 = (ArrayList<String>) configFile.getList("Permissions.level_1");
        permLvl2 = (ArrayList<String>) configFile.getList("Permissions.level_2");
        permLvl3 = (ArrayList<String>) configFile.getList("Permissions.level_3");
        discordPrefix = configFile.getString("message_prefix_discord");
        minecraftPrefix = configFile.getString("message_prefix_minecraft");
        deathBroadCast = configFile.getBoolean("broadcast.death_event");
        leaveBroadCast = configFile.getBoolean("broadcast.leave_event");
        joinBroadCast = configFile.getBoolean("broadcast.join_event");
        hidePlayersWithPerms = configFile.getBoolean("hide_with_incognito_perms");
    }

    public void reload() throws IOException {
        mcb.getPluginFile().reload();
        init();
        mcb.getPluginFile().save();
    }

    public boolean containsChannel(String id) {
        for(String s : textChannels)
            if(s.equalsIgnoreCase(id))
                return true;
        return false;
    }

    public MineCordBot getMcb() {
        return mcb;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getClientID() {
        return clientID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isAutoDeleteCommandResponse() {
        return autoDeleteCommandResponse;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public List<String> getTextChannels() {
        return textChannels;
    }

    public List<String> getPermLvl1() {
        return permLvl1;
    }

    public List<String> getPermLvl2() {
        return permLvl2;
    }

    public List<String> getPermLvl3() {
        return permLvl3;
    }

    public String getDiscordPrefix() {
        return discordPrefix;
    }

    public String getMinecraftPrefix() {
        return minecraftPrefix;
    }

    public boolean isDeathBroadCast() {
        return deathBroadCast;
    }

    public boolean isLeaveBroadCast() {
        return leaveBroadCast;
    }

    public boolean isJoinBroadCast() {
        return joinBroadCast;
    }

    public boolean isHidePlayersWithPerms() {
        return hidePlayersWithPerms;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public void setAutoDeleteCommandResponse(boolean autoDeleteCommandResponse) {
        this.autoDeleteCommandResponse = autoDeleteCommandResponse;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public void setTextChannels(List<String> textChannels) {
        this.textChannels = textChannels;
    }

    public void setPermLvl1(List<String> permLvl1) {
        this.permLvl1 = permLvl1;
    }

    public void setPermLvl2(List<String> permLvl2) {
        this.permLvl2 = permLvl2;
    }

    public void setPermLvl3(List<String> permLvl3) {
        this.permLvl3 = permLvl3;
    }

    public void setDiscordPrefix(String discordPrefix) {
        this.discordPrefix = discordPrefix;
    }

    public void setMinecraftPrefix(String minecraftPrefix) {
        this.minecraftPrefix = minecraftPrefix;
    }

    public void setDeathBroadCast(boolean deathBroadCast) {
        this.deathBroadCast = deathBroadCast;
    }

    public void setLeaveBroadCast(boolean leaveBroadCast) {
        this.leaveBroadCast = leaveBroadCast;
    }

    public void setJoinBroadCast(boolean joinBroadCast) {
        this.joinBroadCast = joinBroadCast;
    }

    public void setHidePlayersWithPerms(boolean hidePlayersWithPerms) {
        this.hidePlayersWithPerms = hidePlayersWithPerms;
    }
}
