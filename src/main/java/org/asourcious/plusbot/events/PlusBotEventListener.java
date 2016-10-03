package org.asourcious.plusbot.events;

import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.PermissionUtil;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.managers.AutoRoleManager;
import org.asourcious.plusbot.managers.CommandManager;

public class PlusBotEventListener extends ListenerAdapter {

    private CommandManager commandManager;
    private AutoRoleManager autoRoleManager;

    public PlusBotEventListener(PlusBot plusBot, CommandManager commandManager) {
        this.commandManager = commandManager;
        autoRoleManager = new AutoRoleManager(plusBot.getConfiguration());
    }

    @Override
    public void onReady(ReadyEvent event) {
        PlusBot.LOG.info(PlusBot.NAME + " v" + PlusBot.VERSION + " now online.");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        if (!PermissionUtil.canTalk(event.getChannel()))
            return;

        commandManager.parseMessage(event.getChannel(), event.getMessage());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        autoRoleManager.handleMemberJoin(event.getGuild(), event.getUser());
    }
}
