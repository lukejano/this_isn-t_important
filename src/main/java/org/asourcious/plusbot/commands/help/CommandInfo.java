package org.asourcious.plusbot.commands.help;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.CommandRegistry;
import org.asourcious.plusbot.commands.PermissionLevel;

public class CommandInfo extends Command {

    private CommandDescription description = new CommandDescription(
            "CommandInfo",
            "Returns details of the specified command, such as required permissions and usage.",
            "commandinfo help",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 1)
            return "The CommandInfo command only takes one arg!";
        if (!CommandRegistry.hasCommand(args[0]))
            return "That command doesn't exist, try the Help command";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        CommandRegistry.CommandEntry commandEntry = CommandRegistry.getCommand(args[0]);
        CommandDescription description = commandEntry.getCommand().getDescription();

        MessageBuilder messageBuilder = new MessageBuilder();

        messageBuilder
                .appendString("```\nCommandInfo for command ")
                .appendString(description.getName())
                .appendString("\n\n")
                .appendString("Description:\n")
                .appendString(description.getDescription())
                .appendString("\n\n")
                .appendString("Arguments:\n")
                .appendString(commandEntry.getArgs())
                .appendString("\n\n")
                .appendString("Example:\n")
                .appendString(description.getExample())
                .appendString("\n\n")
                .appendString("Required Permissions:\n")
                .appendString(description.getRequiredPermissions().toString())
                .appendString("```");

        channel.sendMessageAsync(messageBuilder.build(), null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
