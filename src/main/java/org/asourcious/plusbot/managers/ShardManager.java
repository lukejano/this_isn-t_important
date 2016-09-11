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
        return shards.parallelStream().map(jda -> jda.getGuilds().size()).reduce(0, Integer::sum);
    }

    public int getNumberOfTextChannels() {
        return shards.parallelStream().map(jda -> jda.getTextChannels().size()).reduce(0, Integer::sum);
    }

    public int getNumberOfVoiceChannels() {
        return shards.parallelStream().map(jda -> jda.getVoiceChannels().size()).reduce(0, Integer::sum);
    }

    public int getNumberOfUsers() {
        return shards.parallelStream().map(jda -> jda.getUsers().size()).reduce(0, Integer::sum);
    }
}
