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
            new Argument[] { new Argument("Add / Remove / Clear", true), new Argument("prefix", false) },
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length > 2 || args.length == 0)
            return "The Prefix command takes 2 arguments!";
        if (args.length == 1 && !args[0].equalsIgnoreCase("clear"))
            return "You need two arguments if you are not clearing the prefixes!";
        if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("clear"))
            return "The first argument can only be add or remove!";
        if (args[1].length() > 15)
            return "Prefix maximum supported length is 15!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        if (args[0].equalsIgnoreCase("add")) {
            if (plusBot.getConfiguration().getPrefixesForGuild(event.getGuild()).contains(args[1])) {
                event.getChannel().sendMessageAsync("That prefix is already added!", null);
                return;
            }

            if (plusBot.getConfiguration().getPrefixesForGuild(event.getGuild()).size() >= 15) {
                event.getChannel().sendMessageAsync("This server already has the maximum number of prefixes, delete some to add more.", null);
                return;
            }

            plusBot.getConfiguration().addPrefixToGuild(args[1], event.getGuild());
            event.getChannel().sendMessageAsync("Added prefix **" + args[1] + "**", null);
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!plusBot.getConfiguration().getPrefixesForGuild(event.getGuild()).contains(args[1])) {
                event.getChannel().sendMessageAsync("That prefix doesn't exist!", null);
                return;
            }
            plusBot.getConfiguration().removePrefixFromGuild(args[1], event.getGuild());
            event.getChannel().sendMessageAsync("Removed prefix **" + args[1] + "**", null);
        } else {
            plusBot.getConfiguration().getPrefixesForGuild(event.getGuild()).forEach(prefix ->
                    plusBot.getConfiguration().removePrefixFromGuild(prefix, event.getGuild())
            );
            event.getChannel().sendMessageAsync("Cleared prefixes", null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
