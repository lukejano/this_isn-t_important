package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.managers.AudioManager;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class Leave implements Command {

    private CommandDescription description = new CommandDescription(
            "Leave",
            "Leaves the current Voice Channel",
            "leave",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Leave command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        AudioManager audioManager = channel.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            channel.sendMessageAsync(FormatUtils.error("Not connected to a voice channel!"), null);
            return;
        }

        String channelName = audioManager.getConnectedChannel().getName();
        audioManager.closeAudioConnection();
        channel.sendMessageAsync("Left voice channel **" + channelName + "**", null);
        Statistics.numConnections--;
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
