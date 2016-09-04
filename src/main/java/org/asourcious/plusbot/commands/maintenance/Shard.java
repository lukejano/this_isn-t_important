package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;

public class Shard extends Command {

    private CommandDescription description = new CommandDescription(
            "Shard",
            "Returns information about the current shard",
            "shard",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The shard command doesn't take any arguments!";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        MessageBuilder messageBuilder = new MessageBuilder();

        JDA shard = event.getJDA();

        messageBuilder.appendString("```xl\n I am Shard " + plusBot.getShardManager().getShards().indexOf(shard) + ":"
                + " Guilds: " + shard.getGuilds().size()
                + " Users: " + shard.getUsers().size()
                + " TC's: " + shard.getTextChannels().size()
                + " VC's: " + shard.getVoiceChannels().size()
                + " JDA: " + shard.getStatus().toString()
                + "\n```"
        );

        event.getChannel().sendMessageAsync(messageBuilder.build(), null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
