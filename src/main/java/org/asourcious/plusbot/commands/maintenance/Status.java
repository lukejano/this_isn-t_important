package org.asourcious.plusbot.commands.maintenance;

import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.apache.commons.math3.util.Precision;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

import java.lang.management.ManagementFactory;
import java.time.OffsetDateTime;

public class Status implements Command {

    private CommandDescription description = new CommandDescription(
            "Status",
            "Returns information about " + PlusBot.NAME,
            "status",
            null,
            PermissionLevel.SERVER_MODERATOR
    );

    private OperatingSystemMXBean systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Status command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {


        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.appendString(PlusBot.NAME + " Status:\n")
                .appendString("```Prolog\n")
                .appendString("Name: " + PlusBot.NAME + "\n")
                .appendString("Version " + PlusBot.VERSION + "\n")
                .appendString("Uptime: " + FormatUtils.getFormattedTime(Statistics.startTime, OffsetDateTime.now()) + "\n")
                .appendString("Threads: " + Thread.activeCount() + "\n")
                .appendString("CPU Usage: " + Precision.round(systemMXBean.getProcessCpuLoad() * 100, 2) + "%\n")
                .appendString("RAM Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576 + "MB\n")
                .appendString("Commands: " + Statistics.numCommands + "\n")
                .appendString("Connections: " + Statistics.numConnections + "\n\n")

                .appendString("JDA Info:\n")
                .appendString("Guilds: " + plusBot.getShardManager().getNumberOfGuilds() + "\n")
                .appendString("Text Channels: " + plusBot.getShardManager().getNumberOfTextChannels() + "\n")
                .appendString("Voice Channels: " + plusBot.getShardManager().getNumberOfVoiceChannels() + "\n")
                .appendString("Users: " + plusBot.getShardManager().getNumberOfUsers() + "\n")
                .appendString("```");

        channel.sendMessageAsync(messageBuilder.build(), null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
