package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistCommand extends Command {

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

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
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        MusicPlayer musicPlayer = plusBot.getMusicPlayer(event.getGuild());
        ((MusicPlayerEventListener) musicPlayer.getListeners().get(0)).setStatusUpdateChannel(event.getTextChannel());

        try {
            Playlist playlist = Playlist.getPlaylist(args[0]);
            List<AudioSource> sources = playlist.getSources();

            for (AudioSource source : sources) {
                try {
                    musicPlayer.getAudioQueue().add(source);
                    event.getChannel().sendMessageAsync("Successfully added **" + source.getInfo().getTitle() + "** to queue.", null);
                    executorService.submit(source::getInfo);
                    if (musicPlayer.isStopped())
                        musicPlayer.play();
                } catch(Exception e) {
                    event.getChannel().sendMessageAsync(FormatUtils.error("Could not add song to queue."), null);
                    PlusBot.LOG.log(e);
                }
            }
        } catch (NullPointerException | JSONException ex) {
            event.getChannel().sendMessageAsync(FormatUtils.error("Invalid URL: " + args[0]), null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
