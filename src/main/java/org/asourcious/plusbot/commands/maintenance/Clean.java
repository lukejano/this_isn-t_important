package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.CommandUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Clean implements Command {

    private CommandDescription description = new CommandDescription(
            "Clean",
            "Removes all commands and responses in the last 100 messages",
            "clean",
            null,
            PermissionLevel.SERVER_MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Clean command doesn't take any args";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        List<Message> toRemove = channel.getHistory().retrieve()
                .parallelStream().filter(msg -> CommandUtils.isValidCommand(msg, plusBot)
                        || message.getJDA().getSelfInfo().equals(msg.getAuthor())).collect(Collectors.toList());

        int numRemoved = toRemove.size();

        if (toRemove.size() > 1)
            channel.deleteMessages(toRemove);
        else if (toRemove.size() > 0)
            toRemove.get(0).deleteMessage();

        channel.sendMessageAsync("Removed **" + numRemoved + "** messages.", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
