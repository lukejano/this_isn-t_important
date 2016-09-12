package org.asourcious.plusbot.managers;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.utils.PermissionUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.commands.CommandRegistry;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.CommandUtils;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandManager {

    private PlusBot plusBot;
    private ExecutorService executorService;

    public CommandManager(PlusBot plusBot) {
        this.plusBot = plusBot;

        executorService = Executors.newCachedThreadPool(new BasicThreadFactory.Builder()
                .namingPattern("Command thread %d")
                .daemon(true)
                .build());
    }

    public void parseMessage(MessageReceivedEvent event) {
        String prefix = CommandUtils.getPrefixForMessage(plusBot, event.getMessage());

        if (prefix == null)
            return;
        if (plusBot.getConfiguration().getBlacklist(event.getGuild()).contains(event.getAuthor().getId()))
            return;

        CommandContainer commandContainer = CommandUtils.getArgsForMessage(event.getMessage().getRawContent(), prefix);

        if (!CommandRegistry.hasCommand(commandContainer.name))
            return;

        CommandRegistry.CommandEntry command = CommandRegistry.getCommand(commandContainer.name);

        if (command.getCommand().getDescription().getRequiredPermissions().getValue() > PermissionLevel.getPermissionLevel(event.getAuthor(), event.getGuild()).getValue()) {
            event.getChannel().sendMessageAsync("You don't have the permissions for this command!", null);
            return;
        }

        String argCheck = command.getCommand().checkArgs(commandContainer.args);

        if (argCheck != null) {
            event.getChannel().sendMessageAsync("```Invalid args: " + argCheck + "```", null);
            return;
        }

        if (plusBot.getConfiguration().getDisabledCommands(event.getGuild()).contains(commandContainer.name)) {
            event.getChannel().sendMessageAsync("That command is disabled in this server!", null);
            return;
        }

        if (plusBot.getConfiguration().getDisabledCommands(event.getTextChannel()).contains(commandContainer.name)) {
            event.getChannel().sendMessageAsync("That command is disabled in this channel!", null);
            return;
        }

        PlusBot.LOG.debug("Executing command " + command.getName() + " with args " + Arrays.toString(commandContainer.args));
        executorService.submit(() -> {
            try {
                Statistics.numCommands++;
                command.getCommand().execute(plusBot, commandContainer.args, event);
            } catch (PermissionException ex) {
                if (PermissionUtil.canTalk(event.getTextChannel()))
                    event.getChannel().sendMessageAsync("I don't have the necessary permissions for this command!", null);
            }
        });
    }

    public static class CommandContainer {
        public String name;
        public String[] args;

        public CommandContainer(String name, String[] args) {
            this.name = name;
            this.args = args;
        }
    }
}
