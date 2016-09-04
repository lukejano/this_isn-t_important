package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

import java.time.temporal.ChronoUnit;

public class Ping extends Command {

    private CommandDescription description = new CommandDescription(
            "Ping",
            "Checks the response time of " + PlusBot.NAME,
            "ping",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Ping command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        event.getChannel().sendMessageAsync("Ping: ...", message -> {
            if(message != null) {
                message.updateMessageAsync("Ping: " + event.getMessage().getTime().until(message.getTime(), ChronoUnit.MILLIS) + "ms", null);
            }
        });
    }

    public CommandDescription getDescription() {
        return description;
    }
}
