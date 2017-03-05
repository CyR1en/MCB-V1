package us.cyrien.MineCordBotV1.main;

import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.commands.discordCommands.*;
import us.cyrien.MineCordBotV1.commands.minecraftCommands.MReloadCommand;
import us.cyrien.MineCordBotV1.configuration.MineCordBotConfig;
import us.cyrien.MineCordBotV1.configuration.PluginFile;
import us.cyrien.MineCordBotV1.entity.Messenger;
import us.cyrien.MineCordBotV1.entity.UpTimer;
import us.cyrien.MineCordBotV1.events.BotReadyEvent;
import us.cyrien.MineCordBotV1.handle.Metrics;
import us.cyrien.MineCordBotV1.handle.Updater;
import us.cyrien.MineCordBotV1.listeners.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.bukkit.plugin.java.JavaPlugin;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class MineCordBot extends JavaPlugin {

    public static final String CURRENT_VERSION = "MineCordBot v1.0.2";
    public static MineCordBot mcb;

    private JDA jda;
    private HashMap<String, DiscordCommand> commands;
    private PluginFile pluginFile;
    private MineCordBotConfig mcbConfig;
    private Messenger messenger;
    private Language language;
    private Logger mcbLogger;
    private UpTimer upTimer;
    private Updater updater;
    private Metrics metrics;

    @Override
    public void onEnable() {
        mcbLogger = this.getLogger();
        commands = new HashMap<>();
        pluginFile = new PluginFile(this, "MineCordBotConfig", true);
        mcbConfig = new MineCordBotConfig(this);
        language = new Language(this, mcbConfig.getLanguage(), pluginFile);
        messenger = new Messenger(this);
        upTimer = new UpTimer();
        metrics = new Metrics(this);
        discordInitialization();
        minecraftInitialization();
        if(mcbConfig.isAutoUpdate())
             updater = new Updater(this, 101682, this.getFile(), Updater.UpdateType.DEFAULT, false);
        else
             updater = new Updater(this,101682, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, true);
    }

    public void discordInitialization() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(mcbConfig.getBotToken()).buildAsync();
            jda.addEventListener(new CommandListener(this));
            jda.addEventListener(new BotReadyEvent(this));
            jda.addEventListener(new DiscordMessageListener(this));
            jda.setAutoReconnect(true);
        } catch (LoginException e) {
            e.printStackTrace();
            System.err.println("Could not log in");
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }
        registerCommand("ping", new PingCommand(this));
        registerCommand("help", new HelpCommand(this));
        registerCommand("list", new ListCommand(this));
        registerCommand("info", new InfoCommand(this));
        registerCommand("textchannel", new TextChannelCommand(this));
        registerCommand("setusername", new SetUsernameCommand(this));
        registerCommand("setnickname", new SetNicknameCommand(this));
        registerCommand("setavatar", new SetAvatarCommand(this));
        registerCommand("setgame", new SetGameCommand(this));
        registerCommand("perm", new PermissionCommand(this));
        registerCommand("eval", new EvalCommand(this));
        registerCommand("mcmd", new SendMinecraftCommand(this));
    }

    public void registerCommand(String name, DiscordCommand discordCommand) {
        commands.put(name, discordCommand);
    }

    public void minecraftInitialization() {
        this.getCommand("minecordbot").setExecutor(new MReloadCommand(this));
        getServer().getPluginManager().registerEvents(new MinecraftEventListener(this), this);
        getServer().getPluginManager().registerEvents(new TabCompleteV2(this), this);
    }

    public static MineCordBot getMcb() {
        return mcb;
    }

    public JDA getJda() {
        return jda;
    }

    public Language getLanguage() {
        return language;
    }

    public HashMap<String, DiscordCommand> getDiscordCommands() {
        return commands;
    }

    public PluginFile getPluginFile() {
        return pluginFile;
    }

    public MineCordBotConfig getMcbConfig() {
        return mcbConfig;
    }

    public Messenger getMessenger() {
        return messenger;
    }


    public UpTimer getUpTimer() {
        return upTimer;
    }


    public Logger getMcbLogger() {
        return mcbLogger;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}