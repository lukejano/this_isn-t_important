package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Eval implements Command {

    private CommandDescription description = new CommandDescription(
            "Eval",
            "Evaluates the ",
            "eval jda.getGuilds().size()",
            new Argument[] { new Argument("script", true) },
            PermissionLevel.OWNER
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 1)
            return "The Eval command takes 1 argument";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        ScriptEngine se = new ScriptEngineManager().getEngineByName("Nashorn");
        se.put("bot", plusBot);
        se.put("jda", message.getJDA());
        se.put("guild", channel.getGuild());
        se.put("channel", channel);
        try {
            channel.sendMessageAsync("Evaluated Successfully:\n ```" + se.eval(args[0]) + "```", null);
        }
        catch(Exception e) {
            channel.sendMessageAsync("Exception was thrown: ```" + e + "```", null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
