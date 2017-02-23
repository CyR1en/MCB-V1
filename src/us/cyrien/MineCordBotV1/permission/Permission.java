package us.cyrien.MineCordBotV1.permission;

public interface Permission {

    public static int LEVEL_0 = 0, LEVEL_1 = 1, LEVEL_2 = 2, LEVEL_3 = 3;

    boolean hasPermission(String[] args);

    String noPermissionMessage();
}