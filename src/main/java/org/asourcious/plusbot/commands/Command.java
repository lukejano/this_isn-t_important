package org.asourcious.plusbot.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.asourcious.plusbot.PlusBot;

public abstract class Command {
    public abstract String checkArgs(String[] args);
    public abstract void execute(PlusBot plusBot, String[] args, MessageReceivedEvent event);
    public abstract CommandDescription getDescription();
}
