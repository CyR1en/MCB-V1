package us.cyrien.MineCordBotV1.main;

import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.commands.discordCommands.*;
import us.cyrien.MineCordBotV1.commands.minecraftCommands.Dcmd;
import us.cyrien.MineCordBotV1.commands.minecraftCommands.Dme;
import us.cyrien.MineCordBotV1.commands.minecraftCommands.MReloadCommand;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.configuration.LocalizationFile;
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
import java.util.HashMap;
import java.util.logging.Logger;

public class MineCordBot extends JavaPlugin {

    public static final String CURRENT_VERSION = "MineCordBot v1.0.2";
    public static MineCordBot mcb;

    public static JDA jda;
    private HashMap<String, DiscordCommand> commands;
    private Messenger messenger;
    private Language language;
    private Logger mcbLogger;
    private LocalizationFile pluginFile;
    private UpTimer upTimer;
    private Updater updater;
    private Metrics metrics;

    @Override
    public void onEnable() {
        mcbLogger = this.getLogger();
        boolean initialized = MCBConfig.load();
        if(!initialized) {
            mcbLogger.info("========== [MineCordBot] PLEASE POPULATE YOUR CONFIGURATION ==========");
        } else {
            commands = new HashMap<>();
            pluginFile = new LocalizationFile(this, true);
            language = new Language(this, MCBConfig.get("localization"), pluginFile);
            messenger = new Messenger(this);
            upTimer = new UpTimer();
            metrics = new Metrics(this);
            discordInitialization();
            minecraftInitialization();
            if (MCBConfig.get("auto_update"))
                updater = new Updater(this, 101682, this.getFile(), Updater.UpdateType.DEFAULT, false);
            else
                updater = new Updater(this, 101682, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, true);
        }
    }

    private void discordInitialization() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(MCBConfig.get("bot_token")).buildAsync();
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
        registerCommand("reload", new ReloadCommand(this));
    }

    private void registerCommand(String name, DiscordCommand discordCommand) {
        commands.put(name, discordCommand);
    }

    private void minecraftInitialization() {
        this.getCommand("minecordbot").setExecutor(new MReloadCommand(this));
        this.getCommand("dcmd").setExecutor(new Dcmd(this));
        this.getCommand("dme").setExecutor(new Dme(this));
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