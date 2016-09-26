package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Reset implements Command {

    private CommandDescription description = new CommandDescription(
            "Reset",
            "Resets the Music Player for this server.",
            "reset",
            null,
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The reset command doesn't take any arguments";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        if (channel.getGuild().getAudioManager().isConnected()) {
            channel.getGuild().getAudioManager().closeAudioConnection();
        }
        plusBot.resetMusicPlayer(channel.getGuild());
        channel.sendMessageAsync("Reset Music Player.", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
