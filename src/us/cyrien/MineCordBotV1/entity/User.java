package us.cyrien.MineCordBotV1.entity;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.permission.Permission;

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
        name = e.getAuthor().getName();
        nick = e.getMember().getNickname();
        Id = e.getAuthor().getId();
        permissionLevel = getPermLevel();
        return this;
    }

    public Permission.PermissionLevel getPermLevel() {
        JSONObject perms = MCBConfig.getJSONObject("permissions");
        for(int i = 1; i <= 3; i++) {
            for(Object s : perms.getJSONArray("level_" + i)) {
                if(s.toString().equalsIgnoreCase(getId()))
                    switch (i) {
                        case 1:
                            return Permission.PermissionLevel.LEVEL_1;
                        case 2:
                            return Permission.PermissionLevel.LEVEL_2;
                        case 3:
                            return Permission.PermissionLevel.LEVEL_3;
                    }
            }
        }
        return Permission.PermissionLevel.LEVEL_0;
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
