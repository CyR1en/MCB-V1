package us.cyrien.MineCordBotV1.parse;

public class MultiLangMessageParser {

    public String parsePermsMessage(String message, String perm) {
        return message.replaceAll("\\{permission}", perm);
    }

    public String parsePlayer(String msg, String playerName) {
        return msg.replaceAll("\\{player}", playerName );
    }

    public String parse(String msg, String playerName, String killerName) {
        msg = msg.replaceAll("\\{player}", playerName);
        msg = msg.replaceAll("\\{killer}", killerName);
        return msg;
    }

}
