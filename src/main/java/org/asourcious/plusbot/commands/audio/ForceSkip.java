package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.MusicPlayer;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class ForceSkip extends Command {

    private CommandDescription description = new CommandDescription(
            "ForceSkip",
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
        MusicPlayer musicPlayer = plusBot.getMusicPlayer(channel.getGuild());

        if (musicPlayer.getCurrentAudioSource() == null) {
            channel.sendMessageAsync("There is nothing in the queue!", null);
            return;
        }

        channel.sendMessageAsync("Skipping " + FormatUtils.getFormattedSongName(musicPlayer), null);
        musicPlayer.skipToNext();
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
