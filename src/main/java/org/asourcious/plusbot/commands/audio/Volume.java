package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.apache.commons.lang3.math.NumberUtils;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Volume extends Command {

    private CommandDescription description = new CommandDescription(
            "Volume",
            "Sets the volume of the Music Player",
            "volume 50",
            new Argument[] { new Argument("Volume", false) },
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length > 1)
            return "The Volume command only takes up to one argument!";
        if (args.length == 1) {
            if (!NumberUtils.isNumber(args[0]))
                return "Please enter a valid number";

            int volume = NumberUtils.toInt(args[0]);
            if (volume > 100 || volume < 1)
                return "Enter a number between 1 and 100";
        }
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        if (args.length == 0) {
            channel.sendMessageAsync("The current volume is **" +
                    (int) (plusBot.getMusicPlayer(channel.getGuild()).getVolume() * 100) + "**", null);
            return;
        }

        int volume = NumberUtils.toInt(args[0]);
        int oldVolume = (int) (plusBot.getMusicPlayer(channel.getGuild()).getVolume() * 100);

        plusBot.getMusicPlayer(channel.getGuild()).setVolume(volume / 100f);
        channel.sendMessageAsync("Updated volume from **" + oldVolume + "** to **" + volume + "**", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
