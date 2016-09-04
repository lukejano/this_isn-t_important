package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

import java.util.List;

public class AutoRole extends Command {

    private CommandDescription description = new CommandDescription(
            "AutoRole",
            "Adds and removes roles from this server's roles that are assigned when users/bots join (You must mention the role to specify which role to use)"
                    + "\nYou can make the role mentionable under Server Settings -> Roles -> Your role -> Allow anyone to @mention this role",
            "autorole human add @Role",
            null,
            PermissionLevel.MODERATOR
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
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        List<Role> roles = event.getMessage().getMentionedRoles();
        if (roles.size() > 5) {
            event.getChannel().sendMessageAsync("Only 5 auto roles are allowed to be modified at once", null);
            return;
        }

        if (args[0].equalsIgnoreCase("human")) {
            if (args[1].equalsIgnoreCase("add")) {
                roles.parallelStream()
                        .filter(role -> !plusBot.getConfiguration().getAutoHumanRoles(event.getGuild()).contains(role.getId()))
                        .forEach(role -> plusBot.getConfiguration().addAutoHumanRole(event.getGuild(), role));
            } else {
                roles.parallelStream()
                        .filter(role -> plusBot.getConfiguration().getAutoHumanRoles(event.getGuild()).contains(role.getId()))
                        .forEach(role -> plusBot.getConfiguration().removeAutoHumanRole(event.getGuild(), role.getId()));
            }
        } else {
            if (args[1].equalsIgnoreCase("add")) {
                roles.parallelStream()
                        .filter(role -> !plusBot.getConfiguration().getAutoBotRoles(event.getGuild()).contains(role.getId()))
                        .forEach(role -> plusBot.getConfiguration().addAutoBotRole(event.getGuild(), role));
            } else {
                roles.parallelStream()
                        .filter(role -> plusBot.getConfiguration().getAutoBotRoles(event.getGuild()).contains(role.getId()))
                        .forEach(role -> plusBot.getConfiguration().removeAutoBotRole(event.getGuild(), role.getId()));
            }
        }
        event.getChannel().sendMessageAsync("Successfully updated auto roles", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
