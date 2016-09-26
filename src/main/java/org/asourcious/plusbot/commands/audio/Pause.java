package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.MusicPlayer;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class Pause implements Command {

    private CommandDescription description = new CommandDescription(
            "Pause",
            "Pauses the music queue",
            "pause",
            null,
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Pause command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        MusicPlayer musicPlayer = plusBot.getMusicPlayer(channel.getGuild());

        if (!musicPlayer.isPlaying()) {
            channel.sendMessageAsync(FormatUtils.error("MusicPlayer not playing anything!"), null);
            return;
        }

        musicPlayer.pause();
        channel.sendMessageAsync("Paused playback.", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
