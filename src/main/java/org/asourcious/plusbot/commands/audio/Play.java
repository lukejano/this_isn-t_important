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

import java.util.List;

public class Play extends Command {

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
                    if (musicPlayer.isStopped())
                        musicPlayer.play();
                } catch (Exception ex) {
                    ex.printStackTrace(); // To figure out what exceptions I should catch instead of generic Exception
                    event.getChannel().sendMessageAsync(FormatUtils.error("Could not add " + source.getInfo().getTitle() + " to queue."), null);
                }
            }
        } catch (NullPointerException ex) {
            event.getChannel().sendMessageAsync(FormatUtils.error("Invalid URL: " + args[0]), null);
        }
    }

    public CommandDescription getDescription() {
        return description;
    }
}
