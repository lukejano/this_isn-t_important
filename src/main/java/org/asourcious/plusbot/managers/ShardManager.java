package org.asourcious.plusbot.managers;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.events.PlusBotEventListener;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShardManager {

    private List<JDA> shards;

    public ShardManager(PlusBot plusBot, int numShards) throws LoginException {
        shards = new ArrayList<>();

        if (numShards == 1) { // TODO: Remove once PlusBot requires shards
            shards.add(new JDABuilder()
                    .setBotToken(plusBot.getConfiguration().getToken())
                    .addListener(new PlusBotEventListener(plusBot))
                    .setBulkDeleteSplittingEnabled(false)
                    .buildAsync());
            return;
        }

        for(int i = 0; i < numShards; i++) {
            shards.add(new JDABuilder()
                    .setBotToken(plusBot.getConfiguration().getToken())
                    .addListener(new PlusBotEventListener(plusBot))
                    .setBulkDeleteSplittingEnabled(false)
                    .useSharding(i, numShards)
                    .buildAsync());
        }
    }

    public List<JDA> getShards() {
        return Collections.unmodifiableList(shards);
    }

    public int getNumberOfGuilds() {
        int num = 0;
        for (JDA jda : shards) {
            num += jda.getGuilds().size();
        }

        return num;
    }

    public int getNumberOfTextChannels() {
        int num = 0;
        for (JDA jda : shards) {
            num += jda.getTextChannels().size();
        }

        return num;
    }

    public int getNumberOfVoiceChannels() {
        int num = 0;
        for (JDA jda : shards) {
            num += jda.getVoiceChannels().size();
        }

        return num;
    }

    public int getNumberOfUsers() {
        int num = 0;
        for (JDA jda : shards) {
            num += jda.getUsers().size();
        }

        return num;
    }
}
