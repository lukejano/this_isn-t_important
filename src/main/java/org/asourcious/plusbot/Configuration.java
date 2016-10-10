package org.asourcious.plusbot;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import org.asourcious.plusbot.managers.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Configuration {

    private DatabaseManager databaseManager;
    private ExecutorService executorService;
    private Map<String, List<String>> blacklistCache;
    private Map<String, List<String>> prefixCache;
    private Map<String, List<String>> guildDisabledCommandsCache;
    private Map<String, List<String>> channelDisabledCommandsCache;
    private Map<String, List<String>> autoHumanRoleCache;
    private Map<String, List<String>> autoBotRoleCache;

    public Configuration() {
        executorService = Executors.newSingleThreadExecutor();
        databaseManager = new DatabaseManager();
        blacklistCache = databaseManager.loadDataFromTable("guild_blacklists");
        prefixCache = databaseManager.loadDataFromTable("guild_prefixes");
        guildDisabledCommandsCache = databaseManager.loadDataFromTable("guild_disabled_commands");
        channelDisabledCommandsCache = databaseManager.loadDataFromTable("channel_disabled_commands");
        autoHumanRoleCache = databaseManager.loadDataFromTable("auto_human_roles");
        autoBotRoleCache = databaseManager.loadDataFromTable("auto_bot_roles");
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public String getToken() {
        try {
            return Files.readAllLines(new File("credentials.txt").toPath(), Charset.defaultCharset()).get(0);
        } catch (IOException e) {
            PlusBot.LOG.log(e);
            System.exit(-1);
        }
        return null;
    }

    public void addUserToBlacklist(User user, Guild guild) {
        if (!blacklistCache.containsKey(guild.getId()))
            blacklistCache.put(guild.getId(), new ArrayList<>());

        blacklistCache.get(guild.getId()).add(user.getId());
        executorService.submit(() -> databaseManager.addEntryToTable(guild.getId(), user.getId(), "guild_blacklists"));
    }

    public void removeUserFromBlacklist(User user, Guild guild) {
        blacklistCache.get(guild.getId()).remove(user.getId());
        executorService.submit(() -> databaseManager.removeEntryFromTable(guild.getId(), user.getId(), "guild_blacklists"));
    }

    public List<String> getBlacklist(Guild guild) {
        if (!blacklistCache.containsKey(guild.getId()))
            return Collections.unmodifiableList(new ArrayList<>());

        return Collections.unmodifiableList(new ArrayList<>(blacklistCache.get(guild.getId())));
    }

    public void addPrefixToGuild(String prefix, Guild guild) {
        if (!prefixCache.containsKey(guild.getId()))
            prefixCache.put(guild.getId(), new ArrayList<>());

        prefixCache.get(guild.getId()).add(prefix);
        executorService.submit(() -> databaseManager.addEntryToTable(guild.getId(), prefix, "guild_prefixes"));
    }

    public void removePrefixFromGuild(String prefix, Guild guild) {
        prefixCache.get(guild.getId()).remove(prefix);
        executorService.submit(() -> databaseManager.removeEntryFromTable(guild.getId(), prefix, "guild_prefixes"));
    }

    public List<String> getPrefixesForGuild(Guild guild) {
        if (!prefixCache.containsKey(guild.getId()))
            return Collections.unmodifiableList(new ArrayList<>());

        return Collections.unmodifiableList(new ArrayList<>(prefixCache.get(guild.getId())));
    }

    public void addDisabledCommand(String commandName, Guild guild) {
        if (!guildDisabledCommandsCache.containsKey(guild.getId()))
            guildDisabledCommandsCache.put(guild.getId(), new ArrayList<>());

        guildDisabledCommandsCache.get(guild.getId()).add(commandName);
        executorService.submit(() -> databaseManager.addEntryToTable(guild.getId(), commandName, "guild_disabled_commands"));
    }

    public void removeDisabledCommand(String commandName, Guild guild) {
        guildDisabledCommandsCache.get(guild.getId()).remove(commandName);
        executorService.submit(() -> databaseManager.removeEntryFromTable(guild.getId(), commandName, "guild_disabled_commands"));
    }

    public List<String> getDisabledCommands(Guild guild) {
        if (!guildDisabledCommandsCache.containsKey(guild.getId()))
            return Collections.unmodifiableList(new ArrayList<>());

        return Collections.unmodifiableList(new ArrayList<>(guildDisabledCommandsCache.get(guild.getId())));
    }

    public boolean isCommandDisabled(String command, Guild guild) {
        if (!guildDisabledCommandsCache.containsKey(guild.getId()))
            return false;
        return guildDisabledCommandsCache.get(guild.getId()).contains(command.toLowerCase());
    }

    public boolean isCommandDisabled(String command, TextChannel channel) {
        if (!channelDisabledCommandsCache.containsKey(channel.getId()))
            return false;
        return channelDisabledCommandsCache.get(channel.getId()).contains(command.toLowerCase());
    }

    public void addDisabledCommand(String commandName, TextChannel channel) {
        if (!channelDisabledCommandsCache.containsKey(channel.getId()))
            channelDisabledCommandsCache.put(channel.getId(), new ArrayList<>());

        channelDisabledCommandsCache.get(channel.getId()).add(commandName);
        executorService.submit(() -> databaseManager.addEntryToTable(channel.getId(), commandName, "channel_disabled_commands"));
    }

    public void removeDisabledCommand(String commandName, TextChannel channel) {
        channelDisabledCommandsCache.get(channel.getId()).remove(commandName);
        executorService.submit(() -> databaseManager.removeEntryFromTable(channel.getId(), commandName, "channel_disabled_commands"));
    }

    public List<String> getDisabledCommands(TextChannel channel) {
        if (!channelDisabledCommandsCache.containsKey(channel.getId()))
            return Collections.unmodifiableList(new ArrayList<>());

        return Collections.unmodifiableList(new ArrayList<>(channelDisabledCommandsCache.get(channel.getId())));
    }

    public void addAutoHumanRole(Guild guild, Role role) {
        if (!autoHumanRoleCache.containsKey(guild.getId()))
            autoHumanRoleCache.put(guild.getId(), new ArrayList<>());

        autoHumanRoleCache.get(guild.getId()).add(role.getId());
        executorService.submit(() -> databaseManager.addEntryToTable(guild.getId(), role.getId(), "auto_human_roles"));
    }

    public void removeAutoHumanRole(Guild guild, String role) {
        autoHumanRoleCache.get(guild.getId()).remove(role);
        executorService.submit(() -> databaseManager.removeEntryFromTable(guild.getId(), role, "auto_human_roles"));
    }

    public List<String> getAutoHumanRoles(Guild guild) {
        if (!autoHumanRoleCache.containsKey(guild.getId()))
            return Collections.unmodifiableList(new ArrayList<>());

        return Collections.unmodifiableList(new ArrayList<>(autoHumanRoleCache.get(guild.getId())));
    }

    public void addAutoBotRole(Guild guild, Role role) {
        if (!autoBotRoleCache.containsKey(guild.getId()))
            autoBotRoleCache.put(guild.getId(), new ArrayList<>());

        autoBotRoleCache.get(guild.getId()).add(role.getId());
        executorService.submit(() -> databaseManager.addEntryToTable(guild.getId(), role.getId(), "auto_bot_roles"));
    }

    public void removeAutoBotRole(Guild guild, String role) {
        autoBotRoleCache.get(guild.getId()).remove(role);
        executorService.submit(() -> databaseManager.removeEntryFromTable(guild.getId(), role, "auto_bot_roles"));
    }

    public List<String> getAutoBotRoles(Guild guild) {
        if (!autoBotRoleCache.containsKey(guild.getId()))
            return Collections.unmodifiableList(new ArrayList<>());

        return Collections.unmodifiableList(new ArrayList<>(autoBotRoleCache.get(guild.getId())));
    }
}
