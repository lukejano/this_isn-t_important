package org.asourcious.plusbot;

import net.dv8tion.jda.JDAInfo;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.player.JDAPlayerInfo;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.utils.SimpleLog;
import org.asourcious.plusbot.commands.CommandRegistry;
import org.asourcious.plusbot.commands.audio.*;
import org.asourcious.plusbot.commands.audio.Queue;
import org.asourcious.plusbot.commands.config.AutoRole;
import org.asourcious.plusbot.commands.config.Blacklist;
import org.asourcious.plusbot.commands.config.CommandToggle;
import org.asourcious.plusbot.commands.config.Prefix;
import org.asourcious.plusbot.commands.fun.Google;
import org.asourcious.plusbot.commands.fun.RIP;
import org.asourcious.plusbot.commands.fun.Triggered;
import org.asourcious.plusbot.commands.help.CommandInfo;
import org.asourcious.plusbot.commands.help.Help;
import org.asourcious.plusbot.commands.help.Invite;
import org.asourcious.plusbot.commands.maintenance.*;
import org.asourcious.plusbot.commands.maintenance.Shutdown;
import org.asourcious.plusbot.events.MusicPlayerEventListener;
import org.asourcious.plusbot.managers.ShardManager;
import org.asourcious.plusbot.web.GoogleSearcher;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlusBot {

    public static final String NAME = "PlusBot";
    public static final String VERSION = "1.2";
    public static final String OWNER_ID = "142843635057164288";

    public static final SimpleLog LOG = SimpleLog.getLog("PlusBot");
    public static final File LOG_OUT_FILE = new File("out.log");
    public static final File LOG_ERR_FILE = new File("err.log");

    private GoogleSearcher googleSearcher;

    private ShardManager shardManager;
    private Configuration configuration;
    private Map<String, MusicPlayer> guildMusicPlayers;
    private Map<String, List<User>> guildVoteSkips;

    private ScheduledExecutorService cacheCleaner;

    public void init() throws LoginException {
        Statistics.startTime = OffsetDateTime.now();
        configuration = new Configuration();
        shardManager = new ShardManager(this, 1);
        guildMusicPlayers = new ConcurrentHashMap<>();
        guildVoteSkips = new ConcurrentHashMap<>();
        googleSearcher = new GoogleSearcher();

        cacheCleaner = Executors.newSingleThreadScheduledExecutor();

        cacheCleaner.scheduleAtFixedRate(() -> {
            LOG.info("Cleaning cache");
            googleSearcher.cleanCache();
        }, 0, 4, TimeUnit.HOURS);
    }

    public static void main(String[] args) throws IOException, LoginException {
        setupLogFile(LOG_OUT_FILE);
        setupLogFile(LOG_ERR_FILE);
        SimpleLog.addFileLogs(LOG_OUT_FILE, LOG_ERR_FILE);

        LOG.info("Starting up...");
        LOG.info("JDA Version: " + JDAInfo.VERSION);
        LOG.info("JDA-Player Version: " + JDAPlayerInfo.VERSION);

        PlusBot plusBot = new PlusBot();
        plusBot.init();

        CommandRegistry.registerCommand("Clear", new Clear());
        CommandRegistry.registerCommand("ForceSkip", new ForceSkip());
        CommandRegistry.registerCommand("Join", new Join());
        CommandRegistry.registerCommand("Leave", new Leave());
        CommandRegistry.registerCommand("Move", new Move());
        CommandRegistry.registerCommand("Pause", new Pause());
        CommandRegistry.registerCommand("Play", new Play());
        CommandRegistry.registerCommand("Playlist", new PlaylistCommand());
        CommandRegistry.registerCommand("Queue", new Queue());
        CommandRegistry.registerCommand("Reset", new Reset());
        CommandRegistry.registerCommand("Resume", new Resume());
        CommandRegistry.registerCommand("Shuffle", new Shuffle());
        CommandRegistry.registerCommand("Skip", new Skip());
        CommandRegistry.registerCommand("Volume", new Volume());
        CommandRegistry.registerAlias("Join", "Summon");
        CommandRegistry.registerAlias("Leave", "Unsummon");

        CommandRegistry.registerCommand("AutoRole", new AutoRole());
        CommandRegistry.registerCommand("Blacklist", new Blacklist());
        CommandRegistry.registerCommand("Command", new CommandToggle());
        CommandRegistry.registerCommand("Prefix", new Prefix());

        CommandRegistry.registerCommand("Google", new Google(plusBot.googleSearcher));
        CommandRegistry.registerCommand("RIP", new RIP());
        CommandRegistry.registerCommand("Triggered", new Triggered());
        CommandRegistry.registerAlias("Google", "g");

        CommandRegistry.registerCommand("CommandInfo", new CommandInfo());
        CommandRegistry.registerCommand("Help", new Help());
        CommandRegistry.registerCommand("Invite", new Invite());
        CommandRegistry.registerAlias("Help", "Commands");
        CommandRegistry.registerAlias("Help", "CommandList");

        CommandRegistry.registerCommand("Clean", new Clean());
        CommandRegistry.registerCommand("Ping", new Ping());
        CommandRegistry.registerCommand("Shard", new Shard());
        CommandRegistry.registerCommand("Shards", new Shards());
        CommandRegistry.registerCommand("Shutdown", new Shutdown());
        CommandRegistry.registerCommand("Status", new Status());
    }

    public void shutdown() {
        cacheCleaner.shutdown();
        shardManager.shutdown();
        configuration.shutdown();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public MusicPlayer getMusicPlayer(Guild guild) {
        if (!guildMusicPlayers.containsKey(guild.getId())) {
            guildMusicPlayers.put(guild.getId(), new MusicPlayer());
            guildMusicPlayers.get(guild.getId()).addEventListener(new MusicPlayerEventListener(this, guild.getPublicChannel()));
        }

        return guildMusicPlayers.get(guild.getId());
    }

    public void resetMusicPlayer(Guild guild) {
        guildMusicPlayers.remove(guild.getId());
        guild.getAudioManager().setSendingHandler(null);
    }

    public Guild getGuildForPlayer(MusicPlayer player) {
        return guildMusicPlayers.entrySet().parallelStream().filter(entry -> entry.getValue().equals(player)).map(entry -> shardManager.getGuildById(entry.getKey())).findAny().orElse(null);
    }

    public List<User> getVoteSkips(Guild guild) {
        if (!guildVoteSkips.containsKey(guild.getId()))
            return Collections.emptyList();
        return guildVoteSkips.get(guild.getId());
    }

    public void addVoteSkips(Guild guild, User user) {
        if (!guildVoteSkips.containsKey(guild.getId())) {
            guildVoteSkips.put(guild.getId(), new ArrayList<>());
        }
        if (!guildVoteSkips.get(guild.getId()).contains(user))
            guildVoteSkips.get(guild.getId()).add(user);
    }

    public void clearVoteSkips(Guild guild) {
        if (guildVoteSkips.containsKey(guild.getId())) {
            guildVoteSkips.get(guild.getId()).clear();
        }
    }

    private static void setupLogFile(File file) throws IOException {
        if (!file.createNewFile()) {
            try {
                file.delete();
                file.createNewFile();
            } catch (IOException ex) {
                LOG.fatal("Could not set up log file!");
                LOG.log(ex);
            }
        }
    }
}
