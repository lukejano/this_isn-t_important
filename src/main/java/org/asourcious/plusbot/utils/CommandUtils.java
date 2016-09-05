package org.asourcious.plusbot.utils;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.CommandRegistry;
import org.asourcious.plusbot.managers.CommandManager;

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

    public static CommandManager.CommandContainer getArgsForMessage(String message, String prefix) {
        String formattedMessage = message
                .substring(prefix.length())
                .replaceAll("(<(@(!|&)?|#)\\d+>)|(@everyone)|(@here)", "")
                .trim();
        ArrayList<String> args = new ArrayList<>();
        boolean isInQuote = false;
        StringBuilder currentArg = new StringBuilder();

        for (char ch : formattedMessage.toCharArray()) {
            if (Character.isWhitespace(ch) && !isInQuote) {
                if (currentArg.length() > 0) {
                    args.add(currentArg.toString());
                    currentArg = new StringBuilder();
                }
            } else if (ch == '"') {
                isInQuote = !isInQuote;
                if (currentArg.length() > 0) {
                    args.add(currentArg.toString());
                    currentArg = new StringBuilder();
                }
            } else {
                currentArg.append(ch);
            }
        }
        String name = null;
        if (currentArg.length() > 0) {
            args.add(currentArg.toString());
            name = args.get(0);
            args.remove(0);
        }

        return new CommandManager.CommandContainer(name, args.toArray(new String[args.size()]));
    }

    public static boolean isValidCommand(Message message, PlusBot plusBot) {
        String prefix = CommandUtils.getPrefixForMessage(plusBot, message);
        if (prefix == null)
            return false;

        CommandManager.CommandContainer commandContainer = CommandUtils.getArgsForMessage(message.getRawContent(), prefix);

        return CommandRegistry.hasCommand(commandContainer.name)
                && CommandRegistry.getCommand(commandContainer.name).getCommand().checkArgs(commandContainer.args) == null;
    }
}
