package org.asourcious.plusbot.commands.config;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.*;

public class CommandToggle implements Command {

    private CommandDescription description = new CommandDescription(
            "Command",
            "Enables and disables commands in this server or channel",
            "command server disable skip",
            new Argument[] { new Argument("Server / Channel", true), new Argument("Enable / Disable", true), new Argument("Command name", true) },
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 3)
            return "The Command command takes 3 args";
        if (!args[0].equalsIgnoreCase("Server") && ! args[0].equalsIgnoreCase("Channel"))
            return "The first argument must be either server or channel";
        if (!args[1].equalsIgnoreCase("Enable") && ! args[1].equalsIgnoreCase("Disable"))
            return "The second argument must be either add or remove";
        if (!CommandRegistry.hasCommand(args[2]))
            return "That command doesn't exist!";
        if (args[2].equalsIgnoreCase("Command"))
            return "You can't disable this command";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        boolean isEnable = args[1].equalsIgnoreCase("enable");
        boolean isServer = args[0].equalsIgnoreCase("server");

        if (!isEnable) {
            if (isServer) {
                if (plusBot.getConfiguration().getDisabledCommands(channel.getGuild()).contains(args[2].toLowerCase())) {
                    channel.sendMessageAsync("That command is already disabled!", null);
                    return;
                }
                plusBot.getConfiguration().addDisabledCommand(args[2].toLowerCase(), channel.getGuild());
            } else {
                if (plusBot.getConfiguration().getDisabledCommands(channel).contains(args[2].toLowerCase())) {
                    channel.sendMessageAsync("That command is already disabled!", null);
                    return;
                }
                plusBot.getConfiguration().addDisabledCommand(args[2].toLowerCase(), channel);
            }
            channel.sendMessageAsync("Disabled command **" + args[2] + "**", null);
        } else {
            if (isServer) {
                if (!plusBot.getConfiguration().getDisabledCommands(channel.getGuild()).contains(args[2].toLowerCase())) {
                    channel.sendMessageAsync("That command is already enabled!", null);
                    return;
                }
                plusBot.getConfiguration().removeDisabledCommand(args[2].toLowerCase(), channel.getGuild());
            } else {
                if (!plusBot.getConfiguration().getDisabledCommands(channel).contains(args[2].toLowerCase())) {
                    channel.sendMessageAsync("That command is already enabled!", null);
                    return;
                }
                plusBot.getConfiguration().removeDisabledCommand(args[2].toLowerCase(), channel);
            }
            channel.sendMessageAsync("Enabled command **" + args[2] + "**", null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
