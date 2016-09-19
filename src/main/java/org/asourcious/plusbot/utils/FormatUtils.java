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
        ChronoUnit[] units = new ChronoUnit[] { ChronoUnit.MONTHS, ChronoUnit.DAYS, ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS };

        StringBuilder formattedTime = new StringBuilder();
        OffsetDateTime currentTime = startTime;
        for (int i = 0; i < units.length; i++) {
            ChronoUnit unit = units[i];
            if (currentTime.until(endTime, unit) > 0) {
                formattedTime
                        .append(currentTime.until(endTime, unit))
                        .append(" ")
                        .append(unit.toString().toLowerCase())
                        .append(i == units.length - 1 ? "." : ", ");
                currentTime = currentTime.plus(startTime.until(endTime, unit), unit);
            }
        }

        return formattedTime.toString();
    }
}
