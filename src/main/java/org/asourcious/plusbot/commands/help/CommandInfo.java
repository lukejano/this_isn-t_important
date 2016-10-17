package org.asourcious.plusbot.commands.help;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.CommandRegistry;
import org.asourcious.plusbot.commands.PermissionLevel;

public class CommandInfo implements Command {

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
        CommandRegistry.CommandEntry entry = CommandRegistry.getCommand(args[0]);
        CommandDescription description = entry.getCommand().getDescription();

        String msg = "";
        msg += "```\nCommandInfo for command " + entry.getName() + "\n\n";
        msg += "Description:\n" + description.getDescription() + "\n\n";
        msg += "Arguments:\n" + entry.getArgs() + "\n\n";
        msg += "Example:\n" + description.getExample() + "\n\n";
        msg += "Aliases:\n" + entry.getAliases().toString() + "\n\n";
        msg += "Required Permissions:\n" + description.getRequiredPermissions().toString() + "\n```";

        channel.sendMessageAsync(msg, null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
