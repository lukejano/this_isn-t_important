package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.FormatUtils;
import org.asourcious.plusbot.web.GoogleSearcher;

public class Google implements Command {

    private GoogleSearcher searcher;
    private CommandDescription description = new CommandDescription(
            "Google",
            "Returns the result of a google search.",
            "google discord",
            new Argument[] { new Argument("Search term", true) },
            PermissionLevel.EVERYONE
    );

    public Google(GoogleSearcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public String checkArgs(String[] args) {
        if (args.length != 1)
            return "The Google command takes one argument!";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        String result = searcher.search(args[0]);

        if (result == null) {
            channel.sendMessageAsync(FormatUtils.error("Error searching google!"), null);
            return;
        }

        channel.sendMessageAsync(result, null);
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }
}
