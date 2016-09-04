package org.asourcious.plusbot.events;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.hooks.PlayerListenerAdapter;
import net.dv8tion.jda.player.hooks.events.NextEvent;
import net.dv8tion.jda.player.hooks.events.SkipEvent;
import org.asourcious.plusbot.utils.FormatUtils;

public class MusicPlayerEventListener extends PlayerListenerAdapter {

    private TextChannel statusUpdateChannel;

    public MusicPlayerEventListener(TextChannel textChannel) {
        statusUpdateChannel = textChannel;
    }

    @Override
    public void onNext(NextEvent event) {
        statusUpdateChannel.sendMessageAsync("Now playing " + FormatUtils.getFormattedSongName((MusicPlayer) event.getPlayer()), null);
    }

    @Override
    public void onSkip(SkipEvent event) {
        statusUpdateChannel.sendMessageAsync("Now playing " + FormatUtils.getFormattedSongName((MusicPlayer) event.getPlayer()), null);
    }

    public void setStatusUpdateChannel(TextChannel channel) {
        statusUpdateChannel = channel;
    }
}
