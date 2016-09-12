package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
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
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        String songName = FormatUtils.getFormattedSongName(plusBot.getMusicPlayer(event.getGuild()));
        plusBot.getMusicPlayer(event.getGuild()).skipToNext();
        event.getChannel().sendMessageAsync("Skipped " + songName, null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
