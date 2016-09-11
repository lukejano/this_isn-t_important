package org.asourcious.plusbot.utils;

import net.dv8tion.jda.JDA;
import org.asourcious.plusbot.PlusBot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public final class MiscUtils {
    private MiscUtils() {}

    public static InputStream getDataStream(JDA jda, String urlSource) {
        try {
            URL url = new URL(urlSource);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("user-agent", "PlusBot (https://github.com/Asourcious/PlusBot)");
            urlConnection.setRequestProperty("authorization", jda.getAuthToken());
            return urlConnection.getInputStream();
        } catch(IOException e) {
            PlusBot.LOG.log(e);
        }
        return null;
    }
}
