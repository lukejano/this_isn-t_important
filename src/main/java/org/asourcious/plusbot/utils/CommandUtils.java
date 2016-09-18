package org.asourcious.plusbot.utils;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.apache.commons.lang3.StringUtils;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.CommandRegistry;

import java.util.ArrayList;
import java.util.List;

public final class CommandUtils {
    private CommandUtils(){}

    public static String getPrefixForMessage(PlusBot plusBot, Message message) {
        if (message.getRawContent().startsWith(message.getJDA().getSelfInfo().getAsMention()))
            return message.getJDA().getSelfInfo().getAsMention();

        List<String> prefixes = plusBot.getConfiguration().getPrefixesForGuild(((TextChannel) message.getChannel()).getGuild());

        return prefixes.stream().filter(prefix -> message.getContent().startsWith(prefix))
                .findFirst()
                .orElse(null);
    }

    public static List<String> getArgsForMessage(String message, String prefix) {
        String formattedMessage = message
                .substring(prefix.length())
                .replaceAll("(<(@(!|&)?|#)\\d+>)|(@everyone)|(@here)", "")
                .trim();
        if (StringUtils.isBlank(formattedMessage))
            return new ArrayList<>();

        ArrayList<String> args = new ArrayList<>();
        boolean isInQuote = false;
        StringBuilder currentArg = new StringBuilder();

        for (char ch : formattedMessage.toCharArray()) {
            if (Character.isWhitespace(ch) && !isInQuote) {
                args.add(currentArg.toString());
                currentArg.setLength(0);
            } else if (ch == '"') {
                isInQuote = !isInQuote;
                args.add(currentArg.toString());
                currentArg.setLength(0);
            } else {
                currentArg.append(ch);
            }
        }

        args.add(currentArg.toString());
        args.removeIf(StringUtils::isBlank);

        return args;
    }

    public static boolean isValidCommand(Message message, PlusBot plusBot) {
        String prefix = getPrefixForMessage(plusBot, message);
        if (prefix == null)
            return false;

        List<String> args = getArgsForMessage(message.getRawContent(), prefix);
        if (args.isEmpty())
            return false;

        String name = args.get(0);
        args.remove(0);

        return CommandRegistry.hasCommand(name)
                && CommandRegistry.getCommand(name).getCommand().checkArgs(args.toArray(new String[args.size()])) == null;
    }
}
