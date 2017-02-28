package us.cyrien.MineCordBotV1.entity;


import us.cyrien.MineCordBotV1.configuration.MineCordBotConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.permission.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class User{

    private MineCordBot mcb;

    private String name;
    private String nick;
    private String Id;
    private Permission.PermissionLevel permissionLevel;

    public User(MineCordBot mcb) {
        this.mcb = mcb;
    }

    public User setUser(MessageReceivedEvent e) {
        MineCordBotConfig cfg = mcb.getMcbConfig();
        name = e.getAuthor().getName();
        nick = e.getMember().getNickname();
        Id = e.getAuthor().getId();
        if(cfg.getPermLvl3().contains(getId()))
            permissionLevel = Permission.PermissionLevel.LEVEL_3;
        else if(cfg.getPermLvl2().contains(getId()))
            permissionLevel = Permission.PermissionLevel.LEVEL_2;
        else if(cfg.getPermLvl1().contains(getId()))
            permissionLevel = Permission.PermissionLevel.LEVEL_1;
        else
            permissionLevel = Permission.PermissionLevel.LEVEL_0;
        return this;
    }

    public String getName(){
        return name;
    }

    public String getNick() {
        return nick;
    }

    public String getId() {
        return Id;
    }

    public Permission.PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

}
