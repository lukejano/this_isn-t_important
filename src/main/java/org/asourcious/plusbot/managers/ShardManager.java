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
    private List<CommandManager> commandManagers;

    public ShardManager(PlusBot plusBot, int numShards) throws LoginException {
        shards = new ArrayList<>();
        commandManagers = new ArrayList<>();

        if (numShards == 1) { // TODO: Remove once PlusBot requires shards
            CommandManager commandManager = new CommandManager(plusBot);
            commandManagers.add(commandManager);
            shards.add(new JDABuilder()
                    .setBotToken(plusBot.getConfiguration().getToken())
                    .addListener(new PlusBotEventListener(plusBot, commandManager))
                    .setBulkDeleteSplittingEnabled(false)
                    .buildAsync());
            return;
        }

        for(int i = 0; i < numShards; i++) {
            CommandManager commandManager = new CommandManager(plusBot);
            commandManagers.add(commandManager);
            shards.add(new JDABuilder()
                    .setBotToken(plusBot.getConfiguration().getToken())
                    .addListener(new PlusBotEventListener(plusBot, commandManager))
                    .setBulkDeleteSplittingEnabled(false)
                    .useSharding(i, numShards)
                    .buildAsync());
        }
    }

    public List<JDA> getShards() {
        return Collections.unmodifiableList(shards);
    }

    public int getNumberOfGuilds() {
        return shards.parallelStream().mapToInt(jda -> jda.getGuilds().size()).sum();
    }

    public int getNumberOfTextChannels() {
        return shards.parallelStream().mapToInt(jda -> jda.getTextChannels().size()).sum();
    }

    public int getNumberOfVoiceChannels() {
        return shards.parallelStream().mapToInt(jda -> jda.getVoiceChannels().size()).sum();
    }

    public int getNumberOfUsers() {
        return shards.parallelStream().mapToInt(jda -> jda.getUsers().size()).sum();
    }

    public void shutdown() {
        commandManagers.forEach(CommandManager::shutdown);
        shards.forEach(JDA::shutdown);
    }
}
