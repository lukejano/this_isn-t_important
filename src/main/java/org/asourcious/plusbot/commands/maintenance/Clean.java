package org.asourcious.plusbot.commands.maintenance;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.CommandUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Clean extends Command {

    private CommandDescription description = new CommandDescription(
            "Clean",
            "Removes all commands and responses in the last 100 messages",
            "clean",
            null,
            PermissionLevel.MODERATOR
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 0)
            return "The Clean command doesn't take any args";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event) {
        List<Message> messages = event.getTextChannel().getHistory().retrieve();
        List<Message> toRemove = messages.parallelStream().filter(message -> CommandUtils.isValidCommand(message, plusBot)
                        || event.getJDA().getSelfInfo().equals(message.getAuthor())).collect(Collectors.toList());

        List<Message> current = new ArrayList<>();
        int numRemoved = 0;

        for (Message message : toRemove) {
            current.add(message);
            numRemoved++;
        }
        numRemoved+= current.size();
        if (current.size() > 1)
            event.getTextChannel().deleteMessages(current);
        else if (toRemove.size() > 0)
            current.get(0).deleteMessage();

        event.getChannel().sendMessageAsync("Removed **" + numRemoved + "** messages.", null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
