package org.asourcious.plusbot.managers;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.utils.PermissionUtil;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.commands.CommandRegistry;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.CommandUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandManager {

    private PlusBot plusBot;
    private ExecutorService executorService;

    public CommandManager(PlusBot plusBot) {
        this.plusBot = plusBot;
        this.executorService = Executors.newFixedThreadPool(100);
    }

    public void parseMessage(TextChannel channel, Message message) {
        String prefix = CommandUtils.getPrefixForMessage(plusBot, message);

        if (prefix == null)
            return;
        if (plusBot.getConfiguration().getBlacklist(channel.getGuild()).contains(message.getAuthor().getId()))
            return;

        List<String> args = CommandUtils.getArgsForMessage(message.getRawContent(), prefix);
        if (args.isEmpty())
            return;

        String name = args.get(0);
        args.remove(0);

        if (!CommandRegistry.hasCommand(name))
            return;

        CommandRegistry.CommandEntry entry = CommandRegistry.getCommand(name);

        if (entry.getCommand().getDescription().getRequiredPermissions().getValue() > PermissionLevel.getPermissionLevel(message.getAuthor(), channel.getGuild()).getValue()) {
            channel.sendMessageAsync("You don't have the permissions for this command!", null);
            return;
        }

        String argCheck = entry.getCommand().checkArgs(args.toArray(new String[args.size()]));

        if (argCheck != null) {
            channel.sendMessageAsync("```Invalid args: " + argCheck + "```", null);
            return;
        }

        if (plusBot.getConfiguration().getDisabledCommands(channel.getGuild()).contains(name)) {
            channel.sendMessageAsync("That command is disabled in this server!", null);
            return;
        }

        if (plusBot.getConfiguration().getDisabledCommands(channel).contains(name)) {
            channel.sendMessageAsync("That command is disabled in this channel!", null);
            return;
        }

        PlusBot.LOG.debug("Executing command " + entry.getName() + " with args " + args);

        try {
            Statistics.numCommands++;
            executorService.execute(() -> entry.getCommand().execute(plusBot, args.toArray(new String[args.size()]), channel, message));
        } catch (PermissionException ex) {
            if (PermissionUtil.canTalk(channel))
                channel.sendMessageAsync("I don't have the necessary permissions for this command!", null);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
