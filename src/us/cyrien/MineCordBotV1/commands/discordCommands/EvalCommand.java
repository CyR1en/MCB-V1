package us.cyrien.MineCordBotV1.commands.discordCommands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.mariuszgromada.math.mxparser.Expression;
import us.cyrien.MineCordBotV1.commands.DiscordCommand;
import us.cyrien.MineCordBotV1.main.MineCordBot;
import us.cyrien.MineCordBotV1.utils.ArrayUtils;

import java.awt.*;

public class EvalCommand extends DiscordCommand {

    private EmbedBuilder eb;

    public EvalCommand (MineCordBot mcb) {
        super(mcb, "Math Evaluator", CommandType.FUN, PermissionLevel.LEVEL_0);
        usage = getLanguage().getTranslatedMessage("mcb.commands.eval.usage");
        description = getLanguage().getTranslatedMessage("mcb.commands.eval.description");
        eb = new EmbedBuilder().setTitle("Math Evaluator", null);
    }

    @Override
    public boolean checkArguments(MessageReceivedEvent e, String[] args) {
        if(args.length == 0) {
            sendMessageEmbed(e, getInvalidHelpCard(e), HELPCARD_DURATION);
            return false;
        } else {
            String expression = ArrayUtils.concatenateArgs(args);
            Expression ex = new Expression(expression);
            boolean correctSyntax = ex.checkSyntax();
            if(!correctSyntax) {
                eb.setColor(new Color(217,83,79));
                eb.addField("SYNTAX ERROR", ex.getErrorMessage(), false);
                sendMessageEmbed(e, eb.build(), 120);
                return true;
            }
        }
        return true;
    }

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        Expression ex = new Expression(ArrayUtils.concatenateArgs(args));
        EmbedBuilder eb = new EmbedBuilder().setTitle("Math Evaluator", null);
        eb.setColor(e.getGuild().getMemberById(e.getJDA().getSelfUser().getId()).getColor());
        eb.addField("Result", "(" + ex.getExpressionString() + ") = " + ex.calculate(), false);
        sendMessageEmbed(e, eb.build(), 25);
    }
}
