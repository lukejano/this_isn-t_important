package org.asourcious.plusbot.utils;

import net.dv8tion.jda.player.MusicPlayer;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;


public final class FormatUtils {
    private FormatUtils() {}

    public static String error(String message) {
        return "```Error: " + message + "```";
    }

    public static String getFormattedSongName(MusicPlayer player) {
        if (player.getCurrentAudioSource() == null)
            return null;

        return "**" +  player.getCurrentAudioSource().getInfo().getTitle() + "**";
    }

    public static String getFormattedTime(OffsetDateTime startTime, OffsetDateTime endTime) {
        StringBuilder formattedTime = new StringBuilder();

        long months = startTime.until(endTime, ChronoUnit.MONTHS);
        startTime = startTime.plusMonths(months);
        long days = startTime.until(endTime, ChronoUnit.DAYS);
        startTime = startTime.plusDays(days);
        long hours = startTime.until(endTime, ChronoUnit.HOURS);
        startTime = startTime.plusHours(hours);
        long minutes = startTime.until(endTime, ChronoUnit.MINUTES);
        startTime = startTime.plusMinutes(minutes);
        long seconds = startTime.until(endTime, ChronoUnit.SECONDS);

        if (months > 0)  formattedTime.append(months).append(" months, ");
        if (days > 0)    formattedTime.append(days).append( " days, ");
        if (hours > 0)   formattedTime.append(hours).append(" hours,");
        if (minutes > 0) formattedTime.append(minutes).append(" minutes, ");
        formattedTime.append(seconds).append(" seconds.");

        return formattedTime.toString();
    }
}
