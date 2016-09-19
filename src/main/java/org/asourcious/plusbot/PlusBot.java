package org.asourcious.plusbot;

import net.dv8tion.jda.JDAInfo;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.player.JDAPlayerInfo;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.utils.SimpleLog;
import org.asourcious.plusbot.commands.CommandRegistry;
import org.asourcious.plusbot.commands.audio.Clear;
import org.asourcious.plusbot.commands.audio.Join;
import org.asourcious.plusbot.commands.audio.Leave;
import org.asourcious.plusbot.commands.audio.Move;
import org.asourcious.plusbot.commands.audio.Pause;
import org.asourcious.plusbot.commands.audio.Play;
import org.asourcious.plusbot.commands.audio.PlaylistCommand;
import org.asourcious.plusbot.commands.audio.Queue;
import org.asourcious.plusbot.commands.audio.Resume;
import org.asourcious.plusbot.commands.audio.Shuffle;
import org.asourcious.plusbot.commands.audio.Skip;
import org.asourcious.plusbot.commands.audio.Volume;
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
import org.asourcious.plusbot.commands.maintenance.Clean;
import org.asourcious.plusbot.commands.maintenance.Ping;
import org.asourcious.plusbot.commands.maintenance.Shard;
import org.asourcious.plusbot.commands.maintenance.Shards;
import org.asourcious.plusbot.commands.maintenance.Shutdown;
import org.asourcious.plusbot.commands.maintenance.Status;
import org.asourcious.plusbot.events.MusicPlayerEventListener;
import org.asourcious.plusbot.managers.ShardManager;
import org.asourcious.plusbot.web.GoogleSearcher;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlusBot {

    public static final String NAME = "PlusBot";
    public static final String VERSION = "1.1";
    public static final String OWNER_ID = "142843635057164288";

    public static final SimpleLog LOG = SimpleLog.getLog("PlusBot");
    public static final File LOG_OUT_FILE = new File("out.log");
    public static final File LOG_ERR_FILE = new File("err.log");

    private GoogleSearcher googleSearcher;

    private ShardManager shardManager;
    private Configuration configuration;
    private Map<String, MusicPlayer> guildMusicPlayers;

    private ScheduledExecutorService cacheCleaner;

    public void init() throws LoginException {
        Statistics.startTime = System.currentTimeMillis();
        configuration = new Configuration();
        shardManager = new ShardManager(this, 1);
        guildMusicPlayers = new ConcurrentHashMap<>();
        googleSearcher = new GoogleSearcher();

        cacheCleaner = Executors.newSingleThreadScheduledExecutor();

        cacheCleaner.scheduleAtFixedRate(() -> googleSearcher.cleanCache(), 4, 4, TimeUnit.HOURS);
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
        CommandRegistry.registerCommand("Join", new Join());
        CommandRegistry.registerCommand("Leave", new Leave());
        CommandRegistry.registerCommand("Move", new Move());
        CommandRegistry.registerCommand("Pause", new Pause());
        CommandRegistry.registerCommand("Play", new Play());
        CommandRegistry.registerCommand("Playlist", new PlaylistCommand());
        CommandRegistry.registerCommand("Queue", new Queue());
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
            guildMusicPlayers.get(guild.getId()).addEventListener(new MusicPlayerEventListener(guild.getPublicChannel()));
        }

        return guildMusicPlayers.get(guild.getId());
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
