package org.asourcious.plusbot.utils;

import net.dv8tion.jda.player.MusicPlayer;

import java.util.concurrent.TimeUnit;

public final class FormatUtils {
    private FormatUtils() {}

    public static String error(String message) {
        return "```Error: " + message + "```";
    }

    public static String getFormattedSongName(MusicPlayer player) {
        return "**" +  player.getCurrentAudioSource().getInfo().getTitle() + "**";
    }

    public static String getFormattedTime(long elapsedMillis) {
        String time = "";

        long days = TimeUnit.MILLISECONDS.toDays(elapsedMillis);
        elapsedMillis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis);
        elapsedMillis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis);
        elapsedMillis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);

        if (days > 0)
            time += days + " days, ";
        if (hours > 0)
            time += hours + " hours, ";
        if (minutes > 0)
            time += minutes + " minutes, ";
        time += seconds + " seconds.";
        return time;
    }
}
