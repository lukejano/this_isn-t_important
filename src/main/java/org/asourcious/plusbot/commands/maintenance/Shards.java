package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.managers.ShardManager;

import java.util.List;

public class Shards extends Command {

    private CommandDescription description = new CommandDescription(
            "Shards",
            "Returns details of all of " + PlusBot.NAME + "'s shards",
            "shards",
            null,
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Shards command doesn't take any args!";
        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        MessageBuilder messageBuilder = new MessageBuilder();
        ShardManager shardManager = plusBot.getShardManager();
        List<JDA> shards = shardManager.getShards();

        messageBuilder.appendString("Shard information:\n");
        messageBuilder.appendString("```xl\n");
        for (int i = 0; i < shards.size(); i++) {
            JDA shard = shards.get(i);

            messageBuilder.appendString("Shard " + i + ":"
                    + " Guilds: " + shard.getGuilds().size()
                    + " Users: " + shard.getUsers().size()
                    + " TC's: " + shard.getTextChannels().size()
                    + " VC's: " + shard.getVoiceChannels().size()
                    + " JDA: " + shard.getStatus().toString()
                    + "\n"
            );
        }

        messageBuilder.appendString("\n");
        messageBuilder.appendString("Shards: " + shards.size()
                + " Guilds: " + shardManager.getNumberOfGuilds()
                + " Users: " + shardManager.getNumberOfUsers()
                + " TC's: " + shardManager.getNumberOfTextChannels()
                + " VC's: " + shardManager.getNumberOfVoiceChannels()
        );
        messageBuilder.appendString("\n```");

        event.getChannel().sendMessageAsync(messageBuilder.build(), null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
