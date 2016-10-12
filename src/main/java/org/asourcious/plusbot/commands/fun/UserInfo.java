package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.utils.MiscUtil;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.CommandUtils;
import org.asourcious.plusbot.utils.FormatUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserInfo implements Command {

    private CommandDescription description = new CommandDescription(
            "UserInfo",
            "Reports information about the specified user",
            "userinfo @" + PlusBot.NAME,
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        List<User> users = CommandUtils.getTrimmedMentions(message);
        User target = users.get(0);

        List<String> roles = channel.getGuild().getRolesForUser(target).parallelStream().map(Role::getName).collect(Collectors.toList());

        String msg = "";
        msg += "Username: **" + target.getUsername() + "#" + target.getDiscriminator() + "**\n";
        msg += "ID: **" + target.getId() + "**\n";
        if (roles.size() < 20)
            msg += "Roles: **" + roles.toString() + "**\n";
        msg += "Status: **" + target.getOnlineStatus().toString() + "**\n";
        msg += "Game: **" + (target.getCurrentGame() != null ? target.getCurrentGame() : "None") + "**\n";
        msg += "Creation Time: **" + FormatUtils.getFormattedTime(MiscUtil.getCreationTime(target.getId())) + "**\n";
        msg += "Avatar: " + target.getAvatarUrl();

        channel.sendMessageAsync(msg, null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
