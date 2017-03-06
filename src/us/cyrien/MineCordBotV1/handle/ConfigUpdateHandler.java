package us.cyrien.MineCordBotV1.handle;

import us.cyrien.MineCordBotV1.configuration.MineCordBotConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigUpdateHandler {

    private final int SAVE = 0, LOAD = 1;

    private MineCordBotConfig mcbConfig;
    private MineCordBot mcb;

    private YamlConfiguration cfg;

    public ConfigUpdateHandler(MineCordBotConfig mcbConfig, MineCordBot mcb) {
        this.mcb = mcb;
        this.mcbConfig = mcbConfig;
        this.cfg = mcb.getPluginFile().getConfig();
    }

    public void updateConfig() {
        YamlConfiguration config = mcb.getPluginFile().getConfig();
        Set<String> keys = config.getKeys(true);
        preSaveConfig(keys);
        mcb.getPluginFile().renew(mcb, "MineCordBotConfig", true);
        restorePreSaved(keys);
        try {
            mcb.getPluginFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.set("CONFIG_VERSION", mcbConfig.CURRENT_CONFIG_VERSION);
        mcb.getMcbLogger().info("BotConfig.yml updated to version " + mcbConfig.CURRENT_CONFIG_VERSION);
    }

    public void preSaveConfig(Set<String> keys) {
        for (String s : keys)
            key(s, SAVE);
    }

    public void restorePreSaved(Set<String> keys) {
        for (String s : keys)
            key(s, LOAD);
    }

    private void key(String s, int mode) {
        // 0 = save mode
        // 1 = load mode
        YamlConfiguration config = mcb.getPluginFile().getConfig();
        switch (s) {
            case "bot_token":
                if (mode == SAVE)
                    mcbConfig.setBotToken(config.getString(s));
                else
                    config.set(s, mcbConfig.getBotToken());
                break;
            case "owner_id":
                if (mode == SAVE)
                    mcbConfig.setOwnerID(config.getString(s));
                else
                    config.set(s, mcbConfig.getOwnerID());
                break;
            case "client_id":
                if (mode == SAVE)
                    mcbConfig.setClientID(config.getString(s));
                else
                    config.set(s, mcbConfig.getClientID());
                break;
            case "auto_delete_command_response":
                if(mode == SAVE)
                    mcbConfig.setAutoDeleteCommandResponse(config.getBoolean(s));
                else
                    config.set(s, mcbConfig.isAutoDeleteCommandResponse());
            case "auto_update":
                if(mode == SAVE)
                    mcbConfig.setAutoUpdate(config.getBoolean(s));
                else
                    config.set(s, mcbConfig.isAutoUpdate());
            case "text_channels":
                if (mode == SAVE)
                    mcbConfig.setTextChannels((List<String>) config.getList(s));
                else
                    config.set(s, mcbConfig.getTextChannels());
                break;
            case "trigger":
                if (mode == SAVE)
                    mcbConfig.setTrigger(config.getString(s));
                else
                    config.set(s, mcbConfig.getTrigger());
                break;
            case "language":
                if (mode == SAVE)
                    mcbConfig.setTrigger(config.getString(s));
                else
                    config.set(s, mcbConfig.getLanguage());
                break;
            case "permissions.level_1":
                if (mode == SAVE)
                    mcbConfig.setPermLvl1((List<String>) config.getList(s));
                else
                    config.set(s, mcbConfig.getPermLvl1());
                break;
            case "permissions.level_2":
                if (mode == SAVE)
                    mcbConfig.setPermLvl2((List<String>) config.getList(s));
                else
                    config.set(s, mcbConfig.getPermLvl2());
                break;
            case "permissions.level_3":
                if (mode == SAVE)
                    mcbConfig.setPermLvl3((List<String>) config.getList(s));
                else
                    config.set(s, mcbConfig.getPermLvl3());
                break;
            case "message_prefix_discord":
                if (mode == SAVE)
                    mcbConfig.setDiscordPrefix(config.getString(s));
                else
                    config.set(s, mcbConfig.getDiscordPrefix());
                break;
            case "message_prefix_minecraft":
                if (mode == SAVE)
                    mcbConfig.setMinecraftPrefix(config.getString(s));
                else
                    config.set(s, mcbConfig.getMinecraftPrefix());
                break;
            case "broadcast.death_event":
                if (mode == SAVE)
                    mcbConfig.setDeathBroadCast(config.getBoolean(s));
                else
                    config.set(s, mcbConfig.isDeathBroadCast());
                break;
            case "broadcast.join_event":
                if (mode == SAVE)
                    mcbConfig.setJoinBroadCast(config.getBoolean(s));
                else
                    config.set(s, mcbConfig.isJoinBroadCast());
                break;
            case "broadcast.leave_event":
                if (mode == SAVE)
                    mcbConfig.setLeaveBroadCast(config.getBoolean(s));
                else
                    config.set(s, mcbConfig.isLeaveBroadCast());
                break;
            case "hide_user_with_incognito_perm":
                if(mode == SAVE)
                    mcbConfig.setHidePlayersWithPerms(config.getBoolean(s));
                else
                    config.set(s, mcbConfig.isHidePlayersWithPerms());
                break;
        }
    }
}
