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

public class Play implements Command {

    private CommandDescription description = new CommandDescription(
            "Play",
            "Adds an audio source to the queue",
            "play (Youtube URL)",
            new Argument[] { new Argument("The URL of the audio source", true) },
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 1)
            return "The Play command only takes one argument!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        MusicPlayer player = plusBot.getMusicPlayer(channel.getGuild());
        ((MusicPlayerEventListener) player.getListeners().get(0)).setStatusUpdateChannel(channel);
        Playlist playlist = Playlist.getPlaylist(args[0]);

        if (playlist == null) {
            channel.sendMessageAsync(FormatUtils.error("Invalid URL: " + args[0]), null);
            return;
        }

        List<AudioSource> sources = playlist.getSources();

        if (sources.size() > 1) {
            channel.sendMessageAsync(FormatUtils.error("Found a playlist. If you want to add a playlist to the audio queue, use the Playlist command"), null);
            return;
        }
        if (sources.isEmpty()) {
            channel.sendMessageAsync("No sources found at the provided URL.", null);
            return;
        }

        AudioSource source = sources.get(0);
        AudioInfo info = source.getInfo();
        if (info.getError() == null) {
            player.getAudioQueue().add(source);
            channel.sendMessageAsync("The provided URL has been added the to queue", null);
            if (player.isStopped())
                player.play();
        } else {
            channel.sendMessageAsync("There was an error while loading the provided URL. \nError: " + info.getError(), null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
