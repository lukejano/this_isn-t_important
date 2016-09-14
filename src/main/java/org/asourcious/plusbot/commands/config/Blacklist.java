package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.CommandUtils;

import java.util.List;

public class Blacklist extends Command {

    private CommandDescription description = new CommandDescription(
            "Blacklist",
            "Adds and removes people from this server's blacklist. This prevents them from using any command.",
            "blacklist add @spammer",
            new Argument[] { new Argument("Add/remove", true) },
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 1)
            return "The Blacklist command only takes one argument!";
        if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove"))
            return "The only acceptable args are \"Add\" and \"Remove\"";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        int numUpdated = 0;
        List<User> toAdd = message.getMentionedUsers();

        if (CommandUtils.getPrefixForMessage(plusBot, message).equals(message.getJDA().getSelfInfo().getAsMention()))
            toAdd.remove(message.getJDA().getSelfInfo());

        toAdd.remove(message.getJDA().getSelfInfo());
        if (args[0].equalsIgnoreCase("add")) {
            for (User user : message.getMentionedUsers()) {
                if (!plusBot.getConfiguration().getBlacklist(channel.getGuild()).contains(message.getAuthor().getId())) {
                    if (PermissionLevel.getPermissionLevel(message.getAuthor(), channel.getGuild()).getValue()
                            > PermissionLevel.getPermissionLevel(user, channel.getGuild()).getValue()) {
                        plusBot.getConfiguration().addUserToBlacklist(user, channel.getGuild());
                        numUpdated++;
                    } else {
                        channel.sendMessageAsync("You don't have the necessary permissions to add **" + user.getUsername() + "** to the blacklist", null);
                    }
                }
            }
            channel.sendMessageAsync("Successfully added **" + numUpdated + "** users to the blacklist.", null);
        } else {
            for (User user : message.getMentionedUsers()) {
                if (plusBot.getConfiguration().getBlacklist(channel.getGuild()).contains(message.getAuthor().getId())) {
                    plusBot.getConfiguration().removeUserFromBlacklist(user, channel.getGuild());
                    numUpdated++;
                }
            }
            channel.sendMessageAsync("Successfully removed **" + numUpdated + "** users from the blacklist.", null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
