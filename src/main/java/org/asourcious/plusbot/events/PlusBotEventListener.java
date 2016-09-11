package org.asourcious.plusbot.events;

import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.managers.AutoRoleManager;
import org.asourcious.plusbot.managers.CommandManager;

public class PlusBotEventListener extends ListenerAdapter {

    private CommandManager commandManager;
    private AutoRoleManager autoRoleManager;

    public PlusBotEventListener(PlusBot plusBot) {
        commandManager = new CommandManager(plusBot);
        autoRoleManager = new AutoRoleManager(plusBot.getConfiguration());
    }

    @Override
    public void onReady(ReadyEvent event) {
        PlusBot.LOG.info(PlusBot.NAME + " v" + PlusBot.VERSION + " now online.");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        if (event.isPrivate())
            return;

        commandManager.parseMessage(event);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        autoRoleManager.handleMemberJoin(event);
    }
}
