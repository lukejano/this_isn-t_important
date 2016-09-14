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

import java.util.Arrays;

public class CommandManager {

    private PlusBot plusBot;

    public CommandManager(PlusBot plusBot) {
        this.plusBot = plusBot;
    }

    public void parseMessage(TextChannel channel, Message message) {
        String prefix = CommandUtils.getPrefixForMessage(plusBot, message);

        if (prefix == null)
            return;
        if (plusBot.getConfiguration().getBlacklist(channel.getGuild()).contains(message.getAuthor().getId()))
            return;

        CommandContainer commandContainer = CommandUtils.getArgsForMessage(message.getRawContent(), prefix);

        if (!CommandRegistry.hasCommand(commandContainer.name))
            return;

        CommandRegistry.CommandEntry command = CommandRegistry.getCommand(commandContainer.name);

        if (command.getCommand().getDescription().getRequiredPermissions().getValue() > PermissionLevel.getPermissionLevel(message.getAuthor(), channel.getGuild()).getValue()) {
            channel.sendMessageAsync("You don't have the permissions for this command!", null);
            return;
        }

        String argCheck = command.getCommand().checkArgs(commandContainer.args);

        if (argCheck != null) {
            channel.sendMessageAsync("```Invalid args: " + argCheck + "```", null);
            return;
        }

        if (plusBot.getConfiguration().getDisabledCommands(channel.getGuild()).contains(commandContainer.name)) {
            channel.sendMessageAsync("That command is disabled in this server!", null);
            return;
        }

        if (plusBot.getConfiguration().getDisabledCommands(channel).contains(commandContainer.name)) {
            channel.sendMessageAsync("That command is disabled in this channel!", null);
            return;
        }

        PlusBot.LOG.debug("Executing command " + command.getName() + " with args " + Arrays.toString(commandContainer.args));

        try {
            Statistics.numCommands++;
            command.getCommand().execute(plusBot, commandContainer.args, channel, message);
        } catch (PermissionException ex) {
            if (PermissionUtil.canTalk(channel))
                channel.sendMessageAsync("I don't have the necessary permissions for this command!", null);
        }
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
