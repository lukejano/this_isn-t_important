package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.Statistics;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Leave extends Command {

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
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        AudioManager audioManager = event.getJDA().getAudioManager(event.getGuild());

        String channelName = audioManager.getConnectedChannel().getName();
        audioManager.closeAudioConnection();
        event.getChannel().sendMessageAsync("Left voice channel **" + channelName + "**", null);
        Statistics.numConnections--;
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
