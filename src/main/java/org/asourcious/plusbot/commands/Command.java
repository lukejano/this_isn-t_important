package org.asourcious.plusbot.commands;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;

public interface Command {
    String checkArgs(String[] args);
    void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message);
    CommandDescription getDescription();
}
