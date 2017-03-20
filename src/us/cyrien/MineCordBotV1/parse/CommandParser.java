package us.cyrien.MineCordBotV1.parse;

import us.cyrien.MineCordBotV1.configuration.MCBConfig;
import us.cyrien.MineCordBotV1.entity.User;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class CommandParser {

    private MineCordBot mcb;

    public CommandParser(MineCordBot mcb) {
        this.mcb = mcb;
    }

    public CommandContainer parse(String rw, MessageReceivedEvent e) {
        ArrayList<String> split = new ArrayList<>();
        String raw = rw;
        String beheaded = raw.replaceFirst(MCBConfig.get("trigger"), "");
        String[] splitBeheaded = beheaded.split(" ");
        for (String s : splitBeheaded)
            split.add(s);
        String invoke = split.get(0).toLowerCase();
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);
        User sender = new User(mcb).setUser(e);
        return new CommandContainer(raw, beheaded, splitBeheaded, invoke, args, e, sender);
    }

    public class CommandContainer {
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;
        public final User sender;

        public CommandContainer(String raw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent event, User sender) {
            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
            this.sender = sender;
        }
    }
}
