package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.utils.MiscUtil;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ServerInfo implements Command {

    private CommandDescription description = new CommandDescription(
            "ServerInfo",
            "Reports information about this server",
            "serverinfo",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The ServerInfo command doesn't take any arguments";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        Guild target = channel.getGuild();
        List<String> roles = target.getRoles().parallelStream().filter(role -> !role.equals(target.getPublicRole())).map(Role::getName).collect(Collectors.toList());

        String msg = "";
        msg += "Name **" + target.getName() + "**\n";
        msg += "ID: **" + target.getId() + "**\n";
        msg += "Roles: **" + roles.toString() + "**\n";
        msg += "Owner: **" + target.getEffectiveNameForUser(target.getOwner()) + "**\n";
        msg += "Creation Time: **" + FormatUtils.getFormattedTime(MiscUtil.getCreationTime(target.getId())) + "**\n";
        msg += "Icon: " + target.getIconUrl();

        channel.sendMessageAsync(msg, null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
