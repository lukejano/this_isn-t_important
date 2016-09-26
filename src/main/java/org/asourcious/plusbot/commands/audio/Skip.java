package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.player.MusicPlayer;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;

public class Skip extends Command {

    private CommandDescription description = new CommandDescription(
            "Skip",
            "Votes to skip the current audio source",
            "skip",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The skip command doesn't take any arguments!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        VoiceChannel voiceChannel = channel.getGuild().getVoiceStatusOfUser(message.getAuthor()).getChannel();

        if (voiceChannel == null) {
            return;
        }

        plusBot.addVoteSkips(channel.getGuild(), message.getAuthor());
        int numVotes = plusBot.getVoteSkips(channel.getGuild()).size();

        MusicPlayer musicPlayer = plusBot.getMusicPlayer(channel.getGuild());
        if (numVotes >= (int) Math.ceil(channel.getUsers().size() * 0.4)) {
            channel.sendMessageAsync("Skipping " + FormatUtils.getFormattedSongName(musicPlayer), null);
            musicPlayer.skipToNext();

            plusBot.clearVoteSkips(channel.getGuild());
        } else {
            channel.sendMessageAsync(numVotes + " out of " + (int) Math.ceil(channel.getUsers().size() * 0.4) + " votes to skip.", null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
