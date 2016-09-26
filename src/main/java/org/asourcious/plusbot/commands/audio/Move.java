package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.managers.AudioManager;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class Move implements Command {

    private CommandDescription description = new CommandDescription(
            "Move",
            "Moves to the user's voice channel",
            "move",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Move command doesn't take any args!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        VoiceChannel voiceChannel = channel.getGuild().getVoiceStatusOfUser(message.getAuthor()).getChannel();

        if (voiceChannel == null) {
            channel.sendMessageAsync(FormatUtils.error("You are not in a valid voice channel!"), null);
            return;
        }

        AudioManager audioManager = message.getJDA().getAudioManager(channel.getGuild());

        if (!audioManager.isConnected()) {
            channel.sendMessageAsync(FormatUtils.error("I am not in a voice channel! Use the Join command instead"), null);
            return;
        }

        audioManager.moveAudioConnection(voiceChannel);
        channel.sendMessageAsync("Joined voice channel **" + voiceChannel.getName() + "**", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
