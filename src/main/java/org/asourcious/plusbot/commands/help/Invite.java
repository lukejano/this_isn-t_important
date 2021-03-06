package org.asourcious.plusbot.commands.help;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Invite implements Command {

    private CommandDescription description = new CommandDescription(
            "Invite",
            "Returns an invite link to add " + PlusBot.NAME + " to your server",
            "invite",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Invite command doesn't take any args!";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        channel.sendMessageAsync("To add me to your server, click this:\n"
                + message.getJDA().getSelfInfo().getAuthUrl(Permission.ADMINISTRATOR)
                + "\nRemember to give me the permissions I need so that I will work properly!", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
