package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

import java.time.temporal.ChronoUnit;

public class Ping implements Command {

    private CommandDescription description = new CommandDescription(
            "Ping",
            "Checks the response time of " + PlusBot.NAME,
            "ping",
            null,
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Ping command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        channel.sendMessageAsync("Ping: ...", msg -> {
            if(msg != null) {
                msg.updateMessageAsync("Ping: " + message.getTime().until(msg.getTime(), ChronoUnit.MILLIS) + "ms", null);
            }
        });
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
