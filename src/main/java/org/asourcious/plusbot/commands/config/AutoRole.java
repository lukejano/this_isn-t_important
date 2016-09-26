package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

import java.util.List;

public class AutoRole implements Command {

    private CommandDescription description = new CommandDescription(
            "AutoRole",
            "Adds and removes roles from this server's roles that are assigned when users/bots join (You must mention the role to specify which role to use)"
                    + "\nYou can make the role mentionable under Server Settings -> Roles -> Your role -> Allow anyone to @mention this role",
            "autorole human add @Role",
            null,
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 2)
            return "The AutoRole command takes two arguments!";
        if (!args[0].equalsIgnoreCase("human") && !args[0].equalsIgnoreCase("bot"))
            return "The first argument must be either \"Human\" or \"Bot\"";
        if (!args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove"))
            return "The first argument must be either \"Add\" or \"Remove\"";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        List<Role> roles = message.getMentionedRoles();
        if (roles.size() > 5) {
            channel.sendMessageAsync("Only 5 auto roles are allowed to be modified at once", null);
            return;
        }

        if (args[0].equalsIgnoreCase("human")) {
            if (args[1].equalsIgnoreCase("add")) {
                roles.parallelStream()
                        .filter(role -> !plusBot.getConfiguration().getAutoHumanRoles(channel.getGuild()).contains(role.getId()))
                        .forEach(role -> plusBot.getConfiguration().addAutoHumanRole(channel.getGuild(), role));
            } else {
                roles.parallelStream()
                        .filter(role -> plusBot.getConfiguration().getAutoHumanRoles(channel.getGuild()).contains(role.getId()))
                        .forEach(role -> plusBot.getConfiguration().removeAutoHumanRole(channel.getGuild(), role.getId()));
            }
        } else {
            if (args[1].equalsIgnoreCase("add")) {
                roles.parallelStream()
                        .filter(role -> !plusBot.getConfiguration().getAutoBotRoles(channel.getGuild()).contains(role.getId()))
                        .forEach(role -> plusBot.getConfiguration().addAutoBotRole(channel.getGuild(), role));
            } else {
                roles.parallelStream()
                        .filter(role -> plusBot.getConfiguration().getAutoBotRoles(channel.getGuild()).contains(role.getId()))
                        .forEach(role -> plusBot.getConfiguration().removeAutoBotRole(channel.getGuild(), role.getId()));
            }
        }
        channel.sendMessageAsync("Successfully updated auto roles", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
