package org.asourcious.plusbot.commands;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import org.asourcious.plusbot.PlusBot;

public abstract class Command {
    public abstract String checkArgs(String[] args);
    public abstract void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message);
    public abstract CommandDescription getDescription();
}
