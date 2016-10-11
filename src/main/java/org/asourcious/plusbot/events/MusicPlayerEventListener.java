package org.asourcious.plusbot.events;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.hooks.PlayerListenerAdapter;
import net.dv8tion.jda.player.hooks.events.NextEvent;
import net.dv8tion.jda.player.hooks.events.PlayEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.utils.FormatUtils;

public class MusicPlayerEventListener extends PlayerListenerAdapter {

    private PlusBot plusBot;
    private TextChannel statusUpdateChannel;

    public MusicPlayerEventListener(PlusBot plusBot, TextChannel textChannel) {
        this.plusBot = plusBot;
        statusUpdateChannel = textChannel;
    }

    @Override
    public void onPlay(PlayEvent event) {
        newSong((MusicPlayer) event.getPlayer());
    }

    @Override
    public void onNext(NextEvent event) {
        newSong((MusicPlayer) event.getPlayer());
    }

    public void newSong(MusicPlayer player) {
        plusBot.clearVoteSkips(plusBot.getGuildForPlayer(player));
        String name = FormatUtils.getFormattedSongName(player);

        if (name != null)
            statusUpdateChannel.sendMessageAsync("Now playing " + name, null);
    }

    public void setStatusUpdateChannel(TextChannel channel) {
        statusUpdateChannel = channel;
    }
}
