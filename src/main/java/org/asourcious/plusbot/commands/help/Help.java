package org.asourcious.plusbot.commands.help;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.CommandRegistry;
import org.asourcious.plusbot.commands.PermissionLevel;

import java.util.List;

public class Help extends Command {

    private CommandDescription description = new CommandDescription(
            "Help",
            "Returns the currently available commands in this channel",
            "help",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The help command doesn't take any args!";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        List<CommandRegistry.CommandEntry> commandEntries = CommandRegistry.getRegisteredCommands();

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.appendString("The current commands available to you in **" + channel.getGuild().getName() + "** are:\n```xl\n");

        for (int i = 0; i < commandEntries.size(); i++) {
            CommandRegistry.CommandEntry entry = commandEntries.get(i);
            if (plusBot.getConfiguration().getDisabledCommands(channel.getGuild()).contains(entry.getName().toLowerCase()))
                continue;
            if (entry.isAlias())
                continue;
            if (entry.getCommand().getDescription().getRequiredPermissions().getValue() >
                    PermissionLevel.getPermissionLevel(message.getAuthor(), channel.getGuild()).getValue())
                continue;

            messageBuilder.appendString((i + 1) + ") " + entry.getName() + "\n");
        }
        messageBuilder.appendString("```\n");
        messageBuilder.appendString("If you need any further help, join https://www.discord.gg/dFwYEb7 and ask for assistance");

        message.getAuthor().getPrivateChannel().sendMessageAsync(messageBuilder.build(), null);
        channel.sendMessageAsync(message.getAuthor().getAsMention() + ", help has been sent to your DM's.", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
