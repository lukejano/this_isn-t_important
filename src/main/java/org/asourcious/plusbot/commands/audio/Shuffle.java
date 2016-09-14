package org.asourcious.plusbot.commands.audio;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.player.MusicPlayer;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Shuffle extends Command {

    private CommandDescription description = new CommandDescription(
            "Shuffle",
            "",
            "shuffle on",
            new Argument[] {new Argument("On/Off", false)},
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length > 1)
            return "The Shuffle command only takes up to one argument";
        if (args.length == 1 && !args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("off"))
            return "The only accepted arguments are on and off";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        MusicPlayer player = plusBot.getMusicPlayer(channel.getGuild());
        if (args.length == 0) {
            channel.sendMessageAsync("Shuffling is set to **" + player.isShuffle() + "**", null);
            return;
        }

        player.setShuffle(args[0].equalsIgnoreCase("on"));
        channel.sendMessageAsync("Shuffling set to **" + player.isShuffle() + "**", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
