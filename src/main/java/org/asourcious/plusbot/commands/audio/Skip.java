package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class Skip extends Command {

    private CommandDescription description = new CommandDescription(
            "Skip",
            "Skips the current audio source in the queue",
            "skip",
            null,
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The skip command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        String songName = FormatUtils.getFormattedSongName(plusBot.getMusicPlayer(channel.getGuild()));
        channel.sendMessageAsync("Skipping " + songName, null);
        plusBot.getMusicPlayer(channel.getGuild()).skipToNext();
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
