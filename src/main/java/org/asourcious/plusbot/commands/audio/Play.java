package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.Playlist;
import net.dv8tion.jda.player.source.AudioSource;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.events.MusicPlayerEventListener;
import org.asourcious.plusbot.utils.FormatUtils;
import org.json.JSONException;

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
        MusicPlayer musicPlayer = plusBot.getMusicPlayer(channel.getGuild());
        ((MusicPlayerEventListener) musicPlayer.getListeners().get(0)).setStatusUpdateChannel(channel);

        try {
            Playlist playlist = Playlist.getPlaylist(args[0]);
            List<AudioSource> sources = playlist.getSources();

            if (sources.size() > 1) {
                channel.sendMessageAsync(FormatUtils.error("Found a playlist. If you want to add a playlist to the audio queue, use the Playlist command"), null);
                return;
            }

            try {
                musicPlayer.getAudioQueue().add(sources.get(0));
                channel.sendMessageAsync("Successfully added **" + sources.get(0).getInfo().getTitle() + "** to queue.", null);
                if (musicPlayer.isStopped())
                    musicPlayer.play();
            } catch (Exception ex) {
                channel.sendMessageAsync(FormatUtils.error("Could not add " + sources.get(0).getInfo().getTitle() + " to queue."), null);
                PlusBot.LOG.log(ex);
            }
        } catch (NullPointerException | JSONException ex) {
            channel.sendMessageAsync(FormatUtils.error("Invalid URL: " + args[0]), null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
