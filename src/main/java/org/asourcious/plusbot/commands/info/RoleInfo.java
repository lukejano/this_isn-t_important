package org.asourcious.plusbot.commands.info;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.utils.MiscUtil;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class RoleInfo implements Command {

    private CommandDescription description = new CommandDescription(
            "RoleInfo",
            "Gathers information about the specified role",
            "roleinfo @somerole",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The RoleInfo command doesn't take any arguments!";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        Role target = message.getMentionedRoles().get(0);

        String msg = "";
        msg += "Name: **" + target.getName() + "**\n";
        msg += "ID: **" + target.getId() + "**\n";
        msg += "Permissions: **" + target.getPermissions().toString() + "**\n";
        msg += "Position: **" + target.getPosition() + "**\n";
        msg += "Creation Time: **" + FormatUtils.getFormattedTime(MiscUtil.getCreationTime(target.getId())) + "**";

        channel.sendMessageAsync(msg, null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
