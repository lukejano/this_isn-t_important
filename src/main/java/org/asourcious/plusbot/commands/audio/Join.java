package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class Join extends Command {

    private CommandDescription description = new CommandDescription(
            "Join",
            "Joins the user's voice channel",
            "join",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Join command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        VoiceChannel voiceChannel = event.getGuild().getVoiceStatusOfUser(event.getAuthor()).getChannel();

        if (voiceChannel == null) {
            event.getChannel().sendMessageAsync(FormatUtils.error("You are not in a valid voice channel!"), null);
            return;
        }

        AudioManager audioManager = event.getJDA().getAudioManager(event.getGuild());

        if (audioManager.isConnected()) {
            event.getChannel().sendMessageAsync("I'm already in a voice channel! Use the move command instead!", null);
            return;
        }

        audioManager.openAudioConnection(voiceChannel);
        audioManager.setSendingHandler(plusBot.getMusicPlayer(event.getGuild()));
        plusBot.getMusicPlayer(event.getGuild()).setVolume(0.5f);
        event.getChannel().sendMessageAsync("Joined voice channel **" + voiceChannel.getName() + "**", null);
        plusBot.setNumConnections(plusBot.getNumConnections() + 1);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
