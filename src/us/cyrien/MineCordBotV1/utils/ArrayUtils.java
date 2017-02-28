package us.cyrien.MineCordBotV1.utils;

/**
 * Created by Cyrien on 2/28/2017.
 */
public class ArrayUtils {

    public static String concatenateArgs(String[] args) {
        String out = "";
        for (int i = 0; i < args.length; i++)
            out += " " + args[i];
        return out.trim();
    }
}
