package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Clear extends Command {

    private CommandDescription description = new CommandDescription(
            "Clear",
            "Clears all audio sources from the Music Player",
            "clear",
            null,
            PermissionLevel.MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Clear command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        plusBot.getMusicPlayer(event.getGuild()).getAudioQueue().clear();
        plusBot.getMusicPlayer(event.getGuild()).skipToNext();
        event.getChannel().sendMessageAsync("Cleared the audio queue.", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
