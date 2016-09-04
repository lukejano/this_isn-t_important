package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Shutdown extends Command {

    private CommandDescription description = new CommandDescription(
            "Shutdown",
            "Turns off " + PlusBot.NAME + ". Only usable by Asourcious",
            "shutdown",
            null,
            PermissionLevel.OWNER
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Shutdown command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        event.getChannel().sendMessage("Shutting down...");
        plusBot.shutdown();
        plusBot.getShardManager().getShards().forEach(JDA::shutdown);
    }

    public CommandDescription getDescription() {
        return description;
    }
}
