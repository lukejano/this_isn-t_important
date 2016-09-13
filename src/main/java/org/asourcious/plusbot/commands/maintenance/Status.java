package org.asourcious.plusbot.commands.maintenance;

import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.math3.util.Precision;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

import java.lang.management.ManagementFactory;

public class Status extends Command {

    private CommandDescription description = new CommandDescription(
            "Status",
            "Returns information about " + PlusBot.NAME,
            "status",
            null,
            PermissionLevel.EVERYONE
    );

    private OperatingSystemMXBean systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Status command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        long elapsedMillis = System.currentTimeMillis() - Statistics.startTime;

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.appendString(PlusBot.NAME + " Status:\n");
        messageBuilder.appendString("```xl\n");
        messageBuilder.appendString("Name: " + PlusBot.NAME + "\n");
        messageBuilder.appendString("Version " + PlusBot.VERSION + "\n");
        messageBuilder.appendString("Uptime: " + FormatUtils.getFormattedTime(elapsedMillis) + "\n");
        messageBuilder.appendString("Threads: " + Thread.activeCount() + "\n");
        messageBuilder.appendString("CPU Usage: " + Precision.round(systemMXBean.getProcessCpuLoad() * 100, 2) + "%\n");
        messageBuilder.appendString("RAM Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576 + "MB\n\n");
        messageBuilder.appendString("JDA Info:\n");
        messageBuilder.appendString("Guilds: " + plusBot.getShardManager().getNumberOfGuilds() + "\n");
        messageBuilder.appendString("Text Channels: " + plusBot.getShardManager().getNumberOfTextChannels() + "\n");
        messageBuilder.appendString("Voice Channels: " + plusBot.getShardManager().getNumberOfVoiceChannels() + "\n");
        messageBuilder.appendString("Users: " + plusBot.getShardManager().getNumberOfUsers() + "\n");
        messageBuilder.appendString("Connections: " + Statistics.numConnections + "\n");
        messageBuilder.appendString("Commands: " + Statistics.numCommands + "\n");
        messageBuilder.appendString("```");

        event.getChannel().sendMessageAsync(messageBuilder.build(), null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
