package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Blacklist extends Command {

    private CommandDescription description = new CommandDescription(
            "Blacklist",
            "Adds and removes people from this server's blacklist. This prevents them from using any command.",
            "blacklist add @spammer",
            new Argument[] { new Argument("Add/remove", true) },
            PermissionLevel.MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 1)
            return "The Blacklist command only takes on argument!";
        if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove"))
            return "The only acceptable args are \"Add\" and \"Remove\"";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        int numUpdated = 0;
        if (args[0].equalsIgnoreCase("add")) {
            for (User user : event.getMessage().getMentionedUsers()) {
                if (!plusBot.getConfiguration().getBlacklist(event.getGuild()).contains(event.getAuthor().getId())) {
                    if (PermissionLevel.getPermissionLevel(event.getAuthor(), event.getGuild()).getValue()
                            > PermissionLevel.getPermissionLevel(user, event.getGuild()).getValue()) {
                        plusBot.getConfiguration().addUserToBlacklist(user, event.getGuild());
                        numUpdated++;
                    } else {
                        event.getChannel().sendMessageAsync("You don't have the necessary permissions to add **" + user.getUsername() + "** to the blacklist", null);
                    }
                }
            }
            event.getChannel().sendMessageAsync("Successfully added **" + numUpdated + "** users to the blacklist.", null);
        } else {
            for (User user : event.getMessage().getMentionedUsers()) {
                if (plusBot.getConfiguration().getBlacklist(event.getGuild()).contains(event.getAuthor().getId())) {
                    plusBot.getConfiguration().removeUserFromBlacklist(user, event.getGuild());
                    numUpdated++;
                }
            }
            event.getChannel().sendMessageAsync("Successfully removed **" + numUpdated + "** users from the blacklist.", null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
