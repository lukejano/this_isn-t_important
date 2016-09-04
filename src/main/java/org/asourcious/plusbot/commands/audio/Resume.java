package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.player.MusicPlayer;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class Resume extends Command {

    private CommandDescription description = new CommandDescription(
            "Resume",
            "Resumes playback of the queue",
            "resume",
            null,
            PermissionLevel.MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Resume command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        MusicPlayer musicPlayer = plusBot.getMusicPlayer(event.getGuild());

        if (musicPlayer.isPlaying()) {
            event.getChannel().sendMessageAsync(FormatUtils.error("MusicPlayer already playing!"), null);
            return;
        }

        musicPlayer.play();
        event.getChannel().sendMessageAsync("Resumed playback.", null);
    }

    public CommandDescription getDescription() {
        return description;
    }
}
