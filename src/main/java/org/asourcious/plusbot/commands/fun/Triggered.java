package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

import java.io.File;
import java.util.Random;

public class Triggered implements Command {

    private CommandDescription description = new CommandDescription(
            "Triggered",
            "Sends a fun gif",
            "triggered",
            null,
            PermissionLevel.EVERYONE
    );

    private Random random = new Random();

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Triggered command doesn't take arguments";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        channel.sendFileAsync(new File("media/triggered" + (random.nextInt(3) + 1) + ".gif"), null, null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
