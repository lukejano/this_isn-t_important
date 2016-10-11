package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.Playlist;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.events.MusicPlayerEventListener;
import org.asourcious.plusbot.utils.FormatUtils;

import java.util.List;

public class PlaylistCommand implements Command {

    private CommandDescription description = new CommandDescription(
            "Playlist",
            "Adds an audio source playlist to the queue",
            "play (Youtube PlaylistCommand URL)",
            new Argument[] { new Argument("The URL of the audio source playlist", true) },
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 1)
            return "The PlaylistCommand command only takes one argument!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        MusicPlayer player = plusBot.getMusicPlayer(channel.getGuild());
        ((MusicPlayerEventListener) player.getListeners().get(0)).setStatusUpdateChannel(channel);
        Playlist playlist = Playlist.getPlaylist(args[0]);

        if (playlist == null)  {
            channel.sendMessageAsync(FormatUtils.error("Invalid URL: " + args[0]), null);
            return;
        }

        List<AudioSource> sources = playlist.getSources();

        if (sources.isEmpty()) {
            channel.sendMessageAsync("No sources found at the provided URL.", null);
            return;
        }

        channel.sendMessageAsync("Found a playlist with **" + sources.size() + "** entries.\n" +
                "Queueing sources. This may take some time...", null);

        int numAdded = 0;
        for (AudioSource source : sources) {
            AudioInfo info = source.getInfo();
            List<AudioSource> queue = player.getAudioQueue();
            if (info.getError() == null) {
                queue.add(source);
                numAdded++;
                if (player.isStopped())
                    player.play();
            } else {
                channel.sendMessageAsync("Error:\n" + info.getError(), null);
            }
        }
        channel.sendMessageAsync("Finished queuing provided playlist. Successfully queued **" + numAdded + "** sources", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
