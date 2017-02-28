package us.cyrien.MineCordBotV1.permission;

public interface Permission {

    public enum PermissionLevel { LEVEL_0, LEVEL_1 , LEVEL_2 , LEVEL_3 }

    boolean hasPermission(String[] args);

    String noPermissionMessage();
}