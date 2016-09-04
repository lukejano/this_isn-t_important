package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Prefix extends Command {

    private CommandDescription description = new CommandDescription(
            "Prefix",
            "Modifies currently used prefixes in this server",
            "prefix add /",
            new Argument[] { new Argument("Add / Remove", true), new Argument("prefix", true) },
            PermissionLevel.MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 2)
            return "The Prefix command takes 2 arguments!";
        if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove"))
            return "The first argument can only be add or remove!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        if (args[0].equalsIgnoreCase("add")) {
            if (plusBot.getConfiguration().getPrefixesForGuild(event.getGuild()).contains(args[1])) {
                event.getChannel().sendMessageAsync("That prefix is already added!", null);
                return;
            }

            plusBot.getConfiguration().addPrefixToGuild(args[1], event.getGuild());
            event.getChannel().sendMessageAsync("Added prefix **" + args[1] + "**", null);
        } else {
            if (!plusBot.getConfiguration().getPrefixesForGuild(event.getGuild()).contains(args[1])) {
                event.getChannel().sendMessageAsync("That prefix doesn't exist!", null);
                return;
            }
            plusBot.getConfiguration().removePrefixFromGuild(args[1], event.getGuild());
            event.getChannel().sendMessageAsync("Removed prefix **" + args[1] + "**", null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
