package us.cyrien.MineCordBotV1.utils;

public class ArrayUtils {

    public static String concatenateArgs(String[] args) {
        String out = "";
        for (String arg : args) out += " " + arg;
        return out.trim();
    }
}
