package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.source.AudioSource;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

import java.util.LinkedList;

public class Queue extends Command {

    private CommandDescription description = new CommandDescription(
            "Queue",
            "Returns the queue or the size of the queue",
            "queue",
            new Argument[] { new Argument("Size", false) },
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length > 1)
            return"The Queue command only takes up to one argument!";
        if (args.length == 1 && !args[0].equalsIgnoreCase("size"))
            return "The only accepted arg for Queue is \"Size\"";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        LinkedList<AudioSource> queue = plusBot.getMusicPlayer(channel.getGuild()).getAudioQueue();

        if (args.length == 1) {
            channel.sendMessageAsync("There are **" + queue.size() + "** songs enqueued.", null);
            return;
        }

        StringBuilder queueString = new StringBuilder();

        for (int i = 0; i < queue.size(); i++) {
            AudioSource source = queue.get(i);

            if (queueString.length() + source.getInfo().getTitle().length() + 3 >= 2000) { // If it will be too long
                queueString.append("...");
                break;
            }

            queueString.append(i + 1).append(") ").append(source.getInfo().getTitle()).append("\n");
        }
        if (queueString.length() == 0)
            queueString.append("The queue is empty!");

        channel.sendMessageAsync(queueString.toString(), null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
