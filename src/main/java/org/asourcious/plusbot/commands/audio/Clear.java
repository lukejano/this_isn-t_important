package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Clear implements Command {

    private CommandDescription description = new CommandDescription(
            "Clear",
            "Clears all audio sources from the Music Player",
            "clear",
            null,
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Clear command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        if (plusBot.getMusicPlayer(channel.getGuild()).getCurrentAudioSource() == null) {
            channel.sendMessageAsync("The queue is empty!", null);
            return;
        }

        plusBot.getMusicPlayer(channel.getGuild()).getAudioQueue().clear();
        plusBot.getMusicPlayer(channel.getGuild()).skipToNext();
        channel.sendMessageAsync("Cleared the audio queue.", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
