package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class Pause extends Command {

    private CommandDescription description = new CommandDescription(
            "Pause",
            "Pauses the music queue",
            "pause",
            null,
            PermissionLevel.MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Pause command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        MusicPlayer musicPlayer = plusBot.getMusicPlayer(event.getGuild());

        if (!musicPlayer.isPlaying()) {
            event.getChannel().sendMessageAsync(FormatUtils.error("MusicPlayer not playing anything!"), null);
            return;
        }

        musicPlayer.pause();
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
